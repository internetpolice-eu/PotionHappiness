package eu.internetpolice.potionhappiness.command;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PotionsRemoveCommand extends AbstractPotionsCommand {
    public PotionsRemoveCommand(PotionHappiness plugin) {
        super("remove", plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("potions.remove")) {
            sender.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        // potions remove <effect> - User requests removing of the specified potion effect
        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MSG_TARGET_REQUIRED);
                return true;
            }

            try {
                Player player = (Player) sender;
                PotionEffectType effect = getPotionEffectType(args[1]);
                plugin.getPotionManager().removePotionEffect(effect, player);
                sender.sendMessage(ChatColor.GOLD + String.format("Cleared potion effect %s for: %s",
                    effect.getName(), player.getDisplayName()));
            } catch (PotionNotFoundException ex) {
                sender.sendMessage(String.format(MSG_POTION_NOT_FOUND, args[1]));
            }
            return true;
        }

        // potions remove <effect> <other> - User requests removing of other players specified potion effect
        else if (args.length == 3) {
            if (!sender.hasPermission("potions.remove.others")) {
                sender.sendMessage(MSG_NO_PERMISSION);
                return true;
            }

            // Target is a player name
            if (isValidPlayerName(args[2])) {
                try {
                    Player player = getOnlinePlayer(sender, args[2]);
                    PotionEffectType effect = getPotionEffectType(args[1]);
                    plugin.getPotionManager().removePotionEffect(effect, player);

                    player.sendMessage(ChatColor.GOLD + String.format("Your potion effect %s has been removed.",
                        effect.getName()));
                    sender.sendMessage(ChatColor.GOLD + String.format("Removed potion effect %s for: %s",
                        effect.getName(), player.getDisplayName()));
                } catch (PlayerNotFoundException ex) {
                    sender.sendMessage(MSG_PLAYER_NOT_FOUND);
                } catch (PotionNotFoundException ex) {
                    sender.sendMessage(MSG_POTION_NOT_FOUND);
                }
                return true;
            }

            // Target is a uuid
            if (isValidUniqueId(args[2])) {
                OfflinePlayer player = plugin.getServer().getOfflinePlayer(getUniqueId(args[2]));

                try {
                    PotionEffectType effect = getPotionEffectType(args[1]);
                    plugin.getPotionManager().removePotionEffect(effect, player);

                    if (player.isOnline()) {
                        Player onlinePlayer = (Player) player;
                        onlinePlayer.sendMessage(ChatColor.GOLD + String.format("Your potion effect %s has been removed.",
                            effect.getName()));
                    }
                    sender.sendMessage(ChatColor.GOLD + String.format("Removed potion effect %s for: %s",
                        effect.getName(), player.getUniqueId().toString()));
                } catch (PotionNotFoundException ex) {
                    sender.sendMessage(MSG_POTION_NOT_FOUND);
                }
                return true;
            }
        }
        return false;
    }
}
