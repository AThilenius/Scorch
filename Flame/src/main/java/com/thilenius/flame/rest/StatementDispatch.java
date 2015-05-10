package com.thilenius.flame.rest;


import com.thilenius.flame.Flame;
import com.thilenius.flame.entity.FlameActionException;
import com.thilenius.flame.entity.FlameActionTarget;
import com.thilenius.flame.utilities.types.Location3D;

import java.lang.reflect.InvocationTargetException;

public class StatementDispatch {

    private RestHttpServer m_restHttpServer;

    public StatementDispatch(RestHttpServer restHttpServer) {
        m_restHttpServer = restHttpServer;
    }

    public void dispatchAll() {
        // Pull all waiting requests from the Http server
        for (HttpRequest request : m_restHttpServer.getAllWaiting(false)) {
            Statement statement = request.getStatement();
            StatementResponse response = new StatementResponse();

            FlameActionTarget actionTarget = null;

            actionTarget= Flame.Globals.EntityRegistry.getTargetByEntityName(statement.UserName, statement.EntityName,
                    statement.FlameActionPath);

            if (actionTarget == null) {
                System.out.println("Failed to find target of invocation for FlameAction.");
                response.FailureMessage = "Failed to find target of invocation for FlameAction named "
                        + statement.EntityName;
                request.respond(response);
                continue;
            }

            // Check the FlameTileEntities cool down timer.
            if (actionTarget.TileEntity.isOnCoolDown()) {
                continue;
            }

            // Ready for direct invocation
            Class<?> returnType = actionTarget.TargetMethod.getReturnType();
            try {
                // Invoke Action Path
                Object returnValue = actionTarget.TargetMethod.invoke(actionTarget.TileEntity, statement.Message);

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

}
