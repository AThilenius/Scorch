package com.thilenius.blaze.assignment;

import com.thilenius.blaze.player.BlazePlayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by Alec on 11/15/14.
 */
public class AssignmentLoader {

    private HashMap<String, BlazeAssignment> m_loadedAssignments = new HashMap<String, BlazeAssignment>();

    public AssignmentLoader() {

    }

    public BlazeAssignment LoadAssignment (String name, BlazePlayer player) {

        String key = name + "|" + player.PlayerData.getUserName();
        BlazeAssignment assignment = m_loadedAssignments.get(key);
        if (assignment != null) {
            return assignment;
        }

        Class<?> clazz = null;
        try {
            clazz = Class.forName(name);
            Constructor<?> constructor = clazz.getConstructor(BlazePlayer.class);
            Object instance = constructor.newInstance(player);
            if (instance != null && instance instanceof BlazeAssignment) {
                m_loadedAssignments.put(key, (BlazeAssignment) instance);
                return (BlazeAssignment) instance;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
