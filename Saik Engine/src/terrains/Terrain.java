package terrains;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Terrain {
    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 40;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256; // for all 3 color channels

    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack terrainTexturePack;
    private TerrainTexture blendMap;
    private float [][] heightMapArray;

    public Terrain(int gridX, int gridZ, TerrainTexturePack terrainTexturePack, TerrainTexture blendMap, String heightMap) {
        this.terrainTexturePack = terrainTexturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(heightMap);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / (float) (heightMapArray.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if(gridX >= heightMapArray.length -1 || gridZ >= heightMapArray.length -1 || gridX < 0 || gridZ < 0) {
            return 0;
        }

        float xCoordinate = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoordinate = (terrainZ % gridSquareSize) / gridSquareSize;

        float answer;
        if(xCoordinate <= (1 - zCoordinate)) {
            answer = Maths.barryCentric(new Vector3f(0, heightMapArray[gridX][gridZ], 0), new Vector3f(1, heightMapArray[gridX + 1][gridZ], 0),
                    new Vector3f(0, heightMapArray[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));
        } else {
            answer = Maths.barryCentric(new Vector3f(1, heightMapArray[gridX + 1][gridZ], 0),
                    new Vector3f(1, heightMapArray[gridX + 1][gridZ + 1], 1),
                    new Vector3f(0, heightMapArray[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));
        }

        return answer;
    }

    public TerrainTexturePack getTerrainTexturePack() {
        return terrainTexturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    private RawModel generateTerrain(String heightMap){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/textures/" + heightMap + ".png"));
        } catch (IOException e) {
            System.out.println("Terrain: GenerateTerrain: Exception - " + e);
            e.printStackTrace();
        }

        int vertexCount = image.getHeight();
        heightMapArray = processHeightMap(vertexCount, image);


        int count = vertexCount * vertexCount;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoordinates = new float[count*2];
        int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];
        int vertexPointer = 0;
        for(int i = 0; i < vertexCount; i++){
            for(int j = 0; j < vertexCount; j++){
                vertices[vertexPointer*3] = (float)j/((float)vertexCount - 1) * SIZE;
                vertices[vertexPointer*3+1] = getHeight(j, i, image);
                vertices[vertexPointer*3+2] = (float)i/((float)vertexCount - 1) * SIZE;

                Vector3f normal = calculateNormal(j, i, image);

                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoordinates[vertexPointer*2] = (float)j/((float)vertexCount - 1);
                textureCoordinates[vertexPointer*2+1] = (float)i/((float)vertexCount - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;
        for(int gz=0;gz<vertexCount-1;gz++){
            for(int gx=0;gx<vertexCount-1;gx++){
                int topLeft = (gz*vertexCount)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*vertexCount)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return Loader.getInstance().loadToVAO(vertices, textureCoordinates, normals, indices);
    }

    private Vector3f calculateNormal(int x, int y, BufferedImage image) {
        float heightL = getHeight(x - 1, y, image);
        float heightR = getHeight(x + 1, y, image);
        float heightD = getHeight(x, y - 1, image);
        float heightU = getHeight(x, y + 1, image);

        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    private float getHeight(int x, int y, BufferedImage image) {
        if(x < 0 || x  >= image.getHeight() || y < 0 || y >= image.getHeight()) {
            return 0;
        }

        return heightMapArray[x][y];
    }



    private float [][] processHeightMap(int vertexCount, BufferedImage image) {
        float [][] heightMap = new float[vertexCount][vertexCount];

        for(int x = 0; x < vertexCount; x++) {
            for(int y = 0; y < vertexCount; y++) {
                float height = image.getRGB(x, y);
                height += MAX_PIXEL_COLOR / 2f;
                height /= MAX_PIXEL_COLOR / 2f;
                height *= MAX_HEIGHT;

                heightMap[x][y] = height;
            }
        }

        return heightMap;
    }
}
