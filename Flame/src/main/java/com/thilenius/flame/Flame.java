package com.thilenius.flame;

import com.thilenius.flame.service.thrift.FlameServiceServer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
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
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.event.world.WorldEvent;

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
		// Register Blocks
		sparkBlock = new SparkBlock();
		GameRegistry.registerBlock(sparkBlock, "sparkBlock");
		GameRegistry.registerTileEntity(SparkTileEntity.class, "SparkTileEntity");

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

		
	}

	@SubscribeEvent
	public void onRenderTick(ServerTickEvent event) {
		// On Server Tick (Working)
	}

    @SubscribeEvent
    public void onWorldLoad(WorldEvent event) {
        System.out.println("World Event");
    }

	@EventHandler
	public void onServerStart(FMLServerStartedEvent event) {
        System.out.println("Removing Tile Entities");

        for (WorldServer worldSrv : MinecraftServer.getServer().worldServers) {
            for (int i = 0; i < worldSrv.loadedTileEntityList.size(); i++) {
                TileEntity entity = (TileEntity) worldSrv.loadedEntityList.get(i);

                if (entity instanceof SparkTileEntity) {
                    worldSrv.removeTileEntity(entity.xCoord, entity.yCoord, entity.zCoord);
                }
            }
        }
    }

	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent event) {

		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			if (event.isCanceled()) {
				return;
			}
			
//			// Remove all Spark Tile Entities
//			for(int i = 0; i < MinecraftServer.getServer().worldServers.length; i++) {
//				WorldServer server = MinecraftServer.getServer().worldServers[i];
//				for (int j = 0; i < server.loadedTileEntityList.size(); i++) {
//					Object tei = server.loadedTileEntityList.get(j);
//					if (tei instanceof SparkTileEntity) {
//						SparkTileEntity iterEntity = (SparkTileEntity) tei;
//						System.out.println("Removing Spark at: " + iterEntity.xCoord + ", " + iterEntity.yCoord + ", " + iterEntity.zCoord);
//						server.removeTileEntity(iterEntity.xCoord, iterEntity.yCoord, iterEntity.zCoord);
//					}
//				}
//			}

			//			TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
			//			SparkTileEntity spark = (SparkTileEntity) te;
			//			
			//			System.out.println("====== Flame::onPlayerClick: " + event.x + ", " + event.y + ", " + event.z);
			//
			//			System.out.println("All sparks before:");
			//			for (int i = 0; i < event.world.loadedTileEntityList.size(); i++) {
			//				Object tei = event.world.loadedTileEntityList.get(i);
			//				if (tei instanceof SparkTileEntity) {
			//					SparkTileEntity iterEntity = (SparkTileEntity) tei;
			//					System.out.println("   Spark at: " + iterEntity.xCoord + ", " + iterEntity.yCoord + ", " + iterEntity.zCoord);
			//				}
			//			}
			//			
			//			if (spark != null)
			//			{
			//	        	event.world.removeTileEntity(event.x, event.y, event.z);
			//				event.world.setBlock(event.x, event.y, event.z, Blocks.air, 0, 3);
			//				spark.invalidate();
			//	        	event.world.updateEntities();
			//	        	event.setCanceled(true);
			//			}
			//			
			//			System.out.println("All sparks after:");
			//			for (int i = 0; i < event.world.loadedTileEntityList.size(); i++) {
			//				Object tei = event.world.loadedTileEntityList.get(i);
			//				if (tei instanceof SparkTileEntity) {
			//					SparkTileEntity iterEntity = (SparkTileEntity) tei;
			//					System.out.println("   Spark at: " + iterEntity.xCoord + ", " + iterEntity.yCoord + ", " + iterEntity.zCoord);
			//				}
			//			}

			if (event.world.getTileEntity(event.x, event.y, event.z) == null) {
				event.world.setBlock(event.x, event.y, event.z - 2, sparkBlock);
			}

		}
	}

	//	@SubscribeEvent
	//	public void onRenderTick(ClientTickEvent event) {
	//		System.out.println("Client Tick");
	//	}
}
