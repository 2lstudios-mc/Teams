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

class TeamsInviteCommand {
  TeamsInviteCommand(Server server, TeamPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender,
      String label, String[] args) {
    if (args.length > 1) {
      String senderName = sender.getName();
      TeamPlayer teamPlayer = tPlayerManager.getPlayer(senderName);
      Team team = teamManager.getTeam(teamPlayer.getTeam());
      if (team != null) {
        Role role = team.getRole(senderName);
        if (role.getPower() >= Role.MOD.getPower()) {
          if (!team.isInvited(args[1])) {
            Player player1 = server.getPlayer(args[1]);
            if (player1 != null) {
              player1.sendMessage(ChatColor.translateAlternateColorCodes('&',
                  "&aFuiste invitado al team &b" + team.getDisplayName() + "&a!"));
              team.addInvited(args[1]);
              sender.sendMessage(ChatColor.GREEN + "Invitaste al jugador correctamente!");
            } else {
              sender.sendMessage(ChatColor.RED + "El jugador no esta en linea!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "El jugador ya esta invitado a tu team!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "Debes ser lider/colider/mod para realizar esta accion!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
      }
    } else {
      sender.sendMessage("/" + label + " invite <jugador>");
    }
  }
}
