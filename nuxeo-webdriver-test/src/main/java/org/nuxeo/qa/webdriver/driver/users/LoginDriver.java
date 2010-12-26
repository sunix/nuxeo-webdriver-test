/*
 * (C) Copyright 2009 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Sun Seng David TAN <stan@nuxeo.com>
 */
package org.nuxeo.qa.webdriver.driver.users;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Webdriver login driver. Connect to the login page and login.
 *
 * @author Sun Seng David TAN <stan@nuxeo.com>
 *
 */
public class LoginDriver {

    WebDriver driver;

    URL baseUrl;

    public LoginDriver(WebDriver webdriver, URL basedUrl) {
        driver = webdriver;
        baseUrl = basedUrl;
    }

    /**
     * login.jsp
     *
     * @param username
     * @param password
     * @param language is a value of one of the options in the language select
     *            box. For example, en_US or fr
     */
    public void login(String username, String password, String language) {

        try {
            driver.get(baseUrl.toString() + "/logout");
        } catch (Exception e) {
            // trying but ok if failing
            e.printStackTrace();
        }
        driver.get(baseUrl.toString());
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        if (language != null) {
            List<WebElement> list = driver.findElement(By.id("language")).findElements(
                    By.tagName("option"));
            for (WebElement webElement : list) {
                if (language.equals(webElement.getValue())) {
                    webElement.setSelected();
                    break;
                }
            }
        }
        driver.findElement(By.name("Submit")).click();
    }

}
