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
import org.nuxeo.common.utils.FileUtils;

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

    private static final String DEFAULT_LOG = "test.NuxeoDMInstance";
    private static final Pattern versionPattern = Pattern.compile("\\s*Nuxeo DM ([0-9\\.]+)\\.*");
    private static final String language = "English (United States)";

    /**
     * Defines a default logger for the class.
     */
    private Log log = LogFactory.getLog(DEFAULT_LOG);

    // Interface with WebDriver
    private WebDriver driver;
    private JavascriptExecutor js;

    // Internal state
    protected String version = null;
    private boolean loggedin = false;
    private String dmlogin = null;

    /**
     * Constructor
     * @param mydriver the WebDriver object to be used
     * @param url the URL of the Nuxeo DM instance
     */
    public NuxeoDMInstance(WebDriver mydriver,String url) {

        driver=mydriver;
        js=(JavascriptExecutor)driver;

    }

    /**
     * Close driver
     */
    public void quit() {

        driver.quit();

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

    /**
     * Log in to the Nuxeo DM instance
     * @param login Nuxeo DM user name
     * @param password Nuxeo DM password
     */
    public void loginAs(String login, String password) {

        log.info("Starting login");

        driver.findElement(By.id("username")).sendKeys(login);
        driver.findElement(By.id("password")).sendKeys(password);
        List<WebElement> langopts = driver.findElements(By.xpath("//*[@id='language']/option"));
        boolean langfound = false;
        for (int i=0;i<langopts.size();i++) {
            if (langopts.get(i).getText().equals(language)) {
                langopts.get(i).setSelected();
                langfound = true;
                break;
            }
        }
        assertTrue(langfound,"Could not find language in list: "+language);
        driver.findElement(By.name("Submit")).click();

        String loggedinas = driver.findElement(By.className("userActions")).getText();
        assertTrue(loggedinas.contains("You are logged as "+login),"Login failed");

         // Save login name for future use
        dmlogin=login;
        loggedin=true;

        log.info("Login - done.");

    }

    /**
     * Log out of the Nuxeo DM instance
     */
    public void logout() {

        log.info("Starting logout");

        driver.findElement(By.linkText("Log out")).click();
        driver.findElement(By.id("username"));

        log.info("Logout - done.");

    }

    /**
     * Check ImgaMagick functionality
     */
    public void checkImageMagick() {

        log.info("Starting ImageMagick test");
        if (!loggedin) {
            log.error("You are not logged in");
            return;
        }

        // Create real file from resource
        File imgTest=null;
        try {
            imgTest = File.createTempFile("test_img",".jpg");
            imgTest.deleteOnExit();
            FileUtils.copyToFile(NuxeoDMInstance.class.getClassLoader().getResourceAsStream("test_img.jpg"),imgTest);
        } catch (IOException e) {
            log.error("Can't copy resource to temp file. Test aborted.");
            return;
        }

        
        String imgPath = imgTest.getAbsolutePath();
        String imgTitle = "TestImg-"+UUID.randomUUID().toString();

        // Go to personal workspace
        driver.findElement(By.linkText("Personal Workspace")).click();

        // Click "New"

        driver.findElement(By.xpath("//*[@id='documentActionSubviewUpperListForm']/div/ul/li[1]/a")).click();

        // Click "Picture"

        driver.findElement(By.linkText("Picture")).click();

        // Submit picture

        driver.findElement(By.xpath("//input[@id[contains(.,'nxw_title')]]")).sendKeys(imgTitle);
        driver.findElement(By.xpath("//input[@id[contains(.,'t_inputFileUpload:upload')]]")).sendKeys(new File(imgPath).getAbsolutePath());
        driver.findElement(By.xpath("//input[@value='Create']")).click();

        // Check that thumbnail is smaller than original

        driver.findElement(By.xpath("//div[@class='pictureItemSize']/table/tbody/tr/td[2]/input")).click();
        sleep(2000);
        RenderedWebElement original_img = (RenderedWebElement)driver.findElement(By.xpath("//img[@src[contains(.,'Original')]]"));
        Dimension original_size = original_img.getSize();

        driver.findElement(By.xpath("//div[@class='pictureItemSize']/table/tbody/tr/td[3]/input")).click();
        sleep(2000);
        RenderedWebElement thumbnail_img = (RenderedWebElement)driver.findElement(By.xpath("//img[@src[contains(.,'Thumbnail')]]"));
        Dimension thumbnail_size = thumbnail_img.getSize();

        assertTrue(thumbnail_size.getWidth()<original_size.getWidth(),"Thumbnail width is larger than original width");
        assertTrue(thumbnail_size.getWidth()!=0,"Thumbnail width is zero!");
        assertTrue(thumbnail_size.getHeight()<original_size.getHeight(),"Thumbnail height is larger than original height");
        assertTrue(thumbnail_size.getHeight()!=0,"Thumbnail height is zero!");

        // Go back to personal workspace

        driver.findElement(By.linkText("Back to document base")).click();
        driver.findElement(By.linkText("Personal Workspace")).click();

        // Delete document

        driver.findElement(By.linkText("Modified")).click();
        sleep(1000);
        List<WebElement> docspans = driver.findElements(By.xpath("//span[@id[contains(.,'title_')]]"));
        for (int i=0;i<docspans.size();i++) {
            if (docspans.get(i).getText().trim().equals(imgTitle)) {
                String docref = docspans.get(i).findElement(By.xpath("../..")).getAttribute("docref");
                // Alternate method
                //String docref = docspans.get(i).getAttribute("id").split("_")[1];
                //driver.findElement(By.xpath("//input[@onclick[contains(.,'"+docref+"')]]")).click();
                docspans.get(i).findElement(By.xpath("../../../../td/input")).click();
            }
        }
        // override delete confirmation function as clicking alert boxes isn't implemented in WebDriver yet
        js.executeScript("window.confirmDeleteDocuments=function(msg) {return true;}");
        driver.findElement(By.xpath("//input[@value='Delete']")).click();

        // Go back "home"

        driver.findElement(By.linkText("Back to document base")).click();

        log.info("ImageMagick test - done.");

    }


    /**
     * Check pdftohtml functionality
     */
    public void checkPDFtoHTML() {

        log.info("Starting PDFtoHTML test");
        if (!loggedin) {
            log.error("You are not logged in");
            return;
        }

        // Create real file from resource
        File pdfTest=null;
        try {
            pdfTest = File.createTempFile("test_pdf",".pdf");
            pdfTest.deleteOnExit();
            FileUtils.copyToFile(NuxeoDMInstance.class.getClassLoader().getResourceAsStream("test_pdf.pdf"),pdfTest);
        } catch (IOException e) {
            log.error("Can't copy resource to temp file. Test aborted.");
            return;
        }

        String pdfTitle = "TestPDF-"+UUID.randomUUID().toString();

        // Go to personal workspace

        driver.findElement(By.linkText("Personal Workspace")).click();

        // Click "New"

        driver.findElement(By.xpath("//*[@id='documentActionSubviewUpperListForm']/div/ul/li[1]/a")).click();

        // Click "File"

        driver.findElement(By.linkText("File")).click();

        // Submit PDF file

        driver.findElement(By.xpath("//input[@id[contains(.,'nxw_title')]]")).sendKeys(pdfTitle);
        driver.findElement(By.xpath("//input[@id[contains(.,'nxw_file_file:upload')]]")).sendKeys(pdfTest.getAbsolutePath());
        driver.findElement(By.id("document_create:nxl_file:nxw_file:nxw_file_file:choiceupload")).click();
        driver.findElement(By.xpath("//input[@value='Create']")).click();

        // Preview

        driver.findElement(By.linkText("Preview")).click();
        sleep(5000); // hopefully the preview will be rendered by then

        // Identify the preview frame
        // ( by scanning all the iframe metas for generator=pdftohtml )

        boolean previewFrameFound = false;
        int previewFrameIndex = -1;
        List<WebElement> iframes = driver.findElements(By.xpath("//iframe"));
        for (int i=0;i<iframes.size();i++) {
            driver.switchTo().frame(i);
            List<WebElement> metas = driver.findElements(By.xpath("//meta"));
            for (int j=0;j<metas.size();j++) {
                if ((metas.get(j).getAttribute("name").equals("generator"))&&(metas.get(j).getAttribute("content").startsWith("pdftohtml"))) {
                    previewFrameIndex = i;
                    previewFrameFound = true;
                    break;
                }
            }
            driver.switchTo().defaultContent();
            if (previewFrameFound) break;
        }
        assertTrue(previewFrameFound,"Preview iframe not found");

        driver.switchTo().frame(previewFrameIndex);

        // Check that preview is working

        String refText = driver.findElement(By.xpath("//span[@class='ft0']/b")).getText();
        assertTrue(refText.trim().equals("Test Document"),"Cannot find reference string in preview -> pdftohtml is broken");

        RenderedWebElement bg_img = (RenderedWebElement)driver.findElement(By.xpath("//img[@src[contains(.,'index001')]]"));
        Dimension bg_size = bg_img.getSize();
        assertTrue((bg_size.getWidth()>0)&&(bg_size.getHeight()>0),"Cannot find image in preview -> ghostscript is broken");

        driver.switchTo().defaultContent();

        // Go back to personal workspace

        driver.findElement(By.linkText("Back to document base")).click();
        driver.findElement(By.linkText("Personal Workspace")).click();

        // Delete document

        driver.findElement(By.linkText("Modified")).click();
        sleep(1000);
        List<WebElement> docspans = driver.findElements(By.xpath("//span[@id[contains(.,'title_')]]"));
        for (int i=0;i<docspans.size();i++) {
            if (docspans.get(i).getText().trim().equals(pdfTitle)) {
                String docref = docspans.get(i).findElement(By.xpath("../..")).getAttribute("docref");
                // Alternate method
                //String docref = docspans.get(i).getAttribute("id").split("_")[1];
                //driver.findElement(By.xpath("//input[@onclick[contains(.,'"+docref+"')]]")).click();
                docspans.get(i).findElement(By.xpath("../../../../td/input")).click();
            }
        }
        // override delete confirmation function as clicking alert boxes isn't implemented in WebDriver yet
        js.executeScript("window.confirmDeleteDocuments=function(msg) {return true;}");
        driver.findElement(By.xpath("//input[@value='Delete']")).click();

        // Go back "home"

        driver.findElement(By.linkText("Back to document base")).click();

        log.info("PDFtoHTML test - done.");

    }


    /**
     * Check OpenOffice functionality
     */
    public void checkOpenOffice() {

        log.info("Starting OpenOffice test");
        if (!loggedin) {
            log.error("You are not logged in");
            return;
        }

        // Create real file from resource
        File oooTest=null;
        try {
            oooTest = File.createTempFile("test_ooo",".odt");
            oooTest.deleteOnExit();
            FileUtils.copyToFile(NuxeoDMInstance.class.getClassLoader().getResourceAsStream("test_ooo.odt"),oooTest);
        } catch (IOException e) {
            log.error("Can't copy resource to temp file. Test aborted.");
            return;
        }

        String oooTitle = "TestOOo-"+UUID.randomUUID().toString();

        // Go to personal workspace

        driver.findElement(By.linkText("Personal Workspace")).click();

        // Click "New"

        driver.findElement(By.xpath("//*[@id='documentActionSubviewUpperListForm']/div/ul/li[1]/a")).click();

        // Click "File"

        driver.findElement(By.linkText("File")).click();

        // Submit OOo file

        driver.findElement(By.xpath("//input[@id[contains(.,'nxw_title')]]")).sendKeys(oooTitle);
        driver.findElement(By.xpath("//input[@id[contains(.,'nxw_file_file:upload')]]")).sendKeys(oooTest.getAbsolutePath());
        driver.findElement(By.id("document_create:nxl_file:nxw_file:nxw_file_file:choiceupload")).click();
        driver.findElement(By.xpath("//input[@value='Create']")).click();

        // Preview

        driver.findElement(By.linkText("Preview")).click();
        sleep(5000); // hopefully the preview will be rendered by then

        // Identify the preview frame
        // ( by scanning all the iframe metas for generator=pdftohtml )

        boolean previewFrameFound = false;
        int previewFrameIndex = -1;
        List<WebElement> iframes = driver.findElements(By.xpath("//iframe"));
        for (int i=0;i<iframes.size();i++) {
            driver.switchTo().frame(i);
            List<WebElement> metas = driver.findElements(By.xpath("//meta"));
            for (int j=0;j<metas.size();j++) {
                if ((metas.get(j).getAttribute("name").equals("generator"))&&(metas.get(j).getAttribute("content").startsWith("pdftohtml"))) {
                    previewFrameIndex = i;
                    previewFrameFound = true;
                    break;
                }
            }
            driver.switchTo().defaultContent();
            if (previewFrameFound) break;
        }
        assertTrue(previewFrameFound,"Preview iframe not found");

        driver.switchTo().frame(previewFrameIndex);

        // Check that preview is working

        String refText = driver.findElement(By.xpath("//span[@class='ft0']/b")).getText();
        assertTrue(refText.trim().equals("Test Document"),"Cannot find reference string in preview -> OpenOffice is broken");

        RenderedWebElement bg_img = (RenderedWebElement)driver.findElement(By.xpath("//img[@src[contains(.,'index001')]]"));
        Dimension bg_size = bg_img.getSize();
        assertTrue((bg_size.getWidth()>0)&&(bg_size.getHeight()>0),"Cannot find image in preview -> ghostscript is broken");

        driver.switchTo().defaultContent();

        // Go back to personal workspace

        driver.findElement(By.linkText("Back to document base")).click();
        driver.findElement(By.linkText("Personal Workspace")).click();

        // Delete document

        driver.findElement(By.linkText("Modified")).click();
        sleep(1000);
        List<WebElement> docspans = driver.findElements(By.xpath("//span[@id[contains(.,'title_')]]"));
        for (int i=0;i<docspans.size();i++) {
            if (docspans.get(i).getText().trim().equals(oooTitle)) {
                String docref = docspans.get(i).findElement(By.xpath("../..")).getAttribute("docref");
                // Alternate method
                //String docref = docspans.get(i).getAttribute("id").split("_")[1];
                //driver.findElement(By.xpath("//input[@onclick[contains(.,'"+docref+"')]]")).click();
                docspans.get(i).findElement(By.xpath("../../../../td/input")).click();
            }
        }
        // override delete confirmation function as clicking alert boxes isn't implemented in WebDriver yet
        js.executeScript("window.confirmDeleteDocuments=function(msg) {return true;}");
        driver.findElement(By.xpath("//input[@value='Delete']")).click();

        // Go back "home"

        driver.findElement(By.linkText("Back to document base")).click();

        log.info("OpenOffice test - done.");

    }

}
