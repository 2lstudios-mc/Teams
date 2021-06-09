package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsDeleteCommand {
  TeamsDeleteCommand(TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender) {
    String senderName = sender.getName();
    TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
    if (tPlayer != null) {
      Team team = teamManager.getTeam(tPlayer.getTeam());
      
      if (team != null && team.getRole(senderName) == Role.LIDER) {
        team.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEl team fue eliminado por el lider &b" + senderName + "&c!"));
        teamManager.deleteTeam(team, true, false);
        tPlayer.setTeam(null);
        sender.sendMessage(ChatColor.GREEN + "Se elimino tu team correctamente!");
      } else {
        sender.sendMessage(ChatColor.RED + "No eres lider de ningun team!");
      }
    } else {
      sender.sendMessage(ChatColor.RED + "Tus datos no estan cargados!");
    }
  }
}
