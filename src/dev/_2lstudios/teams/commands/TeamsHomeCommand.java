package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.tasks.TeleportTask;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsHomeCommand {
  TeamsHomeCommand(Server server, TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender,
      boolean homesEnabled) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (!homesEnabled) {
        sender.sendMessage(ChatColor.RED + "Las homes estan desactivadas!");
      } else {
        TPlayer tPlayer = tPlayerManager.getPlayer(sender.getName());
        Team team = teamManager.getTeam(tPlayer.getTeam());
        if (team != null) {
          Location home = team.getTeamHome().getHome(server);
          if (home != null) {
            int seconds;
            if (hasPlayerNear(player)) {
              seconds = 5;
            } else {
              seconds = 1;
            }
            tPlayer.setTeleportTask(new TeleportTask(tPlayerManager, tPlayer, player, home, seconds));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format("&aEstas siendo teletransportado a la &bHome&a! &7(%s segundos)",
                    new Object[] { Integer.valueOf(seconds) })));
          } else {
            sender.sendMessage(ChatColor.RED + "El team no tiene una home establecida!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
        }
      }
    } else {
      sender.sendMessage(ChatColor.RED + "Ese comando no puede ser utilizado desde la consola!");
    }
  }

  private boolean hasPlayerNear(Player player) {
    Location location = player.getLocation();
    World world = location.getWorld();
    for (Player player1 : world.getPlayers()) {
      if (player1 != player && player1.getWorld() == world) {
        Location location1 = player1.getLocation();
        if (location.distance(location1) < 75.0D)
          return true;
      }
    }
    return false;
  }
}
