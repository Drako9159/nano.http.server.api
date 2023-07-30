package com.server.http.infraestructure.controllers;

import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileServer extends NanoHTTPD {
    public static final int PORT = 8080;
    private final File folderToServe;

    public FileServer(File folderToServe){
        super(PORT);
        this.folderToServe = folderToServe;
    }

    @Override
    public Response serve(IHTTPSession session){
        String uri = session.getUri();
        File file = new File(folderToServe, uri);
        if(file.exists() && file.isFile()){
            try{
                FileInputStream fis = new FileInputStream(file);
                return newChunkedResponse(Response.Status.OK, getMimeTypeForFile(uri), fis);
            } catch (IOException e){
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Error al ler el archivo");
            }
        } else {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Archivo no encontrado");
        }
    }
/*
    public String getMimeTypeForFile(String uri){
        return "application/octet-stream";
    }*/


    private Response generateFileListResponse() {
        StringBuilder html = new StringBuilder("<html><body><h1>Archivos disponibles:</h1><ul>");
        File[] files = folderToServe.listFiles();
        if (files != null) {
            for (File file : files) {
                html.append("<li><a href=\"").append(file.getName()).append("\">").append(file.getName()).append("</a></li>");
            }
        }
        html.append("</ul>");

        // Formulario para subir archivos
        html.append("<form method=\"post\" enctype=\"multipart/form-data\">")
                .append("<input type=\"file\" name=\"file\"><br>")
                .append("<input type=\"submit\" value=\"Subir archivo\">")
                .append("</form></body></html>");

        return newFixedLengthResponse(Response.Status.OK, "text/html", html.toString());
    }
}
