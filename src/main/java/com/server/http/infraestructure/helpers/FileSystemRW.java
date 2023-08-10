package com.server.http.infraestructure.helpers;

import com.server.http.domain.models.FileModel;
import com.server.http.infraestructure.dto.file.DataListFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

public class FileSystemRW {
    private final File folderToServe;

    public FileSystemRW(File folderToServe) {
        this.folderToServe = folderToServe;
    }


    // TODO change json put for map in all methods
    public JSONArray convertListFilesToJSON() {
        JSONArray jsonArray = new JSONArray();
        List<DataListFile> dataListFiles = listDataFiles();
        for (DataListFile list : dataListFiles) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", list.id());
            response.put("name", list.filename());
            response.put("size", list.size());
            response.put("path", list.path());
            response.put("mimetype", list.mimetype());
            response.put("is_file", list.isFile());
            jsonArray.add(new JSONObject(response));
        }
        return jsonArray;
    }

    public JSONObject convertListFileToJSON(String path) {
        Map<String, Object> fileMap = new HashMap<>();
        File file = new File(path);
        List<DataListFile> dataListFiles = listDataFiles();
        var isInServer = false;
        for (DataListFile data : dataListFiles) {
            if (data.path().equals(file.getPath())) {
                isInServer = true;
                break;
            }
        }
        if (isInServer) {
            FileModel fileModel = new FileModel(UUID.randomUUID().toString(),
                    file.getName(), Util.getSizeMb(file.length()), file.getPath(),
                    Util.getExtension(file.getName()), file.isFile());
            fileMap.put("name", fileModel.getId());
            fileMap.put("size", fileModel.getSize());
            fileMap.put("path", fileModel.getPath());
            fileMap.put("mimetype", fileModel.getMimetype());
            fileMap.put("is_file", fileModel.getFile());
        } else {
            fileMap.put("message", "File not found");
        }
        return new JSONObject(fileMap);
    }

    public List<DataListFile> listDataFiles() {
        File[] files = this.folderToServe.listFiles();
        List<DataListFile> dataListFiles = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            if (!file.getName().equals("temp") && file.isFile()) {
                dataListFiles.add(new DataListFile(
                        UUID.randomUUID().toString(), file.getName(),
                        Util.getSizeMb(file.length()), file.getPath().replace("\\\\", "/"),
                        Util.getExtension(file.getName()), file.isFile()));
            }
        }
        return dataListFiles;
    }


    public File[] readAllFiles() {
        return Arrays.stream(Objects.requireNonNull(this.folderToServe.listFiles()))
                .filter(File::isFile)
                .toArray(File[]::new);
    }


    public void writeFile(Map<String, String> files, String filename) throws IOException {
        File file = new File(this.folderToServe, filename);
        if (file.createNewFile()) {
            try (FileOutputStream outputStream = new FileOutputStream(file);
                 FileInputStream fileInputStream = new FileInputStream(new File(files.get("file")));
            ) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static File readToDownload(String element, File folderToServe) {
        return new File(folderToServe + "/" + element);
    }

    public boolean checkFile(String filename) {
        File file = new File(this.folderToServe + "/" + filename);
        return file.exists();
    }

    public String eraseFile(String filename) {
        File file = new File(this.folderToServe + "/" + filename);
        file.delete();
        return "deleted";
    }
}
