package dev._2lstudios.teams.team;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import dev._2lstudios.teams.enums.Relation;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.utils.JSONUtil;
import dev._2lstudios.teams.utils.TeamShowBuilder;

public class Team {
  private final TeamsManager teamsManager;
  private final TeamHome teamHome;
  private final TeamRelations teamRelations;
  private final TeamMembers teamMembers;
  private String name;
  private String displayName;
  private String description;
  private long lastUpdate;
  private boolean changed;
  private boolean pvp;

  public Team(Plugin plugin, TeamsManager teamsManager, String name, boolean defaultPvP) {
    setName(name);
    this.teamsManager = teamsManager;
    this.pvp = defaultPvP;
    this.teamHome = new TeamHome();
    this.teamRelations = new TeamRelations(getName());
    this.teamMembers = new TeamMembers();
    TeamPlayerManager tPlayerManager = teamsManager.getTeamPlayerManager();
    JSONObject jsonObject = JSONUtil.get("%datafolder%/teams/" + getName() + ".json");
    this.teamRelations.load(jsonObject);
    this.teamHome.load(jsonObject);
    this.teamMembers.load(plugin, tPlayerManager, jsonObject);
    this.displayName = (String) jsonObject.getOrDefault("displayname", this.displayName);
    this.description = (String) jsonObject.getOrDefault("description", null);
    if (this.description == null || this.description.equals("null"))
      this.description = null;
    this.teamRelations.setRelation(getName(), Relation.TEAM);
  }

  public void save(boolean sync) {
    String path = "%datafolder%/teams/" + getName() + ".json";
    JSONObject jsonObject = new JSONObject();
    this.teamRelations.save(jsonObject);
    this.teamHome.save(jsonObject);
    this.teamMembers.save(jsonObject);
    jsonObject.put("displayname", getDisplayName());
    jsonObject.put("description", this.description);
    JSONUtil.save(path, jsonObject, sync);
    setChanged(false);
  }

  public String getName() {
    return this.name;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public void addPlayer(TeamPlayer teamPlayer, Role role) {
    String tPlayerName = teamPlayer.getName();
    sendMessage(ChatColor.translateAlternateColorCodes('&', "&b" + tPlayerName + " &ase unio a tu team!"));
    teamPlayer.setTeam(getName());
    this.teamMembers.getMembers().put(tPlayerName, role);
    setChanged(true);
  }

  public void addPlayer(TeamPlayer teamPlayer) {
    addPlayer(teamPlayer, Role.MIEMBRO);
  }

  public void removePlayer(TeamPlayer teamPlayer) {
    String tPlayerName = teamPlayer.getName();
    String team = teamPlayer.getTeam();
    if (team != null && team.equals(getName()))
      teamPlayer.setTeam(null);
    this.teamMembers.getOnline().remove(tPlayerName);
    this.teamMembers.getMembers().remove(tPlayerName);
    setChanged(true);
  }

  public TeamMembers getTeamMembers() {
    return this.teamMembers;
  }

  public Map<String, Role> getMembers() {
    return this.teamMembers.getMembers();
  }

  public Role getRole(String member) {
    return this.teamMembers.getMembers().getOrDefault(member, Role.MIEMBRO);
  }

  public Collection<String> getMembers(Role role) {
    Collection<String> filteredMembers = new HashSet<>();
    for (Map.Entry<String, Role> member : this.teamMembers.getMembers().entrySet()) {
      if (member.getValue() == role)
        filteredMembers.add(member.getKey());
    }
    return filteredMembers;
  }

  public boolean isInvited(String name) {
    for (String name1 : this.teamMembers.getInvited()) {
      if (name1.equals(name))
        return true;
    }
    return false;
  }

  public void addInvited(String name) {
    this.teamMembers.getInvited().add(name);
  }

  public void removeInvited(String name) {
    this.teamMembers.getInvited().remove(name);
  }

  private void setChanged(boolean changed) {
    this.changed = changed;
  }

  public boolean isChanged() {
    return this.changed;
  }

  public void setPvp(boolean pvp) {
    this.pvp = pvp;
  }

  public boolean isPvp() {
    return this.pvp;
  }

  public boolean exists() {
    return !getMembers(Role.LIDER).isEmpty();
  }

  public int getKills() {
    Server server = Bukkit.getServer();
    TeamPlayerManager tPlayerManager = this.teamsManager.getTeamPlayerManager();
    int kills = 0;
    for (String memberName : this.teamMembers.getMembers().keySet()) {
      TeamPlayer teamPlayer = tPlayerManager.getPlayer(memberName);
      if (teamPlayer != null) {
        UUID uuid = teamPlayer.getUUID();
        try {
          kills += Integer.parseInt(PlaceholderAPI.setPlaceholders(server.getPlayer(uuid), "%statistic_player_kills%"));
        } catch (NumberFormatException numberFormatException) {
        }
      }
    }
    return kills;
  }

  public TeamHome getTeamHome() {
    return this.teamHome;
  }

  public void sendMessage(Collection<String> filter, String message) {
    Server server = Bukkit.getServer();
    for (String memberName : this.teamMembers.getOnline()) {
      if (!filter.contains(memberName)) {
        Player player = server.getPlayer(memberName);
        if (player != null)
          player.sendMessage(message);
      }
    }
  }

  public void sendMessage(String message) {
    sendMessage(new HashSet<>(), message);
  }

  public String getShow() {
    Server server = Bukkit.getServer();
    TeamShowBuilder teamShowBuilder = new TeamShowBuilder(server, this.teamsManager.getTeamManager());
    Location home = this.teamHome.getHome(server);
    String homeInfo = (home == null) ? "N/A"
        : ("Mundo: " + home.getWorld().getEnvironment().name() + " X: " + (int) home.getX() + " Z: "
            + (int) home.getZ());
    String showFormat = teamShowBuilder.getFormat().replace("%name%", getDisplayName()).replace("%home%", homeInfo)
        .replace("%kills%", String.valueOf(getKills()));
    if (this.description != null) {
      showFormat = showFormat.replace("%description%", this.description);
    } else {
      showFormat = showFormat.replace("%description%", "[No establecido]");
    }
    showFormat = teamShowBuilder.applyMembers(this, showFormat);
    showFormat = teamShowBuilder.applyAllies(this, showFormat);
    return ChatColor.translateAlternateColorCodes('&', showFormat);
  }

  public void update() {
    this.lastUpdate = System.currentTimeMillis();
  }

  public long lastUpdate() {
    return System.currentTimeMillis() - this.lastUpdate;
  }

  public void setDescription(String description) {
    setChanged(true);
    this.description = description;
  }

  public TeamRelations getTeamRelations() {
    return this.teamRelations;
  }

  public void setName(String newTeamName) {
    setChanged(true);
    this.displayName = newTeamName;
    this.name = newTeamName.toLowerCase();
  }
}
