package com.webs.beewyka819.gameengine.engine.items;

import com.webs.beewyka819.gameengine.engine.graph.Mesh;

import org.joml.Vector3f;

public class GameItem {
    private Mesh mesh;

    private final Vector3f position;

    private final Vector3f rotation;

    private float scale;

    public GameItem() {
        position = new Vector3f();
        rotation = new Vector3f();
        scale = 1;
    }

    public GameItem(Mesh mesh) {
        this();
        this.mesh = mesh;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}