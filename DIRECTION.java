package com.malaware.game;

/**
 * Created by Lorence on 6/28/2017.
 */
public enum DIRECTION {

    LEFT(1), RIGHT(2), DOWN(3), UP(4), NONE(4);
    private final int value;
    DIRECTION(int i)
    {
        value = i;
    }
    int getValue()
    {
        return value;
    }

}