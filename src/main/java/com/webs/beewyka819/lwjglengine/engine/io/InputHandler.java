package com.webs.beewyka819.lwjglengine.engine.io;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {
    private static final int NO_STATE = -1;
    private static final int KEYBOARD_SIZE = GLFW_KEY_LAST;
    private static final int MOUSE_SIZE = GLFW_MOUSE_BUTTON_LAST;

    private static long windowHandle;

    private static int[] keyStates = new int[KEYBOARD_SIZE];
    private static boolean[] activeKeys = new boolean[KEYBOARD_SIZE];

    private static int[] mouseButtonStates = new int[MOUSE_SIZE];
    private static boolean[] activeMouseButtons = new boolean[MOUSE_SIZE];
    private static int lastButtonClicked = NO_STATE;
    private static long lastMouseNS = 0;
    private static long mouseDoubleClickPeriodNS = 1000_000_000 / 5;
    
    private static double mouseX, mouseY;

    private static boolean inWindow;

    protected static void init(long windowHandle) {
        InputHandler.windowHandle = windowHandle;

        glfwSetKeyCallback(InputHandler.windowHandle, (window, key, scancode, action, mods) -> {
            activeKeys[key] = action != GLFW_RELEASE;
            keyStates[key] = action;
        });

        glfwSetMouseButtonCallback(InputHandler.windowHandle, (window, button, action, mods) -> {
            activeMouseButtons[button] = action != GLFW_RELEASE;
            mouseButtonStates[button] = action;
        });

        glfwSetCursorPosCallback(InputHandler.windowHandle, (window, xpos, ypos) -> {
            mouseX = xpos;
            mouseY = ypos;
        });

        glfwSetCursorEnterCallback(InputHandler.windowHandle, (window, entered) -> {
            inWindow = entered;
        });

        resetKeyboard();
        resetMouse();
    }

    protected static void update() {
        resetKeyboard();
        resetMouse();

        glfwPollEvents();
    }

    private static void resetKeyboard() {
        for(int i = 0; i < keyStates.length; i++) {
            keyStates[i] = NO_STATE;
        }
    }

    private static void resetMouse() {
        for(int i = 0; i < mouseButtonStates.length; i++) {
            mouseButtonStates[i] = NO_STATE;
        }

        long now = System.nanoTime();

        if(now - lastMouseNS > mouseDoubleClickPeriodNS) {
            lastMouseNS = 0;
        }
    }

    public static boolean keyDown(int key) {
        return activeKeys[key];
    }

    public static boolean keyPressed(int key) {
        return keyStates[key] == GLFW_PRESS;
    }

    public static boolean keyReleased(int key) {
        return keyStates[key] == GLFW_RELEASE;
    }

    public static boolean mouseButtonDown(int button) {
        return activeMouseButtons[button];
    }

    public static boolean mouseButtonPressed(int button) {
        return mouseButtonStates[button] == GLFW_PRESS;
    }

    public static boolean mouseButtonReleased(int button) {
        boolean flag = mouseButtonStates[button] == GLFW_RELEASE;

        if(flag) {
            lastMouseNS = System.nanoTime();
            lastButtonClicked = button;
        }
        return flag;
    }

    public static boolean mouseButtonDoubleClicked(int button) {
        long last = lastMouseNS;
        int lastButton = lastButtonClicked;

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

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public static boolean cursorInWindow() {
        return inWindow;
    }
}