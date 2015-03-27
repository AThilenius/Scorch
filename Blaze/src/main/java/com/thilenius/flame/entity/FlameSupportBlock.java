package com.thilenius.flame.entity;

import com.thilenius.flame.Flame;
import com.thilenius.flame.spark.SparkTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Alec on 3/26/15.
 */
public class FlameSupportBlock  extends Block implements ITileEntityProvider
{
    private Class<?> m_tileEntityType;
    private boolean m_useCustomRenderer;

    public FlameSupportBlock (Class<? extends FlameTileEntity> tileEntityType, boolean useCustomRenderer) {
        super(Material.ground);
        this.isBlockContainer = true;

        m_tileEntityType = tileEntityType;
        m_useCustomRenderer = useCustomRenderer;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_)
    {
        // Pre-Place event
        return super.onBlockPlaced(world, x, y, z, p_149660_5_, p_149660_6_, p_149660_7_, p_149660_8_, p_149660_9_);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        TileEntity tileEntity = null;
        try {
            tileEntity = (TileEntity) m_tileEntityType.getConstructor(new Class[]{}).newInstance();
        } catch (NoSuchMethodException e) {
            System.out.println("Failed to create Flame Tile Entity of type " + m_tileEntityType.getName());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println("Failed to create Flame Tile Entity of type " + m_tileEntityType.getName());
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println("Failed to create Flame Tile Entity of type " + m_tileEntityType.getName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("Failed to create Flame Tile Entity of type " + m_tileEntityType.getName());
            e.printStackTrace();
        }
        return tileEntity;
    }

    @Override
    public int getRenderType() {
        return m_useCustomRenderer ? -1 : 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return !m_useCustomRenderer;
    }

    public void onBlockAdded(World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof FlameTileEntity) {
            ((FlameTileEntity) tileEntity).onBlockAdded(world, x, y, z);
        }
        super.onBlockAdded(world, x, y, z);
    }

    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
        p_149749_1_.removeTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
    }

    public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_)
    {
        super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_);
        TileEntity tileentity = p_149696_1_.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_);
        return tileentity != null ? tileentity.receiveClientEvent(p_149696_5_, p_149696_6_) : false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int metadata, float what, float these, float are) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }
        //code to open gui explained later
        player.openGui(Flame.instance, 0, world, x, y, z);
        return true;
    }
}
