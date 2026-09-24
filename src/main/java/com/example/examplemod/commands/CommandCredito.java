package com.example.examplemod.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandCredito extends CommandBase {

    @Override
    public String getCommandName() {
        return "credito";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/credito";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        msg(sender, "§6§lBDBR §r§7— §fBarriers Don't Block Rain");
        msg(sender, "§7Feito por §b§lsx");
        msg(sender, "§7GitHub: §fhttps://github.com/wallmss");
    }

    private static void msg(ICommandSender sender, String text) {
        sender.addChatMessage(new ChatComponentText(text));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}