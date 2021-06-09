package dev._2lstudios.teams.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;
import dev._2lstudios.teams.utils.ConfigurationUtil;
import dev._2lstudios.teams.utils.JSONUtil;

public class TeamManager {
  private static final Pattern WORDS_PATTERN = Pattern.compile("[^\\w]");
  private final Plugin plugin;
  private final TeamsManager teamsManager;
  private final Map<String, Team> teamMap = new HashMap<>();
  private final boolean defaultPvP;

  TeamManager(Plugin plugin, ConfigurationUtil configurationUtil, TeamsManager teamsManager) {
    this.plugin = plugin;
    this.teamsManager = teamsManager;
    YamlConfiguration yamlConfiguration = configurationUtil.getConfiguration("%datafolder%/config.yml");
    this.defaultPvP = yamlConfiguration.getBoolean("pvp", false);
  }

  public void addTeam(Team team) {
    this.teamMap.put(team.getName(), team);
  }

  public boolean isNameValid(String name) {
    return (name.length() <= 16 && !WORDS_PATTERN.matcher(name).find());
  }

  public Team getTeam(String teamName) {
    if (teamName != null) {
      Team team;
      if (this.teamMap.containsKey(teamName.toLowerCase())) {
        team = this.teamMap.get(teamName.toLowerCase());
      } else {
        team = new Team(this.plugin, this.teamsManager, teamName, this.defaultPvP);
        if (!team.exists() && team.getMembers().size() > 0) {
          deleteTeam(team, true, true);
          this.plugin.getLogger().info("Deleted team " + teamName + " because it had no leader!");
        } else {
          addTeam(team);
        }
      }
      team.update();
      return team;
    }
    return null;
  }

  public Collection<Team> getCachedTeams() {
    return this.teamMap.values();
  }

  public void deleteTeam(Team team, boolean removeMap, boolean sync) {
    String name = team.getName();
    JSONUtil.delete("%datafolder%/teams/" + name + ".json", sync);
    if (removeMap)
      this.teamMap.remove(name);
    for (String teamPlayerName : new HashSet<>(team.getMembers().keySet()))
      team.removePlayer(this.teamsManager.getTPlayerManager().getPlayer(teamPlayerName));
  }

  public boolean renameTeam(String oldTeamName, String newTeamName, boolean sync) {
    if (this.teamMap.containsKey(oldTeamName)) {
      Team team = this.teamMap.get(oldTeamName);
      team.setName(newTeamName);
      String name = team.getName();
      for (String memberName : team.getMembers().keySet()) {
        TPlayer tPlayer = this.teamsManager.getTPlayerManager().getPlayer(memberName);
        tPlayer.setTeam(name);
      }
      this.teamMap.remove(oldTeamName);
      this.teamMap.put(name, team);
      JSONUtil.delete("%datafolder%/teams/" + name + ".json", sync);
      return true;
    }
    return false;
  }

  public void update(boolean ignoreOnline, boolean sync) {
    for (Iterator<Map.Entry<String, Team>> iterator = this.teamMap.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<String, Team> teamEntry = iterator.next();
      Team team = teamEntry.getValue();
      if (!team.exists()) {
        deleteTeam(team, false, sync);
        iterator.remove();
        continue;
      }
      if (ignoreOnline || team.getTeamMembers().getOnline().isEmpty()) {
        if (team.isChanged())
          team.save(sync);
        if (team.lastUpdate() > 60000L)
          iterator.remove();
      }
    }
  }
}
