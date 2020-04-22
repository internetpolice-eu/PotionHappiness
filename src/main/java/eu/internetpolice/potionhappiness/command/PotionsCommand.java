package eu.internetpolice.potionhappiness.command;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class PotionsCommand implements CommandExecutor {
    private PotionHappiness plugin;

    public PotionsCommand(PotionHappiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Error: You need to specify a target user when executing this " +
                        "command as a non-player.");
                return true;
            }

            Player player = (Player) sender;
            plugin.getPotionManager().applyPotionEffect(PotionEffectType.BLINDNESS, player);
        }

        return false;
    }
}
