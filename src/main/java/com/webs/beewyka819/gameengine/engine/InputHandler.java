package com.webs.beewyka819.gameengine.engine;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {
    private static final int NO_STATE = -1;
    private static final int KEYBOARD_SIZE = GLFW_KEY_LAST;
    private static final int MOUSE_SIZE = GLFW_MOUSE_BUTTON_LAST;

    private long windowHandle;

    private int[] keyStates = new int[KEYBOARD_SIZE];
    private boolean[] activeKeys = new boolean[KEYBOARD_SIZE];

    private int[] mouseButtonStates = new int[MOUSE_SIZE];
    private boolean[] activeMouseButtons = new boolean[MOUSE_SIZE];
    private int lastButtonClicked = NO_STATE;
    private long lastMouseNS = 0;
    private long mouseDoubleClickPeriodNS = 1000_000_000 / 5;

    private double xpos, ypos;

    private boolean inWindow;

    protected void init(long windowHandle) {
        this.windowHandle = windowHandle;

        glfwSetKeyCallback(this.windowHandle, (window, key, scancode, action, mods) -> {
            activeKeys[key] = action != GLFW_RELEASE;
            keyStates[key] = action;
        });

        glfwSetMouseButtonCallback(this.windowHandle, (window, button, action, mods) -> {
            activeMouseButtons[button] = action != GLFW_RELEASE;
            keyStates[button] = action;
        });

        glfwSetCursorPosCallback(this.windowHandle, (window, xpos, ypos) -> {
            this.xpos = xpos;
            this.ypos = ypos;
        });

        glfwSetCursorEnterCallback(this.windowHandle, (window, entered) -> {
            this.inWindow = entered;
        });

        resetKeyboard();
        resetMouse();
    }

    protected void update() {
        resetKeyboard();
        resetMouse();
    }

    private void resetKeyboard() {
        for(int i = 0; i < keyStates.length; i++) {
            keyStates[i] = NO_STATE;
        }
    }

    private void resetMouse() {
        for(int i = 0; i < mouseButtonStates.length; i++) {
            mouseButtonStates[i] = NO_STATE;
        }

        long now = System.nanoTime();

        if(now - lastMouseNS > mouseDoubleClickPeriodNS) {
            lastMouseNS = 0;
        }
    }

    public boolean keyDown(int key) {
        return activeKeys[key];
    }

    public boolean keyPressed(int key) {
        return keyStates[key] == GLFW_PRESS;
    }

    public boolean keyReleased(int key) {
        return keyStates[key] == GLFW_RELEASE;
    }

    public boolean mouseButtonDown(int button) {
        return activeMouseButtons[button];
    }

    public boolean mouseButtonPressed(int button) {
        return mouseButtonStates[button] == GLFW_PRESS;
    }

    public boolean mouseButtonReleased(int button) {
        boolean flag = mouseButtonStates[button] == GLFW_RELEASE;

        if(flag) {
            lastMouseNS = System.nanoTime();
            lastButtonClicked = button;
        }

        return flag;
    }

    public boolean mouseButtonDoubleClicked(int button) {
        int lastButton = lastButtonClicked;
        long last = lastMouseNS;

        boolean flag = mouseButtonReleased(button);
        if(lastButton == button) {
            long now = System.nanoTime();
            if(flag && now - last < mouseDoubleClickPeriodNS) {
                lastMouseNS = 0;
                return true;
            }
        }
        return false;
    }

    public double getMouseX() {
        return xpos;
    }

    public double getMouseY() {
        return ypos;
    }

    public boolean cursorInWindow() {
        return inWindow;
    }
}