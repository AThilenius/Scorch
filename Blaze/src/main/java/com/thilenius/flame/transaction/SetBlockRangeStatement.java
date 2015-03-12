package com.thilenius.flame.transaction;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thilenius.flame.Flame;
import com.thilenius.flame.utilities.types.Location3D;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.io.IOException;

public class SetBlockRangeStatement extends Statement {
    private static ObjectMapper m_jsonMapper = new ObjectMapper();
    private Location3D m_fromLocation;
    private Location3D m_toLocation;
    private Location3D m_absStart;
    private Location3D m_size;
    private Block m_blockType;
    private Block[][][] m_oldBlockTypes;

    @Override
    protected Statement fromBaseJson (Statement statement) {
        SetBlockRangeStatement setBlockRangeStatement = new SetBlockRangeStatement();
        String range = statement.SArguments.get("range").asText();
        String blockType = statement.SArguments.get("block_type").asText();

        // Parse 2 locations
        String[] locations = range.split(":");
        if (locations.length != 2) {
            System.out.println("Failed to parse Args for SetBlockRangeStatement");
            return null;
        }
        setBlockRangeStatement.m_fromLocation = Location3D.fromString(locations[0]);
        setBlockRangeStatement.m_toLocation = Location3D.fromString(locations[1]);
        setBlockRangeStatement.m_absStart = new Location3D(
                Math.min(setBlockRangeStatement.m_fromLocation.X, setBlockRangeStatement.m_toLocation.X),
                Math.min(setBlockRangeStatement.m_fromLocation.Y, setBlockRangeStatement.m_toLocation.Y),
                Math.min(setBlockRangeStatement.m_fromLocation.Z, setBlockRangeStatement.m_toLocation.Z));
        setBlockRangeStatement.m_size = new Location3D(
                Math.abs(setBlockRangeStatement.m_fromLocation.X - setBlockRangeStatement.m_toLocation.X),
                Math.abs(setBlockRangeStatement.m_fromLocation.Y - setBlockRangeStatement.m_toLocation.Y),
                Math.abs(setBlockRangeStatement.m_fromLocation.Z - setBlockRangeStatement.m_toLocation.Z));

        // Parse Block type
        setBlockRangeStatement.m_blockType = (Block)Block.blockRegistry.getObject(blockType);

        // Data check
        if (setBlockRangeStatement.m_fromLocation == null || setBlockRangeStatement.m_toLocation == null
                || setBlockRangeStatement.m_blockType == null) {
            System.out.println("Failed to parse Args for SetBlockRangeStatement");
            return null;
        }

        // All is well
        return setBlockRangeStatement;
    }

    @Override
    public void Execute() {
        // Save old blocks
        saveSnapshot();

        // Load new ones
        for (int x = 0; x < m_size.X; x++) {
            for (int y = 0; y < m_size.Y; y++) {
                for (int z = 0; z < m_size.Z; z++) {
                    if (Flame.World.getBlock(m_absStart.X + x, m_absStart.Y + y, m_absStart.Z + z).getMaterial() !=
                            m_blockType.getMaterial()) {
                        Flame.World.setBlock(m_absStart.X + x, m_absStart.Y + y, m_absStart.Z + z, m_blockType);
                    }
                }
            }
        }
    }

    @Override
    public void Rollback() {
        // Set all blocks to old types
        for (int x = 0; x < m_size.X; x++) {
            for (int y = 0; y < m_size.Y; y++) {
                for (int z = 0; z < m_size.Z; z++) {
                    if (Flame.World.getBlock(m_absStart.X + x, m_absStart.Y + y, m_absStart.Z + z).getMaterial() !=
                            m_oldBlockTypes[x][y][z].getMaterial()) {
                        Flame.World.setBlock(m_absStart.X + x, m_absStart.Y + y, m_absStart.Z + z,
                                m_oldBlockTypes[x][y][z]);
                    }
                }
            }
        }
    }

    private void saveSnapshot() {
        m_oldBlockTypes = new Block[m_size.X][][];
        for (int x = 0; x < m_size.X; x++) {
            m_oldBlockTypes[x] = new Block[m_size.Y][];
            for (int y = 0; y < m_size.Y; y++) {
                m_oldBlockTypes[x][y] = new Block[m_size.Z];
                for (int z = 0; z < m_size.Z; z++) {
                    m_oldBlockTypes[x][y][z] = Flame.World.getBlock(m_absStart.X + x, m_absStart.Y + y,
                            m_absStart.Z + z);
                }
            }
        }
    }

}
