package com.thilenius.flame.rest;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.thilenius.flame.entity.FlameActionException;
import com.thilenius.flame.entity.FlameActionPath;
import com.thilenius.flame.entity.FlameTileEntity;
import com.thilenius.flame.utilities.types.Location3D;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class StatementDispatch {

    private class ActionPathTarget {
        public Method TargetMethod;
        public FlameTileEntity TargetTileEntity;
        public ActionPathTarget(Method method, FlameTileEntity targetTileEntity) {
            TargetMethod = method;
            TargetTileEntity = targetTileEntity;
        }
    }

    private RestHttpServer m_restHttpServer;
    private HashMap<String, ActionPathTarget> m_flameActionPaths = new HashMap<String, ActionPathTarget>();

    public StatementDispatch(RestHttpServer restHttpServer) {
        m_restHttpServer = restHttpServer;
    }

    public void dispatchAll() {
        // Pull all waiting requests from the Http server
        for (HttpRequest request : m_restHttpServer.getAllWaiting(false)) {
            Statement statement = request.getStatement();
            StatementResponse response = new StatementResponse();

            // Build out the full hash based on Name or Location
            String fullHash = statement.EntityName != null ?
                    getHashFromName(statement.UserName, statement.UserName, statement.FlameActionPath) :
                    getHashFromLocation(statement.UserName, Location3D.fromString(statement.EntityLocation),
                            statement.FlameActionPath);

            ActionPathTarget actionTarget = m_flameActionPaths.get(fullHash);
            if (actionTarget == null) {
                System.out.println("Failed to find target of invocation for FlameAction from full hash: " + fullHash);
                response.FailureMessage = "Failed to find target of invocation for FlameAction.";
                request.respond(response);
                continue;
            }

            // Check the FlameTileEntities cool down timer.
            if (actionTarget.TargetTileEntity.isOnCoolDown()) {
                continue;
            }

            // Ready for direct invocation
            Class<?> returnType = actionTarget.TargetMethod.getReturnType();
            try {

                // Invoke Action Path
                Object returnValue = actionTarget.TargetMethod.invoke(actionTarget.TargetTileEntity, statement.Message);

                // Non-Void return types
                if (returnType != void.class) {
                    if (returnValue == null) {
                        System.out.println("Failed to invoke TargetMethod for FlameAction");
                        response.FailureMessage = "Failed to invoke TargetMethod for FlameAction.";
                        request.respond(response);
                        continue;
                    }

                    response.setReturnValueFromObject(returnValue);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
                response.FailureMessage = "A fatal error was caught in Flame: " + e.getMessage() + ".";
                request.respond(response);
            } catch (InvocationTargetException e) {
                FlameActionException actionException = (FlameActionException) e.getTargetException();

                if (actionException != null) {
                    System.out.println("The flame Action threw an exception: " + e.getMessage());
                    response.FailureMessage = actionException.getMessage();
                } else {
                    e.printStackTrace();
                    response.FailureMessage = "A fatal error was caught in Flame: " + e.getMessage() + ".";
                }
                request.respond(response);
            }

            // Dispatch the response
            response.DidPass = true;
            request.respond(response);
        }
    }

    public void registerActionHandler(FlameTileEntity flameTileEntity, FlameActionPath actionPath, Method method) {
        // Register by Tile Location
        String locationHash = getHashFromLocation(flameTileEntity.getPlayerName(),
                new Location3D(flameTileEntity.xCoord, flameTileEntity.yCoord, flameTileEntity.zCoord),
                actionPath.value());
        m_flameActionPaths.put(locationHash, new ActionPathTarget(method, flameTileEntity));

        // Register by Entity Name
        String nameHash = getHashFromName(flameTileEntity.getPlayerName(), flameTileEntity.getName(),
                actionPath.value());
        m_flameActionPaths.put(locationHash, new ActionPathTarget(method, flameTileEntity));

        System.out.println("Registering Hash: " + locationHash);
        System.out.println("Registering Hash: " + nameHash);
    }

    public void unregisterPath(FlameTileEntity flameTileEntity, FlameActionPath actionPath) {
        // Register by Tile Location
        String locationHash = getHashFromLocation(flameTileEntity.getPlayerName(),
                new Location3D(flameTileEntity.xCoord, flameTileEntity.yCoord, flameTileEntity.zCoord),
                actionPath.value());
        m_flameActionPaths.remove(locationHash);

        // Register by Entity Name
        String nameHash = getHashFromName(flameTileEntity.getPlayerName(), flameTileEntity.getName(),
                actionPath.value());
        m_flameActionPaths.remove(nameHash);

        System.out.println("Removing Hash: " + locationHash);
        System.out.println("Removing Hash: " + nameHash);
    }

    public static String getHashFromName(String username, String entityName, String action) {
        return "user:[" + username + "],name:[" + entityName + "],action:[" + action + "]";
    }

    public static String getHashFromLocation(String username, Location3D location, String action) {
        return "user:[" + username + "],location:[" + location.toString() + "],action:[" + action + "]";
    }

}
