package com.webs.beewyka819.lwjglengine.engine.io;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Window {
    private final String title;

    private int width;

    private int height;

    private long windowHandle;

    private boolean isResized;

    private boolean vSync;

    private boolean isMouseLocked;

    private boolean isCounterShown;

    private long time;

    private int frames;

    private int updates;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
    }

    public void init() throws Exception {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        if(System.getProperty("os.name").contains("Mac")) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        } else {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        }
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if(windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        InputHandler.init(windowHandle);

        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
            windowHandle,
            (videoMode.width() - width) / 2,
            (videoMode.height() - height) / 2
        );

        glfwMakeContextCurrent(windowHandle);

        if(isvSync()) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }

        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        time = System.currentTimeMillis();
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void closeWindow() {
        glfwSetWindowShouldClose(windowHandle, true);
    }

    public void cleanUp() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void updateFrame() {
        glfwSwapBuffers(windowHandle);
        InputHandler.update();

        frames++;
        if(System.currentTimeMillis() > time + 1000) {
            if(isCounterShown) {
                glfwSetWindowTitle(windowHandle, title + " | Draw Time (ms): " + 1000.0f / frames + " | Update Time (ms): " + 1000.0f / updates + " | FPS: " + frames + " | UPS: " + updates);
            }
            frames = 0;
            updates = 0;
            time = System.currentTimeMillis();
        }
    }

    public void updateTick() {
        updates++;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public boolean isResized() {
        return isResized;
    }

    public void setResized(boolean isResized) {
        this.isResized = isResized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        if(this.vSync != vSync) {
            this.vSync = vSync;
            if(vSync) {
                glfwSwapInterval(1);
            } else {
                glfwSwapInterval(0);
            }
        }
    }

    public boolean isMouseLocked() {
        return isMouseLocked;
    }

    public void setMouseLock(boolean isMouseLocked) {
        if(this.isMouseLocked != isMouseLocked) {
            this.isMouseLocked = isMouseLocked;
            if(isMouseLocked) {
                glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            } else {
                glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }
        }
    }

    public boolean isCounterShown() {
        return isCounterShown;
    }

    public void showCounter(boolean isCounterShown) {
        if(this.isCounterShown != isCounterShown) {
            this.isCounterShown = isCounterShown;
            if(!isCounterShown) {
                glfwSetWindowTitle(windowHandle, title);
            }
        }
    }
}