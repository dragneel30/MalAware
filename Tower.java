package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lorence on 7/3/2017.
 */
import java.util.List;
import java.util.ArrayList;
public class Tower extends Creature {
    Tower(EntityModel iModel, Animation iAnimation, double iSpeed, Vector2 iPosition, Vector2 iSize, Bullet iBullet, ENTITYTYPE iType)
    {
        super(iModel, iAnimation, iSpeed, iPosition, iSize, iType);
        bullets = new ArrayList<Bullet>();
        bullet = iBullet;
        reload();
    }

    Tower(Tower other)
    {
        super(other.getModel(), other.getAnimation(), other.getSpeed(), new Vector2(other.getSprite().getX(), other.getSprite().getY()),
                new Vector2(other.getSprite().getWidth(), other.getSprite().getHeight()), other.getType());
        bullets = new ArrayList<Bullet>();
        bullet = other.getBullet();
    }
    float accumulated_delta;

    boolean isReadyToFire()
    {
        return getAccumulated_delta() >= getSpeed();
    }
    void reload() { accumulated_delta = 0.0f; }
    float getAccumulated_delta() { return accumulated_delta; }
    Bullet getBullet()
    {
        return bullet;
    }
    void update(float delta)
    {

        for ( int a = 0; a < bullets.size(); a++ )
            bullets.get(a).update(delta);
        accumulated_delta += delta;

    }

    @Override
    public void draw(SpriteBatch batch)
    {
        getSprite().draw(batch);
        for ( int a = 0; a < bullets.size(); a++)
        {
            bullets.get(a).draw(batch);
        }
    }
    void fire()
    {
        Bullet newBullet = new Bullet(bullet);
        newBullet.getSprite().setPosition(getSprite().getX(), getSprite().getY());
        bullets.add(newBullet);

    }

    List<Bullet> getBullets() {

        return bullets;
    }

    void setBullet(Bullet newBullet)
    {
        bullet = newBullet;
    }

    Bullet bullet;
    List<Bullet> bullets;



}
