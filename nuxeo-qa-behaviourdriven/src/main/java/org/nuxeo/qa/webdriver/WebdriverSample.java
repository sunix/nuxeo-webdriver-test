/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License (LGPL)
 * version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * Contributors: Sun Seng David TAN <stan@nuxeo.com>
 */
package org.nuxeo.qa.webdriver;

import org.nuxeo.qa.webdriver.driver.users.LoginPage;
import org.nuxeo.qa.webdriver.finder.FindElementUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;

public class WebdriverSample {
    public static void main(String[] args) throws Exception {
        WebDriver driver = new FirefoxDriver();

        LoginPage loginPage = LoginPage.getLoginPage(driver, "http://localhost:8080/nuxeo");
        loginPage.login("Administrator", "Administrator",
                "English (United States)");

        driver.findElement(By.linkText("Bree Van de Kaamp")).click();
        driver.findElement(By.linkText("Test incoming pia document")).click();
        driver.findElement(By.linkText("Distribute")).click();

        WebElement nxw_reponse_recipients_suggest = driver.findElement(By.id("distribution_participants:nxl_cm_participants:nxw_reponse_recipients_suggest"));
        nxw_reponse_recipients_suggest.sendKeys("Bre");

        WebElement ajaxUserListElement = new FindElementUntil(
                driver,
                By.xpath("//table[@id='distribution_participants:nxl_cm_participants:nxw_reponse_recipients_suggestionBox:suggest']/tbody/tr[1]/td[2]")).find();
        String value = ajaxUserListElement.getText();

        driver.quit();
    }
}