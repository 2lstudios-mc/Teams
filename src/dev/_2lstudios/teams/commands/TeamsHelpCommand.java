package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class TeamsHelpCommand {
  private static String COMMAND_FORMAT = "&e/%label% %command% &b- %description%&r\n";

  private String applyFormat(String label, String command, String description) {
    return COMMAND_FORMAT.replace("%label%", label).replace("%command%", command).replace("%description%", description);
  }

  TeamsHelpCommand(CommandSender sender, String label) {
    StringBuilder helpBuilder = new StringBuilder("&aComandos de Teams:\n");
    helpBuilder.append(applyFormat(label, "rename", "Renombra a tu team!"));
    helpBuilder.append(applyFormat(label, "join", "Unete a un team que te haya invitado!"));
    helpBuilder.append(applyFormat(label, "create", "Crea tu propio team!"));
    helpBuilder.append(applyFormat(label, "invite", "Invita a un jugador a tu team!"));
    helpBuilder.append(applyFormat(label, "deinvite", "Desinvita a un jugador a tu team!"));
    helpBuilder.append(applyFormat(label, "delete", "Elimina tu team actual!"));
    helpBuilder.append(applyFormat(label, "show", "Muestra informacion de tu team o otro!"));
    helpBuilder.append(applyFormat(label, "home", "Teletransportate al hogar de tu team!"));
    helpBuilder.append(applyFormat(label, "sethome", "Establece al hogar de tu team!"));
    helpBuilder.append(applyFormat(label, "promote", "Promueve a alguien de tu team!"));
    helpBuilder.append(applyFormat(label, "demote", "Degrada a alguien de tu team!"));
    helpBuilder.append(applyFormat(label, "deposit", "Ingresa dinero a tu team!"));
    helpBuilder.append(applyFormat(label, "withdraw", "Retira dinero de tu team!"));
    helpBuilder.append(applyFormat(label, "pvp", "Alterna el PvP en tu team!"));
    helpBuilder.append(applyFormat(label, "chat", "Alterna el modo de chat con team!"));
    helpBuilder.append(applyFormat(label, "tl", "Muestra tus coordenadas a tu team!"));
    helpBuilder.append(applyFormat(label, "desc", "Establece la descripcion de tu team!"));
    helpBuilder.append(applyFormat(label, "leader", "Transfiere el liderazgo de tu team!"));
    helpBuilder.append(applyFormat(label, "ally", "Forma una alianza con otro team!"));
    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', helpBuilder.toString()));
  }
}
