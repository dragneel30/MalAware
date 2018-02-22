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
        resourcePool.add(new Resource("bullet1", new Texture(Utils.getFile("graphics/textures/sprites/bullets/bullet1.png"))));
        resourcePool.add(new Resource("bullet2", new Texture(Utils.getFile("graphics/textures/sprites/bullets/bullet2.png"))));
        resourcePool.add(new Resource("bullet3", new Texture(Utils.getFile("graphics/textures/sprites/bullets/bullet3.png"))));
        resourcePool.add(new Resource("bullet4", new Texture(Utils.getFile("graphics/textures/sprites/bullets/bullet4.png"))));
        resourcePool.add(new Resource("testtower1", new Texture(Utils.getFile("graphics/textures/sprites/towers/testtower1.png"))));
        resourcePool.add(new Resource("testtower2", new Texture(Utils.getFile("graphics/textures/sprites/towers/testtower2.png"))));
        resourcePool.add(new Resource("testtower3", new Texture(Utils.getFile("graphics/textures/sprites/towers/testtower3.png"))));
        resourcePool.add(new Resource("trojan", new Texture(Utils.getFile("graphics/textures/sprites/towers/trojan.png"))));

        resourcePool.add(new Resource("worm", new Texture(Utils.getFile("graphics/textures/sprites/viruses/virus1.png"))));
        resourcePool.add(new Resource("logicbomb", new Texture(Utils.getFile("graphics/textures/sprites/viruses/logicbomb.png"))));
        resourcePool.add(new Resource("virus", new Texture(Utils.getFile("graphics/textures/sprites/viruses/virus.png"))));
        resourcePool.add(new Resource("trojanhorse", new Texture(Utils.getFile("graphics/textures/sprites/viruses/trojanhorse.png"))));
        resourcePool.add(new Resource("shortcut", new Texture(Utils.getFile("graphics/textures/sprites/viruses/shortcut.png"))));
        resourcePool.add(new Resource("ransomware", new Texture(Utils.getFile("graphics/textures/sprites/viruses/ransomware.png"))));
        resourcePool.add(new Resource("adware", new Texture(Utils.getFile("graphics/textures/sprites/viruses/adware.png"))));


        resourcePool.add(new Resource("q_worm", new Texture(Utils.getFile("graphics/textures/sprites/quarantine/virus1.png"))));
        resourcePool.add(new Resource("q_logicbomb", new Texture(Utils.getFile("graphics/textures/sprites/quarantine/logicbomb.png"))));
        resourcePool.add(new Resource("q_virus", new Texture(Utils.getFile("graphics/textures/sprites/quarantine/virus.png"))));
        resourcePool.add(new Resource("q_trojanhorse", new Texture(Utils.getFile("graphics/textures/sprites/quarantine/trojanhorse.png"))));
        resourcePool.add(new Resource("q_shortcut", new Texture(Utils.getFile("graphics/textures/sprites/quarantine/shortcut.png"))));
        resourcePool.add(new Resource("q_ransomware", new Texture(Utils.getFile("graphics/textures/sprites/quarantine/ransomware.png"))));
        resourcePool.add(new Resource("q_adware", new Texture(Utils.getFile("graphics/textures/sprites/quarantine/adware.png"))));


        resourcePool.add(new Resource("button", new Texture(Utils.getFile("graphics/textures/userinterfaces/button.png"))));
        resourcePool.add(new Resource("disabledStage", new Texture(Utils.getFile("graphics/textures/userinterfaces/disabledButton.png"))));
        resourcePool.add(new Resource("skipButton", new Texture(Utils.getFile("graphics/textures/userinterfaces/skip.png"))));
        resourcePool.add(new Resource("defaultskin", new Texture(Utils.getFile("graphics/textures/userinterfaces/defaultskin.png"))));
        resourcePool.add(new Resource("mainmenu", new Texture(Utils.getFile("graphics/textures/userinterfaces/mainmenu.png"))));
        resourcePool.add(new Resource("splash", new Texture(Utils.getFile("graphics/textures/userinterfaces/splash.png"))));
        resourcePool.add(new Resource("mainsplash", new Texture(Utils.getFile("graphics/textures/userinterfaces/mainsplash.png"))));
        resourcePool.add(new Resource("map1", new Texture(Utils.getFile("graphics/textures/map/map1.png"))));
        resourcePool.add(new Resource("textbox", new Texture(Utils.getFile("graphics/textures/userinterfaces/textbox.png"))));

        resourcePool.add(new Resource("star", new Texture(Utils.getFile("graphics/textures/star.png"))));
        resourcePool.add(new Resource("blackStar", new Texture(Utils.getFile("graphics/textures/blankStar.png"))));
        resourcePool.add(new Resource("stage1background", new Texture(Utils.getFile("graphics/textures/stage1background.png"))));
        resourcePool.add(new Resource("stage2background", new Texture(Utils.getFile("graphics/textures/stage2background.png"))));
        resourcePool.add(new Resource("stage3background", new Texture(Utils.getFile("graphics/textures/stage3background.png"))));

        resourcePool.add(new Resource("almanacBackground", new Texture(Utils.getFile("graphics/textures/almanacBackground.png"))));
        resourcePool.add(new Resource("field", new Texture(Utils.getFile("graphics/textures/field.png"))));
        resourcePool.add(new Resource("spritesheet1", new Texture(Utils.getFile("graphics/textures/sprites/viruses/spritesheet1.png"))));
        resourcePool.add(new Resource("spritesheet2", new Texture(Utils.getFile("graphics/textures/sprites/towers/spritesheet2.png"))));

        resourcePool.add(new Resource("trojantest", new Texture(Utils.getFile("graphics/textures/sprites/test.png"))));
        resourcePool.add(new Resource("avira", new Texture(Utils.getFile("graphics/textures/sprites/shop/avira.png"))));
        resourcePool.add(new Resource("kaspersky", new Texture(Utils.getFile("graphics/textures/sprites/shop/kaspersky.png"))));
        resourcePool.add(new Resource("norton", new Texture(Utils.getFile("graphics/textures/sprites/shop/norton.png"))));
        resourcePool.add(new Resource("mcafee", new Texture(Utils.getFile("graphics/textures/sprites/shop/mcafee.png"))));
        resourcePool.add(new Resource("avast", new Texture(Utils.getFile("graphics/textures/sprites/shop/avast.png"))));
        resourcePool.add(new Resource("k7", new Texture(Utils.getFile("graphics/textures/sprites/shop/k7.png"))));
        resourcePool.add(new Resource("panda", new Texture(Utils.getFile("graphics/textures/sprites/shop/panda.png"))));
        resourcePool.add(new Resource("drweb", new Texture(Utils.getFile("graphics/textures/sprites/shop/drweb.png"))));



        resourcePool.add(new Resource("font", BitmapFontFactory.createdBitmapFrontFromFile(Utils.getFile("graphics/fonts/arial.ttf"), 32)));
        resourcePool.add(new Resource("font16", BitmapFontFactory.createdBitmapFrontFromFile(Utils.getFile("graphics/fonts/arial.ttf"), 16)));
        resourcePool.add(new Resource("selectedItemBackgroundAlmanac", new Texture(Utils.getFile("graphics/textures/userinterfaces/selectedItemBackgroundAlmanac.png"))));

        resourcePool.add(new Resource("bitcoin", new Texture(Utils.getFile("graphics/textures/userinterfaces/bitcoin.png"))));
        resourcePool.add(new Resource("infection", new Texture(Utils.getFile("graphics/textures/userinterfaces/infection.png"))));

        resourcePool.add(new Resource("menuBackgroundMusic", Gdx.audio.newMusic(Utils.getFile("audios/backgroundMenu.mp3"))));
        resourcePool.add(new Resource("backgroundGameplay", Gdx.audio.newMusic(Utils.getFile("audios/backgroundGameplay.mp3"))));
        resourcePool.add(new Resource("closeButton", new Texture(Utils.getFile("graphics/textures/userinterfaces/closeButton.png"))));
        resourcePool.add(new Resource("skilltree", new Texture(Utils.getFile("graphics/textures/sprites/skilltree.png"))));
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
