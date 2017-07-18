package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorence on 7/17/2017.
 */

class ScreenAnimation implements Drawable
{
    ScreenAnimation()
    {
        sprites = new ArrayList<Sprite>();
        finish = false;
        fadingout = false;
        startingAlpha = 0;
    }
    void addSprite(Sprite newSprite)
    {
        if ( !sprites.contains(newSprite) )
            sprites.add(newSprite);
    }
    void clear() { sprites.clear(); }

    void fadein()
    {
        if ( startingAlpha >= 254 )
            finish = true;
        else
            setAlpha(startingAlpha++);
    }
    void fadeout()
    {
        if ( finish )
            finish = false;
        if ( startingAlpha <= 0 )
            finish = true;
        else
            setAlpha(startingAlpha--);
    }
    void setAlpha(float alpha)
    {
        for ( int a = 0; a < sprites.size(); a++ )
        {
            Sprite currentSprite = sprites.get(a);
            Color color = currentSprite.getColor();
            color.a = alpha / 255;
            currentSprite.setColor(color);
        }
    }
    boolean isFinished()
    {
        return finish;
    }
    float startingAlpha;
    float accumulated_time;
    boolean finish;
    boolean fadingout;
    List<Sprite> sprites;

    @Override
    public void draw(SpriteBatch batch) {
        for ( int a = 0; a < sprites.size(); a++ )
        {
            sprites.get(a).draw(batch);
        }
    }
}
public abstract class GameState {



    abstract void draw(SpriteBatch batch);
    abstract void update(float delta);
    protected ScreenAnimation animation;
}

class MenuState extends GameState
{

    MenuState()
    {

    }
    @Override
    void draw(SpriteBatch batch) {

    }

    @Override
    void update(float delta) {

    }
}
class SplashState extends GameState {
    float accumulated_data;
    SplashState()
    {
        animation = new ScreenAnimation();
        accumulated_data = 0;
        fadingout = false;
        sprite = new Sprite(new Texture(Gdx.files.internal("graphics/textures/Splash.png")));
        sprite.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        animation.addSprite(sprite);
        animation.setAlpha(0);
    }
    Sprite sprite;
    @Override
    public void draw(SpriteBatch batch) {
        animation.draw(batch);
    }
    @Override
    public void update(float delta)
    {
        if ( fadingout )
            animation.fadeout();
        else if ( animation.isFinished() )
        {
            Utils.makeLog("Test");
            accumulated_data += delta;
            if ( accumulated_data >= splash_time )
            {
                fadingout = true;
            }
        }
        else
            animation.fadein();
    }

    boolean isFinished()
    {
        return fadingout && animation.isFinished();
    }

    boolean fadingout;
    final float splash_time = 3.0f;
}
