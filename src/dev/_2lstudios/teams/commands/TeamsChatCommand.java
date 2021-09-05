package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.ChatMode;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.team.TeamPlayer;

class TeamsChatCommand {
  private void applyChatMode(final TeamPlayer teamPlayer, final CommandSender sender, final ChatMode chatMode) {
    teamPlayer.setChatMode(chatMode);
    sender.sendMessage(
        ChatColor.translateAlternateColorCodes('&', "&aEstableciste el chat en modo &b" + chatMode.name() + "&a!"));
  }

  TeamsChatCommand(TeamPlayerManager tPlayerManager, CommandSender sender) {
    String senderName = sender.getName();
    TeamPlayer teamPlayer = tPlayerManager.getPlayer(senderName);
    String teamName = teamPlayer.getTeam();
    if (teamName != null) {
      final ChatMode chatMode = teamPlayer.getChatMode();

      if (chatMode == ChatMode.NORMAL) {
        applyChatMode(teamPlayer, sender, ChatMode.TEAM);
      } else if (chatMode == ChatMode.TEAM) {
        applyChatMode(teamPlayer, sender, ChatMode.ALLY);
      } else if (chatMode == ChatMode.ALLY) {
        applyChatMode(teamPlayer, sender, ChatMode.NORMAL);
      }
    } else {
      sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
    }
  }
}
