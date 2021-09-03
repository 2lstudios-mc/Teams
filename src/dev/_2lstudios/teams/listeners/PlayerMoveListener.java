package dev._2lstudios.teams.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.teleport.TeleportSystem;

public class PlayerMoveListener implements Listener {
  private final TeleportSystem teleportSystem;

  public PlayerMoveListener(final TeamsManager teamsManager) {
    this.teleportSystem = teamsManager.getTeleportSystem();
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerMove(final PlayerMoveEvent event) {
    final Player player = event.getPlayer();

    if (teleportSystem.remove(player) != null) {
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTeletransporte pendiente cancelado por daño!"));
    }
  }
}
