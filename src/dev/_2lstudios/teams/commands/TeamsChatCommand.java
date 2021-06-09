package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.ChatMode;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.team.TPlayer;

class TeamsChatCommand {
  TeamsChatCommand(TPlayerManager tPlayerManager, CommandSender sender) {
    String senderName = sender.getName();
    TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
    String teamName = tPlayer.getTeam();
    if (teamName != null) {
      switch (tPlayer.getChatMode()) {
        case ALLY:
          tPlayer.setChatMode(ChatMode.ALLY);
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEstableciste el chat en modo &bALLY&a!"));
          break;
        case NORMAL:
          tPlayer.setChatMode(ChatMode.NORMAL);
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEstableciste el chat en modo &bNORMAL&a!"));
          break;
        case TEAM:
          tPlayer.setChatMode(ChatMode.TEAM);
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEstableciste el chat en modo &bTEAM&a!"));
          break;
      }
    } else {
      sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
    }
  }
}
