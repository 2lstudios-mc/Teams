package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev._2lstudios.teams.enums.Relation;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;
import dev._2lstudios.teams.team.TeamRelations;

public class TeamsAllyCommand {
  TeamsAllyCommand(TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender, String label,
      String[] args) {
    if (args.length > 1) {
      String senderName = sender.getName();
      TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
      if (tPlayer != null) {
        String teamName = tPlayer.getTeam();
        Team team = teamManager.getTeam(teamName);
        if (team != null) {
          Role role = team.getRole(senderName);
          if (role.getPower() >= Role.MOD.getPower()) {
            Team targetTeam = teamManager.getTeam(args[1]);
            if (targetTeam != null && targetTeam.exists()) {
              if (targetTeam != team) {
                String targetTeamName = targetTeam.getName();
                TeamRelations teamRelations = team.getTeamRelations();
                TeamRelations targetTeamRelations = targetTeam.getTeamRelations();
                Relation relation = teamRelations.getRelation(targetTeamName);
                Relation targetRelation = targetTeamRelations.getRelation(teamName);
                if (relation == Relation.ALLY) {
                  if (targetRelation == Relation.ALLY) {
                    team.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cSe ha roto la alianza con &b" + targetTeamName + "&c!"));
                    targetTeam.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cSe ha roto la alianza con &b" + targetTeamName + "&c!"));
                  } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cCancelaste la solicitud de alianza a &b" + targetTeamName + "&c!"));
                  }
                  targetTeamRelations.setRelation(teamName, Relation.ENEMY);
                  teamRelations.setRelation(targetTeamName, Relation.ENEMY);
                } else {
                  if (targetRelation == Relation.ALLY) {
                    team.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aSe ha formado una alianza con &b" + targetTeamName + "&a!"));
                    targetTeam.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aSe ha formado una alianza con &b" + targetTeamName + "&a!"));
                  } else {
                    team.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aSe ha enviado una solicitud de alianza a &b" + targetTeamName + "&a!"));
                    targetTeam.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aEl team &b" + teamName + "&a ha enviado una solicitud de alianza!"));
                  }
                  teamRelations.setRelation(targetTeamName, Relation.ALLY);
                }
              } else {
                sender.sendMessage(ChatColor.RED + "No puedes enviar una solicitud de alianza a tu mismo team!");
              }
            } else {
              sender.sendMessage(ChatColor.RED + "El team " + ChatColor.AQUA + args[1] + ChatColor.RED + " no existe!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "Debes ser lider/colider/mod para realizar esta accion!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "No eres miembro de un team!");
        }
      } else {
        sender.sendMessage(ChatColor.RED + "Tus datos no estan cargados!");
      }
    } else {
      sender.sendMessage(ChatColor.RED + "/" + label + " ally <team>");
    }
  }
}
