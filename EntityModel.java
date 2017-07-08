package com.malaware.game;

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


    String getName() {
        return name;
    }
    String name;
    private Texture texture;
}
