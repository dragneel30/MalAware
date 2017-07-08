package com.malaware.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lorence on 6/28/2017.
 */

public abstract class Entity implements Drawable {

    Entity(EntityModel iModel, Animation iAnimation, double iSpeed, Vector2 iPosition, Vector2 iSize)
    {
        speed = iSpeed;
        model = iModel;


        setAnimation(iAnimation);
        sprite = new Sprite(iModel.getTexture());
        sprite.setPosition(iPosition.x, iPosition.y);
        sprite.setSize(iSize.x, iSize.y);
        if ( !Utils.isObjectNull(getAnimation()) ) {
            animation = new Animation(iAnimation);
            animate();
        }

        else animation = null;

    }

    protected double speed;
    Sprite getSprite() { return sprite; }
    double getSpeed() { return speed; }
    void setAnimation(Animation newAnimation)
    {

        animation = newAnimation;

    }

    public void draw(SpriteBatch batch)
    {
        sprite.draw(batch);
    }
    abstract void update(float delta);
    void animate()
    {
        Rectangle bound = animation.getNextAnimation();
        TextureRegion reg = new TextureRegion(model.getTexture(), (int)bound.getX(), (int)bound.getY(), (int)bound.getWidth(), (int)bound.getHeight());

        sprite.setRegion(reg);

    }

    Animation getAnimation() { return animation; }
    EntityModel getModel() { return model;}
    protected EntityModel model;
    protected Animation animation;
    protected Sprite sprite;
}
