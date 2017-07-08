package com.malaware.game;

import com.badlogic.gdx.math.Vector2;
import com.sun.java.swing.plaf.motif.MotifTreeUI;

/**
 * Created by Lorence on 6/28/2017.
 */

public class Virus extends MoveableCreature {

    Virus(EntityModel iModel, Animation iAnimation, double iSpeed, Vector2 iPosition, Vector2 iSize, DIRECTION dir)
    {
        super(iModel, iAnimation, iSpeed, iPosition, iSize, dir);
    }


}
