package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

/**
 * Created by Lorence on 9/23/2017.
 */

public class LevelStructure {


    public LevelStructure()
    {
        stages = new ArrayList<StageInfo>();

    }

    public java.util.List<StageInfo> stages;
}


class StageInfo
{
    public StageInfo()
    {
        enemies = new ArrayList<String>();
        wave_num = 1;
        enemy_per_wave = 0;
    }
    public int wave_num;
    public int enemy_per_wave;
    public java.util.List<String> enemies;
}

