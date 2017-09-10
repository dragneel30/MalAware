package com.mygdx.game;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorence on 8/11/2017.
 */

public class SaveInfo {

    SaveInfo(String iName) {

        name = iName;
    }
    String name;
}


class SaveList
{
    SaveList()
    {
        list = new ArrayList<SaveInfo>();
    }
    @SerializedName("saves") List<SaveInfo> list;

    List<SaveInfo> getList()
    {
        return list;
    }
    void add(SaveInfo newInfo)
    {
        list.add(newInfo);
    }
    boolean isExist(String name)
    {
        for ( int a = 0; a < list.size(); a++ )
        {
            Utils.makeLog(list.get(a).name);
            if ( list.get(a).name.equals(name) )
            {
                return true;
            }
        }
        return false;
    }
}