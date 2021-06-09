package dev._2lstudios.teams.utils;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import dev._2lstudios.teams.enums.Relation;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.Team;
import dev._2lstudios.teams.team.TeamRelations;

public class TeamShowBuilder {
  private final Server server;
  private final TeamManager teamManager;
  private static final String FORMAT = "&aInformacion de %name%:\n&eHome: &7%home%\n&eKills: &c%kills%\n&eDinero: &6$%money%\n&eLideres: &7(&b%leaders_size%&7) %leaders%\n&eCoLideres: &7(&b%coleaders_size%&7) %coleaders%\n&eMods: &7(&b%mods_size%&7) %mods%\n&eMiembros: &7(&b%members_size%&7) %members%\n&eAliados: &7(&b%allies_amount%&7) &b%allies%\n&eDescripcion: &7%description%";
  
  public TeamShowBuilder(Server server, TeamManager teamManager) {
    this.server = server;
    this.teamManager = teamManager;
  }
  
  public String getFormat() {
    return FORMAT;
  }
  
  public void applyMember(StringBuilder currentBuilder, String name) {
    Player player = this.server.getPlayer(name);
    if (currentBuilder.length() < 3) {
      if (player != null) {
        currentBuilder.append(ChatColor.GREEN + name);
      } else {
        currentBuilder.append(ChatColor.GRAY + name);
      } 
    } else if (player != null) {
      currentBuilder.append(ChatColor.GRAY + ", " + ChatColor.GREEN + name);
    } else {
      currentBuilder.append(ChatColor.GRAY + ", " + name);
    } 
  }
  
  public String applyAllies(Team team, String format) {
    TeamRelations teamRelations = team.getTeamRelations();
    StringBuilder allyBuilder = new StringBuilder();
    int allyAmount = 0;
    for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)teamRelations.getRelations().entrySet()) {
      String key = entry.getKey();
      Relation mutualRelation = teamRelations.getMutual(this.teamManager.getTeam(key));
      if (mutualRelation == Relation.ALLY) {
        if (allyBuilder.length() < 3) {
          allyBuilder.append(ChatColor.AQUA + key);
        } else {
          allyBuilder.append(ChatColor.GRAY + ", " + ChatColor.AQUA + key);
        } 
        allyAmount++;
      } 
    } 
    return format.replace("%allies%", allyBuilder.toString()).replace("%allies_amount%", 
        String.valueOf(allyAmount));
  }
  
  public String applyMembers(Team team, String format) {
    StringBuilder leadersBuilder = new StringBuilder(ChatColor.GRAY.toString());
    StringBuilder coleadersBuilder = new StringBuilder(ChatColor.GRAY.toString());
    StringBuilder modsBuilder = new StringBuilder(ChatColor.GRAY.toString());
    StringBuilder membersBuilder = new StringBuilder(ChatColor.GRAY.toString());
    EnumMap<Role, Integer> roleAmounts = new EnumMap<>(Role.class);
    for (Map.Entry<String, Role> member : (Iterable<Map.Entry<String, Role>>)team.getMembers().entrySet()) {
      StringBuilder currentBuilder;
      String name = member.getKey();
      Role role = member.getValue();
      if (role == Role.LIDER) {
        currentBuilder = leadersBuilder;
      } else if (role == Role.COLIDER) {
        currentBuilder = coleadersBuilder;
      } else if (role == Role.MOD) {
        currentBuilder = modsBuilder;
      } else {
        currentBuilder = membersBuilder;
      } 
      roleAmounts.put(role, Integer.valueOf(((Integer)roleAmounts.getOrDefault(role, Integer.valueOf(0))).intValue() + 1));
      applyMember(currentBuilder, name);
    } 
    return format.replace("%leaders_size%", String.valueOf(roleAmounts.getOrDefault(Role.LIDER, Integer.valueOf(0))))
      .replace("%leaders%", String.valueOf(leadersBuilder.toString().trim()))
      .replace("%coleaders_size%", String.valueOf(roleAmounts.getOrDefault(Role.COLIDER, Integer.valueOf(0))))
      .replace("%coleaders%", String.valueOf(coleadersBuilder.toString().trim()))
      .replace("%mods_size%", String.valueOf(roleAmounts.getOrDefault(Role.MOD, Integer.valueOf(0))))
      .replace("%mods%", String.valueOf(modsBuilder.toString().trim()))
      .replace("%members_size%", String.valueOf(roleAmounts.getOrDefault(Role.MIEMBRO, Integer.valueOf(0))))
      .replace("%members%", String.valueOf(membersBuilder.toString().trim()));
  }
}
