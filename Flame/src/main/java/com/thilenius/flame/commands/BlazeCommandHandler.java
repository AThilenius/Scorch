package com.thilenius.flame.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

/**
 * Created by Alec on 1/27/15.
 */
public class BlazeCommandHandler extends CommandBase {

    @Override
    public String getCommandName() {
        return "blaze";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "Advanced Blaze commands [Limited to server invocation].";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof MinecraftServer) {
            // STUB
        } else if (sender instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) sender;
            ChatStyle errorStyle = new ChatStyle();

            errorStyle.setColor(EnumChatFormatting.RED);
            ChatComponentText errorText = new ChatComponentText("Blaze commands can only be invoked from the Blaze shell.");
            errorText.setChatStyle(errorStyle);
            player.addChatComponentMessage(errorText);
        }
    }
}
