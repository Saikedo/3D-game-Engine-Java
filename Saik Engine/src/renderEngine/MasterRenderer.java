package renderEngine;

import billboards.BillboardEntity;
import billboards.BillboardRenderer;
import billboards.BillboardShader;
import billboards.BillboardTexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.EntityShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private EntityShader shader = new EntityShader();


    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private Map<BillboardTexturedModel, List<BillboardEntity>> billboardEntities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private Matrix4f projectionMatrix;
    private EntityRenderer entityRenderer;
    private BillboardRenderer billboardRenderer;
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    private BillboardShader billboardShader = new BillboardShader();
    private Vector3f mSkyColorRGB;

    private SkyboxRenderer skyboxRenderer;

    public MasterRenderer(Vector3f ambientLight, Vector3f skyColorRGB) {
        mSkyColorRGB = skyColorRGB;


        createProjectionMatrix();
        entityRenderer = new EntityRenderer(shader, projectionMatrix);

        billboardRenderer = new BillboardRenderer(billboardShader, projectionMatrix);

        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        // Start the ambient light once
         shader.start();
         shader.loadAmbientLight(ambientLight);
         shader.stop();

         terrainShader.start();
         terrainShader.loadAmbientLight(ambientLight);
         terrainShader.stop();

         skyboxRenderer = new SkyboxRenderer(projectionMatrix);
    }

    public static void enableCulling() {
        // Do not renter triangles have normals pointing away from camera
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }


    public void render(List<Light> lights, Camera camera) {
        prepare();
        shader.start();
        shader.loadSkyColor(mSkyColorRGB);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        shader.stop();

        billboardShader.start();
        billboardShader.loadViewMatrix(camera);
        billboardRenderer.render(billboardEntities);
        billboardShader.stop();

        terrainShader.start();
        terrainShader.loadSkyColor(mSkyColorRGB);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        skyboxRenderer.render(camera);
        terrains.clear();
        billboardEntities.clear();
        entities.clear();
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();

        List<Entity> batch = entities.get(entityModel);

        if(batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void processBillboardEntity(BillboardEntity entity) {
        BillboardTexturedModel entityModel = entity.getBillboardTexturedModel();

        List<BillboardEntity> batch = billboardEntities.get(entityModel);

        if(batch != null) {
            batch.add(entity);
        } else {
            List<BillboardEntity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            billboardEntities.put(entityModel, newBatch);
        }
    }


    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) (1f /  Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;
    }

    private void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(mSkyColorRGB.x, mSkyColorRGB.y, mSkyColorRGB.z, 1);
    }

    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
    }

}
