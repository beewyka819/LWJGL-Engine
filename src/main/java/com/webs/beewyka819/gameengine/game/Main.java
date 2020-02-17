package com.webs.beewyka819.gameengine.game;

import com.webs.beewyka819.gameengine.engine.GameEngine;
import com.webs.beewyka819.gameengine.engine.IGameLogic;

public class Main {
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new ProjectParadis();
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