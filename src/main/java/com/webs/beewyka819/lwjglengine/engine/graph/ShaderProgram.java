package com.webs.beewyka819.lwjglengine.engine.graph;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import com.webs.beewyka819.lwjglengine.engine.graph.lights.DirectionalLight;
import com.webs.beewyka819.lwjglengine.engine.graph.lights.PointLight;
import com.webs.beewyka819.lwjglengine.engine.graph.lights.SpotLight;
import com.webs.beewyka819.lwjglengine.engine.graph.weather.Fog;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {
    private final int programID;

    private int vertexShaderID;

    private int fragmentShaderID;

    private final Map<String, Integer> uniforms;

    public ShaderProgram() throws Exception {
        programID = glCreateProgram();
        if(programID == 0) {
            throw new Exception("Could not create Shader");
        }
        uniforms = new HashMap<String, Integer>();
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programID, uniformName);
        if(uniformLocation < 0) {
            throw new Exception("Could not find uniform: " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void createPointLightListUniform(String uniformName, int size) throws Exception {
        for(int i = 0; i < size; i++) {
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    public void createSpotLightListUniform(String uniformName, int size) throws Exception {
        for(int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createSpotLightUniform(String uniformName) throws Exception {
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    public void createDirectionalLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    public void createFogUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".activeFog");
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".density");
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformName, PointLight[] pointLights) {
        int numLights = pointLights != null ? pointLights.length : 0;
        for(int i = 0; i < numLights; i++) {
            setUniform(uniformName, pointLights[i], i);
        }
    }

    public void setUniform(String uniformName, PointLight pointLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".color", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(uniformName + ".att.constant", att.getConstant());
        setUniform(uniformName + ".att.linear", att.getLinear());
        setUniform(uniformName + ".att.exponent", att.getExponent());
    }

    public void setUniform(String uniformName, SpotLight[] spotLights) {
        int numLights = spotLights != null ? spotLights.length : 0;
        for(int i = 0; i < numLights; i++) {
            setUniform(uniformName, spotLights[i], i);
        }
    }

    public void setUniform(String uniformName, SpotLight spotLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    public void setUniform(String uniformName, SpotLight spotLight) {
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutOff());
    }

    public void setUniform(String uniformName, DirectionalLight dirLight) {
        setUniform(uniformName + ".color", dirLight.getColor());
        setUniform(uniformName + ".direction", dirLight.getDirection());
        setUniform(uniformName + ".intensity", dirLight.getIntensity());
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbientColor());
        setUniform(uniformName + ".diffuse", material.getDiffuseColor());
        setUniform(uniformName + ".specular", material.getSpecularColor());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    public void setUniform(String uniformName, Fog fog) {
        setUniform(uniformName + ".activeFog", fog.isActive() ? 1 : 0);
        setUniform(uniformName + ".color", fog.getColor());
        setUniform(uniformName + ".density", fog.getDensity());
    }

    public void createVertexShader(String shaderSource) throws Exception {
        vertexShaderID = createShader(shaderSource, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderSource) throws Exception {
        fragmentShaderID = createShader(shaderSource, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderSource, int shaderType) throws Exception {
        int shaderID = glCreateShader(shaderType);
        if(shaderID == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderID));
        }

        glAttachShader(programID, shaderID);

        return shaderID;
    }

    public void link() throws Exception {
        glLinkProgram(programID);
        if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programID));
        }

        if(vertexShaderID != 0) {
            glDetachShader(programID, vertexShaderID);
            glDeleteShader(vertexShaderID);
        }
        if(fragmentShaderID != 0) {
            glDetachShader(programID, fragmentShaderID);
            glDeleteShader(fragmentShaderID);
        }

        glValidateProgram(programID);   //TODO Remove before release
        if(glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID));
        }
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanUp() {
        unbind();
        if(programID != 0) {
            glDeleteProgram(programID);
        }
    }
}