package com.webs.beewyka819.lwjglengine.engine;

import com.webs.beewyka819.lwjglengine.engine.items.GameItem;

public interface IHud {
    GameItem[] getGameItems();

    default void cleanUp() {
        GameItem[] gameItems = getGameItems();
        for(GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}