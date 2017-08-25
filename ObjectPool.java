package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorence on 7/25/2017.
 */

public class ObjectPool<T> {

    ObjectPool()
    {
        objects = new ArrayList<T>();
    }
    void addObject(T newObject)
    {
        objects.add(newObject);
    }
    List<T> objects;

    T getAvailableObject()
    {
        for ( int a = 0; a < objects.size(); a++ )
        {
            Resource resource = (Resource) objects.get(a);
            if ( !resource.isInUse() )
                return objects.get(a);
        }
        return null;
    }
}


