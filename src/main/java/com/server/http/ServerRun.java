package com.server.http;

import com.server.http.infraestructure.helpers.ExampleManagerFactory;
import com.server.http.infraestructure.server.NanoHTTP;
import com.server.http.view.util.PropertiesRW;
import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ServerRun {
    private final File pathServer;

    public ServerRun(){
        this.pathServer = validateFolder(new PropertiesRW().getPathServer());
    }

    public void start(){
        try {
            new NanoHTTP(pathServer).start();
            //server.setTempFileManagerFactory(new ExampleManagerFactory(pathTemp));
        } catch (IOException e) {
            System.out.println("Couldn't, start server:\n" + e);
        }
    }

    public void stop() throws IOException {
        new NanoHTTP(pathServer).stop();
    }

    public File validateFolder(String pathTotal){
        // folder path exits
        File folderExist = new File(pathTotal);
        if(!folderExist.exists()){
            folderExist.mkdir();
        }
        return folderExist;
    }
}
