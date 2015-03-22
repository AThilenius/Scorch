package com.thilenius.flame.statement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thilenius.flame.Flame;
import com.thilenius.flame.utilities.types.Location3D;
import net.minecraft.tileentity.TileEntity;

public class SendBlockMessage extends StatementBase {

    private static ObjectMapper m_jsonMapper = new ObjectMapper();
    private Location3D m_blockLocation;
    private JsonNode m_message;

    @Override
    protected StatementBase fromBaseJson (StatementBase statementBase) {
        SendBlockMessage sendBlockMessage = new SendBlockMessage();
        String location = statementBase.SArguments.get("location").asText();
        m_blockLocation = Location3D.fromString(location);
        m_message = statementBase.SArguments.get("message");

        if (m_blockLocation == null || m_message == null) {
            return null;
        }

        return sendBlockMessage;
    }

    @Override
    public void Execute() {
        TileEntity tileEntity = Flame.World.getTileEntity(m_blockLocation.X, m_blockLocation.Y, m_blockLocation.Z);
        if (tileEntity != null && tileEntity instanceof IBlockMessageHandler) {
            IBlockMessageHandler handler = (IBlockMessageHandler) tileEntity;
            if (!handler.Handle(m_message)) {
                // Error
                System.out.println("Entity at SendBlockMessage's Location failed to handle statement.");
                return;
            }
        } else {
            // Error
            System.out.println("Failed to find a suitable recipient for the SendBlockMessage statement.");
            return;
        }
    }

    @Override
    public void Rollback() {
        // Cannot be rolled back
    }

}
