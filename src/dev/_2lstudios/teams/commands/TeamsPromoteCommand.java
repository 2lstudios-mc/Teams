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

class TeamsPromoteCommand {
  TeamsPromoteCommand(Server server, TeamPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender,
      String[] args) {
    if (args.length > 1) {
      String argument2 = args[1];
      TeamPlayer teamPlayer = tPlayerManager.getPlayer(sender.getName());
      Team team = teamManager.getTeam(teamPlayer.getTeam());
      if (team != null) {
        Role tPlayerRole = team.getRole(teamPlayer.getName());
        if (tPlayerRole == Role.LIDER || tPlayerRole == Role.COLIDER) {
          TeamPlayer teamPlayer1 = tPlayerManager.getPlayer(argument2);
          if (teamManager.getTeam(teamPlayer1.getTeam()) == team) {
            if (tPlayerRole.getPower() > team.getRole(teamPlayer1.getName()).getPower()) {
              Role promote = team.getTeamMembers().promote(teamPlayer1.getName());
              if (promote != null) {
                Player player = server.getPlayer(argument2);
                if (player != null)
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                      "&aFuiste promovido al puesto &b" + promote.name() + "&a de tu team!"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aPromoviste a &b" + teamPlayer1.getName() + "&a al puesto &b" + promote.name() + "&a!"));
              } else {
                sender.sendMessage(ChatColor.RED + "No puedes promover mas a ese jugador!");
              }
            } else {
              sender.sendMessage(ChatColor.RED + "No puedes promover a un jugador de rango mas alto!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "Ese jugador no esta en tu team!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "Debes ser lider/colider para realizar esta accion!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
      }
    } else {
      sender.sendMessage(
          ChatColor.translateAlternateColorCodes('&', "&cDebes ser un jugador para utilizar este comando!"));
    }
  }
}
