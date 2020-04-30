package eu.internetpolice.potionhappiness.command;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PotionsGiveCommand extends AbstractPotionsCommand {
    public PotionsGiveCommand(PotionHappiness plugin) {
        super("give", plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("potions.give")) {
            sender.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        // potions give <effect> - User requests to get a potion effect applied
        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MSG_TARGET_REQUIRED);
                return true;
            }

            try {
                Player player = (Player) sender;
                PotionEffectType effect = getPotionEffectType(args[1]);

                plugin.getPotionManager().applyPotionEffect(effect, player);

                sender.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s for: %s",
                    effect.getName(), player.getDisplayName()));
            } catch (PotionNotFoundException ex) {
                sender.sendMessage(String.format(MSG_POTION_NOT_FOUND, args[1]));
            }
            return true;
        }

        // potions give <effect> <amplifier|other>
        else if (args.length == 3) {
            // 3th argument is an integer: user requests to get a potion effect with specified amplifier applied
            if (isInteger(args[2])) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MSG_TARGET_REQUIRED);
                    return true;
                }

                try {
                    Player player = (Player) sender;
                    PotionEffectType effect = getPotionEffectType(args[1]);
                    int amplifier = Integer.parseInt(args[2]);

                    plugin.getPotionManager().applyPotionEffect(effect, amplifier, player);

                    sender.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s %d for: %s",
                        effect.getName(), amplifier+1, player.getDisplayName()));
                } catch (PotionNotFoundException ex) {
                    sender.sendMessage(String.format(MSG_POTION_NOT_FOUND, args[1]));
                }
                return true;
            }
            // 3th argument is not an integer: user requests to apply a potion effect to another user.
            else {
                if (!sender.hasPermission("potions.give.others")) {
                    sender.sendMessage(MSG_NO_PERMISSION);
                    return true;
                }

                // Target is a player name
                if (isValidPlayerName(args[2])) {
                    try {
                        Player player = getOnlinePlayer(sender, args[2]);
                        PotionEffectType effect = getPotionEffectType(args[1]);

                        plugin.getPotionManager().applyPotionEffect(effect, player);

                        player.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s.",
                            effect.getName()));
                        sender.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s for: %s",
                            effect.getName(), player.getDisplayName()));
                    } catch (PlayerNotFoundException ex) {
                        sender.sendMessage(MSG_PLAYER_NOT_FOUND);
                    } catch (PotionNotFoundException ex) {
                        sender.sendMessage(String.format(MSG_POTION_NOT_FOUND, args[1]));
                    }
                    return true;
                }

                // Target is a uuid
                if (isValidUniqueId(args[2])) {
                    OfflinePlayer player = plugin.getServer().getOfflinePlayer(getUniqueId(args[2]));

                    try {
                        PotionEffectType effect = getPotionEffectType(args[1]);

                        plugin.getPotionManager().applyPotionEffect(effect, player);

                        if (player.isOnline()) {
                            Player onlinePlayer = (Player) player;
                            onlinePlayer.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s.",
                                effect.getName()));
                        }
                        sender.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s for: %s",
                            effect.getName(), player.getUniqueId().toString()));
                    } catch (PotionNotFoundException ex) {
                        sender.sendMessage(String.format(MSG_POTION_NOT_FOUND, args[1]));
                    }
                    return true;
                }
            }
        }
        // potions give <effect> <amplifier> <other>
        else if (args.length == 4) {
            if (!sender.hasPermission("potions.give.others")) {
                sender.sendMessage(MSG_NO_PERMISSION);
                return true;
            }

            if (!isInteger(args[2])) {
                sender.sendMessage(ChatColor.RED + "Error: Invalid amplifier: " + args[2]);
                return true;
            }

            // Target is a player name
            if (isValidPlayerName(args[3])) {
                try {
                    Player player = getOnlinePlayer(sender, args[3]);
                    PotionEffectType effect = getPotionEffectType(args[1]);
                    int amplifier = Integer.parseInt(args[2]);

                    plugin.getPotionManager().applyPotionEffect(effect, amplifier, player);

                    player.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s %d.",
                        effect.getName(), amplifier+1));
                    sender.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s %d for: %s",
                        effect.getName(), amplifier+1, player.getDisplayName()));
                } catch (PlayerNotFoundException ex) {
                    sender.sendMessage(MSG_PLAYER_NOT_FOUND);
                } catch (PotionNotFoundException ex) {
                    sender.sendMessage(String.format(MSG_POTION_NOT_FOUND, args[1]));
                }
                return true;
            }

            // Target is a uuid
            if (isValidUniqueId(args[3])) {
                OfflinePlayer player = plugin.getServer().getOfflinePlayer(getUniqueId(args[3]));

                try {
                    PotionEffectType effect = getPotionEffectType(args[1]);
                    int amplifier = Integer.parseInt(args[2]);

                    plugin.getPotionManager().applyPotionEffect(effect, amplifier, player);

                    if (player.isOnline()) {
                        Player onlinePlayer = (Player) player;
                        onlinePlayer.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s %d.",
                            effect.getName(), amplifier+1));
                    }
                    sender.sendMessage(ChatColor.GOLD + String.format("Applied potion effect %s %d for: %s",
                        effect.getName(), amplifier+1, player.getUniqueId().toString()));
                } catch (PotionNotFoundException ex) {
                    sender.sendMessage(String.format(MSG_POTION_NOT_FOUND, args[1]));
                }
                return true;
            }
        }

        return false;
    }

    private boolean isInteger(String input) {
        if (input.isEmpty()) {
            return false;
        }

        for (int i = 0; i < input.length(); i++) {
            if (i == 0 && input.charAt(i) == '-') {
                if (input.length() == 1) {
                    return false;
                }
                else {
                    continue;
                }
            }
            if (Character.digit(input.charAt(i), 10) < 0) {
                return false;
            }
        }
        return true;
    }
}
