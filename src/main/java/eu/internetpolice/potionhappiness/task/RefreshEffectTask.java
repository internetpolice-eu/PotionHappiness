package eu.internetpolice.potionhappiness.task;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Optional;

public class RefreshEffectTask extends BukkitRunnable {
    private PotionHappiness plugin;

    public RefreshEffectTask(PotionHappiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Optional<Map<PotionEffectType, Integer>> userMap = plugin.getDataStore().getEnabledPotions(player);
            if (userMap.isPresent()) {
                userMap.get().forEach((effectType, amplifier) ->
                        plugin.getPotionManager().refreshPotionEffect(effectType, amplifier, player));
            }
        }
    }
}
