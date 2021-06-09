package dev._2lstudios.teams.team;

import java.util.Comparator;

public class TeamOnlineComparator implements Comparator<Team> {
  public int compare(Team team1, Team team2) {
    int onlineMembers1 = team1.getTeamMembers().getOnline().size();
    int onlineMembers2 = team2.getTeamMembers().getOnline().size();
    return Integer.compare(onlineMembers2, onlineMembers1);
  }
}
