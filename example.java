import org.nuxeo.tools.testing.webdriver.*;


class example {

    public static void main(String args[]) {

        NuxeoDMInstance nuxeodm = new NuxeoDMInstance(DriverFactory.getDriver("firefox"),"https://intranet.nuxeo.com/");

    }

}
