package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import java.util.List;

public class EntityShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

    private static final int MAX_LIGHTS = 4;

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int [] location_lightPosition;
    private int [] location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_ambientLight;
    private int location_useFakeLighting;
    private int location_skyColor;
    private int location_numberOfRows;
    private int location_offset;
    private int location_attenuation[];

    public EntityShader() {
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
       location_shineDamper = super.getUniformLocation("shineDamper");
       location_reflectivity = super.getUniformLocation("reflectivity");
       location_ambientLight = super.getUniformLocation("ambientLight");
       location_useFakeLighting = super.getUniformLocation("useFakeLighting");
       location_skyColor = super.getUniformLocation("skyColor");
       location_numberOfRows = super.getUniformLocation("numberOfRows");
       location_offset = super.getUniformLocation("offset");


       location_lightPosition  = new int[MAX_LIGHTS];
       location_lightColor = new int[MAX_LIGHTS];
       location_attenuation = new int[MAX_LIGHTS];

       for(int i=0; i < MAX_LIGHTS; i++) {
           location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
           location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
           location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
       }
    }

    public void loadSkyColor(Vector3f rgb) {
        super.load3dVector(location_skyColor, rgb);
    }

    public void loadNumberOfRows(int numberOfRows) {
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    public void loadOffset(Vector2f offset) {
        super.load2dVector(location_offset, offset);
    }

    public void loadFakeLighting(boolean useFakeLighting) {
        super.loadBoolean(location_useFakeLighting, useFakeLighting);
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

    public void loadLights(List<Light> lights) {
        for(int lightIndex = 0; lightIndex < MAX_LIGHTS; lightIndex++) {
            if(lightIndex < lights.size()) {
                super.load3dVector(location_lightPosition[lightIndex], lights.get(lightIndex).getPosition());
                super.load3dVector(location_lightColor[lightIndex], lights.get(lightIndex).getColor());
                super.load3dVector(location_attenuation[lightIndex], lights.get(lightIndex).getAttenuation());
            } else {
                super.load3dVector(location_lightPosition[lightIndex], new Vector3f(0, 0, 0));
                super.load3dVector(location_lightColor[lightIndex], new Vector3f(0, 0, 0));
                super.load3dVector(location_attenuation[lightIndex], new Vector3f(1, 0, 0));
            }
        }
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadAmbientLight(Vector3f ambientLight) {
        super.load3dVector(location_ambientLight, ambientLight);
    }
}
