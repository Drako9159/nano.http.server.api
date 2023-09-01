package com.server.http.infraestructure.server;

import com.server.http.infraestructure.controllers.FileController;
import com.server.http.infraestructure.helpers.ExampleManagerFactory;
import com.server.http.infraestructure.helpers.FileSystemRW;
import com.server.http.view.util.NetworkUtil;
import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class NanoHTTP  {

    private NanoHTTPD server;
    private final String pathTemp;
    public static final int PORT = 8080;

    public NanoHTTP(File folderToServe) throws IOException {
        this.pathTemp = folderToServe.getPath();
    }

    public void start() throws IOException {
        server = new NanoHTTPD(PORT) {
            public Response serve(IHTTPSession session) {
                if(session.getUri().contains("/api/files")){
                    return parseResponse(new FileController().serviceFiles(session));
                }
                if(session.getUri().equals("/")){
                    Response staticFileResponse = serveStaticFiles("/index.html");
                    return parseResponse(staticFileResponse);
                }

                Response staticFileResponse = serveStaticFiles(session.getUri());
                return parseResponse(staticFileResponse);


               // return parseResponse(errorNotFound());
            }
        };

        server.setTempFileManagerFactory(new ExampleManagerFactory(pathTemp));
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);

        List<String> ipList = new NetworkUtil().ipList();

        StringBuilder linealIp = new StringBuilder();

        linealIp.append("[server] running point your browser to http://localhost:8080/");
        if(!ipList.isEmpty()){
            linealIp.append(" and");
            for (String ip:ipList){
                linealIp.append("http://"+ip+":8080/ ");
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
            String resourcePath = "/static" + filePath; // Ruta relativa a la carpeta de recursos
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



    public NanoHTTPD.Response parseResponse(NanoHTTPD.Response res){
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        res.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        return res;
    }

/*
    @Override
    public Response serve(IHTTPSession session) {
        if(session.getUri().contains("/api/files")){
            return new FileController().serviceFiles(session);
        }
        if (session.getUri().contains("/api/upload-one-file")) {
            if (session.getMethod() == Method.POST) {
                return new FileController().uploadOneFile(session);
            }
        }
        if (session.getUri().equals("/")) {
            return pageContent();
        }
        return errorNotFound();
    }*/

    private NanoHTTPD.Response errorNotFound() {
        StringBuilder html = new StringBuilder();
        html.append("<p>Route not found</p>");
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/html", html.toString());
    }


    private NanoHTTPD.Response pageContent() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Files</title></head>");
        html.append("<style> body{ margin: 0; padding: 0; background-color: #cccccc; } </style>");
        html.append("<body><h1>Available files:</h1><ul>");
        File[] files = new FileSystemRW().readAllFiles();
        if (files != null) {
            for (File file : files) {
                html.append("<li><a href=\"api/files?download=").append(file.getName()).append("\">").append(file.getName()).append("</a></li>");
            }
        }

        html.append("</ul>");
        html.append("<form method=\"post\" enctype=\"multipart/form-data\" action=\"/api/upload-one-file\">")
                .append("<input type=\"file\" name=\"file\"><br>")
                .append("<input type=\"submit\" value=\"Subir archivo\">")
                .append("</form></body></html>");

        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", html.toString());
    }
}
