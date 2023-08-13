package com.server.http.infraestructure.server;

import com.server.http.infraestructure.controllers.FileController;
import com.server.http.infraestructure.helpers.ExampleManagerFactory;
import com.server.http.infraestructure.helpers.FileSystemRW;
import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.IOException;

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
            }
        };
        server.setTempFileManagerFactory(new ExampleManagerFactory(pathTemp));
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\n[server] running point your browser to http://localhost:8080/ \n");

    }

    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("\n[server] stopped\n");
        }
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
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/html", html.toString());
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


        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", html.toString());
    }
}
