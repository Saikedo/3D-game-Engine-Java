package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;

import java.security.Key;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    private Player player;
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    public Camera(Player player) {
        this.player = player;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleArroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);

        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void changePitch(float dPitch) {
        pitch += dPitch;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }

    private float calculateHorizontalDistance() {
        float horizontalDistance = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
        if(horizontalDistance < 0) {
            horizontalDistance = 0;
        }
        return horizontalDistance;
    }

    private float calculateVerticalDistance() {
        float verticalDistance = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
        if(verticalDistance < 0) {
            verticalDistance = 0;
        }
        return verticalDistance;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
        if(distanceFromPlayer < 20) {
            distanceFromPlayer = 10;
        }
    }

    private void calculatePitch() {
        if(Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;

            if(pitch < 5) {
                pitch = 5;
            } else if(pitch > 90) {
                pitch = 90;
            }
        }
    }

    private   void calculateAngleArroundPlayer() {
        if(Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }
}
