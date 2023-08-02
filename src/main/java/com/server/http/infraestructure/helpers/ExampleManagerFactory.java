package com.server.http.infraestructure.helpers;

import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExampleManagerFactory implements NanoHTTPD.TempFileManagerFactory {
    @Override
    public NanoHTTPD.TempFileManager create() {
        return new ExampleManager();
    }

    private static class ExampleManager implements  NanoHTTPD.TempFileManager{
        private final String tmpdir;
        private final List<NanoHTTPD.TempFile> tempFiles;

        private ExampleManager(){
            String userHome = System.getProperty("user.home");
            String userRute = userHome + File.separator + "Documents/Server/temp"; // Puedes cambiar "Documentos" por el nombre de la carpeta que desees
            File fileCreate = new File(userRute);
            if(!fileCreate.exists()){
                fileCreate.mkdir();
            }
            tmpdir = userRute;
            tempFiles = new ArrayList<NanoHTTPD.TempFile>();

        }

        @Override
        public void clear() {
            if(!tempFiles.isEmpty()){
                System.out.println("Cleaning up:");
            }
            for(NanoHTTPD.TempFile file: tempFiles){
                try{
                    System.out.println("  " + file.getName());
                    file.delete();
                } catch (Exception ignored){
                }
            }
            tempFiles.clear();
        }

        @Override
        public NanoHTTPD.TempFile createTempFile(String filename_hint) throws IOException {/*
            if (filename_hint == null) {
                filename_hint = "default_filename"; // Puedes usar un valor predeterminado si filename_hint es nulo
            }
            if (filename_hint.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid filename_hint: " + filename_hint);
            }*/
            File tempFile = File.createTempFile("NanoHTTPD-", "", new File(tmpdir));
            ExampleTempFile nanoTempFile = new ExampleTempFile(tempFile);
            tempFiles.add(nanoTempFile);
            return nanoTempFile;
        }
    }

    private static class ExampleTempFile implements NanoHTTPD.TempFile {
        private final File file;
        private FileOutputStream outputStream;

        public ExampleTempFile(File file) {
            this.file = file;
        }
        @Override
        public OutputStream open() throws Exception {
            outputStream = new FileOutputStream(file);
            return outputStream;
        }
        @Override
        public void delete() throws Exception {
            if(outputStream != null){
                outputStream.close();
            }
            if (file != null && file.exists()) {
                file.delete();
            }
            // System.gc() can liberate trash, this is recommended for accelerate light temp files
        }

        @Override
        public String getName() {
            return file != null ? file.getAbsolutePath() : null;
        }
    }
}
