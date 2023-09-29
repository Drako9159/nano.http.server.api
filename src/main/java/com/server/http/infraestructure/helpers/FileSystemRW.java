package com.server.http.infraestructure.helpers;

import com.server.http.utils.properties.PropertiesValidate;
import com.server.http.domain.dto.DataListFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class FileSystemRW {
    private final File pathServer;

    public FileSystemRW() {
        this.pathServer = new File(new PropertiesValidate().getPathServer());
    }


    public JSONArray JSONFiles() {
        JSONArray jsonArray = new JSONArray();
        List<DataListFile> dataListFiles = listDataFiles();
        for (DataListFile file : dataListFiles) {
            JSONObject fileInfo = createFileInfoJsonObject(file);
            JSONObject options = createOptionsUrlJsonObject(file);
            Map<String, Object> container = new HashMap<>();
            container.put("file", fileInfo);
            container.put("options", options);
            jsonArray.add(new JSONObject(container));
        }
        JSONObject extraQuery = extraQuery();
        jsonArray.add(extraQuery);
        return jsonArray;
    }

    public JSONObject extraQuery() {
        Map<String, Object> extra = new HashMap<>();
        extra.put("upload_file", "POST:\\api\\files?upload=true");
        return new JSONObject(extra);
    }

    public JSONObject JSONFile(String filename) {
        DataListFile file = listDataFiles().stream().filter(e -> e.filename().equals(filename)).toList().get(0);
        JSONObject fileInfo = createFileInfoJsonObject(file);
        JSONObject options = createOptionsUrlJsonObject(file);
        Map<String, Object> container = new HashMap<>();
        container.put("file", fileInfo);
        container.put("options", options);
        return new JSONObject(container);
    }


    private JSONObject createFileInfoJsonObject(DataListFile file) {
        Map<String, Object> infoFile = new HashMap<>();
        infoFile.put("id", file.id());
        infoFile.put("filename", file.filename());
        infoFile.put("size", file.size());
        infoFile.put("mimetype", file.mimetype());
        infoFile.put("is_file", file.isFile());
        return new JSONObject(infoFile);
    }

    private JSONObject createOptionsUrlJsonObject(DataListFile file) {
        Map<String, Object> options = new HashMap<>();
        options.put("download", "\\api\\files?download=" + file.filename());
        options.put("info", "\\api\\files?file=" + file.filename());
        options.put("delete", "\\api\\files?delete=" + file.filename());
        return new JSONObject(options);
    }


    public Map<String, Object> fileToDownload(String filename) {
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


    public boolean validateFile(String filename) {
        File file = new File(this.pathServer + "/" + filename);
        return !file.exists();
    }

    public String deleteFile(String filename) {
        File file = new File(this.pathServer + "/" + filename);
        file.delete();
        return "deleted";
    }


    // TODO change json put for map in all methods


    private List<DataListFile> listDataFiles() {
        File[] files = this.pathServer.listFiles();
        List<DataListFile> dataListFiles = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            if (!file.getName().equals("temp") && file.isFile()) {
                dataListFiles.add(new DataListFile(
                        UUID.randomUUID().toString(), file.getName(),
                        Util.getSizeMb(file.length()),
                        Util.getExtension(file.getName()), file.isFile()));
            }
        }
        return dataListFiles;
    }


    public File[] readAllFiles() {
        return Arrays.stream(Objects.requireNonNull(this.pathServer.listFiles()))
                .filter(File::isFile)
                .toArray(File[]::new);
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
