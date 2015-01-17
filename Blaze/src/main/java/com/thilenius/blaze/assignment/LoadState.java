package com.thilenius.blaze.assignment;

import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.utilities.types.Location3D;

/**
 * Created by Alec on 12/1/14.
 */
public class LoadState {
    public BlazeAssignment Assignment = null;
    public BlazeLevel Level = null;
    public int LevelNumber = -1;
    
    public BFEProtos.BFEMessage transitionState (AssignmentLoader loader, AssignmentQuery assignmentQuery, int points) {
        BFEProtos.BFELoadLevelResponse response = null;

        try {
            // First check that the correct assignment is loaded
            if (Assignment == null) {
                // No assignment is loaded at all
                loadAssignment(loader, assignmentQuery.JarPath, assignmentQuery.ArenaLocation,
                        assignmentQuery.FirstName + " " + assignmentQuery.LastName);
            } else if (!Assignment.getClass().getCanonicalName().equals(assignmentQuery.JarPath)) {
                // The wrong assignment is loaded. We also need to unload the level at this point
                if (Level != null) {
                    Level.unload();
                    Level = null;
                }
                loadAssignment(loader, assignmentQuery.JarPath, assignmentQuery.ArenaLocation,
                        assignmentQuery.FirstName + " " + assignmentQuery.LastName);
            } else {
                // Correct Assignment loaded
                Assignment.reload();
            }

            // Next check that the correct level is loaded
            if (Level == null) {
                // No level is loaded at all
                loadLevel(assignmentQuery, points);
            } else if (LevelNumber != assignmentQuery.LevelNumber) {
                // Wrong level is loaded
                loadLevel(assignmentQuery, points);
            } else {
                // Correct Level loaded
                Level.reload(points);
            }

            // Return a 'good to go' Proto
            response = BFEProtos.BFELoadLevelResponse.newBuilder()
                    .setSparkCount(Level.getSparks().length)
                    .build();


        } catch (Exception e) {
            // Return an error Proto
            e.printStackTrace();
            response = BFEProtos.BFELoadLevelResponse.newBuilder()
                    .setFailureReason("Error during assignment loading: " + e.getMessage())
                    .build();
        }

        return BFEProtos.BFEMessage.newBuilder()
                .setExtension(BFEProtos.BFELoadLevelResponse.bFELoadLevelResponseExt, response)
                .build();
    }

    private void loadAssignment (AssignmentLoader loader, String jarPath, Location3D arenaLocation, String displayName)
            throws Exception {
        BlazeAssignment newAssignment = loader.loadAssignment(jarPath);
        if (newAssignment == null) {
            // Failed to load the new assignment
            throw new Exception("Failed to load assignment: " + jarPath + ". The Jar likely doesn't exist.");
        }

        // New assignment loaded correctly, unload the old one if needed
        if (Assignment != null) {
            Assignment.unload();
        }
        Assignment = newAssignment;
        Assignment.load(arenaLocation, displayName);
    }

    private void loadLevel (AssignmentQuery assignmentQuery, int points) throws Exception {
        BlazeLevel newLevel = Assignment.getLevel(assignmentQuery.LevelNumber);
        if (newLevel == null) {
            // Failed to load the new level
            throw new Exception("Failed to load level: " + assignmentQuery.LevelNumber +
                    ". The level number likely doesn't exist.");
        }

        // New level loaded correctly, unload the old one if needed
        if (Level != null) {
            Level.unload();
        }
        Level = newLevel;
        Level.load(assignmentQuery, points);
        LevelNumber = assignmentQuery.LevelNumber;
    }
}
