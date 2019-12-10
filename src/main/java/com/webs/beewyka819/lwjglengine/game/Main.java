package com.webs.beewyka819.lwjglengine.game;

import com.webs.beewyka819.lwjglengine.engine.GameEngine;
import com.webs.beewyka819.lwjglengine.engine.IGameLogic;

public class Main {
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("GAME", 1280, 720, vSync, gameLogic);
            if(System.getProperty("os.name").contains("Mac")) {
                gameEng.run();
            } else {
                gameEng.start();
            }
        } catch(Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}