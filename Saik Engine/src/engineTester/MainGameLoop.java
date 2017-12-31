package engineTester;

import entities.*;
import guis.GuiRenderer;
import guis.GuiTexture;
import org.lwjgl.opengl.Display;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = Loader.getInstance();

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("groundTexture_grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("groundTexture_mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("groundTexture_grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("groundTexture_tiles"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
        Terrain terrain = new Terrain(0, -1, texturePack, blendMap, "heightMap");

        System.out.println(terrain.getX() + " " + terrain.getZ());

        EntityCreator entityCreator = new EntityCreator();

        List<Entity> trees = new ArrayList<>();
        for(int i = 0; i < 2; i++ ) {
            Random random = new Random();
            int locationX = random.nextInt(800);
            int locationZ = -random.nextInt(800);
            int scale = random.nextInt(6) + 3;

            Entity tree = entityCreator.createEntity("tree", "tree", 10, 0.5f,
                    false, false, new Vector3f(locationX, terrain.getHeightOfTerrain(locationX, locationZ), locationZ), new Vector3f(0,0,0), scale);
            trees.add(tree);
        }

        List<Entity> lowPolyTree = new ArrayList<>();
        for(int i = 0; i < 1; i++ ) {
            Random random = new Random();
            int locationX = random.nextInt(800);
            int locationZ = -random.nextInt(800);
            int scale = random.nextInt(4) + 1;

            Entity tree = entityCreator.createEntity("lowPolyTree", "lowPolyTree", 100, 0.2f,
                    false, false, new Vector3f(locationX, terrain.getHeightOfTerrain(locationX, locationZ), locationZ), new Vector3f(0,0,0), scale);
            lowPolyTree.add(tree);
        }


        List<Entity> ferns = new ArrayList<>();
        for(int i = 0; i < 4; i ++) {
            Random random = new Random();
            int locationX = random.nextInt(800);
            int locationZ = -random.nextInt(800);
            float scale = 0.5f + random.nextFloat() * (2.0f - 0.5f);

            Entity fern = entityCreator.createEntity("fern", "fern", 2, random.nextInt(4), 10, 1,
                    true, true, new Vector3f(locationX, terrain.getHeightOfTerrain(locationX, locationZ), locationZ), new Vector3f(0,0,0), scale);

            ferns.add(fern);
        }

        List<Entity> grasses = new ArrayList<>();
        for(int i = 0; i < 8; i ++) {
            Random random = new Random();
            int locationX = random.nextInt(800);
            int locationZ = -random.nextInt(800);
            float scale = 0.5f + random.nextFloat() * (2.0f - 0.5f);

            Entity grass = entityCreator.createEntity("grassModel", "grassTexture",10, 1,
                    true, true, new Vector3f(locationX, terrain.getHeightOfTerrain(locationX, locationZ), locationZ), new Vector3f(0,0,0), scale);

            grasses.add(grass);
        }


        Entity stall = entityCreator.createEntity("stall", "stallTexture", 10, 1,
                false, false, new Vector3f(180, terrain.getHeightOfTerrain(180, -220), -220), new Vector3f(0,180,0), 1f);


        Entity dragon = entityCreator.createEntity("dragon", "SusanTexture", 10, 1,
                false, false, new Vector3f(200, terrain.getHeightOfTerrain(200, -220), -220), new Vector3f(0, 0, 0), 1f);

        Entity lamp = entityCreator.createEntity("lamp", "lamp", 10, 1, false, true,
                new Vector3f(200, terrain.getHeightOfTerrain(200, -230), -230), new Vector3f(0, 0, 0), 1f);


        Vector3f skyColorRGB = new Vector3f(0.5f,0.5f,0.5f);

        MasterRenderer renderer = new MasterRenderer(new Vector3f(0.1f, 0.1f, 0.1f), skyColorRGB);


        Entity playerEntity = entityCreator.createEntity("person", "playerTexture", 10, 1,
                false, false, new Vector3f(200,0, -160), new Vector3f(0,180,0), 0.5f);
        Player player = new Player(playerEntity);

        Camera camera = new Camera(player);
        camera.changePitch(10f);


        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(200,200, 100), new Vector3f(0.1f,0.1f,0.1f), camera));
        lights.add(new Light(new Vector3f(200, terrain.getHeightOfTerrain(200, -230) + 14, -230), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f), camera));


        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.8f, 0.8f), new Vector2f(0.2f, 0.2f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer();


        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());

        while(!Display.isCloseRequested()) {
            // Input handling
            // TODO first check on which terrain we are standing on if there are multiple terrains
            player.move(terrain);

            // TODO optimize lights so that lights are loaded only when something changes with the lights and not on each iteration.
            // TODO optimize lights so that only lights that are closer to the player are active and loaded

            // Game logic

            // render



            camera.move();

            picker.update();
            Vector3f position = picker.getCurrentRay();
            dragon.setPosition(position);
            renderer.processEntity(dragon);

            renderer.processEntity(player);




            //dragon.increaseRotation(0, 0.1f, 0);
            //renderer.processEntity(dragon);

            renderer.processEntity(stall);
            renderer.processTerrain(terrain);
            renderer.processEntity(lamp);


            grasses.forEach(renderer::processEntity);
            trees.forEach(renderer::processEntity);
            lowPolyTree.forEach(renderer::processEntity);
            ferns.forEach(renderer::processEntity);

            lights.forEach(light -> renderer.processBillboardEntity(light.getLightEntity()));

            renderer.render(lights, camera);


            //renderer.processBillboardEntity(billboardEntity);

            guiRenderer.render(guis);

            // display update
            DisplayManager.updateDisplay();
        }

        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
