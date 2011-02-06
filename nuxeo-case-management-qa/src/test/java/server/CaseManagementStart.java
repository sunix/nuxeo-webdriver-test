package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.nuxeo.qa.behaviourdriven.BehavioursCase;

public class CaseManagementStart extends BehavioursCase {

    File serverPackageLocation;

    public boolean checkServerStarted(String url) {
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String serverLocationIsSet(String property, boolean isServerStarted) {
        if (isServerStarted) {
            return "is set";
        }
        if (System.getProperty(property) == null) {
            return "is not set";
        }
        serverPackageLocation = new File(System.getProperty(property));
        if (!serverPackageLocation.exists()) {
            return "is set but the folder doesn't exist";
        }
        return "is set";
    }

    public String unzip(String serverFolder, String command,
            boolean isServerStarted) throws IOException, InterruptedException {
        if (isServerStarted) {
            return "unzip";
        }
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

    public String startServer(String command, String url,
            boolean isServerStarted) throws IOException {
        if (isServerStarted) {
            return "starts";
        }
        runCommand(command);
        isServerStarted = checkServerStarted(url);
        if (isServerStarted) {
            return "starts";
        }
        return "doesn't start";
    }

    public String givePermission(String command, boolean isServerStarted)
            throws IOException {
        if (isServerStarted) {
            return "gives";
        }
        runCommand(command);
        return "gives";
    }

}
