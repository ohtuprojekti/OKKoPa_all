package easyb;

import org.openqa.selenium.*
import org.openqa.selenium.htmlunit.*;
import fi.helsinki.cs.okkopa.*
import fi.helsinki.cs.okkopa.shared.Settings;
import fi.helsinki.cs.okkopa.database.OkkopaDatabase;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;

//description "Adding anonymous references"
// 
//scenario "User gives empty reference code and username", {
//   
//    Selenium selenium
//    
//    given "Viitteiden rekisteröinti is selected",{     
//        WebDriver driver = new HtmlUnitDriver(true);
//        String baseUrl = "http://localhost:8080";
//        selenium = new WebDriverBackedSelenium(driver, baseUrl);
//        selenium.open("/add");
//    }
// 
//    when "empty form is submitted", {  
//        selenium.click("css=input[type=\"submit\"]");
//        selenium.waitForPageToLoad("30000");
//    }
// 
//    then "user will see an error message", {
//        selenium.getText("//p[3]").shouldBe "- Käyttäjätunnus pitää olla yli kaksi merkkiä pitkä. / Username must be at least two characters long."
//        selenium.stop();
//    }
//}
//
//scenario "User gives incorrect reference code with correct username", {
//    
//    Selenium selenium
//    
//    given "Viitteiden rekisteröinti is selected",{
//        WebDriver driver = new HtmlUnitDriver(true);
//        String baseUrl = "http://localhost:8080";
//        selenium = new WebDriverBackedSelenium(driver, baseUrl);
//        selenium.open("/add");
//    }
// 
//    when "incorrect reference code is submitted", { 
//        selenium.typeKeys("name=id", "testi");
//	selenium.typeKeys("name=code", "shgsh57");
//        selenium.click("css=input[type=\"submit\"]");
//        selenium.waitForPageToLoad("30000");
//    }
// 
//    then "user will see an error message", {
//        selenium.getText("//p[3]").shouldBe "- Kirjoitit viitteesi väärin, tarkista oikeinkirjoitus. / Reference is incorrect. Pleace check it and try again."
//        selenium.stop();
//    }
//}
//
//scenario "User gives correctly formatted not foundable reference code with correct username", {
//    
//    Selenium selenium
//    
//    given "Viitteiden rekisteröinti is selected",{
//        WebDriver driver = new HtmlUnitDriver(true);
//        String baseUrl = "http://localhost:8080";
//        selenium = new WebDriverBackedSelenium(driver, baseUrl);
//        selenium.open("/add");
//    }
// 
//    when "correctly formatted not foundable reference code is submitted", { 
//        selenium.typeKeys("name=id", "testi");
//        
//        fi.helsinki.cs.okkopa.reference.Reference ref = new fi.helsinki.cs.okkopa.reference.Reference(6);
//        String code = ref.getReference();
//        
//	selenium.typeKeys("name=code", code);
//        selenium.click("css=input[type=\"submit\"]");
//        selenium.waitForPageToLoad("30000");
//    }
// 
//    then "user will see an error message", {
//        selenium.getText("//p[3]").shouldBe "- Antamaasi viitettä ei löytynyt. / Reference does not exist."
//        selenium.stop();
//    }
//}