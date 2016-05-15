package com.noyhillel.survivalgames.command;

import com.noyhillel.networkengine.exceptions.NewNetCommandException;
import com.noyhillel.networkengine.newcommand.CommandMeta;
import com.noyhillel.networkengine.newcommand.NetAbstractCommandHandler;
import com.noyhillel.networkengine.newcommand.Permission;
import com.noyhillel.survivalgames.game.impl.SGGame;
import com.noyhillel.survivalgames.player.SGPlayer;
import com.noyhillel.survivalgames.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.noyhillel.survivalgames.command.LinkChestsCommand.resolveGPlayer;

@Permission("survivalgames.nick")
@CommandMeta(name = "nick", description = "The Nick Command", usage = "/nick")
public final class NickCommand extends NetAbstractCommandHandler {

    @Override
    protected void playerCommand(Player player, String[] args) throws NewNetCommandException {
        if (args.length == 0) throw new NewNetCommandException("Too few arguments, use /nick <name>", NewNetCommandException.ErrorType.FewArguments);
        if (args.length > 1) throw new NewNetCommandException("Too many arguments, use /nick <name>", NewNetCommandException.ErrorType.ManyArguments);
        String nick = args[0];
        if (!nick.matches("^[a-zA-Z_0-9\u00a7]+$")) throw new NewNetCommandException("Nicknames must be AlphaNumeric!", NewNetCommandException.ErrorType.Special);
        SGPlayer SGPlayer = resolveGPlayer(player);
        if (SGGame.spectators.contains(SGPlayer)) throw new NewNetCommandException("You cannot nick as a spectator!", NewNetCommandException.ErrorType.Special);
        if (nick.length() > 16) throw new NewNetCommandException("This nickname is too long!", NewNetCommandException.ErrorType.ManyArguments); // That's the limit, otherwise throws a NPE.
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (nick.equalsIgnoreCase(onlinePlayer.getName())) throw new NewNetCommandException("This nickname is already taken!", NewNetCommandException.ErrorType.Special); // Not sure about this one, change to a message if this isn't correct.
            if (nick.equalsIgnoreCase(onlinePlayer.getDisplayName())) throw new NewNetCommandException("This nickname is already taken!", NewNetCommandException.ErrorType.Special);
            if (nick.equalsIgnoreCase(onlinePlayer.getPlayerListName())) throw new NewNetCommandException("This nickname is already taken!", NewNetCommandException.ErrorType.Special);
        }
        if (nick.equalsIgnoreCase("remove") || nick.equalsIgnoreCase("off")) {
            SGPlayer.setNick(null);
            player.setDisplayName(player.getName());
            player.sendMessage(MessageManager.getFormat("formats.nick-off"));
            return;
        }
        SGPlayer.setNick(nick);
        SGPlayer.sendMessage(MessageManager.getFormat("formats.disguised-player", true, new String[]{"<nickname>", nick}));
    }
}
