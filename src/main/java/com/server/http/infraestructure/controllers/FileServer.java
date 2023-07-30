package com.server.http.infraestructure.controllers;

import com.server.http.infraestructure.helpers.FileSystemRW;
import com.server.http.infraestructure.server.NanoHTTP;
import fi.iki.elonen.NanoHTTPD;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class FileServer{

    private File folderToServe;

    public FileServer(File folderToServe){
        this.folderToServe = folderToServe;
    }

    public NanoHTTPD.Response generateDownload(NanoHTTPD.IHTTPSession session){
        String[] elements = session.getQueryParameterString().split("element=");
        String element = elements[1];
        File file = FileSystemRW.readToDownload(element, this.folderToServe);
        String mimetype = FileSystemRW.getMimeType(file.getName());
        try {
            InputStream inputStream = new FileInputStream(file);
            NanoHTTPD.Response response = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimetype,inputStream, file.length());
            response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            return response;
        } catch (IOException e){
            e.printStackTrace();
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("content", "ELEMENT_NOT_FOUND");
        return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "application/json", jsonResponse.toJSONString());
    }

    public NanoHTTPD.Response getAllFiles(){


    }




}
