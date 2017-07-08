package com.malaware.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lorence on 7/3/2017.
 */
import java.util.List;
import java.util.ArrayList;
public class Tower extends Creature {
    Tower(EntityModel iModel, Animation iAnimation, double iSpeed, Vector2 iPosition, Vector2 iSize, Bullet iBullet)
    {
        super(iModel, iAnimation, iSpeed, iPosition, iSize);
        bullets = new ArrayList<Bullet>();
        bullet = iBullet;
    }

    float accumulated_delta = 0.0f;

    void update(float delta)
    {

        for ( int a = 0; a < bullets.size(); a++ )
            bullets.get(a).update(delta);
        accumulated_delta += delta;
        if ( accumulated_delta >= 1.0f )
        {
            accumulated_delta = 0.0f;
                fire();
        }

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
        bullets.add(new Bullet(bullet));

    }

    List<Bullet> getBullets() {

        return bullets;
    }

    Bullet bullet;
    List<Bullet> bullets;
}
