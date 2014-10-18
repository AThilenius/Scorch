package com.thilenius.flame.client;

import net.minecraft.client.model.ModelBiped;

import com.thilenius.flame.CommonProxy;
import com.thilenius.flame.SparkEntity;
import com.thilenius.flame.SparkEntityRenderer;
import com.thilenius.flame.SparkRenderer;
import com.thilenius.flame.SparkTileEntity;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
		// This is for rendering entities and so forth later on
		RenderingRegistry.registerEntityRenderingHandler(SparkEntity.class, new SparkEntityRenderer(new ModelBiped(), 0.5F));
		ClientRegistry.bindTileEntitySpecialRenderer(SparkTileEntity.class, new SparkRenderer());
	}
}
