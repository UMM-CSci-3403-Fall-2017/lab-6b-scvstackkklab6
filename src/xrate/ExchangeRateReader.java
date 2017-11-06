package xrate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Provide access to basic currency exchange rate services.
 * 
 * @author scvstackkklab6 - Kyle Fluto
 */
public class ExchangeRateReader {
	
	// Class data
	private String baseURL;
	
    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     * 
     * @param baseURL
     *            the base URL for requests
     * @throws IOException 
     */
    public ExchangeRateReader(String baseURL) throws IOException {
    	this.baseURL = baseURL;
    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     * 
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) 
    	   throws ParserConfigurationException, 
    	   SAXException, IOException {
    	
    	// Open the connection
    	String urlString = buildURLString(year, month, day);
    	URL url = new URL(urlString);
    	InputStream xmlStream = url.openStream();
    	Document xmlDoc = createXMLDocument(xmlStream);
    	
    	NodeList exchangeRates = getExchangeRateList(xmlDoc);
    	float rate = getExchangeRate(exchangeRates, currencyCode);
    	
    	xmlStream.close();
    	
    	return rate;
    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     * 
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public float getExchangeRate(
            String fromCurrency, String toCurrency,
            int year, int month, int day) throws IOException, ParserConfigurationException, SAXException {
       
    	// Open the connection and with the full path to the xml file.
    	String urlString = buildURLString(year, month, day);
    	URL url = new URL(urlString);
    	InputStream xmlStream = url.openStream();
    	
    	// Create the xml document from the stream.
    	Document xmlDoc = createXMLDocument(xmlStream);
    	
    	// Get the list of currency exchange nodes.
    	NodeList exchangeRates = getExchangeRateList(xmlDoc);
    	
    	float fromCurrencyRate = getExchangeRate(exchangeRates, fromCurrency);
    	float toCurrencyRate = getExchangeRate(exchangeRates, toCurrency);
    	
    	float rate = fromCurrencyRate / toCurrencyRate;
    	
    	// Clean up.
    	xmlStream.close();
    	
    	return rate;
    }
    
    private Document createXMLDocument(InputStream xmlStream) throws ParserConfigurationException, SAXException, IOException {
    	DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
    	Document doc = docBuilder.parse(xmlStream);
    	doc.getDocumentElement().normalize();
    	
    	return doc;
    }
    
    private String buildURLString(int year, int month, int day) {
    	String baseURL = this.baseURL;
    	
    	/*
    	 * Append 0's in front of days and months that are single digits.
    	 * This ensures that the url is correctly formatted.
    	 */
    	String monthString = String.valueOf(month);;
    	if (monthString.length() == 1) {
    		monthString = "0" + monthString;
    	}
    	
    	String dayString = String.valueOf(day);
    	if (dayString.length() == 1) {
    		dayString = "0" + dayString;
    	}
    	
    	String fullURL = baseURL + year + "/" + monthString + "/" + dayString + ".xml";
    	
    	return fullURL;
    }
    
    private NodeList getExchangeRateList(Document xmlDoc) {
    	return xmlDoc.getElementsByTagName("fx");
    }
    
    private float getExchangeRate(NodeList nodes, String currencyCode) {
    	float rate = (float) 1.0;
    	for (int i = 0; i < nodes.getLength(); i++) {	
    		Node currentNode = nodes.item(i);
    		NodeList children = currentNode.getChildNodes();
    		
    		String currencyString = children.item(1).getTextContent();
    		String rateString = children.item(3).getTextContent();
    		float currentRate = Float.parseFloat(rateString);
    		if (currencyString.equals(currencyCode)) {
    			 rate = currentRate;
    			break;
    		}
    	}
    	return rate;
    }
}