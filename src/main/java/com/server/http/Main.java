package com.server.http;


import com.server.http.infraestructure.helpers.ExampleManagerFactory;
import com.server.http.infraestructure.server.NanoHTTP;
import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        String userHome = System.getProperty("user.home");
        String userRute = userHome + File.separator + "Documents/Server"; // Puedes cambiar "Documentos" por el nombre de la carpeta que desees

        /*
        ClassLoader classLoader = Main.class.getClassLoader();
        String classFilePath = classLoader.getResource("com/server/http/Main.class").getFile();

        System.out.println(classFilePath);
        String projectPath = classFilePath.replaceAll("ServerHttp/target/classes/com/server/http/Main.class", "");
        String ruta = projectPath + "files" + File.separator;
        System.out.println(ruta);*/

        try {
            File folderToServer = new File(userRute);
            if(!folderToServer.exists()){
                folderToServer.mkdir();
            }
            //new NanoHTTP(folderToServer);
            NanoHTTP server = new NanoHTTP(folderToServer);
            server.setTempFileManagerFactory(new ExampleManagerFactory());
        } catch (IOException e) {
            System.out.println("Couldn't, start server:\n" + e);
        }
    }
}
