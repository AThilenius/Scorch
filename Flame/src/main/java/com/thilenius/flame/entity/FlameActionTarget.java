package com.thilenius.flame.entity;

import com.thilenius.flame.entity.FlameActionPath;
import com.thilenius.flame.entity.FlameTileEntity;
import com.thilenius.flame.utilities.types.Location3D;

import java.lang.reflect.Method;

/**
 * Created by Alec on 3/31/15.
 */
public class FlameActionTarget {

    public FlameTileEntity TileEntity;
    public FlameActionPath ActionPath;
    public Method TargetMethod;
    public String EntityName;
    public String Username;
    public Location3D Location;

    public FlameActionTarget(FlameTileEntity tileEntity, FlameActionPath actionPath, Method targetMethod, String entityName, String username,
                             Location3D location) {
        TileEntity = tileEntity;
        ActionPath = actionPath;
        TargetMethod = targetMethod;
        EntityName = entityName;
        Username = username;
        Location = location;
    }

    @Override
    public String toString() {
        return "Location [ " + Location.toString() + " ], Entity Name [ " + EntityName + " ], Username [ " + Username
                + " ], Action Method Name [ " + TargetMethod.getName() + " ]";
    }

}
