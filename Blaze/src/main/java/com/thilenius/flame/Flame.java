package com.thilenius.flame;

import com.thilenius.blaze.Blaze;
import com.thilenius.flame.commands.HomeCommandHandler;
import com.thilenius.flame.jumbotron.JumboBlock;
import com.thilenius.flame.jumbotron.JumboTileEntity;
import com.thilenius.flame.spark.SparkBlock;
import com.thilenius.flame.spark.SparkTileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.lwjgl.Sys;

import java.util.HashSet;

// Note: Program Arguments: -username=athilenius

@Mod(modid = "flame", name = "Flame", version = "0.0.1")
public class Flame {

    public static Blaze BlazeInstance;

	// Notes:
	// Getting access to a world: MinecraftServer.getServer().worldServers[0].

	@Instance(value = "flame")
	public static Flame instance;

	@SidedProxy(clientSide = "com.thilenius.flame.client.ClientProxy", serverSide = "com.thilenius.flame.CommonProxy")
	public static CommonProxy proxy;

	public static Block sparkBlock;
    public static Block jumboBlock;
	public static Item spark;

    private static HashSet<Entity> m_protectedEntities = new HashSet<Entity>();
    private static boolean m_hasStartTickBeenSent;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Register Blocks
		sparkBlock = new SparkBlock();
		GameRegistry.registerBlock(sparkBlock, "sparkBlock");
		GameRegistry.registerTileEntity(SparkTileEntity.class, "SparkTileEntity");

        jumboBlock = new JumboBlock(Material.ground);
        GameRegistry.registerBlock(jumboBlock, "jumboBlock");
        GameRegistry.registerTileEntity(JumboTileEntity.class, "jumboTileEntity");

		// Register Events
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);

		// Register Rendered
		proxy.registerRenderers();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        MinecraftServer server = MinecraftServer.getServer();
        ICommandManager command = server.getCommandManager();
        ServerCommandManager manager = (ServerCommandManager) command;
        manager.registerCommand(new HomeCommandHandler());
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        BlazeInstance = new Blaze(this);
    }

    // Fuck this piece of shit Minecraft code!!!
    @SubscribeEvent
    public void entityJoinEvent(EntityJoinWorldEvent event) {
//        if ((Object)event.entity instanceof SparkTileEntity) {
//            event.setCanceled(true);
//        }
    }

	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
        if (!m_hasStartTickBeenSent && BlazeInstance != null) {
            m_hasStartTickBeenSent = true;
            BlazeInstance.onStart();
        }

		Blaze.onTick();
	}

    @SubscribeEvent
    public void onWorldLoad(WorldEvent event) {
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // On Player Join
        if (BlazeInstance != null) {
            BlazeInstance.onPlayerJoin(event);
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        // On Player Leave
        if (BlazeInstance != null) {
            BlazeInstance.onPlayerLeave(event);
        }
    }

    @SubscribeEvent
    public void onNameFormat(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {
        if (BlazeInstance != null) {
            event.displayname = BlazeInstance.onFormatName(event.username);
        }
    }

	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent event) {
	}

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent breakEvent) {
        if (breakEvent.y >= 199) {
            breakEvent.setExpToDrop(0);
            breakEvent.setCanceled(true);
            breakEvent.world.setBlockToAir(breakEvent.x, breakEvent.y, breakEvent.z);
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPlayerFall(LivingFallEvent livingFallEvent) {
        if (livingFallEvent.distance > 4.0 && livingFallEvent.entity instanceof EntityPlayer &&
                livingFallEvent.entity.posY + livingFallEvent.distance > 199) {
            m_protectedEntities.add(livingFallEvent.entity);
        }
    }

//    @SubscribeEvent
//    public void onEntitySpawn (LivingSpawnEvent livingSpawnEvent) {
//        if (livingSpawnEvent.y >= 200 && !(livingSpawnEvent.entity instanceof EntityPlayer)) {
//            livingSpawnEvent.setResult(Event.Result.DENY);
//        }
//    }

    @SubscribeEvent
    public void onEntitySpawn (LivingSpawnEvent.CheckSpawn checkSpawnEvent) {
        if (checkSpawnEvent.y >= 200 && checkSpawnEvent.entity instanceof EntityLivingBase &&
                !(checkSpawnEvent.entityLiving instanceof EntityPlayer)) {
            checkSpawnEvent.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn (PlayerEvent.PlayerRespawnEvent playerRespawnEvent) {
        playerRespawnEvent.player.setPositionAndUpdate(0, 202, 0);
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPlayerHurt(LivingHurtEvent livingHurtEvent) {
        if (livingHurtEvent.entity instanceof EntityPlayer) {

            // Prevent starvation above 199
            if (livingHurtEvent.entity.posY >= 199 && livingHurtEvent.source == DamageSource.starve) {
                livingHurtEvent.setCanceled(true);
                ((EntityPlayer)livingHurtEvent.entity).getFoodStats().setFoodLevel(10);

            } else if (livingHurtEvent.source == DamageSource.fall &&
                    m_protectedEntities.contains(livingHurtEvent.entity) ) {
                // Prevent fall damage when 'falling from heaven'

                livingHurtEvent.setCanceled(true);
                m_protectedEntities.remove(livingHurtEvent.entity);

            } else if (livingHurtEvent.entity.posY >= 199) {
                // Prevent all damage above 199
                livingHurtEvent.setCanceled(true);
            }

        }



        if (livingHurtEvent.source == DamageSource.starve) {
            ((EntityPlayer)livingHurtEvent.entity).getFoodStats().setFoodLevel(20);
        }

    }

	//	@SubscribeEvent
	//	public void onServerTick(ClientTickEvent event) {
	//		System.out.println("Client Tick");
	//	}
}
