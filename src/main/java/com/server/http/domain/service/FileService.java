package com.server.http.domain.service;

import com.server.http.domain.model.FileModel;
import com.server.http.domain.repository.FileRepositoryRW;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileService implements IFileService {

    private FileRepositoryRW fileRepositoryRW = new FileRepositoryRW();

    @Override
    public List<FileModel> listFiles() {
        return this.fileRepositoryRW.listFiles();
    }

    @Override
    public FileModel findFileByFilename(String filename) {
        return this.fileRepositoryRW.findFileByFilename(filename);
    }

    @Override
    public Map<String, Object> findFileToDownload(String filename) {
        return this.fileRepositoryRW.downloadFile(filename);
    }

    @Override
    public boolean validateFile(String filename) {
        return this.fileRepositoryRW.checkIfExistsFile(filename);
    }

    @Override
    public String deleteFile(String filename) {
        return this.fileRepositoryRW.deleteFile(filename);
    }

    @Override
    public void saveFile(Map<String, String> files, String filename) throws IOException {
        this.fileRepositoryRW.writeFile(files, filename);
    }
}
