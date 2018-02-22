package com.mygdx.game;

import com.badlogic.gdx.Gdx;


public class GAME_GLOBALS {
    public final static int DESKTOP_SCREEN_WIDTH;
    public final static int DESKTOP_SCREEN_HEIGHT;

    //public final static float ANDROID_SCREEN_HEIGHT;
    //public final static float ANDROID_SCREEN_WIDTH;

    public final static float GAME_CAMERA_HEIGHT = 6f;
    public final static float GAME_CAMERA_WIDTH = 9f;

    public final static float ASPECT_RATIO;

    public final static float SPLASH_SCREEN_TIME = 5.0f;
    static
    {
        DESKTOP_SCREEN_HEIGHT = (int)Gdx.graphics.getHeight();
        DESKTOP_SCREEN_WIDTH = (int)Gdx.graphics.getWidth();
        ASPECT_RATIO = GAME_CAMERA_HEIGHT * ( DESKTOP_SCREEN_WIDTH / DESKTOP_SCREEN_HEIGHT );
    }

    public final static int FPS = 60;
    public final static String TITLE = "MalAware";
}
