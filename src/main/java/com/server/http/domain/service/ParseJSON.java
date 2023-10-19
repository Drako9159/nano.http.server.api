package com.server.http.domain.service;

import com.server.http.domain.model.FileModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseJSON {

    private FileService fileService = new FileService();

    public JSONArray listFiles() {
        JSONArray jsonArray = new JSONArray();
        List<FileModel> listFiles = fileService.listFiles();
        for (FileModel file : listFiles) {
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

    public JSONObject fileByFilename(String filename) {
        FileModel file = fileService.findFileByFilename(filename);
        JSONObject fileInfo = createFileInfoJsonObject(file);
        JSONObject options = createOptionsUrlJsonObject(file);
        Map<String, Object> container = new HashMap<>();
        container.put("file", fileInfo);
        container.put("options", options);
        return new JSONObject(container);
    }

    private JSONObject extraQuery() {
        Map<String, Object> extra = new HashMap<>();
        extra.put("upload_file", "POST:\\api\\files?upload=true");
        return new JSONObject(extra);
    }

    private JSONObject createFileInfoJsonObject(FileModel file) {
        Map<String, Object> infoFile = new HashMap<>();
        infoFile.put("filename", file.getFileName());
        infoFile.put("size", file.getSize());
        infoFile.put("mimetype", file.getMimeType());
        infoFile.put("is_file", file.getIsFile());
        return new JSONObject(infoFile);
    }

    private JSONObject createOptionsUrlJsonObject(FileModel file) {
        Map<String, Object> options = new HashMap<>();
        options.put("download", "\\api\\files?download=" + file.getFileName());
        options.put("info", "\\api\\files?file=" + file.getFileName());
        options.put("delete", "\\api\\files?delete=" + file.getFileName());
        return new JSONObject(options);
    }
}
