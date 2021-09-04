package dev._2lstudios.teams.managers;

import org.bukkit.plugin.Plugin;

import dev._2lstudios.teams.teleport.TeleportSystem;
import dev._2lstudios.teams.utils.ConfigurationUtil;
import dev._2lstudios.teams.utils.JSONUtil;

public class TeamsManager {
  private final TeamManager teamManager;
  private final TeamPlayerManager teamPlayerManager;
  private final TeleportSystem teleportSystem;

  public TeamsManager(JSONUtil jsonUtil, Plugin plugin, ConfigurationUtil configurationUtil) {
    this.teamPlayerManager = new TeamPlayerManager(jsonUtil, plugin);
    this.teamManager = new TeamManager(jsonUtil, plugin, configurationUtil, this);
    this.teleportSystem = new TeleportSystem();
  }

  public TeamManager getTeamManager() {
    return teamManager;
  }

  public TeamPlayerManager getTeamPlayerManager() {
    return teamPlayerManager;
  }

  public TeleportSystem getTeleportSystem() {
    return teleportSystem;
  }
}
