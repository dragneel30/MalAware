package com.malaware.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Lorence on 7/3/2017.
 */

public class Bullet extends MoveableCreature {

    Bullet(EntityModel iModel, Animation iAnimation, double iSpeed, Vector2 iPosition, Vector2 iSize, DIRECTION iDir)
    {
        super(iModel, iAnimation, iSpeed, iPosition, iSize, iDir);

    }

    Bullet(Bullet other)
    {
        super(other.getModel(), other.getAnimation(), other.getSpeed(),
                new Vector2(other.getSprite().getX(), other.getSprite().getY()),
                new Vector2(other.getSprite().getWidth(), other.getSprite().getHeight()), other.getDirection());

    }

}
