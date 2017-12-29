package billboards;

import models.RawModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.MasterRenderer;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

public class BillboardRenderer {



    private BillboardShader shader;

    public BillboardRenderer(BillboardShader shader, Matrix4f projectionMatrix) {

        this.shader = shader;



        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }




    public void render(Map<BillboardTexturedModel, List<BillboardEntity>> entities) {
        for(BillboardTexturedModel model: entities.keySet()) {
            prepareTexturedModel(model);
            List<BillboardEntity> batch = entities.get(model);
            for(BillboardEntity entity: batch) {
                if(entity.isConstantSize()) {
                    shader.loadConstantSize(true);
                    shader.loadConstantSizeDimensions(entity.getConstantSizeDimensions());
                } else {
                    shader.loadScale(entity.getScale());
                }

                prepareInstances(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(BillboardTexturedModel model) {
        RawModel rawModel = model.getRawModel();

        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = model.getModelTexture();


        if(texture.hasTransparency()) {
            MasterRenderer.disableCulling();
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getTextureID());
    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstances(BillboardEntity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 0, 0, 0, entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }


}
