package com.thilenius.flame.entity;

import com.thilenius.flame.Flame;
import com.thilenius.flame.utilities.types.LocationF3D;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class FlameTileEntity extends TileEntity {

    // Refection based setup method for subclasses of FlameTileEntity
    public static void setup(Class<? extends FlameTileEntity> entityClass) {
        Method customRenderer = null;

        // Get it's definition
        FlameEntityDefinition definition = entityClass.getAnnotation(FlameEntityDefinition.class);

        // Try to find a custom renderer marked with @FlameCustomRenderer
        for (final Method method : entityClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(FlameCustomRenderer.class)) {
                customRenderer = method;
                break;
            }
        }

        // Construct the Dummy Block
        FlameSupportBlock flameBlock = new FlameSupportBlock(
                entityClass.asSubclass(FlameTileEntity.class), customRenderer != null);
        flameBlock.setHardness(definition.hardness());
        flameBlock.setResistance(definition.resistance());
        flameBlock.setBlockName(definition.name());
        flameBlock.setBlockTextureName(definition.blockTextureName());
        flameBlock.setBlockBounds(definition.bounds()[0], definition.bounds()[1], definition.bounds()[2],
                definition.bounds()[3], definition.bounds()[4], definition.bounds()[5]);
        flameBlock.setCreativeTab(CreativeTabs.tabMisc);

        // Register the block and the Tile Entity with Minecraft
        GameRegistry.registerBlock(flameBlock, definition.name() + "Block");
        GameRegistry.registerTileEntity(entityClass.asSubclass(TileEntity.class), definition.name() + "TileEntity");
        LanguageRegistry.addName(flameBlock, definition.name());

        // Register the custom renderer if we have one
        if (customRenderer != null && Flame.Globals.IsClient) {
            // Register it with Minecraft
            ClientRegistry.bindTileEntitySpecialRenderer(
                    entityClass.asSubclass(TileEntity.class),
                    new FlameSupportRenderer(customRenderer));
        }

        // Finally invoke it's initializer
        for (final Method method : entityClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(FlameEntityInitializer.class)) {
                try {
                    method.invoke(null, flameBlock);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) { }
                break;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        registerEntityWithRestService();
    }

    protected void onBlockAdded(World world, int x, int y, int z) {
        registerEntityWithRestService();
    }

    protected void registerEntityWithRestService() {

    }

    protected void unregisterEntityWithRestService() {

    }

}