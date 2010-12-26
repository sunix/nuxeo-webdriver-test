package org.nuxeo.qa.webdriver;

import java.net.URL;
import org.nuxeo.qa.webdriver.driver.users.LoginDriver;
import org.nuxeo.qa.webdriver.finder.FindElementUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebdriverSample {
    public static void main(String[] args) throws Exception {
        WebDriver driver = new FirefoxDriver();

        LoginDriver logindriver = new LoginDriver(driver, new URL(
                "http://localhost:8080/nuxeo/"));
        logindriver.login("Administrator", "Administrator", "en_US");

        driver.findElement(By.linkText("Bree Van de Kaamp")).click();
        driver.findElement(By.linkText("Test incoming pia document")).click();
        driver.findElement(By.linkText("Distribute")).click();

        WebElement nxw_reponse_recipients_suggest = driver.findElement(By.id("distribution_participants:nxl_cm_participants:nxw_reponse_recipients_suggest"));
        nxw_reponse_recipients_suggest.sendKeys("Bre");

        WebElement ajaxUserListElement = new FindElementUntil(
                driver,
                By.xpath("//table[@id='distribution_participants:nxl_cm_participants:nxw_reponse_recipients_suggestionBox:suggest']/tbody/tr[1]/td[2]")).find();
        String value = ajaxUserListElement.getText();
        System.out.println(value);

        driver.quit();
    }
}