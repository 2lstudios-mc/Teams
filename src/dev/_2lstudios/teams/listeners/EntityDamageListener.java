package dev._2lstudios.teams.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.teleport.TeleportSystem;

public class EntityDamageListener implements Listener {
  private final TeleportSystem teleportSystem;

  public EntityDamageListener(final TeamsManager teamsManager) {
    this.teleportSystem = teamsManager.getTeleportSystem();
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onEntityDamage(final EntityDamageEvent event) {
    final Entity entity = event.getEntity();

    if (entity instanceof Player) {
      final Player player = (Player) entity;

      if (teleportSystem.remove(player) != null) {
        player
            .sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTeletransporte pendiente cancelado por daño!"));
      }
    }
  }
}
