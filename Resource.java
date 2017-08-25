package com.mygdx.game;

import java.util.List;

/**
 * Created by Lorence on 7/25/2017.
 */

public class Resource {
    Resource(String iName, Object iData)
    {
        data = iData;
        inUse = false;
        name = iName;
    }
    boolean inUse;
    boolean isInUse() { return inUse; }
    Object getData() { return data; }
    String getName() { return name; }
    String name;
    Object data;
}
