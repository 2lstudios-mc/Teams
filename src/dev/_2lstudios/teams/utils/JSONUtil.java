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
      try {
        Exception exception2;
        Exception exception1 = null;
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
    runTask(sync, runnable);
  }

  public static JSONObject get(String path) {
    File file = new File(path.replace("%datafolder%", dataFolder));
    if (file.exists() && !file.isDirectory()) {
      JSONParser jsonParser = new JSONParser();
      try {
        Exception exception1 = null, exception2 = null;
        try {

        } finally {
          exception2 = null;
          if (exception1 == null) {
            exception1 = exception2;
          } else if (exception1 != exception2) {
            exception1.addSuppressed(exception2);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      }
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
