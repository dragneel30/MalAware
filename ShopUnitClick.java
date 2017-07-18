package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Lorence on 7/15/2017.
 */

public class ShopUnitClick extends ClickListener {

    ShopUnitClick(ShopUnit iOwner)
    {
        owner = iOwner;
    }
    @Override
    public void clicked (InputEvent event, float x, float y)
    {
        currentClickedUnit = owner;
    }
    private static ShopUnit currentClickedUnit;
    public static ShopUnit getCurrentClickedUnit() { return currentClickedUnit; }
    private ShopUnit owner;
    static void resetClick() { currentClickedUnit = null; }

}
