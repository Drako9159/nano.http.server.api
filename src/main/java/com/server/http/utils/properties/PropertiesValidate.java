package com.server.http.utils.properties;

import java.io.File;

public class PropertiesValidate {

    public PropertiesValidate() {
        writeDefaultPathServerIfNotExist();
        validateFolderPath();
    }

    public File getPathServer() {
        return new File(new PropertiesService().getPropertyByName(PropertyName.PATH_SERVER).getValue());
    }


    private void writeDefaultPathServerIfNotExist() {
        boolean exist = new PropertiesService().existProperty(PropertyName.PATH_SERVER);
        if (exist) {
            return;
        } else {
            String defaultPathServer = System.getProperty("user.home") + File.separator + "Documents/Server";
            new PropertiesRW().addProperty(new Property(PropertyName.PATH_SERVER, defaultPathServer.replace("\\", "/")));
        }
    }

    /*public void assignPathServer(String pathServer) {
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
    }*/

    private void validateFolderPath() {
        File folderCheck = new File(new PropertiesService().getPropertyByName(PropertyName.PATH_SERVER).getValue());
        if (!folderCheck.exists()) {
            folderCheck.mkdir();
        }
    }

}
