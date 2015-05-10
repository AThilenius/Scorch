package com.thilenius.flame.jumbotron;

import com.thilenius.blaze.Blaze;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Alec on 11/22/14.
 */
public class JumboBlock extends BlockContainer {

    public JumboBlock (Material material) {
        super(material);
        this.setBlockBounds(-14.0F, 0.0F, 0.0F, 0.0F, 2.0F, 1.0F);
        this.setBlockName("Jumbotron");
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new JumboTileEntity();
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
