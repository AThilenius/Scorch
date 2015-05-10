package com.thilenius.flame;

import com.thilenius.flame.jumbotron.JumboRenderer;
import com.thilenius.flame.jumbotron.JumboTileEntity;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
        Flame.Globals.IsClient = true;
        ClientRegistry.bindTileEntitySpecialRenderer(JumboTileEntity.class, new JumboRenderer());
	}
}
