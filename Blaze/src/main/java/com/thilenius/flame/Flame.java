package com.thilenius.flame;

import com.thilenius.blaze.Blaze;
import com.thilenius.flame.spark.SparkBlock;
import com.thilenius.flame.spark.SparkTileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;

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
	public static Item spark;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Register Blocks
		sparkBlock = new SparkBlock();
		GameRegistry.registerBlock(sparkBlock, "sparkBlock");
		GameRegistry.registerTileEntity(SparkTileEntity.class, "SparkTileEntity");

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
    public void serverStarted(FMLServerStartedEvent event) {
        BlazeInstance = new Blaze(this);
    }

    // Fuck this piece of shit Minecraft/Forge code!!!
    @SubscribeEvent
    public void entityJoinEvent(EntityJoinWorldEvent event) {
//        if ((Object)event.entity instanceof SparkTileEntity) {
//            event.setCanceled(true);
//        }
    }

	@SubscribeEvent
	public void onRenderTick(ServerTickEvent event) {
		Blaze.onTick();
	}

    @SubscribeEvent
    public void onWorldLoad(WorldEvent event) {
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // On Player Join
        BlazeInstance.onPlayerJoin(event);
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        // On Player Leave
        BlazeInstance.onPlayerLeave(event);
    }

    @SubscribeEvent
    public void onNameFormat(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {
        event.displayname = BlazeInstance.onFormatName(event.username);
    }

	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent event) {
	}

	//	@SubscribeEvent
	//	public void onRenderTick(ClientTickEvent event) {
	//		System.out.println("Client Tick");
	//	}
}
