package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsLeaveCommand {
  TeamsLeaveCommand(TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender) {
    String senderName = sender.getName();
    TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
    String teamName = tPlayer.getTeam();
    Team team = teamManager.getTeam(teamName);
    if (team != null) {
      Role tPlayerRole = team.getRole(senderName);
      if (tPlayerRole != Role.LIDER) {
        team.removePlayer(tPlayer);
        team.sendMessage(ChatColor.RED + "El jugador " + senderName + " salio del team!");
        sender.sendMessage(
            ChatColor.translateAlternateColorCodes('&', "&aSaliste del team &b" + teamName + "&a correctamente!"));
      } else {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
            "&cNo puedes salir del team por que eres el lider! Primero debes eliminarlo!"));
      }
    } else {
      sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
    }
  }
}
