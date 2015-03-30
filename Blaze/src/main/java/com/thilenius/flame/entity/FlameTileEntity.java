package com.thilenius.flame.entity;

import com.thilenius.flame.Flame;
import com.thilenius.flame.rest.StatementDispatch;
import com.thilenius.flame.utilities.types.CoolDownTimer;
import com.thilenius.flame.utilities.types.Location3D;
import com.thilenius.flame.utilities.types.LocationF3D;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
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

    protected CoolDownTimer m_coolDown;

    private String m_name;
    private String m_playerName;

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

    public void postNamedInit() {
        registerEntityWithRestServer();
    }

    // Called when writing to disk or network socket
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        // Only save named tile entities
        if (m_name != null) {
            nbt.setString("name", m_name);
            nbt.setString("playerName", m_playerName);
        }
    }

    // Called when loaded from disk or network socket
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        m_name = nbt.getString("name");
        m_playerName = nbt.getString("playerName");

        // Only load tile entices that are named
        if (m_name != null && !m_name.isEmpty()) {
            postNamedInit();
        }

    }

    public void onBlockDestroyedByPlayer() {
        unregisterEntityWithRestService();
    }


    public void onBlockDestroyedByExplosion() {
        unregisterEntityWithRestService();
    }

    public void coolDown(float seconds) {
        m_coolDown = new CoolDownTimer(seconds);
    }

    public Boolean isOnCoolDown() {
        if (m_coolDown == null) {
            return false;
        }

        return m_coolDown.isOnCoolDown();
    }

    // Called by the Block class
    protected void onBlockAdded(World world, int x, int y, int z) {

    }

    protected void registerEntityWithRestServer() {
        for (final Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(FlameActionPath.class)) {
                FlameActionPath actionPath = method.getAnnotation(FlameActionPath.class);
                Flame.Globals.RestServer.Dispatch.registerActionHandler(this, actionPath, method);
            }
        }
    }

    protected void unregisterEntityWithRestService() {
        for (final Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(FlameActionPath.class)) {
                FlameActionPath actionPath = method.getAnnotation(FlameActionPath.class);
                Flame.Globals.RestServer.Dispatch.unregisterPath(this, actionPath);
            }
        }
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }


    public String getPlayerName() {
        return m_playerName;
    }

    public void setPlayerName(String playerName) {
        this.m_playerName = playerName;
    }
}