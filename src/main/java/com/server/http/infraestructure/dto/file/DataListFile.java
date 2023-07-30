package com.server.http.infraestructure.dto.file;

import com.server.http.domain.models.FileModel;

public record DataListFile(String id, String filename, String size, String path, String mimetype, Boolean isFile) {
    public DataListFile(FileModel fileModel) {
        this(
                fileModel.getId(),
                fileModel.getFilename(),
                fileModel.getSize(),
                fileModel.getPath(),
                fileModel.getMimetype(),
                fileModel.getFile());
    }
}
