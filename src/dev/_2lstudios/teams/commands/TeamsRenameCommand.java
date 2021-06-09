package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

public class TeamsRenameCommand {
  TeamsRenameCommand(TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender, String label,
      String[] args) {
    String senderName = sender.getName();
    TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
    Team team = teamManager.getTeam(tPlayer.getTeam());
    if (team != null && team.exists()) {
      Role role = team.getRole(senderName);
      if (role == Role.LIDER || role == Role.COLIDER) {
        if (args.length < 2) {
          sender.sendMessage(ChatColor.RED + "/" + label + " rename <nombre>");
        } else {
          String newTeamName = args[1];
          if (teamManager.isNameValid(newTeamName)) {
            Team renameTeam = teamManager.getTeam(newTeamName.toLowerCase());
            if (renameTeam != null && renameTeam.exists()) {
              sender.sendMessage(ChatColor.RED + "Ya existe un team con ese nombre!");
            } else if (teamManager.renameTeam(team.getName(), newTeamName, true)) {
              sender.sendMessage(
                  ChatColor.GREEN + "Renombraste el team a " + ChatColor.AQUA + newTeamName + ChatColor.GREEN + "!");
            } else {
              sender.sendMessage(ChatColor.RED + "No se pudo renombrar al team!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "Ingresaste un nombre invalido!");
          }
        }
      } else {
        sender.sendMessage(ChatColor.RED + "Debes ser lider/colider para realizar esta accion!");
      }
    } else {
      sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
    }
  }
}
