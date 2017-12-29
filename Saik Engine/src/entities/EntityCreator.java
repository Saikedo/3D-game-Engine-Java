package entities;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.ObjLoader;
import textures.ModelTexture;

public class EntityCreator {

    public Entity createEntity(String modelName, String textureName, float shineDamper, float reflectivity,
                               boolean hasTransparency, boolean useFakeLighting, Vector3f position, Vector3f rotation, float scale) {

        //RawModel model = ObjLoader.loadObjModel(modelName, mLoader);
        ModelData modelData = OBJFileLoader.loadOBJ(modelName);
       RawModel model = Loader.getInstance().loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(), modelData.getIndices());

        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(Loader.getInstance().loadTexture(textureName)));
        ModelTexture texture = staticModel.getTexture();
        texture.setHasTransparency(hasTransparency);
        texture.setUseFakeLighting(useFakeLighting);
        texture.setShineDamper(shineDamper);
        texture.setReflectivity(reflectivity);
        return new Entity(staticModel, position, rotation.x, rotation.y, rotation.z, scale);
    }



    public Entity createEntity(String modelName, String textureName, int numberOfRows, int textureIndex, float shineDamper, float reflectivity,
                               boolean hasTransparency, boolean useFakeLighting, Vector3f position, Vector3f rotation, float scale) {

        //RawModel model = ObjLoader.loadObjModel(modelName, mLoader);
        ModelData modelData = OBJFileLoader.loadOBJ(modelName);
        RawModel model = Loader.getInstance().loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(), modelData.getIndices());

        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(Loader.getInstance().loadTexture(textureName)));
        ModelTexture texture = staticModel.getTexture();
        texture.setNumberOfRows(numberOfRows);
        texture.setHasTransparency(hasTransparency);
        texture.setUseFakeLighting(useFakeLighting);
        texture.setShineDamper(shineDamper);
        texture.setReflectivity(reflectivity);
        return new Entity(staticModel, textureIndex, position, rotation.x, rotation.y, rotation.z, scale);
    }
}
