package com.thilenius.flame;

import java.util.Random;

import com.thilenius.flame.service.thrift.FlameServiceServer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "flame", name = "Flame", version = "0.0.1")
public class Flame {

	// Notes:
	// Getting access to a world: MinecraftServer.getServer().worldServers[0].
	
	@Instance(value = "flame")
	public static Flame instance;

	@SidedProxy(clientSide = "com.thilenius.flame.client.ClientProxy", serverSide = "com.thilenius.flame.CommonProxy")
	public static CommonProxy proxy;
	
	public static FlameServiceServer FlameService;

	public static Block sparkBlock;
	
	public static Item spark;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Register Mobs
		registerEntity(SparkEntity.class, "sparkEntity");
		
		// Register Items
		spark = new SparkItem();
		GameRegistry.registerItem(spark, "flameSpark");
		
		// Register Blocks
		sparkBlock = new SparkBlock(Material.ground);
		GameRegistry.registerBlock(sparkBlock, "sparkBlock");
		
		// Register Events
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		
		// Register Rendered
		proxy.registerRenderers();
		
		// Bring a Thrift FlameServiceServer online
		FlameService = new FlameServiceServer();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
	
	@SubscribeEvent
	public void onRenderTick(ServerTickEvent event) {
		// On Server Tick (Working)
	}
	
//	@SubscribeEvent
//	public void onPlayerClick(PlayerInteractEvent event) {
//		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
//			System.out.println("Spawning...");
//			event.world.setBlock(event.x, event.y, event.z, Block.getBlockFromName("gold_block"));
//		}
//	}
	
	public static void registerEntity(Class entityClass, String name)
	{
		int entityID = EntityRegistry.findGlobalUniqueEntityId();
		long seed = name.hashCode();
		Random rand = new Random(seed);
		int primaryColor = rand.nextInt() * 16777215;
		int secondaryColor = rand.nextInt() * 16777215;

		EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
		EntityRegistry.registerModEntity(entityClass, name, entityID, instance, 64, 1, true);
		EntityList.entityEggs.put(Integer.valueOf(entityID), new EntityList.EntityEggInfo(entityID, primaryColor, secondaryColor));
	}
	
//	@SubscribeEvent
//	public void onRenderTick(ClientTickEvent event) {
//		System.out.println("Client Tick");
//	}
}
