package com.server.http.infraestructure.server;

import com.server.http.infraestructure.controllers.FileController;
import com.server.http.infraestructure.helpers.ExampleManagerFactory;
import com.server.http.infraestructure.helpers.FileSystemRW;
import fi.iki.elonen.NanoHTTPD;
import java.io.File;
import java.io.IOException;
public class NanoHTTP extends NanoHTTPD {


    private String uri;
    private final File folderToServe;
    private final String pathTemp;
    public static final int PORT = 8080;

    public NanoHTTP(File folderToServe) throws IOException {
        super(PORT);
        this.folderToServe = folderToServe;
        this.pathTemp = folderToServe.getPath();

        //start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        //System.out.println("\n[server] running point your browser to http://localhost:8080/ \n");
    }

    public void start() throws IOException {
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        setTempFileManagerFactory(new ExampleManagerFactory(pathTemp));
        System.out.println("\n[server] running point your browser to http://localhost:8080/ \n");

    }






    @Override
    public Response serve(IHTTPSession session) {
        if (session.getUri().contains("/api/get-all-files")) {
            return new FileController(folderToServe).getAllFiles();
        }
        if(session.getUri().contains("/api/get-one-file")){
            return new FileController(folderToServe).getOneFileByPath(session);
        }

        if(session.getUri().contains("/api/upload-one-file")){
            if(session.getMethod() == Method.POST){
                return new FileController(folderToServe).uploadOneFile(session);
            }
        }
        if(session.getUri().contains("/api/download-one-file")){
            return new FileController(folderToServe).generateDownload(session);
        }

        if(session.getUri().contains("/api/delete-one-file")){
            if(session.getMethod() == Method.DELETE){
                return new FileController(folderToServe).deleteOneFile(session);
            }
        }

        if(session.getUri().equals("/")){
            return pageContent();
        }
        return errorNotFound();
       // return pageContent();
    }

    private Response errorNotFound() {
        StringBuilder html = new StringBuilder();
        html.append("<p>Route not found</p>");
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", html.toString());
    }



    // User interface
    private Response pageContent() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Files</title></head>");
        html.append("<style> body{ margin: 0; padding: 0; background-color: #cccccc; } </style>");
        html.append("<body><h1>Available files:</h1><ul>");
        File[] files = new FileSystemRW(folderToServe).readAllFiles();
        if (files != null) {
            for (File file : files) {
                html.append("<li><a href=\"api/download-one-file?file=").append(file.getName()).append("\">").append(file.getName()).append("</a></li>");
            }
        }

        html.append("</ul>");
        html.append("<form method=\"post\" enctype=\"multipart/form-data\" action=\"/api/upload-one-file\">")
                .append("<input type=\"file\" name=\"file\"><br>")
                .append("<input type=\"submit\" value=\"Subir archivo\">")
                .append("</form></body></html>");


        return newFixedLengthResponse(Response.Status.OK, "text/html", html.toString());

    }
}
