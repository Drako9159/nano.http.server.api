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


    private void validateFolderPath() {
        File folderCheck = new File(new PropertiesService().getPropertyByName(PropertyName.PATH_SERVER).getValue());
        if (!folderCheck.exists()) {
            folderCheck.mkdir();
        }
    }

}
