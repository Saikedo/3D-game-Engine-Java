package billboards;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.ModelTexture;

public class BillboardEntity {

    private RawModel rawModel;
    private ModelTexture modelTexture;
    private BillboardTexturedModel billboardTexturedModel;
    private Vector3f position;
    private float scale;
    private boolean constantSize;
    private Vector2f constantSizeDimensions = new Vector2f(0.01f, 0.01f);


    public BillboardEntity( String textureName, Vector3f position, float scale, Boolean constantSize) {
        billboardTexturedModel = new BillboardTexturedModel(new ModelTexture(Loader.getInstance().loadTexture(textureName)), createRawModel());
        this.position = position;
        this.scale = scale;
        this.constantSize = constantSize;
    }

    public BillboardEntity(String textureName, Vector3f position, Boolean constantSize, Vector2f constantSizeDimensions) {
        billboardTexturedModel = new BillboardTexturedModel(new ModelTexture(Loader.getInstance().loadTexture(textureName)), createRawModel());
        this.position = position;
        this.constantSize = constantSize;
        this.constantSizeDimensions = constantSizeDimensions;
    }

    private RawModel createRawModel() {
        float[] vertices = {
                -0.5f, 0.5f, 0,
                -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, 0.5f, 0
        };

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0,
        };

        int[] indices = {
                0,1,3,
                3,1,2
        };

        return Loader.getInstance().loadToVAO(vertices,textureCoords,indices);
    }


    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public BillboardTexturedModel getBillboardTexturedModel() {
        return billboardTexturedModel;
    }

    public boolean isConstantSize() {
        return constantSize;
    }


    public Vector2f getConstantSizeDimensions() {
        return constantSizeDimensions;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
