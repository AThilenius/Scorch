package com.thilenius.blaze.assignment;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.utilities.types.Location3D;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Alec on 11/15/14.
 */
public class AssignmentLoader {

    public AssignmentLoader() {

    }

    public BlazeAssignment loadAssignment (String jarPath) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(jarPath);
            Constructor<?> constructor = clazz.getConstructor();
            Object instance = constructor.newInstance();
            if (instance != null && instance instanceof BlazeAssignment) {
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
