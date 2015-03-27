package com.thilenius.flame;

import com.thilenius.flame.commands.BlazeCommandHandler;
import com.thilenius.flame.commands.HomeCommandHandler;
import com.thilenius.flame.entity.FlameSupportGuiHandler;
import com.thilenius.flame.entity.FlameTileEntity;
import com.thilenius.flame.http.RestHttpServer;
import com.thilenius.flame.jumbotron.JumboBlock;
import com.thilenius.flame.jumbotron.JumboTileEntity;
import com.thilenius.flame.spark.SparkTileEntity;
import com.thilenius.flame.statement.StatementBase;
import com.thilenius.flame.transaction.Transaction;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashSet;

// Note: Program Arguments: -username=athilenius

@Mod(modid = "flame", name = "Flame", version = "0.0.1")
public class Flame {

	@Instance(value = "flame")
	public static Flame instance;

	@SidedProxy(clientSide = "com.thilenius.flame.ClientProxy", serverSide = "com.thilenius.flame.CommonProxy")
	public static CommonProxy proxy;

    public static GlobalData Globals = new GlobalData();

    public static World World;
    public static RestHttpServer RestServer;

    private static HashSet<Entity> m_protectedEntities = new HashSet<Entity>();
    private static boolean m_hasStartTickBeenSent;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
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
        FlameTileEntity.setup(SparkTileEntity.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new FlameSupportGuiHandler());
	}

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        MinecraftServer server = MinecraftServer.getServer();
        ICommandManager command = server.getCommandManager();
        ServerCommandManager manager = (ServerCommandManager) command;
        manager.registerCommand(new HomeCommandHandler());
        manager.registerCommand(new BlazeCommandHandler());
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
    }

    @SubscribeEvent
    public void entityJoinEvent(EntityJoinWorldEvent event) {
    }

	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
        if (!m_hasStartTickBeenSent) {
            m_hasStartTickBeenSent = true;
            this.World = MinecraftServer.getServer().worldServers[0];
            this.RestServer = new RestHttpServer();
            new Thread(this.RestServer).start();
        }

        for (Transaction transaction : RestServer.getAllWaiting(false)) {
            for (StatementBase statementBase : transaction.statementBases) {
                StatementBase childStatementBase = StatementBase.getSubclass(statementBase);
                childStatementBase.Execute();
            }
        }
	}

    @SubscribeEvent
    public void onWorldLoad(WorldEvent event) {
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {

    }

    @SubscribeEvent
    public void onNameFormat(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {

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
                ((EntityPlayer)livingHurtEvent.entity).getFoodStats().addStats(20, 1.0f);

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
            ((EntityPlayer)livingHurtEvent.entity).getFoodStats().addStats(20, 1.0f);
        }

    }

	//	@SubscribeEvent
	//	public void onServerTick(ClientTickEvent event) {
	//		System.out.println("Client Tick");
	//	}
}
