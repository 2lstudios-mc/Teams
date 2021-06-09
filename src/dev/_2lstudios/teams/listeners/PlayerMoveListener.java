package dev._2lstudios.teams.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TPlayer;

public class PlayerMoveListener implements Listener {
  private final TPlayerManager tPlayerManager;

  public PlayerMoveListener(TeamsManager teamsManager) {
    this.tPlayerManager = teamsManager.getTPlayerManager();
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    TPlayer tPlayer = this.tPlayerManager.getPlayer(player.getName());
    Location from = event.getFrom();
    Location to = event.getTo();
    if (tPlayer != null && tPlayer.getTeleportTask() != null && from.distance(to) > 0.1D) {
      tPlayer.setTeleportTask(null);
      player.sendMessage(
          ChatColor.translateAlternateColorCodes('&', "&cTeletransporte pendiente cancelado por movimiento!"));
    }
  }
}
