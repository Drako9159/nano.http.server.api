package com.server.http.infraestructure.helpers;

import javax.activation.MimetypesFileTypeMap;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Util {

    public static String getExtension(String name){
        int index = name.lastIndexOf(".");
        String extension = null;
        if(index > 0){
            extension = name.substring(index + 1).toLowerCase();
        } else {
            extension = "folder";
        }
        return extension;
    }


    public static String getMimeType(String filename){
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        return fileTypeMap.getContentType(filename);
    }

    public static String getSizeMb(Long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

}
