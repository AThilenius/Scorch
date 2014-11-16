package com.thilenius.blaze.spark;

import com.thilenius.blaze.BlazeWorld;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.flame.Flame;
import com.thilenius.flame.SparkTileEntity;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.tileentity.TileEntity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Alec on 11/15/14.
 */
public class BlazeSpark {

    private SparkTileEntity m_sparkTileEntity;

    public BlazeSpark(BlazeWorld world, Location3D location) {
        System.out.println("Trying to set block to SPARK at " + location.toString());
        if(world.MinecraftWorld.setBlock(location.X, location.Y, location.Z, Flame.sparkBlock)) {
            System.out.println("Seems to have worked. Trying to get the TileEntity");
            m_sparkTileEntity = (SparkTileEntity)world.MinecraftWorld.getTileEntity(location.X, location.Y, location.Z);
        }
    }

    public BFEProtos.BFESparkResponse Handle (BFEProtos.BFESparkCommand.CommandType commandType) {

        BFEProtos.BFESparkResponse response;

        switch(commandType) {
            case MOVE_FORWARD:
                System.out.println("Moving Spark forward, at " + m_sparkTileEntity.xCoord + ", "
                        + m_sparkTileEntity.yCoord + ", " + m_sparkTileEntity.zCoord + ".");
                response = BFEProtos.BFESparkResponse.newBuilder()
                        .setResponseBool(true)
                        .build();
                break;
            case MOVE_BACKWARD:
                throw new NotImplementedException();
            case MOVE_UP:
                throw new NotImplementedException();
            case MOVE_DOWN:
                throw new NotImplementedException();
            case TURN_LEFT:
                throw new NotImplementedException();
            case TURN_RIGHT:
                throw new NotImplementedException();
            default:
                throw new NotImplementedException();
        }

        return response;
    }

}
