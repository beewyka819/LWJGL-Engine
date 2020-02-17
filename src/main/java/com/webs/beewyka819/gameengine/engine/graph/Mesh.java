package com.webs.beewyka819.gameengine.engine.graph;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    private final int vaoID;

    private final int vboID;

    private final int iboID;

    private final int vertexCount;

    private Material material;

    public Mesh(float[] positions, float[] textureCoords, float[] normals, int[] indices) throws Exception {
        FloatBuffer vertexBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vertexCount = indices.length;

            vaoID = glGenVertexArrays();
            glBindVertexArray(vaoID);

            vboID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            vertexBuffer = memAllocFloat(positions.length + textureCoords.length + normals.length);
            vertexBuffer.put(positions).put(textureCoords).put(normals).flip();
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, Float.BYTES * positions.length);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, Float.BYTES * (positions.length + textureCoords.length));

            iboID = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
            indicesBuffer = memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if(vertexBuffer != null) {
                memFree(vertexBuffer);
            }
            if(indicesBuffer != null) {
                memFree(indicesBuffer);
            }
        }
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void render() {
        Texture texture = material.getTexture();
        if(material.hasTexture()) {
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
        }

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        if(material.hasTexture()) {
            texture.unbind();
            glActiveTexture(0);
        }
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
        glDeleteBuffers(iboID);

        if(material.hasTexture()) {
            material.getTexture().cleanUp();
        }

        glBindVertexArray(vaoID);
        glDeleteVertexArrays(vaoID);
    }
}