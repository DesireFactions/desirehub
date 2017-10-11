package com.desiremc.hub.listeners;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.ChatUtils;
import com.desiremc.hub.DesireHub;

public class ChatListener implements Listener
{

    @EventHandler
    public void chat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        event.setCancelled(true);
        Session s = SessionHandler.getSession(player);
        if (s.isMuted() != null)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            player.sendMessage("");
            ChatUtils.sendCenteredMessage(player, DesireCore.getLangHandler().getPrefix().replace(" ", ""));
            player.sendMessage("");
            ChatUtils.sendCenteredMessage(player, ChatColor.GRAY + "You are muted and " + ChatColor.RED + "CANNOT " + ChatColor.GRAY + "speak!");
            ChatUtils.sendCenteredMessage(player, ChatColor.GRAY + "Visit our rules @ " + ChatColor.YELLOW + "https://desirehcf.net/rules");
            player.sendMessage("");
            player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            return;
        }
        if (!s.getRank().isStaff())
        {
            DesireHub.getLangHandler().sendString(player, "chat.deny");
            return;
        }

        String msg = event.getMessage();

        String parsedMessage = s.getRank().getId() >= Rank.ADMIN.getId() ? ChatColor.translateAlternateColorCodes('&', msg) : msg;
        System.out.println(player.getName() + ": " + parsedMessage);
        Bukkit.getOnlinePlayers().stream().forEach(new Consumer<Player>()
        {
            @Override
            public void accept(Player players)
            {

                if (true)
                {
                    new FancyMessage(s.getRank().getPrefix())
                            .then(player.getName())
                            .then(": ")
                            .then(parsedMessage)
                            .color(s.getRank().getColor())
                            .send(players);
                    return;
                }

            }
        });

    }
}