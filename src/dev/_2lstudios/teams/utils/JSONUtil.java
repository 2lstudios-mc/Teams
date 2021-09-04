package dev._2lstudios.teams.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtil {
  private static Logger logger;
  private static String dataFolder;

  public JSONUtil(final Logger logger, final String dataFolder) {
    JSONUtil.logger = logger;
    JSONUtil.dataFolder = dataFolder;
  }

  private void log(final String text) {
    if (logger != null) {
      logger.info(text);
    }
  }

  private File create(final String path) {
    final File file = new File(path.replace("%datafolder%", dataFolder));

    try {
      delete(path);
      file.getParentFile().mkdirs();
      file.createNewFile();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return file;
  }

  public void save(final String path, final JSONObject jsonObject) {
    final File file = create(path);

    try (final Writer output = new BufferedWriter(new FileWriter(file))) {
      output.write(jsonObject.toJSONString());
    } catch (IOException e) {
      log("Can't save JSON: " + file.getPath());
    }
  }

  public JSONObject get(final String path) {
    final File file = new File(path.replace("%datafolder%", dataFolder));
    if (file.exists() && !file.isDirectory()) {
      final JSONParser jsonParser = new JSONParser();

      try {
        return (JSONObject) jsonParser.parse(new FileReader(file));
      } catch (IOException | ParseException e) {
        log("Can't get JSON: " + file.getPath());
      }
    }

    return new JSONObject();
  }

  public void delete(final String path) {
    final File file = new File(path.replace("%datafolder%", dataFolder));

    file.delete();
  }
}
