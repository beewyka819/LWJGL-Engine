package com.webs.beewyka819.lwjglengine.game;

import static org.lwjgl.glfw.GLFW.*;

import com.webs.beewyka819.lwjglengine.engine.IGameLogic;
import com.webs.beewyka819.lwjglengine.engine.Scene;
import com.webs.beewyka819.lwjglengine.engine.SceneLight;
import com.webs.beewyka819.lwjglengine.engine.items.SkyBox;
import com.webs.beewyka819.lwjglengine.engine.items.Terrain;
import com.webs.beewyka819.lwjglengine.engine.graph.Camera;
import com.webs.beewyka819.lwjglengine.engine.graph.lights.DirectionalLight;
import com.webs.beewyka819.lwjglengine.engine.graph.weather.Fog;
import com.webs.beewyka819.lwjglengine.engine.graph.Renderer;
import com.webs.beewyka819.lwjglengine.engine.io.InputHandler;
import com.webs.beewyka819.lwjglengine.engine.io.MouseInput;
import com.webs.beewyka819.lwjglengine.engine.io.Window;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class DummyGame implements IGameLogic {
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.1f;
    private static final float CAMERA_POS_SPRINT = 0.2f;

    private final Renderer renderer;

    private final Camera camera;

    private final Vector3f cameraInc;

    private Scene scene;

    //private float angleInc;

    private float lightAngle;

    private boolean isSprinting;

    private boolean isMouseLocked;

    private Hud hud;

    private Terrain terrain;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init();

        window.showCounter(true);

        scene = new Scene();

        float skyBoxScale = 50.0f;
        float terrainScale = 10.0f;
        int terrainSize = 3;
        float minY = -0.1f;
        float maxY = 0.1f;
        int textInc = 40;
        terrain = new Terrain(terrainSize, terrainScale, minY, maxY, "textures/heightmap.png", "textures/terrain.png", textInc);
        scene.setGameItems(terrain.getGameItems());

        scene.setFog(new Fog(true, new Vector3f(0.39f, 0.62f, 0.99f), 0.05f));
        window.setClearColor(0.39f, 0.62f, 0.99f, 1.0f);

        // Setup SkyBox
        SkyBox skyBox = new SkyBox("/res/models/skybox.obj", "textures/SkyBox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        // Create HUD
        hud = new Hud("SNAPSHOT");

        camera.getPosition().x = 0.0f;
        camera.getPosition().y = 5.0f;
        camera.getPosition().z = 0.0f;
        camera.getRotation().x = 90.0f;

        isMouseLocked = true;
        isSprinting = false;
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightDirection = new Vector3f(-1, 0, 0);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        directionalLight.setShadowPosMult(5);
        directionalLight.setOrthoCords(-100.0f, 100.0f, -100.0f, 100.0f, -1.0f, 150.0f);
        sceneLight.setDirectionalLight(directionalLight);
    }

    @Override
    public void input(Window window, InputHandler inputHandler) throws Exception {
        if(inputHandler.keyPressed(GLFW_KEY_ESCAPE)) {
            window.closeWindow();
        }

        if(inputHandler.keyPressed(GLFW_KEY_F1)) {
            window.showCounter(!window.isCounterShown());
        }

        if(inputHandler.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
            isSprinting = true;
        } else if(!inputHandler.keyDown(GLFW_KEY_W)) {
            isSprinting = false;
        }

        cameraInc.set(0, 0, 0);
        if(inputHandler.keyDown(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if(inputHandler.keyDown(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if(inputHandler.keyDown(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if(inputHandler.keyDown(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if(inputHandler.keyDown(GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -1;
        } else if(inputHandler.keyDown(GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }

        if(inputHandler.keyPressed(GLFW_KEY_E)) {
            isMouseLocked = !isMouseLocked;
        }
     }

    @Override
    public void update(float interval, MouseInput mouseInput, Window window) throws Exception {
        window.setMouseLock(isMouseLocked);

        Vector2f rotVec = mouseInput.getDisplVec();
        if(camera.getRotation().x + rotVec.x * MOUSE_SENSITIVITY > 90) {
            camera.getRotation().x = 90.0f;
            camera.moveRotation(0, rotVec.y * MOUSE_SENSITIVITY, 0);
        } else if(camera.getRotation().x + rotVec.x * MOUSE_SENSITIVITY < -90) {
            camera.getRotation().x = -90.0f;
            camera.moveRotation(0, rotVec.y * MOUSE_SENSITIVITY, 0);
        } else {
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
        
        hud.rotateCompass(camera.getRotation().y);

        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * (isSprinting ? CAMERA_POS_SPRINT : CAMERA_POS_STEP), cameraInc.y * CAMERA_POS_STEP, cameraInc.z * (isSprinting ? CAMERA_POS_SPRINT : CAMERA_POS_STEP));
        // Check if there has been a collision. If true, set the y position to
        // the maximum height
        float height = terrain.getHeight(camera.getPosition());
        if(camera.getPosition().y - 0.2f <= height) {
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }

        SceneLight sceneLight = scene.getSceneLight();

        // Update directional light direction, intensity, and color
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 0.1f;
        if(lightAngle > 90) {
            directionalLight.setIntensity(0);
            if(lightAngle >= 270) {
                lightAngle = -90;
            }
            sceneLight.getSkyBoxLight().set(0.3f, 0.3f, 0.3f);
        } else if(lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            sceneLight.getSkyBoxLight().set(Math.max(factor, 0.3f), Math.max(factor, 0.3f), Math.max(factor, 0.3f));
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            sceneLight.getSkyBoxLight().set(1, 1, 1);
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render(Window window) throws Exception {
        if(hud != null) {
            hud.updateSize(window);
        }
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanUp() {
        renderer.cleanUp();
        scene.cleanUp();
        if(hud != null) {
            hud.cleanUp();
        }
    }
}