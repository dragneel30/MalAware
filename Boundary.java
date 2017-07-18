package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lorence on 7/10/2017.
 */

public class Boundary {

    Boundary(Rectangle iBound)
    {
        iBound = bound;
    }

    Rectangle get()
    {
        return bound;
    }


    Rectangle bound;
}
