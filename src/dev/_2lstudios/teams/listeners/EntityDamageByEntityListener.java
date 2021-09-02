package dev._2lstudios.teams.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import dev._2lstudios.teams.enums.Relation;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.team.Team;

public class EntityDamageByEntityListener implements Listener {
  private final TeamManager teamManager;

  private final TeamPlayerManager tPlayerManager;

  public EntityDamageByEntityListener(TeamsManager teamsManager) {
    this.teamManager = teamsManager.getTeamManager();
    this.tPlayerManager = teamsManager.getTeamPlayerManager();
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    Entity damaged = event.getEntity();
    if (damaged instanceof org.bukkit.entity.Player) {
      Entity damager = event.getDamager();
      if (damager instanceof Projectile) {
        ProjectileSource shooter = ((Projectile) damager).getShooter();
        if (shooter instanceof Entity)
          damager = (Entity) shooter;
      }
      if (damaged != damager && damager instanceof org.bukkit.entity.Player) {
        TeamPlayer damagerTeamPlayer = this.tPlayerManager.getPlayer(damager.getName());
        TeamPlayer damagedTeamPlayer = this.tPlayerManager.getPlayer(damaged.getName());
        if (damagerTeamPlayer != null && damagedTeamPlayer != null) {
          String damagerTeamName = damagerTeamPlayer.getTeam();
          String damagedTeamName = damagedTeamPlayer.getTeam();
          Team damagerTeam = this.teamManager.getTeam(damagerTeamName);
          Team damagedTeam = this.teamManager.getTeam(damagedTeamName);
          if (damagerTeam != null && damagedTeam != null
              && damagerTeam.getTeamRelations().getMutual(damagedTeam) != Relation.ENEMY
              && (!damagerTeam.isPvp() || !damagedTeam.isPvp()))
            event.setCancelled(true);
        }
      }
    }
  }
}
