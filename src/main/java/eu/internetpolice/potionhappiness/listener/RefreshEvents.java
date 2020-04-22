package eu.internetpolice.potionhappiness.listener;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RefreshEvents implements Listener {
    private PotionHappiness plugin;

    public RefreshEvents(PotionHappiness plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Optional<Map<PotionEffectType, Integer>> originalUserMap = plugin.getDataStore().getEnabledPotions(event.getPlayer());
        if (originalUserMap.isPresent()) {
            Map<PotionEffectType, Integer> userMap = new HashMap<>(originalUserMap.get());
            userMap.forEach((effectType, amplifier) ->
                    plugin.getPotionManager().applyPotionEffect(effectType, amplifier, event.getPlayer()));
        }
    }
}
