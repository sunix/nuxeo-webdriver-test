package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.nuxeo.qa.behaviourdriven.BehavioursCase;

public class CaseManagementStart extends BehavioursCase {

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

    public String unzip(String serverFolder, String command)
            throws IOException, InterruptedException {
        // check if the folder exist first before unziping
        String serverFolderLocation = serverPackageLocation.getAbsolutePath()
                + File.separator + serverFolder;

        File serverFolderFile = new File(serverFolderLocation);
        if (serverFolderFile.exists()) {
            return "unzip";
        }

        runCommand(command);
        serverFolderFile = new File(serverFolderLocation);
        if (serverFolderFile.exists()) {
            return "unzip";
        }
        return "not unzip " + serverFolderLocation;
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

    public String startServer(String command) throws IOException {
        runCommand(command);
        return "starts";
    }

    public String givePermission(String command) throws IOException {
        runCommand(command);
        return "gives";
    }

}
