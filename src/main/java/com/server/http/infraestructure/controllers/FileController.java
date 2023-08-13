package com.server.http.infraestructure.controllers;

import com.server.http.infraestructure.helpers.FileSystemRW;
import fi.iki.elonen.NanoHTTPD;
import org.json.simple.JSONObject;
import java.io.*;
import java.util.*;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class FileController {


    public NanoHTTPD.Response serviceFiles(NanoHTTPD.IHTTPSession session)  {
        if(session.getParameters().get("download") != null){
            String filename = session.getParameters().get("download").get(0);
            if(new FileSystemRW().validateFile(filename)) return handleResponse(NanoHTTPD.Response.Status.NOT_FOUND,"File not found");
            Map<String, Object> response = new FileSystemRW().fileToDownload(filename);
            NanoHTTPD.Response nanoResponse = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, response.get("mimetype").toString(), (FileInputStream) response.get("fileInputStream"), (long) response.get("size"));
            nanoResponse.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            return nanoResponse;
        }

        if(session.getParameters().get("file") != null){
            String filename = session.getParameters().get("file").get(0);
            if(new FileSystemRW().validateFile(filename)) return handleResponse(NanoHTTPD.Response.Status.NOT_FOUND, "File not found");
            new FileSystemRW().JSONFile(filename);
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK,
                    "application/json",
                    new FileSystemRW().JSONFile(filename).toJSONString().replace("\\\\", "/"));
        }

        if(session.getParameters().get("delete") != null){
            String filename = session.getParameters().get("delete").get(0);
            if(new FileSystemRW().validateFile(filename)) return handleResponse(NanoHTTPD.Response.Status.NOT_FOUND, "File not found");
            return handleResponse(NanoHTTPD.Response.Status.NO_CONTENT, new FileSystemRW().deleteFile(filename));
        }



        Map<String, Object> response = new HashMap<>();
        response.put("files", new FileSystemRW().JSONFiles());
        JSONObject jsonResponse = new JSONObject(response);
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", jsonResponse.toJSONString().replace("\\\\", "/"));
    }


    public NanoHTTPD.Response uploadOneFile(NanoHTTPD.IHTTPSession session) {
        try {
            Map<String, String> files = new HashMap<>();
            session.parseBody(files);
            String filename = session.getParameters().get("file").get(0);
            int point = filename.lastIndexOf(".");
            String extension = filename.substring(point).toLowerCase();
            String name = filename.substring(0, point);
            String parseName = name + "_" + System.currentTimeMillis() + extension;
            new FileSystemRW().writeFile(files, parseName);
            return handleResponse(NanoHTTPD.Response.Status.OK, "Upload successfully");
        } catch (IOException | NanoHTTPD.ResponseException e) {
            e.printStackTrace();
            return handleError();
        }
    }

/*
    Another form for upload File, but is slow most
    public NanoHTTPD.Response uploadOneFile2(NanoHTTPD.IHTTPSession session) {
        try {
            Map<String, String> files = new HashMap<String, String>();
            session.parseBody(files);
            String filename = session.getParameters().get("file").get(0);
            File tempFile = new File(files.get("file"));
            Files.copy(tempFile.toPath(), new File(folderToServe + "/" + filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return handleResponse(NanoHTTPD.Response.Status.OK, "Upload successfully");
        } catch (IOException | NanoHTTPD.ResponseException e) {
            System.out.println("i am error file upload post ");
            e.printStackTrace();
        }
        return handleError();
    }*/



    // refactor response
    public NanoHTTPD.Response handleResponse(NanoHTTPD.Response.Status status, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        JSONObject jsonResponse = new JSONObject(response);
        return newFixedLengthResponse(status, "application/json", jsonResponse.toJSONString());
    }

    public NanoHTTPD.Response handleError() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Internal Server Error");
        JSONObject jsonResponse = new JSONObject(response);
        return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json", jsonResponse.toJSONString());
    }

}
