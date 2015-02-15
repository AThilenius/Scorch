package com.thilenius.flame.commands;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.data.UserQuery;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Alec on 1/18/15.
 */
public class HomeCommandHandler extends CommandBase {
    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "Returns you to your player arena.";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) sender;
            final World world = player.worldObj;

            UserQuery userQuery = new UserQuery(player.getGameProfile().getName());
            if (Blaze.RemoteDataConnection.query(userQuery)) {
                player.setPositionAndUpdate(userQuery.ArenaLocation.X + 16, userQuery.ArenaLocation.Y + 2,
                        userQuery.ArenaLocation.Z + 5);
            }
        }
    }
}
