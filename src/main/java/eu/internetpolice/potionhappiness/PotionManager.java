package eu.internetpolice.potionhappiness;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PotionManager {
    private PotionHappiness plugin;

    private static final int DEFAULT_AMPLIFIER = 0;
    // 3600 seconds * 20 ticks per second = 1 hour
    private static final int DURATION = 72000;

    PotionManager(PotionHappiness plugin) {
        this.plugin = plugin;
    }

    public void applyPotionEffect(PotionEffectType type, OfflinePlayer player) {
        applyPotionEffect(type, DEFAULT_AMPLIFIER, player);
    }

    public void applyPotionEffect(PotionEffectType type, int amplifier, OfflinePlayer player) {
        // Amplifiers above 9 don't work in-game and get reset to the default.
        if (amplifier > 9) {
            amplifier = 9;
        }

        if (player.isOnline()) {
            Player oPlayer = (Player) player;
            PotionEffect effect = new PotionEffect(type, DURATION, checkAmplifier(amplifier));
            effect.apply(oPlayer);
        }

        plugin.getDataStore().enablePotionEffect(type, amplifier, player);
    }

    public void clearPotionEffects(OfflinePlayer player) {
        if (player.isOnline()) {
            Player oPlayer = (Player) player;
            oPlayer.getActivePotionEffects().forEach((effect) -> oPlayer.removePotionEffect(effect.getType()));
        }

        plugin.getDataStore().clearPotionEffects(player);
    }

    public void removePotionEffect(PotionEffectType type, OfflinePlayer player) {
        if (player.isOnline()) {
            Player oPlayer = (Player) player;
            oPlayer.removePotionEffect(type);
        }

        plugin.getDataStore().disablePotionEffect(type, player);
    }

    public void refreshPotionEffect(PotionEffectType type, int amplifier, Player player) {
        PotionEffect effect = new PotionEffect(type, DURATION, checkAmplifier(amplifier));
        effect.apply(player);
    }

    private int checkAmplifier(int amplifier) {
        // Amplifiers above 9 don't work in-game and get reset to the default.
        return Math.min(amplifier, 9);
    }

    public List<String> getAvailableEffects() {
        return getAvailableEffects(null);
    }

    // TODO: Check for which effects the user has permission.
    public List<String> getAvailableEffects(@Nullable Player player) {
        List<String> available = new ArrayList<>();
        Arrays.stream(PotionEffectType.values()).forEach((effect) -> available.add(effect.getName()));
        available.sort(String::compareTo);
        return available;
    }
}
