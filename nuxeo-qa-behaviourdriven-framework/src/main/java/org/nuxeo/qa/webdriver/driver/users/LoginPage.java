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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Nuxeo default login page. Connect to the login page and login.
 *
 * @author Sun Seng David TAN <stan@nuxeo.com>
 *
 */
public class LoginPage {

    WebDriver driver;

    @FindBy(id = "username")
    WebElement usernameInputTextBox;

    @FindBy(id = "password")
    WebElement passwordInputTextBox;

    @FindBy(name = "Submit")
    WebElement submitButton;

    @FindBy(id = "language")
    WebElement languageSelectBox;

    public LoginPage(WebDriver webdriver) {
        driver = webdriver;
    }

    /**
     * login.jsp
     *
     * @param baseUrl the url to use
     * @param username
     * @param password
     * @param language is a value of one of the options in the language select
     *            box. For example, en_US or fr
     */
    public void login(String baseUrl, String username, String password,
            String language) {

        try {
            driver.get(baseUrl + "/logout");
        } catch (Exception e) {
            // trying but ok if failing
        }
        driver.get(baseUrl);

        usernameInputTextBox.sendKeys(username);
        passwordInputTextBox.sendKeys(password);

        if (language != null) {
            List<WebElement> list = languageSelectBox.findElements(By.tagName("option"));
            for (WebElement webElement : list) {
                if (language.trim().equals(webElement.getText().trim())) {
                    webElement.setSelected();
                    break;
                }
            }
        }
        submitButton.click();
    }

}
