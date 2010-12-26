package org.nuxeo.qa.webdriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebdriverSample {
    public static void main(String[] args) throws Exception {
        WebDriver driver = new FirefoxDriver();

        driver.findElement(By.id("username")).sendKeys("Administrator");
        driver.findElement(By.id("password")).sendKeys("Administrator");
        String language = "en_US";
        List<WebElement> list = driver.findElement(By.id("language")).findElements(
                By.tagName("option"));
        for (WebElement webElement : list) {
            if (language.equals(webElement.getValue())) {
                webElement.setSelected();
                break;
            }
        }
        driver.findElement(By.name("Submit")).click();

        driver.quit();
    }
}