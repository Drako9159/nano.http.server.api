package com.server.http.domain.repository;

import com.server.http.domain.model.FileModel;
import com.server.http.infraestructure.helpers.Util;
import com.server.http.utils.properties.PropertiesValidate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileRepositoryRW {

    private final File pathServer = new File(new PropertiesValidate().getPathServer());

    public List<FileModel> listFiles() {
        File[] files = this.pathServer.listFiles();
        List<FileModel> dataList = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            if (!file.getName().equals("temp") && file.isFile()) {
                dataList.add(new FileModel(file.getName(), Util.getSizeMb(file.length()), Util.getExtension(file.getName()), file.isFile()));
            }
        }
        return dataList;
    }

    public FileModel findFileByFilename(String filename) {
        return listFiles().stream().filter(e -> e.getFileName().equals(filename)).toList().get(0);
    }

    public Map<String, Object> downloadFile(String filename) {
        Map<String, Object> response = new HashMap<>();
        File file = new File(this.pathServer + File.separator + filename);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            response.put("mimetype", Util.getMimeType(filename));
            response.put("fileInputStream", fileInputStream);
            response.put("size", file.length());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public boolean checkIfExistsFile(String filename) {
        File file = new File(this.pathServer + "/" + filename);
        return !file.exists();
    }

    public String deleteFile(String filename) {
        File file = new File(this.pathServer + "/" + filename);
        file.delete();
        return "deleted";
    }

    public void writeFile(Map<String, String> files, String filename) throws IOException {
        File file = new File(this.pathServer, filename);
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


}
