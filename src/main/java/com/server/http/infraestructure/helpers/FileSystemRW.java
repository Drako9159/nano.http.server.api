package com.server.http.infraestructure.helpers;

import com.server.http.infraestructure.dto.file.DataListFile;
import fi.iki.elonen.NanoHTTPD;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;


public class FileSystemRW {


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

    public JSONArray jsonFiles(File folder){
        File[] files = folder.listFiles();
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
        JSONArray jsonArray = new JSONArray();
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


    public File[] readAllFiles(File folder) {
        // Filter files by isFile for not include folders
        File[] files = Arrays.stream(folder.listFiles())
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




}
