package com.server.http.domain.model;

public class FileModel {
    private String fileName;
    private String size;
    private String mimeType;
    private Boolean isFile;

    public FileModel(String fileName, String size, String mimeType, Boolean isFile) {
        this.fileName = fileName;
        this.size = size;
        this.mimeType = mimeType;
        this.isFile = isFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(Boolean isFile) {
        this.isFile = isFile;
    }
}
