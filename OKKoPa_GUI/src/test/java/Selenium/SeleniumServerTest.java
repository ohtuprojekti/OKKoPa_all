//package Selenium;
//
//import com.thoughtworks.selenium.Selenium;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebDriverBackedSelenium;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
//import org.openqa.selenium.server.SeleniumServer;
//
//public class SeleniumServerTest {
//
//    Selenium selenium;
//    SeleniumServer server;
//
//    @Before
//    public void setUp() throws Exception {
//        server = new SeleniumServer();
//        server.start();
//    }
//
//    @Test
//    public void testConnection() throws Exception {
//        System.out.println(server.getPort());
//        WebDriver driver = new HtmlUnitDriver(true);
//        String baseUrl = "http://localhost:5000";
//        selenium = new WebDriverBackedSelenium(driver, baseUrl);
////        selenium.open("/add");
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        selenium.stop();
//        server.stop();
//    }
//}
