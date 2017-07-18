package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.ArrayList;


public abstract class Creature extends Entity {
    Creature(EntityModel iModel, Animation iAnimation, double iSpeed, Vector2 iPosition, Vector2 iSize, ENTITYTYPE iType)
    {
        super(iModel, iAnimation, iSpeed, iPosition, iSize, iType);

    }

}
