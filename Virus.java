package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.sun.java.swing.plaf.motif.MotifTreeUI;

/**
 * Created by Lorence on 6/28/2017.
 */

public class Virus extends MoveableCreature {

    Virus(EntityModel iModel, Animation iAnimation, double iSpeed, Vector2 iPosition, Vector2 iSize, DIRECTION dir, ENTITYTYPE iType)
    {
        super(iModel, iAnimation, iSpeed, iPosition, iSize, dir, iType);

    }

    float accumulated_time_attack = 0;
    float attack_trigger_time = 0.5f;

}
