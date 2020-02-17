package com.webs.beewyka819.gameengine.engine;

import java.util.ArrayList;
import java.util.List;

import com.webs.beewyka819.gameengine.engine.items.GameItem;

public class Scene {
    private final List<GameItem> gameItems;

    private SceneLight sceneLight;

    public Scene() {
        gameItems = new ArrayList<GameItem>();
    }

    public List<GameItem> getGameItems() {
        return gameItems;
    }

    public void addGameItem(GameItem gameItem) {
        gameItems.add(gameItem);
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }

    public void cleanUp() {
        for(GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}