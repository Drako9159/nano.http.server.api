package com.server.http.view.util;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PropertiesRW {

    private final String pathProperties;

    public PropertiesRW(){
        this.pathProperties = System.getProperty("user.dir") + File.separator + "config.properties";
    }

    // TODO realize rw file properties
    private void write(String config){
        try(FileOutputStream fileOutputStream = new FileOutputStream(this.pathProperties)){
            //fileOutputStream.write(config.getBytes());

            StringBuilder build = new StringBuilder();
            List<String> elements = read();
            System.out.println(read());
            for (String element:elements){
                /*if (element.split(" ")[0].equals("path_server:")) {

                    break;
                }*/
                build.append(element);
                System.out.println(element);
            }
            System.out.println(build);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void assignServerPath(String path){
        String toConfig = "path_server: " + path.replace("\\", "/");
        write(toConfig);
    }

    public boolean checkIfExistPathServer(){
        List<String> elements = read();
        boolean exist = false;
        for (String element:elements){
            if (element.split(" ")[0].equals("path_server:")) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public List<String> read(){
        try(FileInputStream inputStream = new FileInputStream(this.pathProperties);
        ){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
