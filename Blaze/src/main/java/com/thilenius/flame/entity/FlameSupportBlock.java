package com.thilenius.flame.entity;

import com.thilenius.flame.Flame;
import com.thilenius.flame.spark.SparkTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Alec on 3/26/15.
 */
public class FlameSupportBlock extends BlockContainer {

    private Class<?> m_tileEntityType;
    private boolean m_useCustomRenderer;

    public FlameSupportBlock (Class<? extends FlameTileEntity> tileEntityType, boolean useCustomRenderer) {
        super(Material.ground);
        m_tileEntityType = tileEntityType;
        m_useCustomRenderer = useCustomRenderer;
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
}
