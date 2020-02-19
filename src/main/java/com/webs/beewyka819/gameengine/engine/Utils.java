package com.webs.beewyka819.gameengine.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.apache.commons.io.IOUtils;
import org.lwjgl.system.MemoryUtil;

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
    
    /**
     * Loads a file in the classpath to a ByteBuffer
     * <p>
     * NOTE: The resultant ByteBuffer must be manually freed after
     * usage via MemoryUtil.memFree(). The GC will not
     * free it automatically.
     *
     * @param filePath The directory of the file in the classpath.
     * @throws Exception
     */
    public static ByteBuffer readJarResourceToByteBuffer(String filePath) throws Exception {
        ByteBuffer buf = null;
        try(InputStream in = Utils.class.getResourceAsStream(filePath)) {
            byte[] resourceData = IOUtils.toByteArray(in);
            
            buf = MemoryUtil.memAlloc(resourceData.length);
            buf.put(resourceData).flip();
        }
        return buf;
    }
}