package dev._2lstudios.teams.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TeamPlayer;

public class EntityDamageListener implements Listener {
  private final TeamPlayerManager tPlayerManager;

  public EntityDamageListener(TeamsManager teamsManager) {
    this.tPlayerManager = teamsManager.getTeamPlayerManager();
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onEntityDamage(EntityDamageEvent event) {
    Entity entity = event.getEntity();
    if (!(entity instanceof Player))
      return;
    Player player = (Player) entity;
    TeamPlayer teamPlayer = this.tPlayerManager.getPlayer(player.getName());
    if (teamPlayer != null && teamPlayer.getTeleportTask() != null) {
      teamPlayer.setTeleportTask(null);
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTeletransporte pendiente cancelado por daño!"));
    }
  }
}
