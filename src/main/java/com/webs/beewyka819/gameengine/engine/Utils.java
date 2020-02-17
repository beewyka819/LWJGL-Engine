package com.webs.beewyka819.gameengine.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    public static String readJarResourceToString(String filePath) throws Exception {
        StringBuilder result = new StringBuilder();
        try(InputStream in = Utils.class.getResourceAsStream(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    String line;
                    while((line = reader.readLine()) != null) {
                        result.append(line).append("\n");
                    }
        }
        return result.toString();
    }
}