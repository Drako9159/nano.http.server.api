package com.server.http.domain.dto;

import com.server.http.domain.model.FileModel;

public record DataListFile(String id, String filename, String size, String mimetype, Boolean isFile) {
    public DataListFile(FileModel fileModel) {
        this(
                fileModel.getId(),
                fileModel.getFilename(),
                fileModel.getSize(),
                fileModel.getMimetype(),
                fileModel.getFile());
    }
}
