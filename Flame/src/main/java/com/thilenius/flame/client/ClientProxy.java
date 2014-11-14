package com.thilenius.flame.client;

import net.minecraft.client.model.ModelBiped;

import com.thilenius.flame.CommonProxy;
import com.thilenius.flame.SparkRenderer;
import com.thilenius.flame.SparkTileEntity;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(SparkTileEntity.class, new SparkRenderer());
	}
}
