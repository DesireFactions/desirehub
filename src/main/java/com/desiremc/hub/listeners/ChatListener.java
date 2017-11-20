package com.desiremc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void chat(AsyncPlayerChatEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
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
        for (Player p : Bukkit.getOnlinePlayers())
        {
            new FancyMessage(s.getRank().getPrefix())
                    .then(s.getName())
                    .then(": ")
                    .then(parsedMessage)
                    .color(s.getRank().getColor())
                    .send(p);

        }

    }
}