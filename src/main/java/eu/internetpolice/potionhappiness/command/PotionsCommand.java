package eu.internetpolice.potionhappiness.command;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionsCommand implements CommandExecutor, TabCompleter {
    private Map<String, AbstractPotionsCommand> commandMap = new HashMap<>();
    private PotionHappiness plugin;

    public PotionsCommand(PotionHappiness plugin) {
        this.plugin = plugin;

        registerCommand(new PotionsClearCommand(plugin));
        registerCommand(new PotionsGiveCommand(plugin));
        registerCommand(new PotionsLookupCommand(plugin));
        registerCommand(new PotionsRemoveCommand(plugin));
    }

    private void registerCommand(AbstractPotionsCommand command) {
        String alias = command.getAlias().toLowerCase(java.util.Locale.ENGLISH).trim();
        if (!commandMap.containsKey(alias)) {
            commandMap.put(alias, command);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean success = false;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "You can use the following potion effects: " +
                ChatColor.RESET + plugin.getPotionManager().getAvailableEffects().toString());
            sender.sendMessage(ChatColor.GOLD + "Use \"/potions help\" for help.");
            success = true;
        } else {
            if (commandMap.containsKey(args[0])) {
                success = commandMap.get(args[0]).onCommand(sender, command, label, args);
            }
        }

        if (!success) {
            sender.sendMessage(ChatColor.RED + "Error: " + "Unknown command. Use \"/potions help\" for help.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();

        // Args length should always be 1 or higher for tab complete, but inculding 0 to be sure.
        if (args.length <= 1) {
            suggestions.addAll(commandMap.keySet());
        } else {
            if (commandMap.containsKey(args[0])) {
                suggestions.addAll(commandMap.get(args[0]).onTabComplete(sender, command, alias, args));
            }
        }
        suggestions.sort(String::compareTo);
        return suggestions;
    }
}
