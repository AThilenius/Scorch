package com.thilenius.flame;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SparkBlock extends BlockContainer 
{
	public SparkBlock (Material material) 
	{
        super(material);
        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 3.0F, 0.6F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new SparkTileEntity(); 
	}
    
    @Override
    public int getRenderType() {
            return -1;
    }
    
    //It's not a normal block, so you need this too.
    public boolean renderAsNormalBlock() {
            return false;
    }
    
}