

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class TestLoginOK extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://localhost:8080/");
		selenium.start();
	}

	@Test
	public void testLoginOK() throws Exception {
		selenium.setTimeout("120000");
		selenium.open("/nuxeo/logout");
		selenium.open("/nuxeo/");
		selenium.type("username", "Administrator");
		selenium.type("password", "Administrator");
		selenium.click("Submit");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Votre identité : Administrator"));
		selenium.click("link=Déconnexion");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
