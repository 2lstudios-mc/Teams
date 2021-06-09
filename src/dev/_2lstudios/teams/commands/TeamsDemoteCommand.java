package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsDemoteCommand {
  TeamsDemoteCommand(Server server, TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender,
      String[] args) {
    if (args.length > 1) {
      String argument2 = args[1];
      TPlayer tPlayer = tPlayerManager.getPlayer(sender.getName());
      Team team = teamManager.getTeam(tPlayer.getTeam());
      if (team != null) {
        Role tPlayerRole = team.getRole(tPlayer.getName());
        if (tPlayerRole == Role.LIDER || tPlayerRole == Role.COLIDER) {
          TPlayer tPlayer1 = tPlayerManager.getPlayer(argument2);
          if (teamManager.getTeam(tPlayer1.getTeam()) == team) {
            Role demote = team.getTeamMembers().demote(tPlayer1.getName());
            if (tPlayerRole.getPower() > team.getRole(tPlayer1.getName()).getPower()) {
              if (demote != null) {
                Player player = server.getPlayer(argument2);
                if (player != null)
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                      "&aFuiste degradado al puesto &b" + demote.name() + "&a de tu team!"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aDegradaste a &b" + tPlayer1.getName() + "&a al puesto &b" + demote.name() + "&a!"));
              } else {
                sender.sendMessage(ChatColor.RED + "No puedes degradar mas a ese jugador!");
              }
            } else {
              sender.sendMessage(ChatColor.RED + "No puedes degradar a un jugador de rango mas alto!");
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
