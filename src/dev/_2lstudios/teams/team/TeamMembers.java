package dev._2lstudios.teams.team;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;

import dev._2lstudios.teams.enums.Role;

public class TeamMembers {
  private final Map<String, Role> members = new HashMap<>();
  private final Collection<String> online = new HashSet<>();
  private final Collection<String> invited = new HashSet<>();

  public Role promote(String memberName) {
    Role toGive = Role.MIEMBRO;
    if (this.members.containsKey(memberName)) {
      Role currentRole = this.members.get(memberName);
      if (currentRole == Role.MIEMBRO) {
        toGive = Role.MOD;
      } else if (currentRole == Role.MOD) {
        toGive = Role.COLIDER;
      }
      this.members.put(memberName, toGive);
    }
    return toGive;
  }

  public Role demote(String memberName) {
    Role toGive = Role.MIEMBRO;
    if (this.members.containsKey(memberName)) {
      Role currentRole = this.members.get(memberName);
      if (currentRole == Role.COLIDER) {
        toGive = Role.MOD;
      } else if (currentRole == Role.MOD) {
        toGive = Role.MIEMBRO;
      }
      this.members.put(memberName, toGive);
    }
    return toGive;
  }

  public void deserialize(Plugin plugin, JSONObject jsonObject) {
    try {
      JSONObject jsonMembers = (JSONObject) jsonObject.getOrDefault("members", new JSONObject());
      for (Object memberObject : jsonMembers.entrySet()) {
        try {
          Map.Entry<String, String> member = (Map.Entry<String, String>) memberObject;
          String memberKey = member.getKey();
          if (memberKey != null && !memberKey.isEmpty())
            this.members.put(memberKey, Role.valueOf(member.getValue()));
        } catch (Exception exception) {
          plugin.getLogger().info("Found invalid member. Exception: " + exception);
        }
      }
    } catch (Exception exception) {
      Collection<String> memberCollection = (Collection<String>) jsonObject.getOrDefault("members", new HashSet());
      for (String member : memberCollection)
        this.members.put(member, Role.MIEMBRO);
      plugin.getLogger().info("Failed to load member data from team file: " + exception);
    }
  }

  public JSONObject serialize() {
    JSONObject membersData = new JSONObject();

    for (Map.Entry<String, Role> member : this.members.entrySet()) {
      membersData.put(member.getKey(), ((Role) member.getValue()).name());
    }

      return membersData;
  }

  public Map<String, Role> getMembers() {
    return this.members;
  }

  public Collection<String> getInvited() {
    return this.invited;
  }

  public Collection<String> getOnline() {
    return this.online;
  }
}
