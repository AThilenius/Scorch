package com.thilenius.flame.spark;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SparkBlock extends BlockContainer {
	public SparkBlock () {
        super(Material.ground);
        this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.8F, 0.8F);
        this.setCreativeTab(CreativeTabs.tabMisc);
	}

//	@Override
//	public int onBlockPlaced(World world, int x, int y, int z, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_)
//    {
//		System.out.println("==== onBlockPlaced: " + x + ", " + y + ", " + z);
//		return super.onBlockPlaced(world, x, y, z, p_149660_5_, p_149660_6_, p_149660_7_, p_149660_8_, p_149660_9_);
//    }

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		SparkTileEntity spark = new SparkTileEntity();
		return spark;
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