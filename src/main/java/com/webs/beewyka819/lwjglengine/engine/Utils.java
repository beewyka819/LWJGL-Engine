package com.webs.beewyka819.lwjglengine.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Utils {
    public static String loadResource(String fileName) throws Exception {
        StringBuilder result = new StringBuilder();
        try(InputStream in = Utils.class.getResourceAsStream(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    String line;
                    while((line = reader.readLine()) != null) {
                        result.append(line).append("\n");
                    }
        }
        return result.toString();
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for(int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }
}