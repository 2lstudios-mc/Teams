package dev._2lstudios.teams.enums;

public enum Role {
  LIDER(3),
  COLIDER(2),
  MOD(1),
  MIEMBRO(0);
  
  private int power;
  
  Role(int power) {
    this.power = power;
  }
  
  public int getPower() {
    return this.power;
  }
}
