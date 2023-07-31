package com.server.http.infraestructure.controllers;

import com.server.http.infraestructure.helpers.FileSystemRW;
import com.server.http.infraestructure.helpers.Util;
import fi.iki.elonen.NanoHTTPD;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class FileController {

    private File folderToServe;

    public FileController(File folderToServe) {
        this.folderToServe = folderToServe;
    }

    public NanoHTTPD.Response getAllFiles() {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("content", new FileSystemRW(folderToServe).convertListFilesToJSON());
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", jsonResponse.toJSONString().replace("\\\\", "/"));
    }

    public NanoHTTPD.Response getOneFileByPath(NanoHTTPD.IHTTPSession session)  {
        String path = session.getParameters().get("file_path").get(0);
        JSONObject jsonObject = new FileSystemRW(folderToServe).convertListFileToJSON(path);
        if (jsonObject.get("message") == "File not found"){
            return handleResponse(NanoHTTPD.Response.Status.NOT_FOUND, jsonObject.get("message").toString());
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", jsonObject.toJSONString().replace("\\\\", "/"));
    }


    public NanoHTTPD.Response generateDownload(NanoHTTPD.IHTTPSession session) {
        String filename = session.getParameters().get("file").get(0);
        File file = FileSystemRW.readToDownload(filename, this.folderToServe);
        if(!file.exists()) return handleResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "File not found");
        try {
            String mimetype = Util.getMimeType(file.getName());
            InputStream inputStream = new FileInputStream(file);
            NanoHTTPD.Response response = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimetype, inputStream, file.length());
            response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return handleError();
        }
    }

/*
    public NanoHTTPD.Response uploadOneFile(NanoHTTPD.IHTTPSession session){
        try{
            Map<String, String> files = new HashMap<>();
            session.parseBody(files);
            String filename = session.getParms().get("file");
            //new FileSystemRW(folderToServe).writeFile(files, filename);


            return handleResponse(NanoHTTPD.Response.Status.OK, "Upload successfully");
        } catch (IOException | NanoHTTPD.ResponseException e){
            e.printStackTrace();
            return handleError();
        }
    }*/

    public NanoHTTPD.Response uploadOneFiless(NanoHTTPD.IHTTPSession session) {
        System.out.println("received HTTP post with upload body...");
        int streamLength = Integer.parseInt(session.getHeaders().get("content-length"));
        System.out.println("Content length is: " + streamLength);
        byte[] fileContent = new byte[streamLength];
        try {
            InputStream input = session.getInputStream();

            int bytesRead = 0;
            int iterations = 0;
            while (bytesRead < streamLength) {
                int thisRead = input.read(fileContent, bytesRead, streamLength-bytesRead);
                bytesRead += thisRead;
                iterations++;
            }
            System.out.println("Read " + bytesRead + " bytes in " + iterations + " iterations.");
        }
        catch (Exception e) {
            System.out.println("We have other problems...");
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", "[\"no prob\"]");
    }











    public NanoHTTPD.Response uploadOneFile(NanoHTTPD.IHTTPSession session) {
        try {
            Map<String, String> headers = session.getHeaders();
            String contentType = headers.get("content-type");
            if (contentType != null && contentType.startsWith("multipart/form-data")) {
                InputStream inputStream = session.getInputStream();
                String filename = getFileNameFromHeaders(headers);
                saveFileToDestination(inputStream, filename);
                return handleResponse(NanoHTTPD.Response.Status.OK, "Upload successfully");
            } else {
                return handleResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "Invalid request");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return handleError();
        }
    }
    private String getFileNameFromHeaders(Map<String, String> headers) {
        String contentDisposition = headers.get("content-disposition");
        String filename = "";
        if (contentDisposition != null && contentDisposition.startsWith("attachment") && contentDisposition.contains("filename")) {
            int index = contentDisposition.indexOf("filename");
            filename = contentDisposition.substring(index + 10);
            filename = filename.substring(0, filename.length() - 1);
        }
        return filename;
    }
    private void saveFileToDestination(InputStream inputStream, String filename) throws IOException {
        String destinationPath = this.folderToServe + "/" + "bb.exe";
        try (OutputStream outputStream = new FileOutputStream(destinationPath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
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
