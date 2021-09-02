package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsShowCommand {
  TeamsShowCommand(TeamPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender, String[] args) {
    Team team = null;
    if (args.length < 2) {
      TeamPlayer teamPlayer = tPlayerManager.getPlayer(sender.getName());
      if (teamPlayer != null) {
        team = teamManager.getTeam(teamPlayer.getTeam());
      } else {
        team = null;
      }
    } else {
      team = teamManager.getTeam(args[1].toLowerCase());
      if (team == null || !team.exists()) {
        TeamPlayer teamPlayer = tPlayerManager.getPlayer(args[1]);
        team = teamManager.getTeam(teamPlayer.getTeam());
      }
    }
    if (team != null && team.exists()) {
      sender.sendMessage(team.getShow());
    } else {
      sender.sendMessage(ChatColor.RED + "El team solicitado no existe!");
    }
  }
}
