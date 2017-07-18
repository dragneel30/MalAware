package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Lorence on 7/14/2017.
 */

public class MainMenu implements Drawable {

    MainMenu()
    {
        sprite = new Sprite(new Texture(Gdx.files.internal("graphics/textures/Splash.png")));
        sprite.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        adjustOpacity(0);
        isFadingout = false;
    }
    private void adjustOpacity(float alpha)
    {
        Color c = sprite.getColor();
        c.a = alpha;
        sprite.setColor(c);
    }
    Sprite sprite;
    float accumulated_data;
    float alpha = 0;
    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
    public void update(float delta)
    {
        if ( isFadingout )
        {
            if ( alpha <= 0 )
                finish = true;
            else
                alpha--;
        }
        else
        {
            if ( alpha >= 254 )
            {
                accumulated_data += delta;
                if ( accumulated_data >= GAME_GLOBALS.SPLASH_SCREEN_TIME )
                {
                    isFadingout = true;
                }
            }
            else
                alpha++;
        }
        adjustOpacity(alpha / 255);

    }
    boolean finish;
    boolean isFadingout;

    boolean isFinished() { return finish; }

}
