package com.webs.beewyka819.lwjglengine.engine.graph;

import static org.lwjgl.opengl.GL46.*;

public class ShadowMap {
    public static final int SHADOW_MAP_WIDTH = 1024;
    public static final int SHADOW_MAP_HEIGHT = 1024;

    private final int depthMapFBO;

    private final Texture depthMap;

    public ShadowMap() throws Exception {
        // Create an FBO to render the depth map
        depthMapFBO = glGenFramebuffers();

        // Create the depth map texture
        depthMap = new Texture(SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT, GL_DEPTH_COMPONENT);

        // Attach the depth map texture to the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap.getId(), 0);
        // Set only depth
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new Exception("Could not create Framebuffer");
        }

        // Unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public Texture getDepthMapTexture() {
        return depthMap;
    }

    public int getDepthMapFBO() {
        return depthMapFBO;
    }

    public void cleanUp() {
        glDeleteFramebuffers(depthMapFBO);
        depthMap.cleanUp();
    }
}