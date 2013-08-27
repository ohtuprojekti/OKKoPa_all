package easyb;

import org.openqa.selenium.*
import org.openqa.selenium.htmlunit.*;
import fi.helsinki.cs.okkopa.*
import fi.helsinki.cs.okkopa.database.OkkopaDatabase;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;

description "Adding anonymous references"
 
scenario "User gives empty reference code and username", {
    
    Selenium selenium
    
    given "Viitteiden rekisteröinti is selected",{       
        WebDriver driver = new HtmlUnitDriver(true);
        String baseUrl = "http://localhost:8081/";
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
        selenium.open("/");
        selenium.click("link=Viitteiden rekisteröinti");
        selenium.waitForPageToLoad("30000");
    }
 
    when "empty form is submitted", {       
        selenium.click("name=Lähetä");
        selenium.waitForPageToLoad("30000");
    }
 
    then "user will see an error message", {
        selenium.getText("//p[3]").shouldBe "- Käyttäjätunnus pitää olla yli kaksi merkkiä pitkä./ Username must be at least two characters."
        selenium.stop();
    }
}

scenario "User gives incorrect reference code with correct username", {
    
    Selenium selenium
    
    given "Viitteiden rekisteröinti is selected",{
        WebDriver driver = new HtmlUnitDriver(true);
        String baseUrl = "http://localhost:8081/";
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
        selenium.open("/");
        selenium.click("link=Viitteiden rekisteröinti");
        selenium.waitForPageToLoad("30000");
    }
 
    when "incorrect reference code is submitted", { 
        selenium.typeKeys("name=id", "testi");
	selenium.typeKeys("name=code", "shgsh57");
        selenium.click("name=Lähetä");
        selenium.waitForPageToLoad("30000");
    }
 
    then "user will see an error message", {
        selenium.getText("//p[3]").shouldBe "- Kirjoitit viitteesi väärin, tarkista oikeinkirjoitus./ Reference is incorrect. Pleace check it and try again."
        selenium.stop();
    }
}


//
//scenario "User gives correct reference code and username", {
//    
//    Selenium selenium
//    
//    given "Viitteiden rekisteröinti is selected",{
//        WebDriver driver = new HtmlUnitDriver(true);
//        String baseUrl = "http://localhost:8081/";
//        selenium = new WebDriverBackedSelenium(driver, baseUrl);
//        selenium.open("/");
//        selenium.click("link=Viitteiden rekisteröinti");
//        selenium.waitForPageToLoad("30000");
//    }
// 
//    when "correct reference code is submitted", { 
//        
//        newReference = new fi.helsinki.cs.okkopa.reference.Reference(6);
//        stringReference = newReference.getReference();
//        
//        db = new OkkopaDatabase();
//        db.addQRCode(stringReference); 
//        
//        selenium.typeKeys("name=id", "testi");
//	selenium.typeKeys("name=code", ""+stringReference);
//        selenium.click("name=Lähetä");
//        selenium.waitForPageToLoad("30000");
//    }
// 
//    then "reference code is registered", {
//        selenium.getText("//p[3]").shouldBe "- homma OK! Koe tulee sinulle kunhan se on tarkistettu."
//        selenium.stop();
//    }
//}