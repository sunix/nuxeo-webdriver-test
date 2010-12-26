package org.nuxeo.qa.webdriver;

import java.net.URL;
import org.nuxeo.qa.webdriver.driver.users.LoginDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebdriverSample {
    public static void main(String[] args) throws Exception {
        WebDriver driver = new FirefoxDriver();

        LoginDriver logindriver = new LoginDriver(driver, new URL(
                "http://localhost:8080/nuxeo/"));
        logindriver.login("Administrator", "Administrator", "en_US");

        driver.quit();
    }
}