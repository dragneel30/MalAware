package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.List;

/**
 * Created by Lorence on 7/10/2017.
 */

public class HUD implements Drawable {



    HUD(Stage iStage, TextButton button, List<ShopUnit> iShopUnits) {
        shopUnits = iShopUnits;
        stage = iStage;
        stage.addActor(button);
        for (int a = 0; a < iShopUnits.size(); a++)
        {
            stage.addActor(iShopUnits.get(a));
        }
    }


    Stage getStage() { return stage; }
    private Stage stage;

    @Override
    public void draw(SpriteBatch batch)
    {

        stage.draw();
    }

    void update(float delta)
    {
        for ( int a = 0; a < shopUnits.size(); a++ )
        {
            shopUnits.get(a).update(delta);

        }
    }
    List<ShopUnit> shopUnits;


}
