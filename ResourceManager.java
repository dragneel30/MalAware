package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorence on 7/24/2017.
 */

public class ResourceManager {

    static List<Resource> resourcePool;

    static void load()
    {
        resourcePool = new ArrayList<Resource>();

        resourcePool.add(new Resource("bullet", new Texture(Utils.getFile("graphics/textures/sprites/towers/bullet.png"))));
        resourcePool.add(new Resource("testtower1", new Texture(Utils.getFile("graphics/textures/sprites/towers/testtower1.png"))));
        resourcePool.add(new Resource("testtower2", new Texture(Utils.getFile("graphics/textures/sprites/towers/testtower2.png"))));
        resourcePool.add(new Resource("testtower3", new Texture(Utils.getFile("graphics/textures/sprites/towers/testtower3.png"))));

        resourcePool.add(new Resource("virus1", new Texture(Utils.getFile("graphics/textures/sprites/viruses/virus1.png"))));

        resourcePool.add(new Resource("button", new Texture(Utils.getFile("graphics/textures/userinterfaces/button.png"))));
        resourcePool.add(new Resource("defaultskin", new Texture(Utils.getFile("graphics/textures/userinterfaces/defaultskin.png"))));
        resourcePool.add(new Resource("mainmenu", new Texture(Utils.getFile("graphics/textures/userinterfaces/mainmenu.png"))));
        resourcePool.add(new Resource("splash", new Texture(Utils.getFile("graphics/textures/userinterfaces/splash.png"))));
        resourcePool.add(new Resource("map1", new Texture(Utils.getFile("graphics/textures/map/map1.png"))));


        resourcePool.add(new Resource("font", BitmapFontFactory.createdBitmapFrontFromFile(Utils.getFile("graphics/fonts/arial.ttf"), 32)));
        resourcePool.add(new Resource("font16", BitmapFontFactory.createdBitmapFrontFromFile(Utils.getFile("graphics/fonts/arial.ttf"), 16)));
        resourcePool.add(new Resource("selectedItemBackgroundAlmanac", new Texture(Utils.getFile("graphics/textures/userinterfaces/selectedItemBackgroundAlmanac.png"))));

        resourcePool.add(new Resource("closeButton", new Texture(Utils.getFile("graphics/textures/userinterfaces/closeButton.png"))));
    }

    static Resource getResource(String name)
    {
        for ( int a = 0; a < resourcePool.size(); a++ )
        {
            Resource curResource = resourcePool.get(a);
            if ( curResource.getName().equals(name) )
                return curResource;
        }
        return null;
    }

    static Object getObjectFromResource(String name)
    {
        return getResource(name).getData();
    }

    static List<Resource> getResourcePool() { return resourcePool; }

    static void addResource(Resource newResource)
    {
         getResourcePool().add(newResource);
    }


}
