package com.thilenius.blaze.spark;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.BlazeWorld;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.flame.Flame;
import com.thilenius.flame.SparkTileEntity;
import com.thilenius.utilities.types.Location3D;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Alec on 11/15/14.
 */
public class BlazeSpark {

    public SparkTileEntity m_sparkTileEntity;

    public BlazeSpark(Location3D location) {
        CreateMinecraftSpark(location);
    }

    public BFEProtos.BFESparkResponse Handle (BFEProtos.BFESparkCommand.CommandType commandType) {
        BFEProtos.BFESparkResponse response;

        SparkTileEntity oldSpark = null;

        switch(commandType) {
            case MOVE_FORWARD:
                // Create a new spark first
                oldSpark = m_sparkTileEntity;
                CreateMinecraftSpark(m_sparkTileEntity.getBlockFromAction(SparkTileEntity.AnimationTypes.Forward));
                m_sparkTileEntity.animateClients(SparkTileEntity.AnimationTypes.Forward);
                response = BFEProtos.BFESparkResponse.newBuilder()
                        .setResponseBool(true)
                        .build();
                break;
            case MOVE_BACKWARD:
                // Create a new spark first
                oldSpark = m_sparkTileEntity;
                CreateMinecraftSpark(m_sparkTileEntity.getBlockFromAction(SparkTileEntity.AnimationTypes.Backward));
                m_sparkTileEntity.animateClients(SparkTileEntity.AnimationTypes.Backward);
                response = BFEProtos.BFESparkResponse.newBuilder()
                        .setResponseBool(true)
                        .build();
                break;
            case MOVE_UP:
                // Create a new spark first
                oldSpark = m_sparkTileEntity;
                CreateMinecraftSpark(m_sparkTileEntity.getBlockFromAction(SparkTileEntity.AnimationTypes.Up));
                m_sparkTileEntity.animateClients(SparkTileEntity.AnimationTypes.Up);
                response = BFEProtos.BFESparkResponse.newBuilder()
                        .setResponseBool(true)
                        .build();
                break;
            case MOVE_DOWN:
                // Create a new spark first
                oldSpark = m_sparkTileEntity;
                CreateMinecraftSpark(m_sparkTileEntity.getBlockFromAction(SparkTileEntity.AnimationTypes.Down));
                m_sparkTileEntity.animateClients(SparkTileEntity.AnimationTypes.Down);
                response = BFEProtos.BFESparkResponse.newBuilder()
                        .setResponseBool(true)
                        .build();
                break;
            case TURN_LEFT:
                m_sparkTileEntity.animateClients(SparkTileEntity.AnimationTypes.TurnLeft);
                response = BFEProtos.BFESparkResponse.newBuilder()
                        .setResponseBool(true)
                        .build();
                break;
            case TURN_RIGHT:
                m_sparkTileEntity.animateClients(SparkTileEntity.AnimationTypes.TurnRight);
                response = BFEProtos.BFESparkResponse.newBuilder()
                        .setResponseBool(true)
                        .build();
                break;
            default:
                throw new NotImplementedException();
        }


        // Need to remove old spark?
        switch (commandType) {
            case MOVE_FORWARD:
            case MOVE_BACKWARD:
            case MOVE_UP:
            case MOVE_DOWN:
                Blaze.World.MinecraftWorld.setBlockToAir(oldSpark.xCoord, oldSpark.yCoord, oldSpark.zCoord);
                break;
        }

        return response;
    }

    private void CreateMinecraftSpark(Location3D location) {
        if(Blaze.World.MinecraftWorld.setBlock(location.X, location.Y, location.Z, Flame.sparkBlock)) {
            m_sparkTileEntity = (SparkTileEntity)Blaze.World.MinecraftWorld
                    .getTileEntity(location.X, location.Y, location.Z);
            System.out.println("Spark created: " + m_sparkTileEntity.toString());
        } else {
            System.err.println("Failed to create spark at: " + location.toString());
        }
    }

}
