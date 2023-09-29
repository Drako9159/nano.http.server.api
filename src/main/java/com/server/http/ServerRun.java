package com.server.http;

import com.server.http.utils.properties.PropertiesValidate;
import com.server.http.infraestructure.server.NanoHTTP;

import java.io.File;
import java.io.IOException;

public class ServerRun {
    private final File pathServer;

    public ServerRun() {
        this.pathServer = new PropertiesValidate().getFolderServerPath();

    }

    public void start() {
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

}
