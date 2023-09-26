package com.server.http.helpers;

import java.io.*;
import java.util.List;

public class PropertiesRW {

    private final String pathPropertiesFile;

    public PropertiesRW() {
        // default file storage
        this.pathPropertiesFile = System.getProperty("user.dir") + File.separator + "config.properties";
    }

    private void write(String config) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.pathPropertiesFile)) {
            fileOutputStream.write(config.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> read() {
        try (FileInputStream inputStream = new FileInputStream(this.pathPropertiesFile)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
