package eu.internetpolice.potionhappiness.command;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PotionsClearCommand extends AbstractPotionsCommand {
    public PotionsClearCommand(PotionHappiness plugin) {
        super("clear", plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("potions.cleareffects")) {
            sender.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        // potions clear - User requests clearing of own potion effects
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MSG_TARGET_REQUIRED);
                return true;
            }

            plugin.getPotionManager().clearPotionEffects((Player) sender);
            sender.sendMessage(ChatColor.GOLD + String.format("Cleared all potion effects for %s",
                    ((Player) sender).getDisplayName()));
            return true;
        }

        //potions clear <other> - User requests clearing of others potion effects
        else if (args.length == 2) {
            if (!sender.hasPermission("potions.cleareffects.others")) {
                sender.sendMessage(MSG_NO_PERMISSION);
                return true;
            }

            // Target is a player name
            if (isValidPlayerName(args[1])) {
                try {
                    Player player = getOnlinePlayer(sender, args[1]);
                    plugin.getPotionManager().clearPotionEffects(player);

                    player.sendMessage(ChatColor.GOLD + "Your potion effects have been cleared!");
                    sender.sendMessage(ChatColor.GOLD + "Cleared potion effects for: " + player.getDisplayName());
                } catch (PlayerNotFoundException ex) {
                    sender.sendMessage(MSG_PLAYER_NOT_FOUND);
                }
                return true;
            }

            // Target is a uuid
            if (isValidUniqueId(args[1])) {
                OfflinePlayer player = plugin.getServer().getOfflinePlayer(getUniqueId(args[1]));

                plugin.getPotionManager().clearPotionEffects(player);
                if (player.isOnline()) {
                    Player onlinePlayer = (Player) player;
                    onlinePlayer.sendMessage(ChatColor.GOLD + "Your potion effects have been cleared!");
                }
                sender.sendMessage(ChatColor.GOLD + "Cleared potion effects for: " +
                        player.getUniqueId().toString());
                return true;
            }
        }

        return false;
    }
}
