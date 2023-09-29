package com.server.http.utils.properties;

import com.server.http.utils.properties.PropertiesRW;

import java.io.File;
import java.util.List;

public class PropertiesValidate {


    public PropertiesValidate() {
        createDefaultPathServer();
        validateFolderPath();
    }

    public void createDefaultPathServer() {
        List<String> pathServer = filerBy("PATH_SERVER");
        if (pathServer.isEmpty()) {
            String defaultPathServer = System.getProperty("user.home") + File.separator + "Documents/Server";
            String toConfig = "PATH_SERVER -> " + defaultPathServer.replace("\\", "/");
            new PropertiesRW().write(toConfig);
        }
    }

    public void assignPathServer(String pathServer) {
        String toConfig = "PATH_SERVER -> " + pathServer.replace("\\", "/");
        StringBuilder properties = new StringBuilder();
        List<String> elements = new PropertiesRW().read();
        for (String element : elements) {
            if (element.split(" -> ")[0].equals("PATH_SERVER")) {
                element = toConfig;
            }
            properties.append(element).append("\n");
        }
        new PropertiesRW().write(properties.toString());
    }

    public String getPathServer() {
        List<String> pathServer = filerBy("PATH_SERVER");
        try {
            return pathServer.get(0).split(" -> ")[1];
        } catch (Exception e) {
            return "empty";
        }
    }

    private List<String> filerBy(String element) {
        return new PropertiesRW().read().stream().filter(e -> e.split(" -> ")[0].equals(element)).toList();
    }

    private void validateFolderPath() {
        File folderCheck = new File(getPathServer());
        if (!folderCheck.exists()) {
            folderCheck.mkdir();
        }
    }

    public File getFolderServerPath() {
        return new File(getPathServer());

    }

}
