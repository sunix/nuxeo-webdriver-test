import org.nuxeo.tools.testing.webdriver.*;


class example {

    public static void main(String args[]) {

        NuxeoDMInstance nuxeodm = NuxeoDMInstanceFactory.getInstance(DriverFactory.getDriver("firefox"),"https://intranet.nuxeo.com/");
        if (nuxeodm==null) {
            return;
        } else {
            System.out.println("Got Nuxeo DM instance");
        }

    }

}
