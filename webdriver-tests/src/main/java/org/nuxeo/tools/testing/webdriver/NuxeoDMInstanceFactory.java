package org.nuxeo.tools.testing.webdriver;

import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.TimeUnit;
import java.net.*;
import java.awt.Dimension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.By;




/**
 * Helper class to create a NuxeoDMInstance instance.
 *
 * @author Mathieu Guillaume <mg@nuxeo.com>
 */
public class NuxeoDMInstanceFactory {

    private static final String DEFAULT_LOG="test.NuxeoDMInstanceFactory";
    private static final Pattern versionPattern = Pattern.compile("\\s*Nuxeo DM ([0-9\\.]+)\\.*");

    /**
     * Defines a default logger for the class.
     */
    private static Log log = LogFactory.getLog(DEFAULT_LOG);

    /**
     * Returns a NuxeoDMInstance tailored to the Nuxeo DM version at url
     * @param driver the WebDriver object to be used
     * @param url the URL of the Nuxeo DM instance
     * @return a NuxeoDMInstance for the site at 'url'
     */
    public static NuxeoDMInstance getInstance(WebDriver driver,String url) {

        if (driver==null) {
            log.error("No WebDriver instance available.");
            return null;
        }

        try {
            driver.get(url);
        } catch (Exception e) {
            log.error("Could not connect to the Nuxeo DM instance.");
            log.error(e.getMessage());
            driver.quit();
            return null;
        }

        // Grab Nuxeo DM version
        String nuxeoFullVersion = null;
        try {
            nuxeoFullVersion = driver.findElement(By.xpath("//div[@class[contains(.,'loginLegal')] and contains(.,'Nuxeo DM')]")).getText();
        } catch (Exception e) {
            log.error("Not a Nuxeo DM instance or instance not responding.");
            log.error(e.getMessage());
            driver.quit();
            return null;
        }
        Matcher versionMatcher = versionPattern.matcher(nuxeoFullVersion);
        if (!versionMatcher.matches()) {
            log.error("No version string could be identified");
            driver.quit();
            return null;
        }
        String version = versionMatcher.group(1);
        log.debug("Found version "+version);

        if (version.equals("5.3.2")) {
            return (NuxeoDMInstance)new NuxeoDM532Instance(driver,url);
        } else if (version.equals("5.4.0")||version.equals("5.4.0.1")) {
            return (NuxeoDMInstance)new NuxeoDM540Instance(driver,url);
        } else {
            log.error("Nuxeo DM version "+version+" not supported");
            driver.quit();
            return null;
        }
    }

}
