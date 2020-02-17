package com.webs.beewyka819.gameengine.engine.graph;

import static org.lwjgl.assimp.Assimp.*;

import org.joml.Vector4f;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

public class ModelLoader {
    public static boolean CCW = true;
    public static boolean CW = false;

    public static Mesh loadModel(String objFilePath, String textureFilePath, boolean windingOrder) throws Exception {
        Mesh mesh = loadMesh(objFilePath, windingOrder);
        Material material = new Material(new Texture(textureFilePath));
        mesh.setMaterial(material);
        return mesh;
    }

    public static Mesh loadModel(String objFilePath, Vector4f color, boolean windingOrder) throws Exception {
        Mesh mesh = loadMesh(objFilePath, windingOrder);
        Material material = new Material(color, 0.0f);
        mesh.setMaterial(material);
        return mesh;
    }

    private static Mesh loadMesh(String objFilePath, boolean windingOrder) throws Exception {
        AIScene scene = null;
        if(windingOrder) {
            scene = aiImportFile(objFilePath, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_FindInvalidData | aiProcess_FlipUVs);
        } else {
            scene = aiImportFile(objFilePath, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_FindInvalidData | aiProcess_FlipUVs | aiProcess_FlipWindingOrder);
        }
        if(scene == null) {
            throw new Exception("Model file [" + objFilePath + "] not loaded: " + aiGetErrorString());
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
                textureCoordsList[i * 2 + 1] = texture.y();
            }
        }

        int faceCount = mesh.mNumFaces();
        AIFace.Buffer faces = mesh.mFaces();
        int[] indicesList = new int[faceCount * 3];

        for(int i = 0; i < faceCount; i++) {
            AIFace face = faces.get(i);
            indicesList[i * 3] = face.mIndices().get(0);
            indicesList[i * 3 + 1] = face.mIndices().get(1);
            indicesList[i * 3 + 2] = face.mIndices().get(2);
        }

        return new Mesh(positionsList, textureCoordsList, normalsList, indicesList);
    }
}