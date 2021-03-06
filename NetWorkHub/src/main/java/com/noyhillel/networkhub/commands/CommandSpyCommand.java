package com.noyhillel.networkhub.commands;

import com.noyhillel.networkengine.command.*;
import com.noyhillel.networkengine.util.player.NetPlayer;
import com.noyhillel.networkhub.MessageManager;
import com.noyhillel.networkhub.NetHub;
import com.noyhillel.networkhub.listeners.CommandSpyListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Noy on 27/05/2014.
 */
public final class CommandSpyCommand extends AbstractCommandHandler {

    @NetCommand(
            name = "commandspy",
            usage = "/cw",
            description = "The Command Spy plugin",
            permission = "hub.command-spy",
            senders = {NetCommandSenders.PLAYER}
    )
    public CommandStatus commandspy(CommandSender sender, NetCommandSenders senders, NetCommand meta, Command command, String[] args) {
        if (args.length > 0) return CommandStatus.HELP;
        Player p = (Player) sender;
        NetPlayer netPlayer = NetHub.getNetPlayerManager().getOnlinePlayer(p);
        if (CommandSpyListener.commandListeners.contains(netPlayer)) {
            CommandSpyListener.commandListeners.remove(netPlayer);
            netPlayer.sendMessage(MessageManager.getFormats("formats.spy-off"));
            return CommandStatus.SUCCESS;
        } else {
            CommandSpyListener.commandListeners.add(netPlayer);
            netPlayer.sendMessage(MessageManager.getFormats("formats.spy-on"));
        }
        return CommandStatus.SUCCESS;
    }
}