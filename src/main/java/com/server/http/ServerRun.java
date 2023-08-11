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
        String pathTotal = System.getProperty("user.home") + File.separator + "Documents/Server";
        this.pathServer = validateFolder(pathTotal);

        new PropertiesRW().assignServerPath(pathTotal);
        ///System.out.println(new PropertiesRW().checkIfExistPathServer());
        //List<String> properties = new PropertiesRW().read();
        //System.out.println(properties);

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
        new NanoHTTP(pathServer).closeAllConnections();
        new NanoHTTP(pathServer).stop();
    }


    public File validateFolder(String pathTotal){
        // folder path is in documents
        File folderExist = new File(pathTotal);
        if(!folderExist.exists()){
            folderExist.mkdir();
        }
        return folderExist;
    }
}
