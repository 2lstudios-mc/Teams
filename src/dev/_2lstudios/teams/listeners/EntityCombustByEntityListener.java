package dev._2lstudios.teams.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

public class EntityCombustByEntityListener implements Listener {
  private final TeamManager teamManager;

  private final TPlayerManager tPlayerManager;

  public EntityCombustByEntityListener(TeamsManager teamsManager) {
    this.teamManager = teamsManager.getTeamManager();
    this.tPlayerManager = teamsManager.getTPlayerManager();
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
  public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
    Entity entity = event.getEntity();
    if (entity instanceof Player) {
      Entity combuster = event.getCombuster();
      if (combuster instanceof Player) {
        Player damager = (Player) combuster;
        TPlayer tPlayer = this.tPlayerManager.getPlayer(entity.getName());
        TPlayer tPlayer1 = this.tPlayerManager.getPlayer(damager.getName());
        if (tPlayer != null && tPlayer1 != null) {
          String teamName = tPlayer.getTeam();
          Team team = this.teamManager.getTeam(teamName);
          if (team != null && !team.isPvp() && teamName.equals(tPlayer1.getTeam()))
            event.setCancelled(true);
        }
      }
    }
  }
}
