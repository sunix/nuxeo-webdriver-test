package org.nuxeo.tools.testing.webdriver;

import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.net.*;
import java.awt.Dimension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.By;




/**
 * Helper class to drive tests on a Nuxeo DM instance.
 *
 * @author Mathieu Guillaume <mg@nuxeo.com>
 */
public class NuxeoDMInstance {

    private static final String DEFAULT_LOG="test.NuxeoDMInstance";

    /**
     * Defines a default logger for the class.
     */
    private Log log = LogFactory.getLog(DEFAULT_LOG);

    // Internal state
    private String version = null;
    private boolean loggedin = false;
    private String dmlogin = null;
    private String dmpass = null;

    /**
     * Constructor
     * @param driver the WebDriver object to be used
     * @param url the URL of the Nuxeo DM instance
     */
    public NuxeoDMInstance(WebDriver driver,String url) {

        try {
            driver.get(url);
        } catch (Exception e) {
            log.error("Could not connect to the Nuxeo DM instance.");
            throw new RuntimeException(e.getMessage());
        }

        // Grab Nuxeo DM version
        String nuxeoFullVersion = driver.findElement(By.xpath("//div[@class='loginLegal ' and contains(.,'Nuxeo DM')]")).getText();
        log.debug("***"+nuxeoFullVersion+"***");
    }

    /**
     * Utility method : sleep
     * @param msec time to sleep in milliseconds
     */
    private void sleep(int msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            log.warn("Sleep interrupted! ARGH!!!");
        }
    }

    /**
     * Utility method : evaluate a condition and raise an exception if false
     * @param cond condition to evaluate
     * @param msg message to display if the condition is false
     */
    private void assertTrue(boolean cond,String msg) {
        if (!cond) {
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }


    /**
     * Utility method : evaluate a condition and raise an exception if true
     * @param cond condition to evaluate
     * @param msg message to display if the condition is true
     */
    private void assertFalse(boolean cond,String msg) {
        if (cond) {
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }







}
