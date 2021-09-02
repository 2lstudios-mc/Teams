package dev._2lstudios.teams.commands;

import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsLeaderCommand {
  TeamsLeaderCommand(Plugin plugin, CommandSender sender, TeamManager teamManager, TeamPlayerManager tPlayerManager,
      String label, String[] args) {
    if (args.length > 1) {
      String senderName = sender.getName();
      TeamPlayer teamPlayer = tPlayerManager.getPlayer(senderName);
      if (teamPlayer != null) {
        String teamName = teamPlayer.getTeam();
        Team team = teamManager.getTeam(teamName);
        if (team != null) {
          if (team.getRole(senderName) == Role.LIDER) {
            TeamPlayer teamPlayer1 = tPlayerManager.getPlayer(args[1]);
            String teamName1 = teamPlayer1.getTeam();
            if (teamName1 != null && teamName1.equals(teamName)) {
              Map<String, Role> members = team.getMembers();
              Player player = plugin.getServer().getPlayer(args[1]);
              if (player != null)
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aFuiste promovido por &b" + senderName + "&a a lider del team!"));
              members.put(senderName, Role.COLIDER);
              members.put(args[1], Role.LIDER);
              sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                  "&aPromoviste al jugador &b" + args[1] + "&a a lider del team!"));
            } else {
              sender.sendMessage(ChatColor.RED + "No puedes iconos raros en el nombre del team o es muy largo!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "No eres el lider de este team!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "Tus datos no estan cargados!");
      }
    } else {
      sender.sendMessage("/" + label + " leader <jugador>");
    }
  }
}
