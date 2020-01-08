package com.webs.beewyka819.lwjglengine.engine;

import com.webs.beewyka819.lwjglengine.engine.io.InputHandler;
import com.webs.beewyka819.lwjglengine.engine.io.MouseInput;
import com.webs.beewyka819.lwjglengine.engine.io.Window;

/**
 * The IGameLogic interface should be implemented by any class 
 * that defines the game logic to be used by a GameEngine. 
 * The class must define methods called init, input, update, 
 * render, and cleanUp.
 * <p>
 * The init and render methods must each contain a Window
 * argument for the game window.
 * <p>
 * <p>
 * The input method must contain a Window argument as well as
 * an InputHandler argument.
 * <p>
 * The update method must contain a float argument for the update
 * interval that it's called on, as well as a Window argument.
 * <p>
 * The cleanUp method must contain no arguments.
 */
public interface IGameLogic {
    /**
     * An object that implements the IGameLogic interface
     * has the init method called before the main game loop
     * is started.
     * <p>
     * This method is intended to handle all startup operations.
     * @param window - the game window
     * @throws Exception
     */
    void init(Window window) throws Exception;

    /**
     * An object that implements the IGameLogic interface
     * has the input method called once every frame in the
     * main game loop.
     * <p>
     * This method is intended to handle all user
     * inputs.
     * <p>
     * Note: GameEngine handles the InputHandler init and
     * update methods. They do not need to be called by the
     * game logic.
     * @param window - the game window
     * @param inputHandler - the InputHandler for the game window
     * @throws Exception
     */
    void input(Window window, InputHandler inputHandler) throws Exception;

    /**
     * An object that implements the IGameLogic interface
     * has the update method called at the UPS interval.
     * <p>
     * This method is intended for all time sensitive game logic that must
     * run on a consistent interval.
     * @param interval - the interval at which the method is called, 
     * in seconds
     * @param window - the game window
     * @throws Exception
     */
    void update(float interval, MouseInput mouseInput, Window window) throws Exception;

    /**
     * An object that implements the IGameLogic interface
     * has the render method called once every frame in the
     * main game loop.
     * <p>
     * This method is intended to handle all rendering
     * operations.
     * @param window
     * @throws Exception
     */
    void render(Window window) throws Exception;

    /**
     * An object that implements the IGameLogic interface
     * has the cleanUp method called during the termination
     * of the game engine.
     * <p>
     * This method is intended to handle the
     * cleaning/freeing of all memory, shaders, etc. that are
     * not handled by the JVM.
     */
    void cleanUp();
}