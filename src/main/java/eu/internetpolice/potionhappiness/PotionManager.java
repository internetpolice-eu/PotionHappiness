package eu.internetpolice.potionhappiness;

import eu.internetpolice.potionhappiness.api.IPotionManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PotionManager implements IPotionManager {
    private PotionHappiness plugin;

    PotionManager(PotionHappiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public void applyPotionEffect(@NotNull PotionEffectType type, @NotNull OfflinePlayer player) {
        applyPotionEffect(type, DEFAULT_AMPLIFIER, player);
    }

    @Override
    public void applyPotionEffect(@NotNull PotionEffectType type, int amplifier, @NotNull OfflinePlayer player) {
        // Amplifiers above 9 don't work in-game and get reset to the default.
        if (amplifier > 9) {
            amplifier = 9;
        }

        if (player.isOnline()) {
            Player oPlayer = (Player) player;
            PotionEffect effect = new PotionEffect(type, DEFAULT_DURATION, checkAmplifier(amplifier));
            effect.apply(oPlayer);
        }

        plugin.getDataStore().enablePotionEffect(type, amplifier, player);
    }

    @Override
    public void clearPotionEffects(@NotNull OfflinePlayer player) {
        if (player.isOnline()) {
            Player oPlayer = (Player) player;
            oPlayer.getActivePotionEffects().forEach((effect) -> {
                if (plugin.getDataStore().hasPotionEnabled(effect.getType(), oPlayer)) {
                    oPlayer.removePotionEffect(effect.getType());
                }
            });
        }

        plugin.getDataStore().clearPotionEffects(player);
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffectType type, @NotNull OfflinePlayer player) {
        if (player.isOnline()) {
            Player oPlayer = (Player) player;
            if (plugin.getDataStore().hasPotionEnabled(type, oPlayer)) {
                oPlayer.removePotionEffect(type);
            }

        }

        plugin.getDataStore().disablePotionEffect(type, player);
    }

    @Override
    public void refreshPotionEffect(@NotNull PotionEffectType type, int amplifier, @NotNull Player player) {
        if (plugin.getDataStore().hasPotionEnabled(type, player)) {
            PotionEffect effect = new PotionEffect(type, DEFAULT_DURATION, checkAmplifier(amplifier));
            effect.apply(player);
        }
    }

    @Override
    public @Nullable Map<PotionEffectType, Integer> getAppliedEffects(@NotNull OfflinePlayer player) {
        return plugin.getDataStore().getEnabledPotions(player).orElse(null);
    }

    @Override
    public List<String> getAvailableEffects() {
        List<String> available = new ArrayList<>();
        Arrays.stream(PotionEffectType.values()).forEach((effect) -> available.add(effect.getName()));
        available.sort(String::compareTo);
        return available;
    }

    private int checkAmplifier(int amplifier) {
        // Amplifiers above 9 don't work in-game and get reset to the default.
        return Math.min(amplifier, 9);
    }
}
