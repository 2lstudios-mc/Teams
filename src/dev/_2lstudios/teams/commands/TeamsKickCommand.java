package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsKickCommand {
  TeamsKickCommand(Server server, TeamPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender,
      String label, String[] args) {
    if (args.length > 1) {
      String senderName = sender.getName();
      TeamPlayer teamPlayer = tPlayerManager.getPlayer(senderName);
      String teamName = teamPlayer.getTeam();
      Team team = teamManager.getTeam(teamName);
      if (team != null) {
        Role tPlayerRole = team.getRole(senderName);
        if (tPlayerRole == Role.LIDER || tPlayerRole == Role.COLIDER || tPlayerRole == Role.MOD) {
          TeamPlayer targetTeamPlayer = tPlayerManager.getPlayer(args[1]);
          if (teamName.equals(targetTeamPlayer.getTeam())) {
            Role tPlayerTargetRole = team.getRole(args[1]);
            if (tPlayerRole.getPower() > tPlayerTargetRole.getPower()) {
              team.removePlayer(targetTeamPlayer);
              team.sendMessage(
                  ChatColor.RED + "El jugador " + args[1] + " fue kickeado del team por " + senderName + "!");
              Player targetPlayer = server.getPlayer(args[1]);
              if (targetPlayer != null)
                targetPlayer.sendMessage(ChatColor.RED + "Fuiste kickeado del team que estabas!");
            } else {
              sender.sendMessage(ChatColor.RED + "No puedes kickear a alguien con rango superior o igual!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "El jugador no es miembro de tu team!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "Debes ser lider/colider/mod para realizar esta accion!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
      }
    } else {
      sender.sendMessage(ChatColor.RED + "/" + label + " kick <jugador>");
    }
  }
}
