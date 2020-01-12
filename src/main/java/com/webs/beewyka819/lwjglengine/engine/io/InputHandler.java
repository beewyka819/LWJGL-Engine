package com.webs.beewyka819.lwjglengine.engine.io;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Sets up key, mouse, and cursor callbacks, and can be
 * queried for key, mouse, and cursor states.
 * @author beewyka819
 */
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

    private double mouseX, mouseY;

    private boolean inWindow;

    /**
     * Initializes the key, mouse, and cursor callbacks for the given
     * window.
     * @param windowHandle - the handle of the window. See
     * {@link Window#getWindowHandle()}.
     * @see com.webs.beewyka819.tempengine.engine.io.Window
     */
    public void init(long windowHandle) {
        this.windowHandle = windowHandle;

        glfwSetKeyCallback(this.windowHandle, (window, key, scancode, action, mods) -> {
            activeKeys[key] = action != GLFW_RELEASE;
            keyStates[key] = action;
        });

        glfwSetMouseButtonCallback(this.windowHandle, (window, button, action, mods) -> {
            activeMouseButtons[button] = action != GLFW_RELEASE;
            mouseButtonStates[button] = action;
        });

        glfwSetCursorPosCallback(this.windowHandle, (window, xpos, ypos) -> {
            mouseX = xpos;
            mouseY = ypos;
        });

        glfwSetCursorEnterCallback(this.windowHandle, (window, entered) -> {
            inWindow = entered;
        });

        resetKeyboard();
        resetMouse();
    }

    /**
     * Wipes the current list of key and mouse states.
     * <p>
     * Should be called right after querying key inputs for a frame.
     */
    public void update() {
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

    /**
     * Returns if a given key is being held down.
     * @param key - the id of the key being queried. The id
     * is based on constants in {@link org.lwjgl.glfw.GLFW}.
     * @return if a given key is being held down.
     * @see org.lwjgl.glfw.GLFW
     */
    public boolean keyDown(int key) {
        return activeKeys[key];
    }

    /**
     * Returns if a given key has been pressed in the last frame.
     * @param key - the id of the key being queried. The id
     * is based on constants in {@link org.lwjgl.glfw.GLFW}.
     * @return if a given key has been pressed in the last frame.
     * @see org.lwjgl.glfw.GLFW
     */
    public boolean keyPressed(int key) {
        return keyStates[key] == GLFW_PRESS;
    }

    /**
     * Returns if a given key has been released in the last frame.
     * @param key - the id of the key being queried. The id
     * is based on constants in {@link org.lwjgl.glfw.GLFW}.
     * @return if a given key has been released in the last frame.
     * @see org.lwjgl.glfw.GLFW
     */
    public boolean keyReleased(int key) {
        return keyStates[key] == GLFW_RELEASE;
    }

    /**
     * Returns if a given mouse button is being held down.
     * @param button - the id of the mouse button being queried. The id
     * is based on constants in {@link org.lwjgl.glfw.GLFW}.
     * @return if a given mouse button is being held down.
     * @see org.lwjgl.glfw.GLFW
     */
    public boolean mouseButtonDown(int button) {
        return activeMouseButtons[button];
    }

    /**
     * Returns if a given mouse button has been pressed in the last frame.
     * @param button - the id of the mouse button being queried. The id
     * is based on constants in {@link org.lwjgl.glfw.GLFW}.
     * @return if a given mouse button has been pressed in the last frame.
     * @see org.lwjgl.glfw.GLFW
     */
    public boolean mouseButtonPressed(int button) {
        return mouseButtonStates[button] == GLFW_PRESS;
    }

    /**
     * Returns if a given mouse button has been released in 
     * the last frame.
     * @param button - the id of the mouse button being queried. The id
     * is based on constants in {@link org.lwjgl.glfw.GLFW}.
     * @return if a given mouse button has been released in 
     * the last frame.
     * @see org.lwjgl.glfw.GLFW
     */
    public boolean mouseButtonReleased(int button) {
        boolean flag = mouseButtonStates[button] == GLFW_RELEASE;

        if(flag) {
            lastMouseNS = System.nanoTime();
            lastButtonClicked = button;
        }
        return flag;
    }
    /**
     * Returns if a given mouse button has been double clicked.
     * @param button - the id of the mouse button being queried. The id
     * is based on constants in {@link org.lwjgl.glfw.GLFW}.
     * @return if a given mouse button has been double clicked.
     * @see org.lwjgl.glfw.GLFW
     */
    public boolean mouseButtonDoubleClicked(int button) {
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

    /**
     * @return the current x coordinate of the cursor
     */
    public double getMouseX() {
        return mouseX;
    }

    /**
     * @return the current y coordinate of the cursor
     */
    public double getMouseY() {
        return mouseY;
    }

    /**
     * @return if the cursor is inside the window
     */
    public boolean cursorInWindow() {
        return inWindow;
    }
}
