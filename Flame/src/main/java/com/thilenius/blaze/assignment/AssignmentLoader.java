package com.thilenius.blaze.assignment;

import com.thilenius.blaze.player.BlazePlayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by Alec on 11/15/14.
 */
public class AssignmentLoader {

    private HashMap<String, BlazeAssignment> m_loadedAssignmentsByUsername = new HashMap<String, BlazeAssignment>();

    public AssignmentLoader() {

    }

    public BlazeLevel LoadLevel (BlazePlayer player, String assignmentName, int levelNumber, int seed) {

        // Does the user already has a loaded assignment?
        BlazeAssignment assignment = m_loadedAssignmentsByUsername.get(player.PlayerData.getUserName());
        if (assignment != null) {
            // Same assignment?
            if (assignment.getClass().getCanonicalName().equals(assignmentName)) {
                // Same assignment, just load the new level
                return assignment.loadLevel(levelNumber, seed);
            }

            // Different assignment. Unload the old one
            assignment.unload();
        }

        Class<?> clazz = null;
        try {
            clazz = Class.forName(assignmentName);
            Constructor<?> constructor = clazz.getConstructor(BlazePlayer.class);
            Object instance = constructor.newInstance(player);
            if (instance != null && instance instanceof BlazeAssignment) {
                m_loadedAssignmentsByUsername.put(player.PlayerData.getUserName(), (BlazeAssignment) instance);
                return ((BlazeAssignment)instance).loadLevel(levelNumber, seed);
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

    public BlazeLevel getActiveLevelForUsername(String username) {
        BlazeAssignment assignment = m_loadedAssignmentsByUsername.get(username);
        if (assignment == null) {
            return null;
        } else {
            return assignment.getActiveLevel();
        }
    }

}
