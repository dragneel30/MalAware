package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorence on 7/3/2017.
 */

public class MoveableCreature extends Creature {

    MoveableCreature(EntityModel iModel, Animation iAnimation, double iSpeed, Vector2 iPosition, Vector2 iSize, DIRECTION iDir, ENTITYTYPE iType)
    {
        super(iModel, iAnimation, iSpeed, iPosition, iSize, iType);
        dir = iDir;

        vectorDirections = new ArrayList<Vector2>();

        vectorDirections.add(new Vector2(-1, 0));
        vectorDirections.add(new Vector2(1, 0));
        vectorDirections.add(new Vector2(0, -1));
        vectorDirections.add(new Vector2(0, 1));

    }

    public void update(float delta)
    {
        double velocityDelta = delta * speed;
        Vector2 vDir = vectorDirections.get(dir.getValue() - 1);
        float offsetX = vDir.x * ((float) (velocityDelta * 100) / (float) GAME_GLOBALS.DESKTOP_SCREEN_WIDTH);
        float offsetY = vDir.y * ((float) (velocityDelta * 100) / (float) GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT);
        getSprite().setPosition(getSprite().getX() + offsetX, getSprite().getY() + offsetY);
        if (!Utils.isObjectNull(getAnimation()))
        {
            getAnimation().setCurrentAnimation(dir.getValue());
            animate(delta);
        }

    }
    DIRECTION getDirection() { return dir; }
    private DIRECTION dir;
    private List<Vector2> vectorDirections;
}
