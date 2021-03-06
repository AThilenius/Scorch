package com.thilenius.flame.entity;

import com.thilenius.flame.Flame;
import com.thilenius.flame.utilities.types.CountdownTimer;
import com.thilenius.flame.utilities.types.Location3D;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

public class FlameTileEntity extends TileEntity {

    protected CountdownTimer m_coolDownTimer;

    private String m_name;
    private String m_playerName;
    private HashMap<String, FlameActionTarget> m_actionTargetsByName = new HashMap<String, FlameActionTarget>();

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
            }
        }
    }

    public void postNamedInit() {
        registerEntityWithRestServer();
    }

    public void copyFrom(Object obj) {
        FlameTileEntity flameTileEntity = (FlameTileEntity) obj;
        if (flameTileEntity == null) {
            return;
        }

        m_name = flameTileEntity.m_name;
        m_playerName = flameTileEntity.m_playerName;
        m_coolDownTimer = flameTileEntity.m_coolDownTimer;
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
        m_coolDownTimer = new CountdownTimer(seconds);
    }

    public Boolean isOnCoolDown() {
        if (m_coolDownTimer == null) {
            return false;
        }

        return !m_coolDownTimer.hasElapsed();
    }

    // Called by the Block class
    protected void onBlockAdded(World world, int x, int y, int z) {

        // TODO: Need to make a support Item and invoke this there. Ideally before dropping the FlameTileEntity
//        FlameSupportGuiHandler.ActiveEntity = this;
//        FlameSupportGuiHandler.ActiveLocation = new Location3D(x, y, z);
//        Minecraft.getMinecraft().thePlayer.openGui(Flame.instance, 0, world, x, y, z);


        FlameSupportGuiHandler.ActiveEntity = this;
        FlameSupportGuiHandler.ActiveLocation = new Location3D(x, y, z);
        Minecraft.getMinecraft().thePlayer.openGui(Flame.instance, 0, world, x, y, z);
    }

    protected void registerEntityWithRestServer() {
        for (final Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(FlameActionPath.class)) {
                FlameActionPath actionPath = method.getAnnotation(FlameActionPath.class);
                FlameActionTarget target = new FlameActionTarget(this, actionPath, method, getName(), getPlayerName(),
                        new Location3D(xCoord, yCoord, zCoord));
                if (m_actionTargetsByName.containsKey(actionPath.value())) {
                    System.out.println("!! DUPLICATE ACTION TARGET FOUND !! [ " + actionPath.value() + " ]");
                }
                m_actionTargetsByName.put(actionPath.value(), target);
            }
        }
        Flame.Globals.EntityRegistry.register(this);
    }

    protected void unregisterEntityWithRestService() {
        m_actionTargetsByName.clear();
        Flame.Globals.EntityRegistry.unregister(this);
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

    public FlameActionTarget getActionTargetByName(String actionName) {
        return m_actionTargetsByName.get(actionName);
    }
}