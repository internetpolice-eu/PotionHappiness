package eu.internetpolice.potionhappiness.datastore;

import org.bukkit.OfflinePlayer;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public interface IDataStore {
    /**
     * Enables the given {@link PotionEffectType} in the datastore for the given {@link OfflinePlayer}.
     * @param effect Potion effect to enable.
     * @param amplifier Potion amplifier.
     * @param player Player to enable the effect for.
     */
    void enablePotionEffect(@NotNull PotionEffectType effect, int amplifier, @NotNull OfflinePlayer player);

    /**
     * Disables all given {@link PotionEffectType}'s in the datastore for the given {@link OfflinePlayer}.
     * @param player Player to disable effects for.
     */
    void clearPotionEffects(@NotNull OfflinePlayer player);

    /**
     * Disables the given {@link PotionEffectType} in the datastore for the given {@link OfflinePlayer}.
     * @param effect Potion effect to disable.
     * @param player Player to disable effect for.
     */
    void disablePotionEffect(@NotNull PotionEffectType effect, @NotNull OfflinePlayer player);

    /**
     * Gets all the enabled {@link PotionEffectType}'s in the datastore for the given {@link OfflinePlayer}.
     * @param player Player to lookup.
     * @return Optional map containing the enabled effects for the player.
     */
    @NotNull Optional<Map<PotionEffectType, Integer>> getEnabledPotions(@NotNull OfflinePlayer player);

    /**
     * Returns if the given {@link OfflinePlayer} has the specified {@link PotionEffectType} applied and managed
     * by PotionHappiness.
     * @param effect PotionEffectType to check for.
     * @param player OfflinePlayer to check.
     * @return True if applied and managed by PotionHappiness, false otherwise.
     */
    boolean hasPotionEnabled(@NotNull PotionEffectType effect, @NotNull OfflinePlayer player);

    /**
     * Flush the currently enabled effects to the datastore. Used in auto-saving tasks and on shutdown.
     */
    void save();
}
