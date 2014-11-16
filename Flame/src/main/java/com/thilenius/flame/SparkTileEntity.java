package com.thilenius.flame;

import java.util.ArrayList;

import com.thilenius.utilities.types.Location3D;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class SparkTileEntity extends TileEntity {

	private int offset;
	public float displace;
	
	static
    {
        addMapping(SparkTileEntity.class, "Spark");
    }
	
	public SparkTileEntity() {
	}
	
//	public void writeToNBT(NBTTagCompound var1)
//	{
//		//var1.setInteger("theNameOfTheVariable", this.whateverYourSaving);
//		super.writeToNBT(var1);
//	}
//
//	public void readFromNBT(NBTTagCompound var1)
//	{
//		//this.whateverYourSaving = var1.getInteger("theNameOfTheVariable");
//		super.readFromNBT(var1);
//	}

}