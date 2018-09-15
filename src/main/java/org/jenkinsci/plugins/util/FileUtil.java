package org.jenkinsci.plugins.util;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static String readFile(String filePath) {
        try {
            String str = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8); 
            return str;
        } catch (Exception e) {
        }
        return "";
    }
}
