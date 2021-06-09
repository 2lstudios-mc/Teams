package dev._2lstudios.teams.managers;

import org.bukkit.plugin.Plugin;
import dev._2lstudios.teams.utils.ConfigurationUtil;

public class TeamsManager {
  private final TeamManager teamManager;
  private final TPlayerManager tPlayerManager;

  public TeamsManager(Plugin plugin, ConfigurationUtil configurationUtil) {
    this.tPlayerManager = new TPlayerManager(plugin, this);
    this.teamManager = new TeamManager(plugin, configurationUtil, this);
  }

  public TeamManager getTeamManager() {
    return this.teamManager;
  }

  public TPlayerManager getTPlayerManager() {
    return this.tPlayerManager;
  }
}
