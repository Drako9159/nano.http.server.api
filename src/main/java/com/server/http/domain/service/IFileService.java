package com.server.http.domain.service;

import com.server.http.domain.model.FileModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IFileService {

    public List<FileModel> listFiles();

    public FileModel findFileByFilename(String filename);

    public Map<String, Object> findFileToDownload(String filename);

    public boolean validateFile(String filename);

    public String deleteFile(String filename);

    public void saveFile(Map<String, String> files, String filename) throws IOException;

}
