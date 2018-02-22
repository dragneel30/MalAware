package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lorence on 6/28/2017.
 */

enum ENTITYTYPE
{

    TOWER(0), VIRUS(1), BULLET(2);
    private int value;
    int getValue() { return value; }
    ENTITYTYPE(int i)
    {
        value = i;
    }
}
public abstract class Entity implements Drawable {


    private boolean visible;
    boolean isVisible()
    {
        return visible;
    }
    void setVisible(boolean newVisible)
    {
        visible = newVisible;
    }
    float getAlignment()
    {
        return getSprite().getY();
    }
    ENTITYTYPE type;
    ENTITYTYPE getType() { return type; }
    Entity(EntityModel iModel, Animation iAnimation, double iSpeed, Vector2 iPosition, Vector2 iSize, ENTITYTYPE iType)
    {
        visible = true;
        type = iType;
        speed = iSpeed;



        if ( setModel(iModel) )
        {
            sprite.setPosition(iPosition.x, iPosition.y);
            sprite.setSize(iSize.x, iSize.y);
            if ( iType == ENTITYTYPE.VIRUS || iType == ENTITYTYPE.TOWER )
            sprite.setSize((float)0.8, (float)0.8);
        }

        setAnimation(iAnimation);

        if ( !Utils.isObjectNull(iAnimation) ) {
            animate(sec_per_anim);
        }


    }
    Vector2 getPostiion()
    {
        return new Vector2(getSprite().getX(), getSprite().getY());
    }

    int hp;
    int getHp() { return hp; }
    protected double speed;
    Sprite getSprite() { return sprite; }
    double getSpeed() { return speed; }
    void setAnimation(Animation newAnimation)
    {
        animation = newAnimation;
    }

    public void draw(SpriteBatch batch)
    {
        if (isVisible()) sprite.draw(batch);
    }
    abstract void update(float delta);


    float sec_per_anim = (float)0.5;
    float accumulated = 0;
    void animate(float delta)
    {
        accumulated += delta;
        if ( accumulated >= sec_per_anim ) {
            Rectangle bound = animation.getNextAnimation();
            TextureRegion reg = new TextureRegion(model.getTexture(), (int) bound.getX(), (int) bound.getY(), (int) bound.getWidth(), (int) bound.getHeight());

            sprite.setRegion(reg);
            accumulated = 0;
        }

    }

    Animation getAnimation() { return animation; }
    EntityModel getModel() { return model;}
    boolean setModel(EntityModel newModel)
    {
        model = newModel;
        if ( !Utils.isObjectNull(newModel) )
        {
            Utils.makeLog("testsetsetsetset");
            sprite = new Sprite(newModel.getTexture());
            sprite.setSize(1, 1);
            return true;
        }
        return false;
    }
    protected EntityModel model;
    protected Animation animation;
    protected Sprite sprite;
}
