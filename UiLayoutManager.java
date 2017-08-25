package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;

import java.awt.Image;
import java.util.List;

/**
 * Created by Lorence on 7/24/2017.
 */

public abstract class UiLayoutManager {


    void add(ImageTextButton component)
    {
        components.add(component);
    }

    abstract void fixLayout();

    protected List<ImageTextButton> components;
}

class ButtonLayoutManager extends UiLayoutManager
{
    @Override
    void fixLayout()
    {
        int count = components.size();
        int counter = count;
        float baseX = GAME_GLOBALS.DESKTOP_SCREEN_WIDTH / 2;
        float baseY = GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT / 2;
        for ( int a = 0; a < count; a++ )
        {
            ImageTextButton currComp = components.get(a);
            float w = currComp.getWidth();
            float h = currComp.getHeight();
            currComp.setPosition(baseX - (w/2), baseY + ((count/2) - 1) * h);
        }
    }
}