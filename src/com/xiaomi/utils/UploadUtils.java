package com.xiaomi.utils;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class UploadUtils {
    public static String createNewFileName(String filename) {
        String s = UUID.randomUUID().toString().replace("-", "");
        return s + "_" + filename;
    }

    public static String createNewPath(String basePath, String filename) {
        int hashCode = Math.abs(filename.hashCode());
        int dir1 = hashCode&0xf;
        int dir2 = (hashCode >> 4)&0xf;
        String newPath = basePath + "/" + dir1 + "/" + dir2;
        File file = new File(newPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return newPath;
    }

    public static void listFiles(File file, HashMap<String, String> filemaps) {
        File[] files = file.listFiles();
        if (file != null && files.length > 0) {
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    listFiles(file1, filemaps);
                } else {
                    //文件放入map
                    filemaps.put(file1.getName(), file1.getName().split("_")[1]);
                }
            }
        }
    }
}
