package xrate;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

public class ExchangeRateTest {
    private static final String NICS_DUMMY_DATA_URL = "http://www.morris.umn.edu/~mcphee/ExchangeRateData/";
    private static final double DELTA = 0.0001;

    @Test
    public void checkUSD20091112() throws IOException, ParserConfigurationException, SAXException {
        ExchangeRateReader xrReader = new ExchangeRateReader(NICS_DUMMY_DATA_URL);
        float rate = xrReader.getExchangeRate("USD", 2009, 11, 12);
        assertEquals(1.492200, rate, DELTA);
    }

    @Test
    public void checkGBP20100625() throws IOException, ParserConfigurationException, SAXException {
        ExchangeRateReader xrReader = new ExchangeRateReader(NICS_DUMMY_DATA_URL);
        float rate = xrReader.getExchangeRate("GBP", 2010, 6, 25);
        assertEquals(0.823600, rate, DELTA);
    }

    @Test
    public void checkCHF20100705() throws IOException, ParserConfigurationException, SAXException {
        ExchangeRateReader xrReader = new ExchangeRateReader(NICS_DUMMY_DATA_URL);
        float rate = xrReader.getExchangeRate("CHF", 2010, 7, 5);
        assertEquals(1.332500, rate, DELTA);
    }

    @Test
    public void checkZAR20100909() throws IOException, ParserConfigurationException, SAXException {
        ExchangeRateReader xrReader = new ExchangeRateReader(NICS_DUMMY_DATA_URL);
        float rate = xrReader.getExchangeRate("ZAR", 2010, 9, 9);
        assertEquals(9.196400, rate, DELTA);
    }
    
    @Test
    public void checkUSDvsGBP20100625() throws IOException, ParserConfigurationException, SAXException {
        ExchangeRateReader xrReader = new ExchangeRateReader(NICS_DUMMY_DATA_URL);
        float rate = xrReader.getExchangeRate("USD", "GBP", 2010, 6, 25);
        assertEquals(1.492714910151, rate, DELTA);
    }

    @Test
    public void checkUSDvsGBP20100625online() throws IOException, ParserConfigurationException, SAXException {
        ExchangeRateReader xrReader = new ExchangeRateReader("http://api.finance.xaviermedia.com/api/");
        float rate = xrReader.getExchangeRate("USD", "GBP", 2010, 6, 25);
        assertEquals(1.492714910151, rate, DELTA);
    }
}
