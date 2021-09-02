package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.ChatMode;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.team.TeamPlayer;

class TeamsChatCommand {
  TeamsChatCommand(TeamPlayerManager tPlayerManager, CommandSender sender) {
    String senderName = sender.getName();
    TeamPlayer teamPlayer = tPlayerManager.getPlayer(senderName);
    String teamName = teamPlayer.getTeam();
    if (teamName != null) {
      switch (teamPlayer.getChatMode()) {
        case ALLY:
          teamPlayer.setChatMode(ChatMode.ALLY);
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEstableciste el chat en modo &bALLY&a!"));
          break;
        case NORMAL:
          teamPlayer.setChatMode(ChatMode.NORMAL);
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEstableciste el chat en modo &bNORMAL&a!"));
          break;
        case TEAM:
          teamPlayer.setChatMode(ChatMode.TEAM);
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEstableciste el chat en modo &bTEAM&a!"));
          break;
      }
    } else {
      sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
    }
  }
}
