package eu.internetpolice.potionhappiness.task;

import eu.internetpolice.potionhappiness.PotionHappiness;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {
    private PotionHappiness plugin;

    public AutoSaveTask(PotionHappiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            plugin.getDataStore().save();
        } catch (Exception ex) {
            plugin.getLogger().warning("Failed while autosaving datastore.");
            ex.printStackTrace();
        }
    }
}
