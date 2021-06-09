package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsCreateCommand {
  TeamsCreateCommand(TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender, String label,
      String[] args) {
    if (args.length > 1) {
      String senderName = sender.getName();
      TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
      if (tPlayer != null) {
        String teamName = tPlayer.getTeam();
        if (teamName == null || !teamManager.getTeam(teamName).exists()) {
          if (teamManager.isNameValid(args[1])) {
            Team team = teamManager.getTeam(args[1]);
            if (team != null && !team.exists()) {
              teamManager.addTeam(team);
              team.addPlayer(tPlayer, Role.LIDER);
              team.getTeamMembers().getOnline().add(senderName);
              sender.sendMessage(ChatColor.GREEN + "Team creado correctamente!");
            } else {
              sender.sendMessage(ChatColor.RED + "El team solicitado ya existe!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "No puedes iconos raros en el nombre del team o es muy largo!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "Ya eres miembro de un team!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "Tus datos no estan cargados!");
      }
    } else {
      sender.sendMessage("/" + label + " create <team>");
    }
  }
}
