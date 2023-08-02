package com.server.http.infraestructure.server;

import com.server.http.infraestructure.controllers.FileController;
import com.server.http.infraestructure.helpers.FileSystemRW;
import fi.iki.elonen.NanoHTTPD;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class NanoHTTP extends NanoHTTPD {

    private String uri;
    private final File folderToServe;
    public static final int PORT = 8080;

    public NanoHTTP(File folderToServe) throws IOException {
        super(PORT);
        this.folderToServe = folderToServe;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
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
                //return new FileController(folderToServe).POST(session);
            }
        }
        if(session.getUri().contains("/api/download-one-file")){
            return new FileController(folderToServe).generateDownload(session);
        }

        return errorNotFound();
    }

    private Response errorNotFound() {
        StringBuilder html = new StringBuilder();
        html.append("<p>Route not found</p>");
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", html.toString());
    }



    // User interface
    private Response generateFileListResponse() {

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Files</title></head>");
        html.append("<style> body{ margin: 0; padding: 0; background-color: #cccccc; } </style>");
        html.append("<body><h1>Available files:</h1><ul>");
        File[] files = new FileSystemRW(folderToServe).readAllFiles();
        if (files != null) {
            for (File file : files) {
                html.append("<li><a href=\"").append(file.getName()).append("\">").append(file.getName()).append("</a></li>");
            }
        }
        html.append("</ul>");
        html.append("<form method=\"post\" enctype=\"multipart/form-data\">")
                .append("<input type=\"file\" name=\"file\"><br>")
                .append("<input type=\"submit\" value=\"Subir archivo\">")
                .append("</form></body></html>");

        JSONArray jsonArray = new FileSystemRW(folderToServe).convertListFilesToJSON();
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("content", jsonArray);

        return newFixedLengthResponse(Response.Status.OK, "application/json", jsonResponse.toJSONString());
        //return newFixedLengthResponse(Response.Status.OK, "text/html", html.toString());
        //return newFixedLengthResponse(Response.Status.OK, "application/json", jsonResponse.toJSONString());

    }

}
