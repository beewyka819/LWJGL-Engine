package com.webs.beewyka819.lwjglengine.engine;

import com.webs.beewyka819.lwjglengine.engine.io.MouseInput;
import com.webs.beewyka819.lwjglengine.engine.io.Window;

public interface IGameLogic {
    void init(Window window) throws Exception;

    void input(Window window) throws Exception;

    void update(float interval, MouseInput mouseInput, Window window) throws Exception;

    void render(Window window) throws Exception;

    void cleanUp();
}