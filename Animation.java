package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lorence on 6/28/2017.
 */

import java.util.List;
import java.util.ArrayList;
public class Animation {

    Animation(int iCurrentFace, int iCurrentAnimation, AnimationModel iModel)
    {
        model = iModel;
        currentAnimation = iCurrentAnimation;
        currentFace = iCurrentFace;
    }

    Animation(Animation other)
    {
        model = other.model;
        currentAnimation = other.getCurrentAnimation();
        currentFace = other.getCurrentFace();
    }


    Rectangle getNextAnimation()
    {
        if ( currentFace > model.getNumAnimationPerFace() )
        {
            currentFace = 1;
        }
        return model.getBounds().get(((currentAnimation - 1) * model.getNumAnimationPerFace()) + ((currentFace++) - 1));
    }

    int currentFace;
    int currentAnimation;
    AnimationModel model;
    int getCurrentFace() { return currentFace; }
    int getCurrentAnimation() { return currentAnimation; }
    void setCurrentFace(int newCurrentFace) { currentFace = newCurrentFace; }
    void setCurrentAnimation(int newCurrentAnimation) { currentAnimation = newCurrentAnimation; }
    AnimationModel getModel() { return model; }
    void setModel(AnimationModel newModel) { model = newModel; }
}
