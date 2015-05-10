package com.thilenius.flame.entity;

import com.thilenius.flame.utilities.types.LocationF3D;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Pass through renderer for custom tile entities.
 */
public class FlameSupportRenderer extends TileEntitySpecialRenderer {

    private Method m_renderingMethod;

    public FlameSupportRenderer(Method renderingMethod) {
        m_renderingMethod = renderingMethod;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float deltaTime) {
        if (m_renderingMethod == null) {
            return;
        }

        try {
            m_renderingMethod.invoke(null, this, (FlameTileEntity) te, new LocationF3D((float)x, (float)y, (float)z));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            m_renderingMethod = null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            m_renderingMethod = null;
        }
    }

    @Override
    public void bindTexture(ResourceLocation resourceLocation) {
        super.bindTexture(resourceLocation);
    }

    public void drawLabel(String label, LocationF3D entityLocation) {
        // TODO
    }
}