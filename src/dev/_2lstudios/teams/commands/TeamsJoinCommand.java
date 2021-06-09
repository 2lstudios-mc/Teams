package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsJoinCommand {
  TeamsJoinCommand(TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender, String label,
      String[] args) {
    if (args.length > 1) {
      String senderName = sender.getName();
      TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
      String currentTeamName = tPlayer.getTeam();
      Team currentTeam = teamManager.getTeam(currentTeamName);
      if (currentTeam == null || !currentTeam.exists()) {
        String targetTeamName = args[1].toLowerCase();
        Team targetTeam = teamManager.getTeam(targetTeamName);
        if (targetTeam != null && targetTeam.isInvited(senderName)) {
          targetTeam.addPlayer(tPlayer);
          targetTeam.removeInvited(senderName);
          targetTeam.getTeamMembers().getOnline().add(senderName);
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
              "&aTe uniste al team &b" + targetTeam.getDisplayName() + "&a!"));
        } else {
          sender.sendMessage(
              ChatColor.RED + "No estas invitado al team " + ChatColor.AQUA + targetTeamName + ChatColor.RED + "!");
        }
      } else {
        sender.sendMessage(
            ChatColor.translateAlternateColorCodes('&', "&cYa eres parte del team &b" + currentTeamName + "&c!"));
      }
    } else {
      sender.sendMessage("/" + label + " join <team>");
    }
  }
}
