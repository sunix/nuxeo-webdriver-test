import org.nuxeo.tools.testing.webdriver.*;


class example {

    public static void main(String args[]) {

        NuxeoDMInstance nuxeodm = NuxeoDMInstanceFactory.getInstance(DriverFactory.getDriver("firefox"),"http://localhost:8080/nuxeo/");
        if (nuxeodm==null) {
            return;
        } 

        System.out.println("Got Nuxeo DM instance");
        nuxeodm.loginAs("mylogin","mypassword");
        nuxeodm.checkImageMagick();
        nuxeodm.checkPDFtoHTML();
        nuxeodm.checkOpenOffice();

        nuxeodm.quit();

    }

}
