package com.thilenius.flame.jumbotron;

import com.thilenius.utilities.types.Location2D;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Alec on 11/22/14.
 */
public class JumboTileEntity extends TileEntity {

    static {
        addMapping(JumboTileEntity.class, "Jumbotron");
    }

    public String Text;
    public Location2D Size;

    public JumboTileEntity() {
        System.out.println("Creating Jumbrotron Tile Entity");
    }

    public void build(Location2D size) {
        clean();
        Size = size;

        for (int x = -size.X; x < 0; x++) {
            for (int y = 0; y < size.Y; y++) {
                this.worldObj.setBlock(this.xCoord + x, this.yCoord + y, this.zCoord, Blocks.stained_hardened_clay, 15, 2);
            }
        }

        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void clean() {
        if (Size == null) {
            return;
        }

        for (int x = -Size.X; x < 0; x++) {
            for (int y = 0; y < Size.Y; y++) {
                this.worldObj.setBlockToAir(this.xCoord + x, this.yCoord + y, this.zCoord);
            }
        }

        Size = null;
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("text", Text);
        nbt.setInteger("sizeX", Size.X);
        nbt.setInteger("sizeY", Size.Y);
        super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        Text = nbt.getString("text");
        int x = nbt.getInteger("sizeX");
        int y = nbt.getInteger("sizeY");
        Size = new Location2D(x, y);
        super.readFromNBT(nbt);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
    }


}
