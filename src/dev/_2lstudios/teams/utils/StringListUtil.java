package dev._2lstudios.teams.utils;

import java.util.List;

public class StringListUtil {
  public static boolean equals(List<String> list1, List<String> list2) {
    if (list1.size() != list2.size())
      return false; 
    for (String string : list1) {
      boolean bool = false;
      for (String string2 : list2) {
        if (string.equals(string2)) {
          bool = true;
          break;
        } 
      } 
      if (!bool)
        return false; 
    } 
    return true;
  }
  
  public static boolean containsEquals(List<String> list, String string) {
    for (String string1 : list) {
      if (string.equals(string1))
        return true; 
    } 
    return false;
  }
}
