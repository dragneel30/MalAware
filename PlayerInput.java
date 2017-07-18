package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Lorence on 7/10/2017.
 */

public class PlayerInput implements InputProcessor
{
    PlayerInput(Camera iWorldCam)
    {
        unprojected = new Vector3(-1, -1 ,0);
        projected = new Vector3(-1, -1, 0);
        worldCam = iWorldCam;
    }

    private Camera worldCam;
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        unprojected = worldCam.unproject(new Vector3(screenX, screenY, 0));
        projected = worldCam.unproject(new Vector3(screenX, screenY, 0));
        return false;
    }

    Vector3 getUnprojectedCoord() { return unprojected; }
    Vector3 getProjectedCoord() { return projected; }
    Vector3 unprojected;
    Vector3 projected;
    int x = -1, y = -1;
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
