package dev._2lstudios.teams.enums;

public enum Relation {
  ENEMY("&c"),
  ALLY("&b"),
  TEAM("&a");
  
  private String prefix;
  
  Relation(String prefix) {
    this.prefix = prefix;
  }
  
  public String getPrefix() {
    return this.prefix;
  }
  
  public static Relation fromString(String string, Relation def) {
    try {
      return valueOf(string);
    } catch (Exception exception) {
      return def;
    } 
  }
}
