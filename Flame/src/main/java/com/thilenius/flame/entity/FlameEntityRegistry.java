package com.thilenius.flame.entity;

import com.thilenius.flame.entity.FlameActionTarget;
import com.thilenius.flame.utilities.StringUtils;
import com.thilenius.flame.utilities.types.Location3D;

import java.util.HashMap;
import java.util.HashSet;

public class FlameEntityRegistry {

    // Forms a tree structure:
    //   - Username
    //     - EntityName
    private HashMap<String, HashMap<String, FlameTileEntity>> m_entitiesByUsername
            = new HashMap<String, HashMap<String, FlameTileEntity>>();

    public void register (FlameTileEntity entity) {
        HashMap<String, FlameTileEntity> usersEntities = m_entitiesByUsername.get(entity.getPlayerName());
        if (usersEntities == null) {
            usersEntities = new HashMap<String, FlameTileEntity>();
            m_entitiesByUsername.put(entity.getPlayerName(), usersEntities);
        }
        usersEntities.put(entity.getName(), entity);

        System.out.println("Registering: " + entity.toString());
    }

    public void unregister (FlameTileEntity entity) {
        HashMap<String, FlameTileEntity> usersEntities = m_entitiesByUsername.get(entity.getPlayerName());
        if (usersEntities != null) {
            usersEntities.remove(entity.getName());
        }

        System.out.println("Unregistering: " + entity.toString());
    }

    public FlameActionTarget getTargetByEntityName(String username, String entityName, String actionName) {
        HashMap<String, FlameTileEntity> tileEntities = m_entitiesByUsername.get(username);
        if (tileEntities == null) { return null; }
        FlameTileEntity tileEntity = tileEntities.get(entityName);
        if (tileEntity == null) { return null; }
        return tileEntity.getActionTargetByName(actionName);
    }
}
