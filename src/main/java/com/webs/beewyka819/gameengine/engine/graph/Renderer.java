package com.webs.beewyka819.gameengine.engine.graph;

import static org.lwjgl.opengl.GL46.*;

import com.webs.beewyka819.gameengine.engine.ConsoleOutput;
import com.webs.beewyka819.gameengine.engine.Scene;
import com.webs.beewyka819.gameengine.engine.SceneLight;
import com.webs.beewyka819.gameengine.engine.Utils;
import com.webs.beewyka819.gameengine.engine.Window;
import com.webs.beewyka819.gameengine.engine.graph.lights.DirectionalLight;
import com.webs.beewyka819.gameengine.engine.graph.lights.PointLight;
import com.webs.beewyka819.gameengine.engine.graph.lights.SpotLight;
import com.webs.beewyka819.gameengine.engine.items.GameItem;

import org.fusesource.jansi.Ansi.Color;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Renderer {
    private static final float FOV = (float) Math.toRadians(70.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;

    private final Transformation transformation;

    private ShaderProgram sceneShaderProgram;

    private final float specularPower;

    public Renderer() {
        transformation = new Transformation();
        specularPower = 10.0f;
    }

    public void init() throws Exception {
        ConsoleOutput.printMessage("Initializing Renderer...", Color.GREEN);
        ConsoleOutput.printMessage("Starting Shader Construction Stage", Color.GREEN);

        String openglVer = glGetString(GL_VERSION).split(" ")[0].replace(".", "");

        setupSceneShader(openglVer);

        ConsoleOutput.printMessage("Shader Construction Complete!", Color.GREEN);

        glEnable(GL_DEPTH_TEST);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        
        ConsoleOutput.printMessage("Renderer Initialization Complete!", Color.GREEN);
    }

    private void setupSceneShader(String openglVer) throws Exception {
        String vertexSource = Utils.readJarResourceToString("/res/shaders/scene_vertex.vs");
        String fragmentSource = Utils.readJarResourceToString("/res/shaders/scene_fragment.fs");
        vertexSource = vertexSource.replaceFirst("VER", openglVer);
        fragmentSource = fragmentSource.replaceFirst("VER", openglVer);

        sceneShaderProgram = new ShaderProgram();
        sceneShaderProgram.createVertexShader(vertexSource);
        sceneShaderProgram.createFragmentShader(fragmentSource);
        sceneShaderProgram.link();
        sceneShaderProgram.validate();

        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");
        sceneShaderProgram.createUniform("textureSampler");

        sceneShaderProgram.createMaterialUniform("material");

        sceneShaderProgram.createUniform("specularPower");
        sceneShaderProgram.createUniform("ambientLight");
        sceneShaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
        sceneShaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
        sceneShaderProgram.createDirectionalLightUniform("directionalLight");
    }

    private void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Scene scene) throws Exception {
        clear();

        if(window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        sceneShaderProgram.bind();

        Matrix4f projectionMatrix = transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = transformation.updateViewMatrix(camera);

        SceneLight sceneLight = scene.getSceneLight();
        renderLights(viewMatrix, sceneLight);

        sceneShaderProgram.setUniform("textureSampler", 0);
        for(GameItem gameItem : scene.getGameItems()) {
            Mesh mesh = gameItem.getMesh();
            sceneShaderProgram.setUniform("material", mesh.getMaterial());
            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameItem, viewMatrix);
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            mesh.render();
        }

        sceneShaderProgram.unbind();
    }

    private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight) throws Exception {
        sceneShaderProgram.setUniform("ambientLight", sceneLight.getAmbientLight());
        sceneShaderProgram.setUniform("specularPower", specularPower);

        // Process Point Lights
        PointLight[] pointLightList = sceneLight.getPointLightList();
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for(int i = 0; i < numLights; i++) {
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            sceneShaderProgram.setUniform("pointLights", currPointLight, i);
        }

        // Process Spot Lights
        SpotLight[] spotLightList = sceneLight.getSpotLightList();
        numLights = spotLightList != null ? spotLightList.length : 0;
        for(int i = 0; i < numLights; i++) {
            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
            Vector3f lightPos = currSpotLight.getPointLight().getPosition();

            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            sceneShaderProgram.setUniform("spotLights", currSpotLight, i);
        }

        // Process Directional Light
        DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        sceneShaderProgram.setUniform("directionalLight", currDirLight);
    }

    public void cleanUp() {
        if(sceneShaderProgram != null) {
            sceneShaderProgram.cleanUp();
        }
    }
}