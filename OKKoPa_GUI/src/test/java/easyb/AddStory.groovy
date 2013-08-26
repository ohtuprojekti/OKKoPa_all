package stories

import org.openqa.selenium.*
import org.openqa.selenium.htmlunit.*;
import fi.helsinki.cs.okkopa.*
import fi.helsinki.cs.okkopa.database.OkkopaDatabase
import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.DefaultSelenium

description "Adding references"
 
scenario "User can give right reference with right username", {
    
    Selenium selenium
    
    given "Proper url given",{
        WebDriver driver = new HtmlUnitDriver(true);
        String baseUrl = "http://localhost:8080/";
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
    }
 
    when "moo", {
        selenium.open("/");
        selenium.click("link=Viitteiden rekisteröinti");
        selenium.waitForPageToLoad("30000");
        selenium.click("name=Lähetä");
        selenium.waitForPageToLoad("30000");
    }
 
    then "user will be logged in to system", {
        selenium.getText("//p[3]").shouldBe "- Käyttäjätunnus pitää olla yli kaksi merkkiä pitkä."
        selenium.stop();
    }
}