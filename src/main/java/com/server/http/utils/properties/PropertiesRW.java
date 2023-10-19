package com.server.http.utils.properties;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PropertiesRW {

    private final String pathPropertiesFile;

    public PropertiesRW() {
        this.pathPropertiesFile = System.getProperty("user.dir") + File.separator + "configs.properties";

    }

    /*public void write(String config) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.pathPropertiesFile)) {
            fileOutputStream.write(config.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
/*
    public List<String> read() {
        try (FileInputStream inputStream = new FileInputStream(this.pathPropertiesFile)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    /*public void validateExistProperties() {
        File properties = new File(pathPropertiesFile);
        if (!properties.exists()) write("created");
    }*/


    public Property findPropertyByName(PropertyName propertyName) {
        try (var input = new BufferedReader(new FileReader(pathPropertiesFile))) {
            String textLine;
            Property property = new Property(propertyName);
            textLine = input.readLine();
            var index = 1;
            var finder = false;
            var propertyFind = propertyName.toString();
            while (textLine != null) {
                if (propertyFind != null && propertyFind.equalsIgnoreCase(textLine.split(" -> ")[0])) {
                    finder = true;
                    property.setValue(textLine.split(" -> ")[1]);
                    break;
                }
                textLine = input.readLine();
                index++;
            }
            if (finder) {
                return property;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error in: " + propertyName.toString());
        }
        return null;
    }


    public void addProperty(Property property) {
        boolean adder = false;
        var file = new File(pathPropertiesFile);
        try {
            adder = file.exists();
            var out = new PrintWriter(new FileWriter(pathPropertiesFile, adder));
            String element = property.getName().toString() + " -> " + property.getValue();
            out.println(element);
            out.close();
        } catch (Exception e) {
            System.out.println("Error in: " + e.getMessage());
        }
    }

    public void deleteProperty(PropertyName propertyName) {
        try (var input = new BufferedReader(new FileReader(pathPropertiesFile))) {
            String textLine;
            List<String> propertyList = new ArrayList<>();
            textLine = input.readLine();
            while (textLine != null) {
                propertyList.add(textLine);
                textLine = input.readLine();
            }
            var out = new PrintWriter(new FileWriter(pathPropertiesFile, false));
            for (String property : propertyList){
                if(!property.split(" -> ")[0].equals(PropertyName.PATH_SERVER.toString())){
                    out.println(property);
                }
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error in: " + propertyName.toString());
        }
    }

    public boolean checkPropertyByName(PropertyName propertyName) {
        if (!new File(pathPropertiesFile).exists()) return false;
        try (var input = new BufferedReader(new FileReader(pathPropertiesFile))) {
            String textLine;
            textLine = input.readLine();
            var index = 1;
            var finder = false;
            var propertyFind = propertyName.toString();
            while (textLine != null) {
                if (propertyFind != null && propertyFind.equalsIgnoreCase(textLine.split(" -> ")[0])) {
                    finder = true;
                    break;
                }
                textLine = input.readLine();
                index++;
            }
            if (finder) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error in: " + propertyName.toString());
        }
        return false;
    }
}
