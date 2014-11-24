package com.thilenius.flame.jumbotron;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Alec on 11/22/14.
 */
public class JumboBlock extends BlockContainer {

    public JumboBlock () {
        super(Material.ground);
        this.setBlockBounds(-8.0F, 0.0F, 0.2F, 8.0F, 0.8F, 0.8F);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        JumboTileEntity jumbo = new JumboTileEntity();
        return jumbo;
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
