package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import java.util.List;

public class TerrainShader extends  ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";
    private static final int MAX_LIGHTS = 4;

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_ambientLight;
    private int location_skyColor;
    private int location_backgroundTexture;
    private int location_rTexture;
    private int location_gTexture;
    private int location_bTexture;
    private int location_blendMap;
    private int [] location_lightPosition;
    private int [] location_lightColor;
    private int location_attenuation[];

    public TerrainShader() {
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
        location_skyColor = super.getUniformLocation("skyColor");
        location_backgroundTexture = super.getUniformLocation("backgroundTexture");
        location_rTexture = super.getUniformLocation("rTexture");
        location_gTexture = super.getUniformLocation("gTexture");
        location_bTexture = super.getUniformLocation("bTexture");
        location_blendMap = super.getUniformLocation("blendMap");

        location_lightPosition  = new int[MAX_LIGHTS];
        location_lightColor = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];

        for(int i=0; i < MAX_LIGHTS; i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + Integer.toString(i) + "]");
            location_lightColor[i] = super.getUniformLocation("lightColor[" + Integer.toString(i) + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }


    public void connectTextureUnits() {
        super.loadInt(location_backgroundTexture, 0);
        super.loadInt(location_rTexture, 1);
        super.loadInt(location_gTexture, 2);
        super.loadInt(location_bTexture, 3);
        super.loadInt(location_blendMap, 4);
    }

    public void loadSkyColor(Vector3f skyColorRGB) {
        super.load3dVector(location_skyColor, skyColorRGB);
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
