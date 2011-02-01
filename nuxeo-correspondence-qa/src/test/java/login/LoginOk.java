/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
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
package login;

import org.junit.After;
import org.junit.Before;
import org.nuxeo.qa.behaviourdriven.BehavioursCase;
import org.nuxeo.qa.webdriver.driver.users.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author Sun Seng David TAN <stan@nuxeo.com>
 * 
 */
public class LoginOk extends BehavioursCase {
    WebDriver driver;

    LoginPage loginPage;

    @Before
    public void init() {
        driver = new FirefoxDriver();
    }

    @After
    public void quit() {
        driver.quit();
        driver = null;
    }

    public String connectToLoginPage(String nuxeourl) {
        loginPage = LoginPage.getLoginPage(driver, nuxeourl);
        if (loginPage != null) {
            return "connects";
        }
        return "doesn't connect";
    }

    public String login(String username, String password, String language) {
        loginPage.login(username, password, language);
        return "login";
    }

    public String isConnected() {
        String loggedText = "You are logged as";
        if (driver.getPageSource().contains(loggedText)) {
            return "connected";
        }
        return "not connected";
    }

}
