package com.server.http.infraestructure.helpers;

import com.server.http.domain.models.FileModel;
import com.server.http.infraestructure.dto.file.DataListFile;
import fi.iki.elonen.NanoHTTPD;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;


public class FileSystemRW {
    private final File folderToServe;

    public FileSystemRW(File folderToServe) {
        this.folderToServe = folderToServe;
    }


    public JSONArray convertListFilesToJSON() {
        JSONArray jsonArray = new JSONArray();
        List<DataListFile> dataListFiles = listDataFiles();
        for (DataListFile list : dataListFiles) {
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

    public JSONObject convertListFileToJSON(String path) {
        JSONObject jsonObject = new JSONObject();
        File file = new File(path);
        List<DataListFile> dataListFiles = listDataFiles();
        var isInServer = false;
        for (DataListFile data : dataListFiles) {
            if (data.path().equals(file.getPath())) {
                isInServer = true;
            }
        }
        if (isInServer) {
            FileModel fileModel = new FileModel(UUID.randomUUID().toString(),
                    file.getName(), Util.getSizeMb(file.length()), file.getPath(),
                    Util.getExtension(file.getName()), file.isFile());
            jsonObject.put("name", fileModel.getId());
            jsonObject.put("size", fileModel.getSize());
            jsonObject.put("path", fileModel.getPath());
            jsonObject.put("mimetype", fileModel.getMimetype());
            jsonObject.put("is_file", fileModel.getFile());
            ;
        } else {
            jsonObject.put("message", "File not found");
        }
        return jsonObject;
    }

    public List<DataListFile> listDataFiles() {
        File[] files = this.folderToServe.listFiles();
        List<DataListFile> dataListFiles = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            if(!file.getName().equals("temp") && file.isFile()){
                dataListFiles.add(new DataListFile(
                        UUID.randomUUID().toString(), file.getName(),
                        Util.getSizeMb(file.length()), file.getPath().replace("\\\\", "/"),
                        Util.getExtension(file.getName()), file.isFile()));
            }
        }
        return dataListFiles;
    }


    public File[] readAllFiles() {
        File[] files = Arrays.stream(this.folderToServe.listFiles())
                .filter(File::isFile)
                .toArray(File[]::new);
        return files;
    }


    public void writeFile(Map<String, String> files, String filename) throws IOException {
        File file = new File(this.folderToServe, filename);
        if(file.createNewFile()){
            try(FileOutputStream outputStream = new FileOutputStream(file);
                FileInputStream fileInputStream = new FileInputStream(new File(files.get("file")));
            ) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1){
                    outputStream.write(buffer, 0 ,bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    public static File readToDownload(String element, File folderToServe) {
        File file = new File(folderToServe + "/" + element);
        return file;
    }


}
