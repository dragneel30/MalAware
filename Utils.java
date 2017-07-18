package com.mygdx.game;

import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/**
 * Created by Lorence on 4/18/2017.
 */
import java.util.Random;
public class Utils {

    private static Random r = new Random();
    static int getRand(int highest)
    {
        return r.nextInt(highest);
    }

    static DIRECTION intToDirection(int n)
    {
        if ( DIRECTION.RIGHT.getValue() == n )
            return DIRECTION.RIGHT;
        else if ( DIRECTION.LEFT.getValue() == n )
            return DIRECTION.LEFT;
        else if ( DIRECTION.DOWN.getValue() == n )
            return DIRECTION.DOWN;
        else if ( DIRECTION.UP.getValue() == n )
            return DIRECTION.UP;
        return DIRECTION.UP;
    }
    public static float getDeltaTime()
    {
        return Gdx.graphics.getDeltaTime();
    }
    public static ApplicationLogger log = Gdx.app.getApplicationLogger();
    public static void makeLog(String message)
    {
        log.debug("makeLog()", message);
    }
    static
    {

    }

    public static boolean has2DCollision(Rectangle obj1, Rectangle obj2)
    {
        return ( obj1.getX() <= obj2.getX() + obj2.getWidth() &&
                obj1.getX() <= obj2.getY() + obj2.getWidth() &&
                obj1.getX() + obj1.getWidth() >= obj2.getX() &&
                obj1.getY() + obj1.getHeight() >= obj2.getY() );
    }

    public static boolean has2DCollision(Sprite obj1, Sprite obj2)
    {
        return has2DCollision(obj1.getBoundingRectangle(), obj2.getBoundingRectangle());
    }
    public static boolean isAligned(float y1, float y2)
    {
        return y1 == y2;
    }
    public static boolean has2DPositionCollision(Vector2 obj1, Vector2 obj2)
    {
        return obj1 == obj2;
    }

    public static boolean isObjectNull(Object o)
    {
        return o == null;
    }


    static <T> T getFromJson(String file, Class<T> type)
    {
        try
        {
            Reader reader = new FileReader(file);
            Gson gson = new Gson();
            return gson.fromJson(reader, type);
        }
        catch(FileNotFoundException e)
        {
            Utils.makeLog(e.getMessage());
        }
        return null;
    }

}
