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
  private final JSONUtil jsonUtil;
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

  public Team(final JSONUtil jsonUtil, final Plugin plugin, final TeamsManager teamsManager, final String name, final boolean defaultPvP) {
    setName(name);
    this.jsonUtil = jsonUtil;
    this.teamsManager = teamsManager;
    this.pvp = defaultPvP;
    this.teamHome = new TeamHome();
    this.teamRelations = new TeamRelations(getName());
    this.teamMembers = new TeamMembers();

    final TeamPlayerManager tPlayerManager = teamsManager.getTeamPlayerManager();
    final JSONObject jsonObject = jsonUtil.get("%datafolder%/teams/" + getName() + ".json");

    this.teamRelations.deserialize(jsonObject);
    this.teamHome.deserialize(jsonObject);
    this.teamMembers.deserialize(plugin, jsonObject);
    this.displayName = (String) jsonObject.getOrDefault("displayname", this.displayName);
    this.description = (String) jsonObject.getOrDefault("description", null);
    if (this.description == null || this.description.equals("null"))
      this.description = null;
    this.teamRelations.setRelation(getName(), Relation.TEAM);
  }

  public JSONObject serialize() {
    final JSONObject teamData = new JSONObject();

    teamData.put("relations", teamRelations.serialize());
    teamData.put("home", teamHome.serialize());
    teamData.put("members", teamMembers.serialize());
    teamData.put("displayname", getDisplayName());
    teamData.put("description", this.description);

    return teamData;
  }

  public void save() {
    final String path = "%datafolder%/teams/" + getName() + ".json";

    jsonUtil.save(path, serialize());

    setChanged(false);
  }

  public String getName() {
    return this.name;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public void addPlayer(final TeamPlayer teamPlayer, final Role role) {
    final String tPlayerName = teamPlayer.getName();
    sendMessage(ChatColor.translateAlternateColorCodes('&', "&b" + tPlayerName + " &ase unio a tu team!"));
    teamPlayer.setTeam(getName());
    this.teamMembers.getMembers().put(tPlayerName, role);
    setChanged(true);
  }

  public void addPlayer(final TeamPlayer teamPlayer) {
    addPlayer(teamPlayer, Role.MIEMBRO);
  }

  public void removePlayer(final TeamPlayer teamPlayer) {
    final String tPlayerName = teamPlayer.getName();
    final String team = teamPlayer.getTeam();
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

  public Role getRole(final String member) {
    return this.teamMembers.getMembers().getOrDefault(member, Role.MIEMBRO);
  }

  public Collection<String> getMembers(final Role role) {
    final Collection<String> filteredMembers = new HashSet<>();
    for (final Map.Entry<String, Role> member : this.teamMembers.getMembers().entrySet()) {
      if (member.getValue() == role)
        filteredMembers.add(member.getKey());
    }
    return filteredMembers;
  }

  public boolean isInvited(final String name) {
    for (final String name1 : this.teamMembers.getInvited()) {
      if (name1.equals(name))
        return true;
    }
    return false;
  }

  public void addInvited(final String name) {
    this.teamMembers.getInvited().add(name);
  }

  public void removeInvited(final String name) {
    this.teamMembers.getInvited().remove(name);
  }

  private void setChanged(final boolean changed) {
    this.changed = changed;
  }

  public boolean isChanged() {
    return this.changed;
  }

  public void setPvp(final boolean pvp) {
    this.pvp = pvp;
  }

  public boolean isPvp() {
    return this.pvp;
  }

  public boolean exists() {
    return !getMembers(Role.LIDER).isEmpty();
  }

  public int getKills() {
    final Server server = Bukkit.getServer();
    final TeamPlayerManager tPlayerManager = this.teamsManager.getTeamPlayerManager();
    int kills = 0;
    for (final String memberName : this.teamMembers.getMembers().keySet()) {
      final TeamPlayer teamPlayer = tPlayerManager.getPlayer(memberName);
      if (teamPlayer != null) {
        final UUID uuid = teamPlayer.getUUID();
        try {
          kills += Integer.parseInt(PlaceholderAPI.setPlaceholders(server.getPlayer(uuid), "%statistic_player_kills%"));
        } catch (final NumberFormatException numberFormatException) {
        }
      }
    }
    return kills;
  }

  public TeamHome getTeamHome() {
    return this.teamHome;
  }

  public void sendMessage(final Collection<String> filter, final String message) {
    final Server server = Bukkit.getServer();
    for (final String memberName : this.teamMembers.getOnline()) {
      if (!filter.contains(memberName)) {
        final Player player = server.getPlayer(memberName);
        if (player != null)
          player.sendMessage(message);
      }
    }
  }

  public void sendMessage(final String message) {
    sendMessage(new HashSet<>(), message);
  }

  public String getShow() {
    final Server server = Bukkit.getServer();
    final TeamShowBuilder teamShowBuilder = new TeamShowBuilder(server, this.teamsManager.getTeamManager());
    final Location home = this.teamHome.getHome(server);
    final String homeInfo = (home == null) ? "N/A"
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

  public void setDescription(final String description) {
    setChanged(true);
    this.description = description;
  }

  public TeamRelations getTeamRelations() {
    return this.teamRelations;
  }

  public void setName(final String newTeamName) {
    setChanged(true);
    this.displayName = newTeamName;
    this.name = newTeamName.toLowerCase();
  }
}
