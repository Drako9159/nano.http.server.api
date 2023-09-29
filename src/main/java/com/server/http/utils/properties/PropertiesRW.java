package com.server.http.utils.properties;

import java.io.*;
import java.util.List;

public class PropertiesRW {

    private final String pathPropertiesFile;

    public PropertiesRW() {
        // default file storage
        this.pathPropertiesFile = System.getProperty("user.dir") + File.separator + "configs.properties";
        validateExistProperties();
    }

    public void write(String config) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.pathPropertiesFile)) {
            fileOutputStream.write(config.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> read() {
        try (FileInputStream inputStream = new FileInputStream(this.pathPropertiesFile)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void validateExistProperties() {
        File properties = new File(pathPropertiesFile);
        if (!properties.exists()) write("created");
    }


}
