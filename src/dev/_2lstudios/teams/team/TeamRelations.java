package dev._2lstudios.teams.team;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import dev._2lstudios.teams.enums.Relation;

public class TeamRelations {
  private final Map<String, String> relations = new HashMap<>();
  
  private final String teamName;
  
  public TeamRelations(String teamName) {
    this.teamName = teamName;
  }
  
  public void load(JSONObject jsonObject) {
    this.relations.clear();
    this.relations.putAll((Map<? extends String, ? extends String>)jsonObject.getOrDefault("relations", new HashMap<>()));
  }
  
  public void save(JSONObject jsonObject) {
    Map<String, String> filteredRelations = new HashMap<>();
    for (Map.Entry<String, String> entry : this.relations.entrySet()) {
      String key = entry.getKey();
      if (!key.equals(Relation.TEAM.name()) && !key.equals(Relation.ENEMY.name()))
        filteredRelations.put(key, entry.getValue()); 
    } 
    if (!filteredRelations.isEmpty())
      jsonObject.put("relations", new JSONObject(filteredRelations)); 
  }
  
  public Relation getRelation(String teamName) {
    String relation = this.relations.getOrDefault(teamName, Relation.ENEMY.name());
    try {
      return Relation.valueOf(relation);
    } catch (Exception exception) {
      return Relation.ENEMY;
    } 
  }
  
  public void setRelation(String teamName, Relation teamRelation) {
    if (teamRelation != Relation.ENEMY) {
      this.relations.put(teamName, teamRelation.name());
    } else {
      this.relations.remove(teamName);
    } 
  }
  
  public Map<String, String> getRelations() {
    return this.relations;
  }
  
  public Relation getMutual(Team team) {
    Relation relation = getRelation(team.getName());
    Relation relation1 = team.getTeamRelations().getRelation(this.teamName);
    if (relation == relation1)
      return relation; 
    return Relation.ENEMY;
  }
  
  public Collection<String> getTeams() {
    return this.relations.keySet();
  }
}
