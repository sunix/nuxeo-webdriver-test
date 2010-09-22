package org.nuxeo.tools.testing.webdriver;


import java.lang.RuntimeException;
import java.net.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;


/**
 * DriverFactory - a class to simplify getting a WebDriver or RemoteWebDriver instance.
 *
 * @author Mathieu Guillaume <mg@nuxeo.com>
 * @see WebDriver
 * @see RemoteWebDriver
 */
public class DriverFactory {

    private static final String DEFAULT_BROWSER="firefox";
    private static final String DEFAULT_PLATFORM="any";
    private static final String DEFAULT_LOG="test.DriverFactory";

    /**
     * Defines a default logger for the class.
     */
    private static Log log = LogFactory.getLog(DEFAULT_LOG);

    /**
     * Creates a WebDriver instance with default values (firefox,local).
     */
    public static WebDriver getDriver() {

        return getDriver(DEFAULT_BROWSER);

    }

    /**
     * Creates a local WebDriver instance.
     * @param browser the chosen browser.
     */
    public static WebDriver getDriver(String browser) {

        return getDriver(browser,null,null);

    }

    /**
     * Creates a remote WebDriver instance.
     * @param browser the chosen browser.
     * @param url the URL of the webdriver server.
     */
    public static WebDriver getDriver(String browser,String url) {

        return getDriver(browser,null,url);

    }

    /**
     * Creates a remote WebDriver instance.
     * @param browser the chosen browser.
     * <br>Permitted values are: firefox, ie, chrome, htmlunit, remote-firefox, remote-ie, remote-chrome, remote-htmlunit
     * <br>The other parameters are unused if the browser isn't remote.
     * @param platform the chosen platform.
     * <br>Permitted values are: any, unix, windows, xp, vista, mac
     * @param url the URL of the webdriver server.
     */
    public static WebDriver getDriver(String browser,String platform,String url) {

        WebDriver driver=null;

        if (browser.startsWith("remote-")) {

            DesiredCapabilities capabilities = null;

            // Try to create a remote browser with the given parameters (browser, platform, url)
            //
            // The idea is to eventually remove the url parameter and let the driver pick it
            // from a list of matching nodes.

            browser = browser.substring(7);
            if (browser.equals("firefox")) {
                capabilities = DesiredCapabilities.firefox();
            } else if (browser.equals("ie")) {
                capabilities = DesiredCapabilities.internetExplorer();
            } else if (browser.equals("chrome")) {
                capabilities = DesiredCapabilities.chrome();
            } else if (browser.equals("htmlunit")) {
                capabilities = DesiredCapabilities.htmlUnit();
            } else {
                log.error("Browser "+browser+" not supported, choose one of firefox, ie, chrome, htmlunit or remote-*");
                return null;
            }

            if (platform==null) {
                platform=DEFAULT_PLATFORM;
            }

            if (platform.equals("any")) {
                capabilities.setPlatform(Platform.ANY);
            } else if (platform.equals("unix")) {
                capabilities.setPlatform(Platform.UNIX);
            } else if (platform.equals("windows")) {
                capabilities.setPlatform(Platform.WINDOWS);
            } else if (platform.equals("xp")) {
                capabilities.setPlatform(Platform.XP);
            } else if (platform.equals("vista")) {
                capabilities.setPlatform(Platform.VISTA);
            } else if (platform.equals("mac")) {
                capabilities.setPlatform(Platform.MAC);
            } else {
                log.error("Platform "+platform+" not supported, choose one of any, unix, windows, xp, vista, mac");
                return null;
            }

            capabilities.setJavascriptEnabled(true);

            URL driverURL = null;
            try {
                driverURL = new URL(url);
            } catch (MalformedURLException e) {
                log.error("Driver URL "+url+" is invalid");
                log.error(e.getMessage());
                return null;
            }

            RemoteWebDriver remoteDriver = null;
            try {
                remoteDriver = new RemoteWebDriver(driverURL,capabilities);
            } catch (Exception e) {
                log.error("Could not create remote driver");
                log.error(e.getMessage());
                return null;
            }

            Capabilities realCapabilities = remoteDriver.getCapabilities();
            log.debug("Remote browser: "+realCapabilities.getBrowserName());
            log.debug("Remote version: "+realCapabilities.getVersion());
            log.debug("Remote javascript: "+Boolean.toString(realCapabilities.isJavascriptEnabled()));
            log.debug("Remote platform: "+realCapabilities.getPlatform().toString());

            driver = (WebDriver)remoteDriver;

        // Create a local browser
        } else if (browser.equals("firefox")) {
            try {
                driver = (WebDriver)new FirefoxDriver();
            } catch (Exception e) {
                log.error("Could not create Firefox driver");
                log.error(e.getMessage());
                return null;
            }
        } else if (browser.equals("ie")) {
            try {
                driver = (WebDriver)new InternetExplorerDriver();
            } catch (Exception e) {
                log.error("Could not create Internet Explorer driver");
                log.error(e.getMessage());
                return null;
            }
        } else if (browser.equals("chrome")) {
            try {
            driver = (WebDriver)new ChromeDriver();
            } catch (Exception e) {
                log.error("Could not create Chrome driver");
                log.error(e.getMessage());
                return null;
            }
        } else if (browser.equals("htmlunit")) {
            try {
                driver = (WebDriver)new HtmlUnitDriver();
            } catch (Exception e) {
                log.error("Could not create HtmlUnit driver");
                log.error(e.getMessage());
                return null;
            }
        } else {
            log.error("Browser "+browser+" not supported, choose one of firefox, ie, chrome, htmlunit or remote-*");
            return null;
        }

        return driver;

    }

}

