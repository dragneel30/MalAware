package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Lorence on 6/28/2017.
 */

public class EntityModel {

    EntityModel(Texture iTexture, String iName)
    {
        texture = iTexture;
        name = iName;
    }

    Texture getTexture()
    {
        return texture;
    }

    int money;
    int damage;

    int getDamage() { return damage; }
    int getMoney() { return money; }


    String getName() {
        return name;
    }
    String name;
    private Texture texture;
}
