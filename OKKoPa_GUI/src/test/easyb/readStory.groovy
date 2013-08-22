//package stories
//
//import org.openqa.selenium.*
//import org.openqa.selenium.htmlunit.*;
//import fi.helsinki.cs.okkopa.*
//import fi.helsinki.cs.okkopa.database.OkkopaDatabase
//
//description "Adding references"
// 
//scenario "User can give right reference with right username", {
//    
//    
//    given "Proper url given",{
//        
//        driver = new HtmlUnitDriver();
//        driver.get("http://localhost:8081/OKKoPa_GUI/list");
//    }
// 
//    when "moo", {
//        username = driver.findElement(By.name("id"));
//        reference = driver.findElement(By.name("code"));
//        
//        ref = new fi.helsinki.cs.okkopa.reference.Reference(6);
//        string = ref.getReference();
//        
//        db = new OkkopaDatabase();
//        db.addQRCode(string);
//
//        username.sendKeys("testinukke");
//        reference.sendKeys(string);
//        
//        element = driver.findElement(By.linkText("Lähetä"));
//        element.click();
//    }
// 
//    then "user will be logged in to system", {
//        driver.getPageSource().contains("homma OK!").shouldBe true
//    }
//}