package toolbox;

import entities.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MousePicker {

    private Vector3f currentRay;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;


    public MousePicker(Camera camera, Matrix4f projectionMatrix) {
        this.camera = camera;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = Maths.createViewMatrix(camera);
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public void update() {
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();
    }

    private  Vector3f calculateMouseRay() {
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();
        Vector2f normalizedCoordinates = getNormalizedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoordinates = new Vector4f(normalizedCoordinates.x, normalizedCoordinates.y, -1f, 1f);
        Vector4f eyeCoordinates = toEyeCoordinates(clipCoordinates);
        Vector3f worldRay = toWorldCoordinates(eyeCoordinates);
        return worldRay;
    }

    private Vector3f toWorldCoordinates(Vector4f eyeCoordinates) {
        Matrix4f invertedViewMatrix = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedViewMatrix, eyeCoordinates, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

    private Vector4f toEyeCoordinates(Vector4f clipCoordinates) {
        Matrix4f invertedProjectionMatrix = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoordinates = Matrix4f.transform(invertedProjectionMatrix, clipCoordinates, null);
        return new Vector4f(eyeCoordinates.x, eyeCoordinates.y, -1f, 0f);
    }

    private Vector2f getNormalizedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2f * mouseX) / Display.getWidth() - 1f;
        float y = (2f * mouseY) / Display.getHeight() - 1f;

        return new Vector2f(x, y);
    }
}
