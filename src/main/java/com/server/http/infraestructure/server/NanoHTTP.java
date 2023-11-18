package com.server.http.infraestructure.server;

import com.server.http.infraestructure.controllers.FileController;
import com.server.http.infraestructure.helpers.ExampleManagerFactory;
import com.server.http.utils.network.NetworkUtil;
import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class NanoHTTP {

    private NanoHTTPD server;
    private final String pathTemp;
    public static final int PORT = 8080;

    public NanoHTTP(File folderToServe) throws IOException {
        this.pathTemp = folderToServe.getPath();
    }

    public void start() throws IOException {
        server = new NanoHTTPD(PORT) {
            public Response serve(IHTTPSession session) {
                if (session.getUri().contains("/api/files")) {
                    return parseResponse(new FileController().serviceFiles(session));
                }
                if (session.getUri().equals("/")) {
                    Response staticFileResponse = serveStaticFiles("/index.html");
                    return parseResponse(staticFileResponse);
                }
/*
                if (!session.getUri().contains(".")) {
                    Response htmlResponse = serveStaticHtml(session.getUri());
                    return parseResponse(htmlResponse);
                }*/

                Response staticFileResponse = serveStaticFiles(session.getUri());
                return parseResponse(staticFileResponse);


            }
        };

        server.setTempFileManagerFactory(new ExampleManagerFactory(pathTemp));
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);

        List<String> ipList = new NetworkUtil().ipList();

        StringBuilder linealIp = new StringBuilder();

        linealIp.append("[server] running point your browser to http://localhost:8080/");
        if (!ipList.isEmpty()) {
            linealIp.append(" and ");
            for (String ip : ipList) {
                linealIp.append("http://" + ip + ":8080/ ");
            }
        }
        System.out.println(linealIp.toString());

        //System.out.println("\n[server] running point your browser to http://localhost:8080/ "+linealIp+" \n");
    }


    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("\n[server] stopped\n");
        }
    }


    private NanoHTTPD.Response serveStaticFiles(String filePath) {
        try {
            String resourcePath = "/static" + filePath;
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            if (inputStream != null) {
                String mimeType = NanoHTTPD.getMimeTypeForFile(filePath);
                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType, inputStream, inputStream.available());
            } else {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Archivo no encontrado");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Error al servir el archivo");
        }
    }


    public NanoHTTPD.Response parseResponse(NanoHTTPD.Response res) {
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        res.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        return res;
    }


}
