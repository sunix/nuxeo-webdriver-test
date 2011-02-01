import org.nuxeo.tools.testing.webdriver.*;


class example {

    public static void main(String args[]) {

        NuxeoDMInstance nuxeodm = NuxeoDMInstanceFactory.getInstance(DriverFactory.getDriver("firefox"),"https://intranet.nuxeo.com/nuxeo/");
        if (nuxeodm==null) {
            return;
        } 

        System.out.println("Got Nuxeo DM instance");
        nuxeodm.loginAs("monitor","monitor://nx");
        //nuxeodm.checkImageMagick();
        //nuxeodm.checkPDFtoHTML();
        //nuxeodm.checkOpenOffice();
        nuxeodm.logout();

        nuxeodm.quit();

    }

}
