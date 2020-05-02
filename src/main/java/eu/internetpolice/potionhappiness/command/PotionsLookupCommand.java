package eu.internetpolice.potionhappiness.command;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PotionsLookupCommand extends AbstractPotionsCommand {
    public PotionsLookupCommand(PotionHappiness plugin) {
        super("lookup", plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("potions.lookup")) {
            sender.sendMessage(MSG_NO_PERMISSION);
            return true;
        }

        // potions lookup - User requests it's own data
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(MSG_TARGET_REQUIRED);
                return true;
            }

            Player player = (Player) sender;
            Map<PotionEffectType, Integer> userMap = plugin.getPotionManager().getAppliedEffects(player);
            if (userMap == null) {
                sender.sendMessage(ChatColor.GOLD + "You have no potion effects applied.");
                return true;
            }

            List<String> effectList = new ArrayList<>();
            userMap.forEach((effect, amplifier) ->
                effectList.add(String.format("%s %d", effect.getName(), amplifier+1)));
            sender.sendMessage(ChatColor.GOLD + "You have the following potion effects applied: ");
            sender.sendMessage(ChatColor.RESET + effectList.toString());
            return true;
        }

        // potions lookup <target> - User requests another users data
        if (args.length == 2) {
            if (!sender.hasPermission("potions.lookup.others")) {
                sender.sendMessage(MSG_NO_PERMISSION);
            }

            // Target is a player name
            if (isValidPlayerName(args[1])) {
                try {
                    Player player = getOnlinePlayer(sender, args[1]);
                    Map<PotionEffectType, Integer> userMap = plugin.getPotionManager().getAppliedEffects(player);

                    if (userMap == null) {
                        sender.sendMessage(ChatColor.GOLD + String.format(
                            "%s has no potion effects applied by PotionHappiness.", player.getDisplayName()));
                        return true;
                    }

                    List<String> effectList = new ArrayList<>();
                    userMap.forEach((effect, amplifier) ->
                        effectList.add(String.format("%s %d", effect.getName(), amplifier+1)));

                    sender.sendMessage(ChatColor.GOLD + String.format("%s has the following potion effects applied" +
                        "by PotionHappiness: ", player.getDisplayName()));
                    sender.sendMessage(ChatColor.RESET + effectList.toString());
                    return true;
                } catch (PlayerNotFoundException ex) {
                    sender.sendMessage(MSG_PLAYER_NOT_FOUND);
                }
                return true;
            }

            // Target is a uuid
            if (isValidUniqueId(args[1])) {
                OfflinePlayer player = plugin.getServer().getOfflinePlayer(getUniqueId(args[1]));

                Map<PotionEffectType, Integer> userMap = plugin.getPotionManager().getAppliedEffects(player);

                if (userMap == null) {
                    sender.sendMessage(ChatColor.GOLD + String.format(
                        "%s has no potion effects applied by PotionHappiness.", player.getUniqueId().toString()));
                    return true;
                }

                List<String> effectList = new ArrayList<>();
                userMap.forEach((effect, amplifier) ->
                    effectList.add(String.format("%s %d", effect.getName(), amplifier+1)));

                sender.sendMessage(ChatColor.GOLD + String.format("%s has the following potion effects applied" +
                    "by PotionHappiness: ", player.getUniqueId().toString()));
                sender.sendMessage(ChatColor.RESET + effectList.toString());
                return true;
            }
        }

        return false;
    }
}
