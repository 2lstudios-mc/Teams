package dev._2lstudios.teams.utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtil {
  private static Plugin plugin;

  private static String dataFolder;

  private static void runTask(boolean sync, Runnable runnable) {
    if (sync) {
      runnable.run();
    } else if (!Bukkit.isPrimaryThread()) {
      runnable.run();
    } else {
      plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }
  }

  public static void initialize(Plugin plugin, String dataFolder) {
    JSONUtil.plugin = plugin;
    JSONUtil.dataFolder = dataFolder;
  }

  private static File create(String path) {
    File file = new File(path.replace("%datafolder%", dataFolder));
    try {
      delete(path, true);
      file.getParentFile().mkdirs();
      file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }

  public static void save(String path, JSONObject jsonObject, boolean sync) {
    Runnable runnable = () -> {
      File file = create(path);
      // TODO: Broken code
    };
    runTask(sync, runnable);
  }

  public static JSONObject get(String path) {
    File file = new File(path.replace("%datafolder%", dataFolder));
    if (file.exists() && !file.isDirectory()) {
      JSONParser jsonParser = new JSONParser();
      // TODO: Broken code
    }
    return new JSONObject();
  }

  public static void getAsync(String path, AtomicReference<JSONObject> reference, Runnable callback) {
    runTask(false, () -> {
      JSONObject jsonObject = get(path);
      reference.set(jsonObject);
      callback.run();
    });
  }

  public static void delete(String path, boolean sync) {
    Runnable runnable = () -> {
      File file = new File(path.replace("%datafolder%", dataFolder));
      file.delete();
    };
    runTask(sync, runnable);
  }
}
