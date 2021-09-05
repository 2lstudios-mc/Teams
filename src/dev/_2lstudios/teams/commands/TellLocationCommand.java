package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.team.Team;
import dev._2lstudios.teams.team.TeamPlayer;

public class TellLocationCommand implements CommandExecutor {
  private final Plugin plugin;
  private final Server server;
  private final TeamPlayerManager teamPlayerManager;
  private final TeamManager teamManager;

  public TellLocationCommand(final Plugin plugin, final TeamPlayerManager teamPlayerManager,
      final TeamManager teamManager) {
    this.plugin = plugin;
    this.server = plugin.getServer();
    this.teamPlayerManager = teamPlayerManager;
    this.teamManager = teamManager;
  }

  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    this.server.getScheduler().runTaskAsynchronously(this.plugin, () -> {
      if (sender instanceof Player) {
        final String senderName = sender.getName();
        final Player player = (Player) sender;
        final TeamPlayer teamPlayer = teamPlayerManager.getPlayer(senderName);
        final Team team = teamManager.getTeam(teamPlayer.getTeam());

        if (team != null) {
          final Location location = player.getLocation();

          team.sendMessage(ChatColor.translateAlternateColorCodes('&',
              "&8[&aTeam&8] &b" + senderName + "&8:&e&l TL: &6" + location.getWorld().getEnvironment() + " &a(X: &b"
                  + (int) location.getX() + "&a Y: &b" + (int) location.getY() + "&a Z: &b" + (int) location.getZ()
                  + "&a)"));
        } else {
          sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "Ese comando no puede ser utilizado desde la consola!");
      }
    });

    return true;
  }
}
