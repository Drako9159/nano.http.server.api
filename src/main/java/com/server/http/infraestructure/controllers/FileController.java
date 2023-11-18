package com.server.http.infraestructure.controllers;

import com.server.http.domain.service.FileService;
import com.server.http.domain.service.ParseJSON;
import fi.iki.elonen.NanoHTTPD;
import org.json.simple.JSONObject;
import java.io.*;
import java.util.*;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class FileController {

    public NanoHTTPD.Response serviceFiles(NanoHTTPD.IHTTPSession session)  {
        if(session.getParameters().get("download") != null){
            String filename = session.getParameters().get("download").get(0);
            if(new FileService().validateFile(filename)) return handleResponse(NanoHTTPD.Response.Status.NOT_FOUND,"File not found");
            //Map<String, Object> response = new FileSystemRW().fileToDownload(filename);
            Map<String, Object> response = new FileService().findFileToDownload(filename);
            NanoHTTPD.Response nanoResponse = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, response.get("mimetype").toString(), (FileInputStream) response.get("fileInputStream"), (long) response.get("size"));
            nanoResponse.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            return nanoResponse;
        }

        if(session.getParameters().get("file") != null){
            String filename = session.getParameters().get("file").get(0);
            if(new FileService().validateFile(filename)) return handleResponse(NanoHTTPD.Response.Status.NOT_FOUND, "File not found");
           // new ParseJSON().fileByFilename(filename);
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK,
                    "application/json",
                    new ParseJSON().fileByFilename(filename).toJSONString().replace("\\\\", "/"));
        }

        if(session.getParameters().get("delete") != null){
            String filename = session.getParameters().get("delete").get(0);
            if(new FileService().validateFile(filename)) return handleResponse(NanoHTTPD.Response.Status.NOT_FOUND, "File not found");
            return handleResponse(NanoHTTPD.Response.Status.NO_CONTENT, new FileService().deleteFile(filename));
        }

        if(session.getParameters().get("upload") != null){
            String condition = session.getParameters().get("upload").get(0);
            System.out.println(session.getParameters().get("file"));
            //System.out.println(session.getParameters());
/*
            long contentLength = Long.parseLong(session.getHeaders().get("content-length"));
            int bytesRead;
            byte[] buffer = new byte[8192];
            while((bytesRead = session.getInputStream().read(buffer)) != -1){
                System.out.println(bytesRead);
            }*/


            if(condition.equals("true")){
                return uploadOneFile(session);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("files", new ParseJSON().listFiles());
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

            new FileService().saveFile(files, parseName);
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

    //





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
