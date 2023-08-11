package com.server.http.view.util;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertiesRW {

    private final String pathProperties;

    public PropertiesRW() {
        this.pathProperties = System.getProperty("user.dir") + File.separator + "config.properties";
        validateProperties();
    }


    public void assignPathServer(String path) {
        String toConfig = "PATH_SERVER " + path.replace("\\", "/");
        StringBuilder properties = new StringBuilder();
        List<String> elements = read();
        for (String element : elements) {
            if (element.split(" ")[0].equals("PATH_SERVER")) {
                element = toConfig;
            }
            properties.append(element).append("\n");
        }
        write(properties.toString());
    }




    public String getPathServer() {
        List<String> element = read().stream().filter(e -> e.split(" ")[0].equals("PATH_SERVER")).toList();
        try {
            return element.get(0).split(" ")[1];
        } catch (Exception e) {
            e.printStackTrace();
            return "empty";
        }
    }

    private void write(String config) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.pathProperties)) {
            fileOutputStream.write(config.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<String> read() {
        try (FileInputStream inputStream = new FileInputStream(this.pathProperties);
        ) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void validateProperties(){
        List<String> path_server = read().stream().filter(e -> e.split(" ")[0].equals("PATH_SERVER")).toList();
        if(path_server.isEmpty()){
            String defaultPathServer = System.getProperty("user.home") + File.separator + "Documents/Server";
            String toConfig = "PATH_SERVER " + defaultPathServer.replace("\\", "/");
            write(toConfig);
        }
    }
}
