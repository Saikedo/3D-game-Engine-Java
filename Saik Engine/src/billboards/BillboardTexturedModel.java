package billboards;


import models.RawModel;
import textures.ModelTexture;

public class BillboardTexturedModel {

    private ModelTexture modelTexture;
    private RawModel rawModel;

    public BillboardTexturedModel(ModelTexture modelTexture, RawModel rawModel) {
        this.modelTexture = modelTexture;
        this.rawModel = rawModel;
    }


    public ModelTexture getModelTexture() {
        return modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }
}

