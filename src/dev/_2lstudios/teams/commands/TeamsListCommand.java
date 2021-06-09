package dev._2lstudios.teams.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.Team;
import dev._2lstudios.teams.team.TeamOnlineComparator;

class TeamsListCommand {
  private static final BaseComponent[] INFORMATION_COMPONENT = TextComponent
      .fromLegacyText("Click para ver informacion!");

  private List<Team> getOnlineTeams(TeamManager teamManager) {
    List<Team> onlineTeams = new ArrayList<>();
    for (Team team : teamManager.getCachedTeams()) {
      Collection<String> onlineMembers = team.getTeamMembers().getOnline();
      if (!onlineMembers.isEmpty())
        onlineTeams.add(team);
    }
    onlineTeams.sort((Comparator<? super Team>) new TeamOnlineComparator());
    return onlineTeams;
  }

  private void sendTeamEntry(CommandSender sender, Team team, int index) {
    int onlinePlayers = team.getTeamMembers().getOnline().size();
    int totalPlayers = team.getMembers().size();
    String teamName = team.getDisplayName();
    String teamInfo = "&f " + (index + 1) + ". &c" + teamName + "&7 [&a" + onlinePlayers + "&7/" + totalPlayers
        + " Online]";
    if (sender instanceof Player) {
      ComponentBuilder component = new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', teamInfo));
      component.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f who " + teamName));
      component.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, INFORMATION_COMPONENT));
      ((Player) sender).spigot().sendMessage(component.create());
    } else {
      sender.sendMessage(ChatColor.translateAlternateColorCodes('&', teamInfo));
    }
  }

  TeamsListCommand(TeamManager teamManager, CommandSender sender, String[] args) {
    int page = 1;
    if (args.length > 1)
      try {
        page = Integer.parseInt(args[1]);
      } catch (NumberFormatException numberFormatException) {
      }
    if (page < 1) {
      sender.sendMessage(ChatColor.RED + "Especifica una pagina mayor a 0!");
      return;
    }
    List<Team> onlineTeams = getOnlineTeams(teamManager);
    int onlineTeamsSize = onlineTeams.size();
    int maxPage = onlineTeamsSize / 10 + 1;
    if (page > maxPage) {
      sender.sendMessage(ChatColor.RED + "Pagina no encontrada!");
      return;
    }
    int maxEntryNumber = page * 10;
    sender.sendMessage(
        ChatColor.translateAlternateColorCodes('&', "&aLista de Teams: &7(Pagina " + page + "/" + maxPage + ")"));
    for (int i = maxEntryNumber - 10; i < maxEntryNumber && i >= 0 && i < onlineTeamsSize; i++) {
      Team team = onlineTeams.get(i);
      sendTeamEntry(sender, team, i);
    }
  }
}
