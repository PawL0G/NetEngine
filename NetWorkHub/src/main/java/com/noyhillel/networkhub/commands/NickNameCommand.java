package com.noyhillel.networkhub.commands;

import com.noyhillel.networkengine.command.*;
import com.noyhillel.networkhub.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Noy on 28/05/2014.
 */
public final class NickNameCommand extends AbstractCommandHandler {

    @NetCommand(
            name = "nick",
            usage = "/nick",
            description = "The NickName Command",
            permission = "hub.nick",
            senders = {NetCommandSenders.PLAYER}
    )
    public CommandStatus nick(CommandSender sender, NetCommandSenders senders, NetCommand netCommand, Command command, String[] args) {
        if (args.length == 0) return CommandStatus.FEW_ARGUMENTS;
        if (args.length > 2) return CommandStatus.MANY_ARGUMENTS;
        Player player = (Player) sender;
        try {
            String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
            if (!nick.matches("^[a-zA-Z_0-9\u00a7]+$")) return CommandStatus.NULL;
            if (nick.length() > 16) return CommandStatus.NULL;
            if (nick.equalsIgnoreCase("off")) nick = null;
            if (args.length == 1) {
                player.setDisplayName(nick);
                player.setPlayerListName(nick);
                String coloredName = ChatColor.translateAlternateColorCodes('&', nick == null ? player.getName() : nick);
                player.sendMessage(MessageManager.getFormat("formats.new-nick", true, new String[]{"<nick>", coloredName}));
                return CommandStatus.SUCCESS;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) return CommandStatus.NULL;
            String targetNick = ChatColor.translateAlternateColorCodes('&', args[1]);
            if (targetNick.equalsIgnoreCase("off")) targetNick = null;
            target.setPlayerListName(targetNick);
            target.setDisplayName(targetNick);
            String targetColoredName = ChatColor.translateAlternateColorCodes('&', targetNick == null ? target.getName() : targetNick);
            target.sendMessage(MessageManager.getFormat("formats.new-nick", true, new String[]{"<nick>", targetColoredName}));
            sender.sendMessage(MessageManager.getFormat("formats.set-nick", true, new String[]{"<nick>", targetColoredName}, new String[]{"<player>", target.getName()}));
            return CommandStatus.SUCCESS;
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
            player.sendMessage(MessageManager.getFormats("formats.nick-error"));
            return CommandStatus.SUCCESS;
        }
    }
}