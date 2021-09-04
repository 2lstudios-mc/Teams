package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsDeleteCommand {
  TeamsDeleteCommand(TeamPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender) {
    String senderName = sender.getName();
    TeamPlayer teamPlayer = tPlayerManager.getPlayer(senderName);
    if (teamPlayer != null) {
      Team team = teamManager.getTeam(teamPlayer.getTeam());
      
      if (team != null && team.getRole(senderName) == Role.LIDER) {
        team.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEl team fue eliminado por el lider &b" + senderName + "&c!"));
        teamManager.deleteTeam(team, true);
        teamPlayer.setTeam(null);
        sender.sendMessage(ChatColor.GREEN + "Se elimino tu team correctamente!");
      } else {
        sender.sendMessage(ChatColor.RED + "No eres lider de ningun team!");
      }
    } else {
      sender.sendMessage(ChatColor.RED + "Tus datos no estan cargados!");
    }
  }
}
