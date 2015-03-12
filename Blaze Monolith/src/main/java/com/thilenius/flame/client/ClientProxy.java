package com.thilenius.flame.client;

import com.thilenius.flame.CommonProxy;
import com.thilenius.flame.jumbotron.JumboRenderer;
import com.thilenius.flame.jumbotron.JumboTileEntity;
import com.thilenius.flame.spark.SparkRenderer;
import com.thilenius.flame.spark.SparkTileEntity;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(SparkTileEntity.class, new SparkRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(JumboTileEntity.class, new JumboRenderer());
	}
}
