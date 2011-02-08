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

import org.nuxeo.qa.webdriver.pages.users.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebdriverSample {
    public static void main(String[] args) throws Exception {
        WebDriver driver = new FirefoxDriver();

        LoginPage loginPage = LoginPage.getLoginPage(driver,
                "http://localhost:8080/nuxeo");
        loginPage.login("Administrator", "Administrator",
                "English (United States)");
        driver.findElement(By.linkText("Workspaces")).click();
        driver.findElement(By.linkText("test")).click();

        driver.findElement(
                By.id("document_content:nxl_document_listing_ajax:nxw_listing_ajax_selection_box_with_current_document")).click();

        boolean wait = true;
        while (wait) {
            try {
                driver.findElement(
                        By.id("document_content:clipboardActionsTable_0_0:3:clipboardActionsButton")).isEnabled();
                wait = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        driver.findElement(
                By.id("document_content:clipboardActionsTable_0_0:3:clipboardActionsButton")).click();

        driver.switchTo().alert().accept();

        driver.quit();
    }
}