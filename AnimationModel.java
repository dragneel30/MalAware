package com.malaware.game;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorence on 7/3/2017.
 */

public class AnimationModel {
    AnimationModel (int iNumAnimationPerFace, int iNumFace)
    {
        numFace = iNumFace;
        numAnimationPerFace = iNumAnimationPerFace;
        bounds = new ArrayList<Rectangle>();
    }

    void addBound(Rectangle bound)
    {
        bounds.add(bound);
    }


    List<Rectangle> getBounds() { return bounds; }
    int numAnimationPerFace;
    int numFace;
    int getNumFace() { return numFace; }
    int getNumAnimationPerFace() { return numAnimationPerFace; }
    List<Rectangle> bounds; // bounds in atlas
}
