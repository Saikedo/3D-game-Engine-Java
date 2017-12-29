package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends  Entity {

    private static final float RUN_SPEED = 0.2f;
    private static final float TURN_SPEED = 1;
    private static final float GRAVITY = -0.02f;
    private static final float JUMP_POWER = 0.5f;


    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;
    private boolean isInAir = false;

    public Player(Entity entity) {
        super(entity.getModel(), entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
    }

    public void move(Terrain terrain) {
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);

        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);

        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);

        if(super.getPosition().y < terrainHeight) {
            upwardSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
    }

    private void jump() {
        if(!isInAir) {
            isInAir = true;
            this.upwardSpeed = JUMP_POWER;
        }
    }

    private void checkInputs() {
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            currentSpeed = RUN_SPEED;
        } else  if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            currentSpeed = -RUN_SPEED;
        } else {
            currentSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            currentTurnSpeed = TURN_SPEED;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            currentTurnSpeed = -TURN_SPEED;
        } else {
            currentTurnSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
           jump();
        }
    }
}

