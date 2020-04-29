package eu.internetpolice.potionhappiness.command;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractPotionsCommand {
    private final String alias;
    protected final PotionHappiness plugin;

    /* Common messages */
    protected final String MSG_NO_PERMISSION = ChatColor.RED + "I'm sorry, but you do not have permission to perform " +
            "this command. Please contact the server administrators if you believe that this is in error.";
    protected final String MSG_PLAYER_NOT_FOUND = ChatColor.RED + "Error: The requested player cannot be found.";
    protected final String MSG_POTION_NOT_FOUND = ChatColor.RED + "Error: The requested potion effect (%s) cannot be found.";
    protected final String MSG_TARGET_REQUIRED = ChatColor.RED + "Error: You need to specify a target user when " +
            "executing this command as a non-player.";

    /* Regular expressions for user input checks */
    protected final String REGEX_VALID_NAME = "[a-zA-Z0-9_]{1,16}";
    protected final String REGEX_VALID_PLAIN_UUID = "[0-9a-f]{32}";
    protected final String REGEX_VALID_DASHED_UUID = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    public AbstractPotionsCommand(@NotNull String alias, @NotNull PotionHappiness plugin) {
        this.alias = alias;
        this.plugin = plugin;
    }

    public @NotNull String getAlias() {
        return alias;
    }

    /**
     * Executes the given command, returning its success.
     *
     * If false is returned, the plugin will send an error message to the user referring to the help command.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender Source of the command.  For players tab-completing a
     *     command inside of a command block, this will be the player, not
     *     the command block.
     * @param command Command which was executed
     * @param alias The alias used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     *     to default to the command executor
     */
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    protected PotionEffectType getPotionEffectType(String input) throws PotionNotFoundException {
        if (input == null) {
            throw new PotionNotFoundException();
        }

        PotionEffectType foundEffect = PotionEffectType.getByName(input);
        if (foundEffect == null) {
            throw new PotionNotFoundException();
        }
        return foundEffect;
    }

    protected Player getOnlinePlayer(CommandSender requester, String input) throws PlayerNotFoundException {
        Player foundPlayer = plugin.getServer().getPlayerExact(input);
        if (foundPlayer == null) {
            throw new PlayerNotFoundException();
        }

        if (requester instanceof Player) {
            Player requestingPlayer = (Player) requester;
            if (!requestingPlayer.canSee(foundPlayer)) {
                throw new PlayerNotFoundException();
            }
        }

        return foundPlayer;
    }

    protected UUID getUniqueId(@NotNull String input) {
        if (input.matches(REGEX_VALID_DASHED_UUID)) {
            return UUID.fromString(input);
        }

        if (input.matches(REGEX_VALID_PLAIN_UUID)) {
            return UUID.fromString(input
                    .replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                            "$1-$2-$3-$4-$5")
            );
        }

        return null;
    }

    /**
     * Checks if the given String is a valid playername.
     *
     * @param input String to check.
     * @return True if valid, false otherwise.
     */
    protected boolean isValidPlayerName(@NotNull String input) {
        return input.matches(REGEX_VALID_NAME);
    }

    /**
     * Checks if the given String is a valid UUID. Accepts both dashed and plain UUIDs.
     *
     * @param input String to check.
     * @return True if valid, false otherwise.
     */
    protected boolean isValidUniqueId(@NotNull String input) {
        return input.matches(REGEX_VALID_DASHED_UUID) || input.matches(REGEX_VALID_PLAIN_UUID);
    }
}
