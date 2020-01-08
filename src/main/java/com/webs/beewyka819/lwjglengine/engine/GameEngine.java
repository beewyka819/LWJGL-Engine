package com.webs.beewyka819.lwjglengine.engine;

import com.webs.beewyka819.lwjglengine.engine.io.InputHandler;
import com.webs.beewyka819.lwjglengine.engine.io.MouseInput;
import com.webs.beewyka819.lwjglengine.engine.io.Sync;
import com.webs.beewyka819.lwjglengine.engine.io.Window;

/**
 * Handles the entire game engine, including the window and
 * main game loop structure.
 * </p>
 * To run on a new thread, call the start method, else call 
 * the run method.
 */
public class GameEngine implements Runnable {
    /** The FPS the engine will run at.
     * </p>
     * Note: if Target FPS is higher than the monitors current refresh rate,
     * then the engine will instead run at the refresh rate if vSync
     * is enabled.
     */
    private static final int TARGET_FPS = 75;
    
    /** The rate at which the game state is updated. 
     * </p>
     * Time sensitive calculations, such as physics calculations, 
     * should be bound to UPS, as UPS is consistent while FPS 
     * fluctuations can result in inaccurate calculations.
     */
    private static final int TARGET_UPS = 30;

    private final Window window;

    private final Sync sync;

    private final Timer timer;
    
    private final InputHandler inputHandler;

    private final IGameLogic gameLogic;

    private final MouseInput mouseInput;

    private final Thread gameLoopThread;

    /**
     * Creates a new instance of GameEnigne
     * @param windowTitle - the title of the game window
     * @param width - the starting width of the game window
     * @param height - the starting height of the game window
     * @param vSync - whether or not to use vSync at startup
     * @param gameLogic - the game logic that the engine runs on,
     * see {@link IGameLogic}.
     * @throws Exception
     * @see com.webs.beewyka819.tempengine.engine.IGameLogic
     */
    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        mouseInput = new MouseInput();
        sync = new Sync();
        timer = new Timer();
        inputHandler = new InputHandler();
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
        inputHandler.init(window.getWindowHandle());
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
        mouseInput.input(window, inputHandler);
        gameLogic.input(window, inputHandler);
        inputHandler.update();
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