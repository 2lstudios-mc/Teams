package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsDescriptionCommand {
  TeamsDescriptionCommand(TeamPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender, String[] args) {
    String senderName = sender.getName();
    TeamPlayer teamPlayer = tPlayerManager.getPlayer(senderName);
    Team team = teamManager.getTeam(teamPlayer.getTeam());
    if (team != null) {
      Role role = team.getRole(senderName);
      if (role == Role.LIDER || role == Role.COLIDER) {
        if (args.length < 2) {
          team.setDescription(null);
          sender.sendMessage(ChatColor.GREEN + "Eliminaste la descripcion de tu faction correctamente!");
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          for (int i = 1; i < args.length; i++) {
            String argument = args[i];
            if (i > 1) {
              stringBuilder.append(" " + argument);
            } else {
              stringBuilder.append(argument);
            }
          }
          team.setDescription(stringBuilder.toString());
          sender.sendMessage(ChatColor.GREEN + "Cambiaste la descripcion de tu faction correctamente!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "Debes ser lider/colider para realizar esta accion!");
      }
    } else {
      sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
    }
  }
}
