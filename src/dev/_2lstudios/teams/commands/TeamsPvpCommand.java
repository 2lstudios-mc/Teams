package dev._2lstudios.teams.commands;

import java.util.Collection;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsPvpCommand {
  TeamsPvpCommand(TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender) {
    String senderName = sender.getName();
    TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
    if (tPlayer != null) {
      Team team = teamManager.getTeam(tPlayer.getTeam());
      Role role = team.getRole(senderName);
      if (role == Role.COLIDER || role == Role.LIDER) {
        Collection<String> filter = new HashSet<>();
        filter.add(senderName);
        if (!team.isPvp()) {
          team.sendMessage(filter, ChatColor.RED + "El pvp del team fue habilitado por " + senderName + "!");
          team.setPvp(true);
          sender.sendMessage(ChatColor.RED + "Activaste el pvp del team! Puedes atacar a miembros y aliados!");
        } else {
          team.sendMessage(filter, ChatColor.GREEN + "El pvp del team fue deshabilitado por " + senderName + "!");
          team.setPvp(false);
          sender.sendMessage(ChatColor.GREEN + "Desactivaste el pvp del team!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "Debes ser lider/colider para realizar esta accion!");
      }
    } else {
      sender.sendMessage(ChatColor.RED + "Tus datos no estan cargados!");
    }
  }
}
