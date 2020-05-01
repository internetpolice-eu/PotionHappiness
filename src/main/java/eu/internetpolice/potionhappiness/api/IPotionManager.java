package eu.internetpolice.potionhappiness.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface IPotionManager {
    int DEFAULT_AMPLIFIER = 0;
    int DEFAULT_DURATION = 72000; // 3600 seconds * 20 ticks per second = 1 hour

    /**
     * Apply the specified {@link PotionEffectType} for the given {@link OfflinePlayer}. The applied potion effect
     * will be saved in the datastore and given on the next join, or instant if the player is online.
     * @param effect PotionEffectType to apply.
     * @param player OfflinePlayer to apply to.
     */
    void applyPotionEffect(@NotNull PotionEffectType effect, @NotNull OfflinePlayer player);

    /**
     * Apply the specified {@link PotionEffectType} with the specified amplifier for the given {@link OfflinePlayer}.
     * The applied potion effect will be saved in the datastore and given on the next join, or instant if the
     * player is online.
     * @param effect PotionEffectType to apply.
     * @param amplifier Amplifier to apply.
     * @param player OfflinePlayer to apply to.
     */
    void applyPotionEffect(@NotNull PotionEffectType effect, int amplifier, @NotNull OfflinePlayer player);

    /**
     * Clear all {@link PotionEffectType} applied to the given {@link OfflinePlayer} applied by PotionHappiness.
     * @param player OfflinePlayer to clear.
     */
    void clearPotionEffects(@NotNull OfflinePlayer player);

    /**
     * Remove the specified {@link PotionEffectType} for the given {@link OfflinePlayer}. The {@link PotionEffectType}
     * will only be removed from the player if it has been applied by PotionHappiness.
     * @param type PotionEffectType to remove.
     * @param player OfflinePlayer to remove from.
     */
    void removePotionEffect(@NotNull PotionEffectType type, @NotNull OfflinePlayer player);

    /**
     * Refresh the specified {@link PotionEffectType} for the given {@link Player}. The {@link PotionEffectType}
     * will only be refreshed if it has been applied by PotionHappiness.
     * @param type PotionEffectType to refresh.
     * @param amplifier Amplifier to apply.
     * @param player Player to refresh for.
     */
    void refreshPotionEffect(@NotNull PotionEffectType type, int amplifier, @NotNull Player player);

    /**
     * Gets all the applied {@link PotionEffectType} with their amplifier as a {@link Map} for the given
     * {@link OfflinePlayer} applied by PotionHappiness. Returns null if the player has no effects applied.
     * @param player OfflinePlayer to lookup.
     * @return Map containing applied PotionEffectTypes and their amplifiers.
     */
    @Nullable Map<PotionEffectType, Integer> getAppliedEffects(@NotNull OfflinePlayer player);

    /**
     * Gets all the available {@link PotionEffectType} on the server.
     * @return All available PotionEffectTypes.
     */
    List<String> getAvailableEffects();
}
