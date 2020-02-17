package com.webs.beewyka819.gameengine.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.fusesource.jansi.Ansi.Color;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class Window {
    private static final int MINIMUM_GL_VERSION = 410;
    
    private final String title;

    private int width, savedWidth;

    private int height, savedHeight;

    private int savedPosX, savedPosY;

    private long windowHandle;

    private GLFWVidMode videoMode;

    private boolean vSync;

    private boolean isResized;

    private boolean isCounterShown;

    private boolean isMouseLocked;

    private boolean isFullscreen;

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

        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        savedPosX = (videoMode.width() - width) / 2;
        savedPosY = (videoMode.height() - height) / 2;

        if(isFullscreen) {
            savedWidth = width;
            savedHeight = height;
            windowHandle = glfwCreateWindow(videoMode.width(), videoMode.height(), title, glfwGetPrimaryMonitor(), NULL);
        } else {
            windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        }
        if(windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        glfwSetWindowPos(windowHandle, savedPosX, savedPosY);

        glfwMakeContextCurrent(windowHandle);

        GL.createCapabilities();

        String[] version = glGetString(GL_VERSION).split(" ");

        ConsoleOutput.printMessage("Detected " + glGetString(GL_RENDERER) + " with driver version: " + version[2], Color.GREEN);
        if(Integer.parseInt(version[0].replace(".", "")) < MINIMUM_GL_VERSION) {
            throw new Exception("This device does not support the minimum required version of OpenGL\n\t"
            + "Minimum version: 4.1.0 | Your Version: " + version[0] + "\n");
        }
        ConsoleOutput.printMessage("Using OpenGL version " + version[0], Color.GREEN);

        glfwSwapInterval(vSync ? 1 : 0);

        glfwShowWindow(windowHandle);

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
        if(windowHandle != 0) {
            glfwFreeCallbacks(windowHandle);
            glfwDestroyWindow(windowHandle);
        }
        
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void updateFrame() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();

        frames++;
        long newTime;
        if((newTime = System.currentTimeMillis()) > time + 1000) {
            if(isCounterShown) {
                glfwSetWindowTitle(windowHandle, title + " | Draw Time (ms): " + 1000.0f / frames + " | Update Time (ms): " + 1000.0f / updates + " | FPS: " + frames + " | UPS: " + updates);
            }
            frames = 0;
            updates = 0;
            time = newTime;
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

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        if(this.vSync != vSync) {
            this.vSync = vSync;
            glfwSwapInterval(vSync ? 1 : 0);
        }
    }

    public boolean isResized() {
        return isResized;
    }

    public void setResized(boolean isResized) {
        this.isResized = isResized;
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

    public boolean isMouseLocked() {
        return isMouseLocked;
    }

    public void setMouseLock(boolean isMouseLocked) {
        if(this.isMouseLocked != isMouseLocked) {
            this.isMouseLocked = isMouseLocked;
            glfwSetInputMode(windowHandle, GLFW_CURSOR, isMouseLocked ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
        }
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setFullscreen(boolean isFullscreen) {
        if(this.isFullscreen != isFullscreen) {
            this.isFullscreen = isFullscreen;
            if(isFullscreen) {
                try(MemoryStack stack = MemoryStack.stackPush()) {
                    IntBuffer xpos = stack.mallocInt(1);
                    IntBuffer ypos = stack.mallocInt(1);
                    glfwGetWindowPos(windowHandle, xpos, ypos);
                    savedPosX = xpos.get();
                    savedPosY = ypos.get();
                }
                savedWidth = width;
                savedHeight = height;
                glfwSetWindowMonitor(windowHandle, glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate());
            } else {
                glfwSetWindowMonitor(windowHandle, NULL, savedPosX, savedPosY, savedWidth, savedHeight, videoMode.refreshRate());
            }
        }
    }
}