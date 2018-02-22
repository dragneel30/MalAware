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
        leaked = new ArrayList<leakedvirus>();
        almanac = new viruses();
    }
    String name;
    public int skilltree = 1;
    public int stage = 1;
    public int stage1star = 0;
    public int stage2star = 0;
    public int stage3star = 0;
    java.util.List<leakedvirus> leaked;
    viruses almanac;
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



class leakedvirus
{
    public leakedvirus(String n, int s)
    {
        name = n;
        stage = s;
    }
    String name;
    int stage;
}
class viruses
{
    java.util.List<String> almanaccontent = new ArrayList<String>();
}