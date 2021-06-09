package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsShowCommand {
  TeamsShowCommand(TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender, String[] args) {
    Team team = null;
    if (args.length < 2) {
      TPlayer tPlayer = tPlayerManager.getPlayer(sender.getName());
      if (tPlayer != null) {
        team = teamManager.getTeam(tPlayer.getTeam());
      } else {
        team = null;
      }
    } else {
      team = teamManager.getTeam(args[1].toLowerCase());
      if (team == null || !team.exists()) {
        TPlayer tPlayer = tPlayerManager.getPlayer(args[1]);
        team = teamManager.getTeam(tPlayer.getTeam());
      }
    }
    if (team != null && team.exists()) {
      sender.sendMessage(team.getShow());
    } else {
      sender.sendMessage(ChatColor.RED + "El team solicitado no existe!");
    }
  }
}
