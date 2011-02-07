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

import org.nuxeo.qa.webdriver.driver.users.UserManagementFormPage;
import org.nuxeo.qa.webdriver.finder.ElementNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Sun Seng David TAN <stan@nuxeo.com>
 *
 */
public class CreateUsers extends LoginOk {

    /**
     * <ul>
     * <li>Creation of each user is done on the users & groups tab</li>
     * <li>For each user, we check that the user is not already by typing its
     * name and searching</li>
     * <li>Then we add the user by clicking on Create a new user with the
     * following informations:</li>
     * </ul>
     *
     * @throws ElementNotFoundException
     *
     */
    public String createUser(String userAndGroupLink, String username,
            String firstname, String lastname, String company, String email,
            String password, String group) throws ElementNotFoundException {
        if (isUserCreated(userAndGroupLink, username)) {
            return "Created";
        }
        driver.findElement(By.linkText(userAndGroupLink)).click();
        driver.findElement(By.id("createUserActionsForm:createUserButton")).click();
        UserManagementFormPage userMgtPage = PageFactory.initElements(driver, UserManagementFormPage.class);

        userMgtPage.createUser(username, firstname, lastname, company, email, password, group);

        if (isUserCreated(userAndGroupLink, username)) {
            return "Created";
        }

        return "Not Created";
    }

    protected boolean isUserCreated(String userAndGroupLink, String username) {
        driver.findElement(By.linkText(userAndGroupLink)).click();
        // search the user
        WebElement searchTextInput = driver.findElement(By.id("searchForm:searchText"));
        searchTextInput.clear();
        searchTextInput.sendKeys(username);
        driver.findElement(By.id("searchForm:searchButton")).click();
        try {
            driver.findElement(By.linkText(username));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

}
