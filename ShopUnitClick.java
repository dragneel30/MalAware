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

        if ( currentClickedUnit == owner ) {
            currentClickedUnit = null;
        }
        else {

            currentClickedUnit = owner;
        }
    }
    private static ShopUnit currentClickedUnit;
    public static ShopUnit getCurrentClickedUnit() { return currentClickedUnit; }
    private ShopUnit owner;
    public ShopUnit getOwner() { return owner; }
    static void resetClick() { currentClickedUnit = null; }

}
