package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.nuxeo.qa.behaviourdriven.BehavioursCase;

public class CaseManagementStop extends BehavioursCase {

    File serverPackageLocation;

    public String serverLocationIsSet(String property) {

        if (System.getProperty(property) == null) {
            return "is not set";
        }
        serverPackageLocation = new File(System.getProperty(property));
        if (!serverPackageLocation.exists()) {
            return "is set but the folder doesn't exist";
        }
        return "is set";
    }



    void runCommand(String command) throws IOException {
        Process p1 = Runtime.getRuntime().exec(command, null,
                serverPackageLocation);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                p1.getInputStream()));
        String line = null;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }

    public String stopServer(String command) throws IOException {
        runCommand(command);
        return "stops";
    }



}
