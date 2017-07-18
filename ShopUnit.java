package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Tower;

/**
 * Created by Lorence on 7/15/2017.
 */

public class ShopUnit extends ImageTextButton {
    public ShopUnit(String iText, ImageTextButtonStyle iStyle, Tower iUnit) {
        super(iText, iStyle);
        unit = iUnit;
    }

    private Tower unit;
    public Tower getUnit() { return unit; }
}
