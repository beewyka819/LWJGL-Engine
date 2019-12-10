package com.webs.beewyka819.lwjglengine.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webs.beewyka819.lwjglengine.engine.graph.Mesh;
import com.webs.beewyka819.lwjglengine.engine.graph.weather.Fog;
import com.webs.beewyka819.lwjglengine.engine.items.GameItem;
import com.webs.beewyka819.lwjglengine.engine.items.SkyBox;

public class Scene {
    private Map<Mesh, List<GameItem>> meshMap;

    private SkyBox skyBox;

    private SceneLight sceneLight;

    private Fog fog;

    public Scene() {
        meshMap = new HashMap<Mesh, List<GameItem>>();
        fog = Fog.NOFOG;
    }

    public Map<Mesh, List<GameItem>> getGameMeshes() {
        return meshMap;
    }

    public void setGameItems(GameItem[] gameItems) {
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for(int i = 0; i < numGameItems; i++) {
            GameItem gameItem = gameItems[i];
            Mesh mesh = gameItem.getMesh();
            List<GameItem> list = meshMap.get(mesh);
            if(list == null) {
                list = new ArrayList<GameItem>();
                meshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
    }

    public boolean hasSkyBox() {
        return skyBox != null;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }

    public void cleanUp() {
        for(Mesh mesh : meshMap.keySet()) {
            mesh.cleanUp();
        }
    }

    public Fog getFog() {
        return fog;
    }

    public void setFog(Fog fog) {
        this.fog = fog;
    }
}