package com.server.http.infraestructure.controllers;

import com.server.http.infraestructure.helpers.FileSystemRW;
import com.server.http.infraestructure.helpers.Util;
import fi.iki.elonen.NanoHTTPD;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class FileController {

    private File folderToServe;

    public FileController(File folderToServe) {
        this.folderToServe = folderToServe;
    }

    public NanoHTTPD.Response generateDownload(NanoHTTPD.IHTTPSession session) {
        String[] elements = session.getQueryParameterString().split("file=");
        String element = elements[1];
        File file = FileSystemRW.readToDownload(element, this.folderToServe);
       if(!file.exists()) return handleResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "File not found");
        try {
            String mimetype = Util.getMimeType(file.getName());
            InputStream inputStream = new FileInputStream(file);
            NanoHTTPD.Response response = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimetype, inputStream, file.length());
            response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("content", "Internal Server Error");
        return handleError();
    }

    public NanoHTTPD.Response getAllFiles() {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("content", new FileSystemRW(folderToServe).convertListFilesToJSON());
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", jsonResponse.toJSONString().replace("\\\\", "/"));
    }

    public NanoHTTPD.Response getOneFileByPath(NanoHTTPD.IHTTPSession session)  {
        String path = session.getParms().get("file_path");
        JSONObject jsonObject = new FileSystemRW(folderToServe).convertListFileToJSON(path);
        if (jsonObject.get("message") == "File not found"){
            return handleResponse(NanoHTTPD.Response.Status.NOT_FOUND, jsonObject.get("message").toString());
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", jsonObject.toJSONString().replace("\\\\", "/"));
    }


    public NanoHTTPD.Response uploadOneFile(NanoHTTPD.IHTTPSession session){
        try{
            Map<String, String> files = new HashMap<>();
            session.parseBody(files);
            String filename = session.getParms().get("file");
            new FileSystemRW(folderToServe).writeFile(files, filename);
            return handleResponse(NanoHTTPD.Response.Status.OK, "Upload successfully");
        } catch (IOException | NanoHTTPD.ResponseException e){
            e.printStackTrace();
        }
        return handleError();
    }










    public NanoHTTPD.Response handleResponse(NanoHTTPD.Response.Status status, String message) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", message);
        return newFixedLengthResponse(status, "application/json", jsonResponse.toJSONString());
    }

    public NanoHTTPD.Response handleError() {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", "Internal Server Error");
        return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json", jsonResponse.toJSONString());
    }
}
