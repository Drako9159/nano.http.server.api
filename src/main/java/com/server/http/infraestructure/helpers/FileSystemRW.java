package com.server.http.infraestructure.helpers;

import com.server.http.infraestructure.controllers.FileServer;
import com.server.http.infraestructure.dto.file.DataListFile;
import fi.iki.elonen.NanoHTTPD;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.crypto.Data;
import java.io.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;


public class FileSystemRW {
    private File folderToServe;

    public FileSystemRW(File folderToServe){
        this.folderToServe = folderToServe;
    }

    public static String getSizeMb(Long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public JSONArray convertListFilesToJSON(){
        JSONArray jsonArray = new JSONArray();
        List<DataListFile> dataListFiles = listDataFiles();
        for(DataListFile list: dataListFiles){
            Map<String, Object> mapper = new HashMap<>();
            mapper.put("id", list.id());
            mapper.put("name", list.filename());
            mapper.put("size", list.size());
            mapper.put("path", list.path());
            mapper.put("mimetype", list.mimetype());
            mapper.put("is_file", list.isFile());
            jsonArray.add(new JSONObject(mapper));
        }
        return jsonArray;
    }

    public List<DataListFile> listDataFiles(){
        File[] files = this.folderToServe.listFiles();
        List<DataListFile> dataListFiles = new ArrayList<>();
        for (File file: files){
            int index = file.getName().lastIndexOf(".");
            String mimetype = null;
            if(index > 0){
                mimetype = file.getName().substring(index + 1).toLowerCase();
            } else {
                mimetype = "folder";
            }
            dataListFiles.add(new DataListFile(
                    UUID.randomUUID().toString(), file.getName(),
                    getSizeMb(file.length()),  file.getPath().replaceAll("(\\\\)", "/"),
                    mimetype, file.isFile()));
        }
        return dataListFiles;
    }


    public File[] readAllFiles() {
        // Filter files by isFile for not include folders
        File[] files = Arrays.stream(this.folderToServe.listFiles())
                .filter(File::isFile)
                .toArray(File[]::new);
        return files;
    }

    public void writeFile(File folderToServe, NanoHTTPD.IHTTPSession session) {
        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            String filename = session.getParms().get("file");
            File file = new File(folderToServe, filename);
            if (file.createNewFile()) {
                try (FileOutputStream outputStream = new FileOutputStream(file);
                     FileInputStream fileInputStream = new FileInputStream(new File(files.get("file")))) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (IOException | NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }
    }


    public static File readToDownload(String element, File folderToServe){
        File file = new File(folderToServe + "/" + element);
        return file;
    }



    public static String getMimeType(String filename){
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        return fileTypeMap.getContentType(filename);
    }




}
