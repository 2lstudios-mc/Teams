package dev._2lstudios.teams.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.team.Team;

public class EntityCombustByEntityListener implements Listener {
  private final TeamManager teamManager;

  private final TeamPlayerManager tPlayerManager;

  public EntityCombustByEntityListener(TeamsManager teamsManager) {
    this.teamManager = teamsManager.getTeamManager();
    this.tPlayerManager = teamsManager.getTeamPlayerManager();
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
  public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
    Entity entity = event.getEntity();
    if (entity instanceof Player) {
      Entity combuster = event.getCombuster();
      if (combuster instanceof Player) {
        Player damager = (Player) combuster;
        TeamPlayer teamPlayer = this.tPlayerManager.getPlayer(entity.getName());
        TeamPlayer teamPlayer1 = this.tPlayerManager.getPlayer(damager.getName());
        if (teamPlayer != null && teamPlayer1 != null) {
          String teamName = teamPlayer.getTeam();
          Team team = this.teamManager.getTeam(teamName);
          if (team != null && !team.isPvp() && teamName.equals(teamPlayer1.getTeam()))
            event.setCancelled(true);
        }
      }
    }
  }
}
