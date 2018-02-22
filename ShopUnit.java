package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
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
        id = idGenerator++;
        Utils.makeLog(Integer.toString(id));
    }
    void setCooldown(Cooldown iCooldown)
    {
        cd = iCooldown;
    }
    Cooldown cd;
    void setPrice(int iPrice) { price = iPrice; }
    int price;
    private Tower unit;
    String name;
    int id;
    static int idGenerator = 0;

    void update(float delta)
    {
         cd.update(delta);
         if (cd.isCooldown)
             setColor(Color.BLACK);
         else
             setColor(Color.WHITE);
    }

    int counter = 0;
    public Tower getUnit() { return unit; }
}
