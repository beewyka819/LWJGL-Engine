package com.webs.beewyka819.lwjglengine.engine;

import com.webs.beewyka819.lwjglengine.engine.io.MouseInput;
import com.webs.beewyka819.lwjglengine.engine.io.Sync;
import com.webs.beewyka819.lwjglengine.engine.io.Window;

public class GameEngine implements Runnable {
    private static final int TARGET_FPS = 75;
    private static final int TARGET_UPS = 30;

    private final Window window;

    private final Sync sync;

    private final Timer timer;

    private final IGameLogic gameLogic;

    private final MouseInput mouseInput;

    private final Thread gameLoopThread;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        mouseInput = new MouseInput();
        sync = new Sync();
        timer = new Timer();
    }

    public void start() {
        gameLoopThread.start();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch(Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    private void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init(window);
    }

    private void gameLoop() throws Exception {
        float elapsedTime;
        float accumulator = 0.0f;
        float interval = 1.0f / TARGET_UPS;

        boolean running = true;
        while(running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while(accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();
            
            if(!window.isvSync()) {
                sync.sync(TARGET_FPS);
            }
        }
    }

    private void input() throws Exception {
        mouseInput.input(window);
        gameLogic.input(window);
    }

    private void update(float interval) throws Exception {
        gameLogic.update(interval, mouseInput, window);
        window.updateTick();
    }

    private void render() throws Exception {
        gameLogic.render(window);
        window.updateFrame();
    }

    private void cleanUp() {
        gameLogic.cleanUp();
        window.cleanUp();
    }
}