package eu.internetpolice.potionhappiness.datastore;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FileDataStore implements IDataStore {
    private File dataStorePath;
    private FileConfiguration dataStore;
    private Map<UUID, Map<PotionEffectType, Integer>> effsEnabled = new ConcurrentHashMap<>();

    private PotionHappiness plugin;

    private static final Map<PotionEffectType, Integer> DEFAULT_USER_MAP = new HashMap<>();

    public FileDataStore(PotionHappiness plugin) {
        this.plugin = plugin;
        dataStorePath = new File(plugin.getDataFolder(), "users.yml");
        dataStore = YamlConfiguration.loadConfiguration(dataStorePath);

        loadAll();
    }

    @Override
    public void enablePotionEffect(@NotNull PotionEffectType effect, int amplifier, @NotNull OfflinePlayer player) {
        Map<PotionEffectType, Integer> userMap = effsEnabled.getOrDefault(player.getUniqueId(), DEFAULT_USER_MAP);
        userMap.remove(effect);
        userMap.put(effect, amplifier);
        effsEnabled.put(player.getUniqueId(), userMap);
    }

    @Override
    public void clearPotionEffects(@NotNull OfflinePlayer player) {
        effsEnabled.remove(player.getUniqueId());
    }

    @Override
    public void disablePotionEffect(@NotNull PotionEffectType effect, @NotNull OfflinePlayer player) {
        Map<PotionEffectType, Integer> userMap = effsEnabled.getOrDefault(player.getUniqueId(), DEFAULT_USER_MAP);
        userMap.remove(effect);
        effsEnabled.put(player.getUniqueId(), userMap);
    }

    @Override
    public @NotNull Optional<Map<PotionEffectType, Integer>> getEnabledPotions(@NotNull OfflinePlayer player) {
        return Optional.ofNullable(effsEnabled.get(player.getUniqueId()));
    }

    private void loadAll() {
        ConfigurationSection userSection = dataStore.getConfigurationSection("users");
        userSection.getValues(false).forEach((uniqueId, potionList) ->
                effsEnabled.put(UUID.fromString(uniqueId), stringListToUserMap((List<String>) potionList)));
    }

    @Override
    public void save() {
        dataStore.set("users", new HashMap<>());
        effsEnabled.forEach((player, userMap) ->
                dataStore.set(String.format("users.%s", player.toString()), userMapToStringList(userMap)));

        try {
            dataStore.save(dataStorePath);
        } catch (IOException ex) {
            plugin.getLogger().severe("Failed to save FileDataStore to disk.");
            ex.printStackTrace();
        }
    }

    private Map<PotionEffectType, Integer> stringListToUserMap(@NotNull List<String> effs) {
        Map<PotionEffectType, Integer> userMap = new HashMap<>();
        effs.forEach((value) -> {
            String[] parts = value.split(":");
            PotionEffectType potionEffect = PotionEffectType.getByName(parts[0]);
            if (potionEffect == null) {
                plugin.getLogger().warning(String.format("Invalid potion effect in user file: %s", parts[0]));
                return;
            }

            userMap.put(potionEffect, Integer.parseInt(parts[1]));
        });
        return userMap;
    }

    private List<String> userMapToStringList(@NotNull Map<PotionEffectType, Integer> userMap) {
        List<String> effs = new ArrayList<>();
        userMap.forEach((key, value) -> effs.add(String.format("%s:%s", key.getName(), value.toString())));
        return effs;
    }
}
