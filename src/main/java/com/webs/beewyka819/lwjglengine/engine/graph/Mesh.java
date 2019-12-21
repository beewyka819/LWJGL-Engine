package com.webs.beewyka819.lwjglengine.engine.graph;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.function.Consumer;

import com.webs.beewyka819.lwjglengine.engine.items.GameItem;

import org.lwjgl.system.MemoryUtil;

public class Mesh {
    private final int vaoID;

    private final int vboID;

    private final int iboID;

    private final int vertexCount;

    private Material material;

    public Mesh(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        FloatBuffer vertexBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vertexCount = indices.length;

            vaoID = glGenVertexArrays();
            glBindVertexArray(vaoID);

            vboID = glGenBuffers();
            vertexBuffer = MemoryUtil.memAllocFloat(positions.length + textureCoords.length + normals.length);
            vertexBuffer.put(positions).put(textureCoords).put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, Float.BYTES * positions.length);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, Float.BYTES * (positions.length + textureCoords.length));

            iboID = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if(vertexBuffer != null) {
                MemoryUtil.memFree(vertexBuffer);
            }
            if(indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getIboID() {
        return iboID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    private void initRender() {
        Texture texture = material.getTexture();
        if(texture != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
        Texture normalMap = material.getNormalMap();
        if(normalMap != null) {
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, normalMap.getId());
        }

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
    }

    private void endRender() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void render() {
        initRender();
        
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        endRender();
    }

    public void renderList(List<GameItem> gameItems, Consumer<GameItem> consumer) {
        initRender();

        for(GameItem gameItem : gameItems) {
            // Setup data required by gameItem
            consumer.accept(gameItem);
            // Render this game item
            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }

        endRender();
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
        glDeleteBuffers(iboID);

        if(material.isTextured()) {
            material.getTexture().cleanUp();
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }

    public void deleteBuffers() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
        glDeleteBuffers(iboID);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }
}
