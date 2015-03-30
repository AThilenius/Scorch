package com.thilenius.flame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thilenius.flame.rest.RestServer;
import net.minecraft.world.World;

public class GlobalData {
    public ObjectMapper JsonObjectMapper = new ObjectMapper();
    public Boolean IsClient = false;
    public net.minecraft.world.World World;
    public RestServer RestServer = null;
}
