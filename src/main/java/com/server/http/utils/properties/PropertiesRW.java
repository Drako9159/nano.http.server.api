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


    public void searchFile() {
        var file = new File(pathPropertiesFile);
        try {
            if (file.exists()) {
                System.out.println("File exists");
            } else {
                var out = new PrintWriter(new FileWriter(file));
                out.close();
                System.out.println("File is created");
            }
        } catch (Exception e) {
            System.out.println("Error in: " + e.getMessage());
        }

    }


    public void addProperty(Property property) {
        boolean adder = false;
        var file = new File(pathPropertiesFile);
        try {
            adder = file.exists();
            var out = new PrintWriter(new FileWriter(pathPropertiesFile, adder));
            out.println(property);
            out.close();
            System.out.println("Property " + file + " is added in file");
        } catch (Exception e) {
            System.out.println("Error in: " + e.getMessage());
        }
    }

    public void findProperty(Property property) {
        var file = new File(pathPropertiesFile);
        try {
            var input = new BufferedReader(new FileReader(pathPropertiesFile));
            String textLine;
            textLine = input.readLine();
            var index = 1;
            var finder = false;
            var propertyFind = property.getName();
            while (textLine != null) {
                if (propertyFind != null && propertyFind.equalsIgnoreCase(textLine)) {
                    finder = true;
                    break;
                }
                textLine = input.readLine();
                index++;
            }
            if (finder) {
                System.out.println("Property " + textLine + " is in index: " + index);
            } else {
                System.out.println("Property " + property.getName() + " is not found");
            }
            input.close();

        } catch (Exception e) {
            System.out.println("Error in: " + property.getName());
        }
    }



}
