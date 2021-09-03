package dev._2lstudios.teams;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import dev._2lstudios.teams.commands.HeadQuartersCommand;
import dev._2lstudios.teams.commands.TeamsCommand;
import dev._2lstudios.teams.commands.TellLocationCommand;
import dev._2lstudios.teams.listeners.AsyncPlayerChatListener;
import dev._2lstudios.teams.listeners.EntityCombustByEntityListener;
import dev._2lstudios.teams.listeners.EntityDamageByEntityListener;
import dev._2lstudios.teams.listeners.EntityDamageListener;
import dev._2lstudios.teams.listeners.PlayerJoinListener;
import dev._2lstudios.teams.listeners.PlayerMoveListener;
import dev._2lstudios.teams.listeners.PlayerQuitListener;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.placeholders.TeamsPlaceholders;
import dev._2lstudios.teams.team.Team;
import dev._2lstudios.teams.utils.ConfigurationUtil;
import dev._2lstudios.teams.utils.JSONUtil;
import net.milkbowl.vault.economy.Economy;

public class Teams extends JavaPlugin {
  private Economy economy;
  private static TeamsManager teamsManager;
  private TeamsPlaceholders teamsPlaceholders = null;

  public static TeamsManager getTeamsManager() {
    return teamsManager;
  }

  private void setupConfigurations(ConfigurationUtil configurationUtil) {
    configurationUtil.createConfiguration("%datafolder%/config.yml");
  }

  private void setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") != null) {
      RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
      if (rsp != null)
        this.economy = (Economy) rsp.getProvider();
    }
  }

  public void onEnable() {
    Server server = getServer();
    BukkitScheduler scheduler = server.getScheduler();
    PluginManager pluginManager = server.getPluginManager();
    ConfigurationUtil configurationUtil = new ConfigurationUtil(this);

    setupEconomy();
    setupConfigurations(configurationUtil);
    JSONUtil.initialize(this, getDataFolder().toString());
    teamsManager = new TeamsManager(this, configurationUtil);

    TeamManager teamManager = teamsManager.getTeamManager();
    TeamPlayerManager tPlayerManager = teamsManager.getTeamPlayerManager();

    for (Player player : server.getOnlinePlayers()) {
      String playerName = player.getName();
      Team team = teamManager.getTeam(tPlayerManager.getPlayer(playerName).getTeam());

      team.getTeamMembers().getOnline().add(playerName);
    }

    pluginManager.registerEvents(new AsyncPlayerChatListener(teamsManager), this);
    pluginManager.registerEvents(new EntityCombustByEntityListener(teamsManager), this);
    pluginManager.registerEvents(new EntityDamageByEntityListener(teamsManager), this);
    pluginManager.registerEvents(new EntityDamageListener(teamsManager), this);
    pluginManager.registerEvents(new PlayerJoinListener(teamsManager), this);
    pluginManager.registerEvents(new PlayerMoveListener(teamsManager), this);
    pluginManager.registerEvents(new PlayerQuitListener(teamsManager), this);

    boolean homesEnabled = configurationUtil.getConfiguration("%datafolder%/config.yml").getBoolean("homes", true);

    getCommand("teams").setExecutor(new TeamsCommand(this, this.economy, teamsManager, homesEnabled));
    getCommand("tl").setExecutor(new TellLocationCommand(this, teamsManager));
    getCommand("hq").setExecutor(new HeadQuartersCommand(this, teamsManager, homesEnabled));

    if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
      teamsPlaceholders = new TeamsPlaceholders(this, teamsManager);
      teamsPlaceholders.register();
    }

    scheduler.runTaskTimerAsynchronously(this, () -> {
      teamManager.update(false, false);
      tPlayerManager.update(false, false);
    }, 1200L, 1200L);

    scheduler.runTaskTimer(this, teamsManager.getTeleportSystem(), 20L, 20L);
  }

  public void onDisable() {
    TeamManager teamManager = teamsManager.getTeamManager();
    TeamPlayerManager tPlayerManager = teamsManager.getTeamPlayerManager();

    teamManager.update(true, true);
    tPlayerManager.update(true, true);

    if (teamsPlaceholders != null)
      teamsPlaceholders.unregister();
  }
}
