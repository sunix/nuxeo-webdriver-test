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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.By;




/**
 * Helper class to drive tests on a Nuxeo DM 5.3.2 instance.
 *
 * @author Mathieu Guillaume <mg@nuxeo.com>
 */
public class NuxeoDM532Instance extends NuxeoDMInstance {

    /**
     * Constructor
     * @param mydriver the WebDriver object to be used
     * @param url the URL of the Nuxeo DM instance
     */
    public NuxeoDM532Instance(WebDriver mydriver,String url) {

        super(mydriver,url);
        version="5.3.2";

    }

}
