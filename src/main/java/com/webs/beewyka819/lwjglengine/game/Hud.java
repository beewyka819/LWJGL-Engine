package com.webs.beewyka819.lwjglengine.game;

import java.awt.Font;

import com.webs.beewyka819.lwjglengine.engine.items.GameItem;
import com.webs.beewyka819.lwjglengine.engine.IHud;
import com.webs.beewyka819.lwjglengine.engine.items.TextItem;
import com.webs.beewyka819.lwjglengine.engine.graph.FontTexture;
import com.webs.beewyka819.lwjglengine.engine.graph.Mesh;
import com.webs.beewyka819.lwjglengine.engine.graph.ModelLoader;
import com.webs.beewyka819.lwjglengine.engine.io.Window;

import org.joml.Vector4f;

public class Hud implements IHud {
    private static final Font FONT = new Font("Arial", Font.PLAIN, 40);
    private static final String CHARSET = "ISO-8859-1";

    private final GameItem[] gameItems;

    private final TextItem statusTextItem;

    private final GameItem compassItem;

    public Hud(String statusText) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        this.statusTextItem = new TextItem(statusText, fontTexture);
        this.statusTextItem.getMesh().getMaterial().setAmbientColor(new Vector4f(1, 1, 1, 1));
        
        Mesh mesh = ModelLoader.loadModel("/res/models/compass.obj", new Vector4f(1, 0, 0, 1), ModelLoader.COUNTER_CLOCKWISE);
        compassItem = new GameItem(mesh);
        compassItem.setScale(40.0f);
        // Rotate to transform it to screen coordinates
        compassItem.setRotation(0, 0, 180);

        gameItems = new GameItem[] { statusTextItem, compassItem };
    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }

    public void rotateCompass(float angle) {
        this.compassItem.setRotation(0, 0, 180 - angle);
    }

    @Override
    public GameItem[] getGameItems() {
        return gameItems;
    }

    public void updateSize(Window window) {
        this.statusTextItem.setPosition(10.0f, window.getHeight() - statusTextItem.getScale() * 50.0f, 0);
        this.compassItem.setPosition(window.getWidth() - 40.0f, 50, 0);
    }
}