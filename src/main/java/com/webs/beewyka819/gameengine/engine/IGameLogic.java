package com.webs.beewyka819.gameengine.engine;

public interface IGameLogic {
    void init(Window window) throws Exception;

    void input(Window window, InputHandler inputHandler) throws Exception;

    void update(float interval, MouseInput mouseInput, Window window) throws Exception;

    void render(Window window) throws Exception;

    void cleanUp();
}