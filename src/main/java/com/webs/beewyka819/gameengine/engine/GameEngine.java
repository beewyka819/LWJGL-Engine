package com.webs.beewyka819.gameengine.engine;

import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi.Color;

public class GameEngine implements Runnable {
    private static final int TARGET_FPS = 75;
    private static final int TARGET_UPS = 30;

    private final Window window;

    private final Timer timer;

    private final Sync sync;

    private final InputHandler inputHandler;

    private final MouseInput mouseInput;

    private final IGameLogic gameLogic;

    private final Thread gameLoopThread;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        timer = new Timer();
        sync = new Sync();
        inputHandler = new InputHandler();
        mouseInput = new MouseInput();
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
            ConsoleOutput.printException(excp, Color.RED);
        } finally {
            ConsoleOutput.printMessage("Terminating Process...", Color.GREEN);
            cleanUp();
        }
    }

    private void init() throws Exception {
        AnsiConsole.systemInstall();
        ConsoleOutput.printMessage("Initializing...", Color.GREEN);
        window.init();
        inputHandler.init(window.getWindowHandle());
        timer.init();
        gameLogic.init(window);
        ConsoleOutput.printMessage("Initialization Complete!", Color.GREEN);
    }

    private void gameLoop() throws Exception {
        float elapsedTime;
        float accumulator = 0.0f;
        float interval = 1.0f / TARGET_UPS;
        
        boolean running = true;
        while(running) {
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

            running = !window.windowShouldClose();
        }
    }

    private void input() throws Exception {
        gameLogic.input(window, inputHandler);
        inputHandler.update();
    }

    private void update(float interval) throws Exception {
        mouseInput.input(window, inputHandler);
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
        AnsiConsole.systemUninstall();
    }
}