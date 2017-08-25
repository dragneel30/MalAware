package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by Lorence on 7/25/2017.
 */

public class ButtonListener extends ClickListener {

    ButtonListener(GameState iCurrState, Button iOwner)
    {
        currState = iCurrState;
        owner = iOwner;
    }
    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
    {
        super.touchUp(event, x, y, pointer, button);
        Utils.makeLog("touchUp()");

    }
    @Override
    public void clicked (InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        currState.start();

    }

    GameState currState;
    Button owner;
}
