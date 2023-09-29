package com.server.http.domain.model;

public class FileModel {

    private String id;
    private String filename;
    private String size;

    private String mimetype;
    private Boolean isFile;

    public FileModel(String id, String filename, String size, String mimetype, Boolean isFile) {
        this.id = id;
        this.filename = filename;
        this.size = size;
        this.mimetype = mimetype;
        this.isFile = isFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public Boolean getFile() {
        return isFile;
    }

    public void setFile(Boolean file) {
        isFile = file;
    }
}
