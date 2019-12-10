package com.webs.beewyka819.lwjglengine.engine.items;

import com.webs.beewyka819.lwjglengine.engine.graph.Mesh;
import com.webs.beewyka819.lwjglengine.engine.graph.ModelLoader;

public class SkyBox extends GameItem {
    public SkyBox(String objModel, String textureFile) throws Exception {
        super();
        Mesh skyBoxMesh = ModelLoader.loadModel(objModel, textureFile, ModelLoader.CLOCKWISE);
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }
}