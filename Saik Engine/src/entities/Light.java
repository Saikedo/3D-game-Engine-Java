package entities;

import billboards.BillboardEntity;
import billboards.BillboardRenderer;
import billboards.BillboardTexture;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.ModelTexture;

public class Light {

    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation = new Vector3f(1, 0, 0);
    private BillboardEntity lightEntity;


    public Light(Vector3f position, Vector3f color, Camera camera) {
        this.position = position;
        this.color = color;

        lightEntity = new BillboardEntity("lightBulb", new Vector3f(position.x, position.y + 5f, position.z), true, new Vector2f(0.02f, 0.04f));
    }

    public Light(Vector3f position, Vector3f color, Vector3f attenuation, Camera camera) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;

        lightEntity = new BillboardEntity("lightBulb", new Vector3f(position.x, position.y, position.z), true, new Vector2f(0.02f, 0.04f));
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public BillboardEntity getLightEntity() {
        return lightEntity;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
