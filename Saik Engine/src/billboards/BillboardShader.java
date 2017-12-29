package billboards;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import shaders.ShaderProgram;
import toolbox.Maths;

public class BillboardShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/billboards/billboardVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/billboards/billboardFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_constantSize;
    private int location_scale;
    private int location_constantSizeDimensions;

    public BillboardShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }


    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
       location_transformationMatrix = super.getUniformLocation("transformationMatrix");
       location_projectionMatrix = super.getUniformLocation("projectionMatrix");
       location_viewMatrix = super.getUniformLocation("viewMatrix");
       location_constantSize = super.getUniformLocation("constantSize");
       location_scale = super.getUniformLocation("scale");
       location_constantSizeDimensions = super.getUniformLocation("constantSizeDimensions");
    }

    public void loadConstantSizeDimensions(Vector2f constantSizeDimensions) {
        super.load2dVector(location_constantSizeDimensions, constantSizeDimensions);
    }

    public void loadScale(float scale) {
        super.loadFloat(location_scale, scale);
    }

    public void loadConstantSize(Boolean constantSize) {
        super.loadBoolean(location_constantSize, constantSize);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
}
