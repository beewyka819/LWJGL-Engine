package com.webs.beewyka819.gameengine.game;

import static org.lwjgl.glfw.GLFW.*;

import com.webs.beewyka819.gameengine.engine.IGameLogic;
import com.webs.beewyka819.gameengine.engine.InputHandler;
import com.webs.beewyka819.gameengine.engine.MouseInput;
import com.webs.beewyka819.gameengine.engine.Scene;
import com.webs.beewyka819.gameengine.engine.SceneLight;
import com.webs.beewyka819.gameengine.engine.Window;
import com.webs.beewyka819.gameengine.engine.graph.Camera;
import com.webs.beewyka819.gameengine.engine.graph.Mesh;
import com.webs.beewyka819.gameengine.engine.graph.ModelLoader;
import com.webs.beewyka819.gameengine.engine.graph.Renderer;
import com.webs.beewyka819.gameengine.engine.graph.lights.DirectionalLight;
//import com.webs.beewyka819.gameengine.engine.graph.lights.PointLight;
import com.webs.beewyka819.gameengine.engine.items.GameItem;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ProjectParadis implements IGameLogic {
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.1f;
    private static final float CAMERA_POS_SPRINT = 0.2f;

    private final Renderer renderer;

    private final Camera camera;

    private final Vector3f cameraInc;

    private boolean isMouseLocked;

    private boolean isSprinting;

    private boolean isFullscreen;

    private Scene scene;

    private float lightAngle;

    public ProjectParadis() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init();
        
        window.showCounter(true);

        scene = new Scene();

        Mesh grassBlock = ModelLoader.loadModel("models/cube.obj", "textures/grassblock.png", ModelLoader.CCW);
        grassBlock.getMaterial().setReflectance(2.0f);

        GameItem grass = new GameItem(grassBlock);
        grass.setScale(0.5f);
        grass.setPosition(0, 0, -5);

        Mesh dragon = ModelLoader.loadModel("models/dragon.obj", new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), ModelLoader.CCW);
        dragon.getMaterial().setReflectance(2.0f);

        GameItem dragonItem = new GameItem(dragon);
        dragonItem.setScale(0.5f);
        dragonItem.setPosition(-10, 0, -10);
        dragonItem.setRotation(0, -90, 0);

        Mesh bunny = ModelLoader.loadModel("models/bunny.obj", new Vector4f(0.5f, 0.0f, 0.5f, 1.0f), ModelLoader.CCW);
        bunny.getMaterial().setReflectance(2.0f);

        GameItem bunnyItem = new GameItem(bunny);
        bunnyItem.setScale(1.5f);
        bunnyItem.setPosition(0, 0, -20);

        scene.addGameItem(grass);
        scene.addGameItem(dragonItem);
        scene.addGameItem(bunnyItem);

        setupLights();

        isMouseLocked = true;
        isSprinting = false;
        isFullscreen = false;
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));

        // Point Light
        /*PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), new Vector3f(0, 0, -3), 1.0f);
        PointLight.Attenuation att = new PointLight.Attenuation(0, 0, 1);
        pointLight.setAttenuation(att);
        PointLight[] pointLightList = { pointLight };
        sceneLight.setPointLightList(pointLightList);*/

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightDirection = new Vector3f(-1, 0, 0);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
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

        if(inputHandler.keyPressed(GLFW_KEY_F11)) {
            isFullscreen = !isFullscreen;
        }

        cameraInc.set(0, 0, 0);
        if(inputHandler.keyDown(GLFW_KEY_W)) {
            cameraInc.z += -1;
            if(inputHandler.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
                isSprinting = true;
            }
        } else if(isSprinting) {
            isSprinting = false;
        }
        if(inputHandler.keyDown(GLFW_KEY_S)) {
            cameraInc.z += 1;
        }
        if(inputHandler.keyDown(GLFW_KEY_A)) {
            cameraInc.x += -1;
        }
        if(inputHandler.keyDown(GLFW_KEY_D)) {
            cameraInc.x += 1;
        }
        if(inputHandler.keyDown(GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y += -1;
        }
        if(inputHandler.keyDown(GLFW_KEY_SPACE)) {
            cameraInc.y += 1;
        }

        if(inputHandler.keyPressed(GLFW_KEY_E)) {
            isMouseLocked = !isMouseLocked;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput, Window window) throws Exception {
        window.setFullscreen(isFullscreen);
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

        camera.movePosition(cameraInc.x * (isSprinting ? CAMERA_POS_SPRINT : CAMERA_POS_STEP), cameraInc.y * CAMERA_POS_STEP, cameraInc.z * (isSprinting ? CAMERA_POS_SPRINT : CAMERA_POS_STEP));
    
        SceneLight sceneLight = scene.getSceneLight();

        // Update directional light direction, intensity, and color
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 0.1f;
        if(lightAngle > 90) {
            directionalLight.setIntensity(0);
            if(lightAngle >= 270) {
                lightAngle = -90;
            }
        } else if(lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
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
        renderer.render(window, camera, scene);
    }

    @Override
    public void cleanUp() {
        if(scene != null) {
            scene.cleanUp();
        }
        renderer.cleanUp();
    }
}