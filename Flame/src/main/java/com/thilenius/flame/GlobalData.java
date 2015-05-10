package com.thilenius.flame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thilenius.flame.entity.FlameEntityRegistry;
import com.thilenius.flame.rest.RestServer;

public class GlobalData {
    public ObjectMapper JsonObjectMapper = new ObjectMapper();
    public FlameEntityRegistry EntityRegistry = new FlameEntityRegistry();
    public Boolean IsClient = false;
    public net.minecraft.world.World World;
    public RestServer RestServer = null;
}
