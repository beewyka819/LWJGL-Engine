package com.webs.beewyka819.lwjglengine.engine.io;

import org.joml.Vector2d;
import org.joml.Vector2f;

public class MouseInput {
    private final Vector2d previousPos;

    private final Vector2d currentPos;

    private final Vector2f displVec;

    public MouseInput() {
        previousPos = new Vector2d(0, 0);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void input(Window window, InputHandler inputHandler) {
        displVec.x = 0;
        displVec.y = 0;
        currentPos.x = inputHandler.getMouseX();
        currentPos.y = inputHandler.getMouseY();
        if(window.isMouseLocked()) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            displVec.y = (float) deltax;
            displVec.x = (float) deltay;
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }
}