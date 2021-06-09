package dev._2lstudios.teams.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigurationUtil {
  private final Plugin plugin;
  
  public ConfigurationUtil(Plugin plugin) {
    this.plugin = plugin;
  }
  
  public YamlConfiguration getConfiguration(String filePath) {
    File file = new File(filePath.replace("%datafolder%", this.plugin.getDataFolder().toPath().toString()));
    if (file.exists())
      try {
        return YamlConfiguration.loadConfiguration(file);
      } catch (Exception e) {
        return new YamlConfiguration();
      }  
    return new YamlConfiguration();
  }
  
  public void createConfiguration(String filePath) {
    try {
      File file = new File(filePath.replace("%datafolder%", this.plugin.getDataFolder().toPath().toString()));
      if (!file.exists()) {
        String[] files = filePath.split("/");
        InputStream inputStream = this.plugin.getClass().getClassLoader()
          .getResourceAsStream(files[files.length - 1]);
        File parentFile = file.getParentFile();
        if (parentFile != null)
          parentFile.mkdirs(); 
        if (inputStream != null) {
          Files.copy(inputStream, file.toPath(), new java.nio.file.CopyOption[0]);
        } else {
          file.createNewFile();
        } 
        this.plugin.getLogger().info(("File " + file + " has been created!").replace("%pluginname%", 
              this.plugin.getDescription().getName()));
      } 
    } catch (IOException e) {
      this.plugin.getLogger().info("Unable to create configuration file!".replace("%pluginname%", 
            this.plugin.getDescription().getName()));
    } 
  }
  
  public void saveConfiguration(FileConfiguration yamlConfiguration, String path, boolean sync) {
    if (sync) {
      saveConfiguration(yamlConfiguration, path);
    } else {
      this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> saveConfiguration(yamlConfiguration, path));
    } 
  }
  
  public void saveConfiguration(FileConfiguration yamlConfiguration, String path) {
    try {
      if (yamlConfiguration != null) {
        File dataFolder = this.plugin.getDataFolder();
        yamlConfiguration.save(path.replace("%datafolder%", dataFolder.toPath().toString()));
      } 
    } catch (IOException e) {
      this.plugin.getLogger().info("Unable to save configuration file!".replace("%pluginname%", 
            this.plugin.getDescription().getName()));
    } 
  }
  
  public void deleteConfiguration(String filePath, boolean sync) {
    if (sync) {
      deleteConfiguration(filePath);
    } else {
      this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> deleteConfiguration(filePath));
    } 
  }
  
  private void deleteConfiguration(String filePath) {
    File file = new File(filePath.replace("%datafolder%", this.plugin.getDataFolder().toString()));
    if (file.exists())
      file.delete(); 
  }
}
