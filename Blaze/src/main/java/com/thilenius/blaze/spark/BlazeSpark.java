package com.thilenius.blaze.spark;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.flame.Flame;
import com.thilenius.flame.spark.SparkTileEntity;
import com.thilenius.utilities.types.Location3D;

import java.nio.channels.SocketChannel;

/**
 * Created by Alec on 11/15/14.
 */
public class BlazeSpark {

    public SparkTileEntity TileEntity;

    public BlazeSpark(Location3D location) {
        CreateMinecraftSpark(location);
    }

    public BFEProtos.BFESparkResponse handle (BFEProtos.BFESparkCommand.CommandType commandType) {
        BFEProtos.BFESparkResponse response;

        SparkTileEntity.AnimationTypes animationAction = toAnimationType(commandType);
        Location3D newLocation = TileEntity.getBlockFromAction(animationAction);
        SparkTileEntity oldSpark = TileEntity;

        if (newLocation.X != TileEntity.xCoord ||
                newLocation.Y != TileEntity.yCoord ||
                newLocation.Z != TileEntity.zCoord) {
            // Check if we can move to the new spot
            if (Blaze.World.MinecraftWorld.isAirBlock(newLocation.X, newLocation.Y, newLocation.Z)) {
                CreateMinecraftSpark(newLocation);
                TileEntity.copyFrom(oldSpark);
                Blaze.World.MinecraftWorld.setBlockToAir(oldSpark.xCoord, oldSpark.yCoord, oldSpark.zCoord);
                TileEntity.animateClients(animationAction);
                return BFEProtos.BFESparkResponse.newBuilder()
                        .setResponseBool(true)
                        .build();
            } else {
                return BFEProtos.BFESparkResponse.newBuilder()
                        .setResponseBool(false)
                        .build();
            }
        } else {
            // Simply animate and return true
            TileEntity.animateClients(animationAction);
            return BFEProtos.BFESparkResponse.newBuilder()
                    .setResponseBool(true)
                    .build();
        }
    }

    public Location3D getLocation() {
        return new Location3D(TileEntity.xCoord, TileEntity.yCoord, TileEntity.zCoord);
    }

    private static SparkTileEntity.AnimationTypes toAnimationType(BFEProtos.BFESparkCommand.CommandType command) {
        switch (command) {
            case MOVE_FORWARD: return SparkTileEntity.AnimationTypes.Forward;
            case MOVE_BACKWARD: return SparkTileEntity.AnimationTypes.Backward;
            case MOVE_UP: return SparkTileEntity.AnimationTypes.Up;
            case MOVE_DOWN: return SparkTileEntity.AnimationTypes.Down;
            case TURN_LEFT: return SparkTileEntity.AnimationTypes.TurnLeft;
            case TURN_RIGHT: return SparkTileEntity.AnimationTypes.TurnRight;
        }

        return null;
    }

    private void CreateMinecraftSpark(Location3D location) {
        if(Blaze.World.MinecraftWorld.setBlock(location.X, location.Y, location.Z, Flame.sparkBlock)) {
            TileEntity = (SparkTileEntity)Blaze.World.MinecraftWorld
                    .getTileEntity(location.X, location.Y, location.Z);
            System.out.println("Spark created: " + TileEntity.toString());
        } else {
            System.err.println("Failed to create spark at: " + location.toString());
        }
    }

}
