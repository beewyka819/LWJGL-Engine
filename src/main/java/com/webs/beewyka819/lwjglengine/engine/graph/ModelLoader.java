package com.webs.beewyka819.lwjglengine.engine.graph;

import static org.lwjgl.assimp.Assimp.*;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.system.MemoryUtil;

public class ModelLoader {
    public static final boolean COUNTER_CLOCKWISE = true;
    public static final boolean CLOCKWISE = false;

    public static Mesh loadModel(String filePath, String texturePath, boolean windingOrder) throws Exception {
        Mesh mesh = loadMesh(filePath, windingOrder);
        Texture texture = new Texture(texturePath);
        Material material = new Material(texture);
        mesh.setMaterial(material);
        return mesh;
    }

    public static Mesh loadModel(String filePath, Vector4f color, boolean windingOrder) throws Exception {
        Mesh mesh = loadMesh(filePath, windingOrder);
        Material material = new Material(color, 0.0f);
        mesh.setMaterial(material);
        return mesh;
    }

    private static Mesh loadMesh(String filePath, boolean windingOrder) throws Exception {
        ByteBuffer modelBuffer = null;
        Mesh finalMesh = null;
        try(InputStream in = ModelLoader.class.getResourceAsStream(filePath)) {
            if(in == null) {
                throw new Exception("Model file [" + filePath + "] not loaded: Unable to open file");
            }

            byte[] modelData = IOUtils.toByteArray(in);

            modelBuffer = MemoryUtil.memAlloc(modelData.length);
            modelBuffer.put(modelData).flip();

            String extension = filePath.split(Pattern.quote("."))[1];

            AIScene scene = null;
            if(windingOrder) {
                scene = aiImportFileFromMemory(modelBuffer, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_FindInvalidData, extension);
            } else {
                scene = aiImportFileFromMemory(modelBuffer, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_FindInvalidData | aiProcess_FlipWindingOrder, extension);
            }
            if(scene == null) {
                throw new Exception("Model file [" + filePath + "] not loaded: " + aiGetErrorString());
            }

            AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
            int vertexCount = mesh.mNumVertices();

            AIVector3D.Buffer vertices = mesh.mVertices();
            AIVector3D.Buffer normals = mesh.mNormals();

            float[] positionsList = new float[vertexCount * 3];
            float[] textureCoordsList = new float[vertexCount * 2];
            float[] normalsList = new float[vertexCount * 3];

            for(int i = 0; i < vertexCount; i++) {
                AIVector3D vertex = vertices.get(i);
                positionsList[i * 3] = vertex.x();
                positionsList[i * 3 + 1] = vertex.y();
                positionsList[i * 3 + 2] = vertex.z();

                if(normals != null) {
                    AIVector3D normal = normals.get(i);
                    normalsList[i * 3] = normal.x();
                    normalsList[i * 3 + 1] = normal.y();
                    normalsList[i * 3 + 2] = normal.z();
                }
                
                if(mesh.mNumUVComponents().get(0) != 0) {
                    AIVector3D texture = mesh.mTextureCoords(0).get(i);
                    textureCoordsList[i * 2] = texture.x();
                    textureCoordsList[i * 2 + 1] = 1 - texture.y();
                }
            }

            int faceCount = mesh.mNumFaces();
            AIFace.Buffer indices = mesh.mFaces();
            int[] indicesList = new int[faceCount * 3];

            for(int i = 0; i < faceCount; i++) {
                AIFace face = indices.get(i);
                indicesList[i * 3] = face.mIndices().get(0);
                indicesList[i * 3 + 1] = face.mIndices().get(1);
                indicesList[i * 3 + 2] = face.mIndices().get(2);
            }

            finalMesh = new Mesh(positionsList, textureCoordsList, normalsList, indicesList);
        } finally {
            if(modelBuffer != null) {
                MemoryUtil.memFree(modelBuffer);
            }
        }
        return finalMesh;
    }
}