package eu.internetpolice.potionhappiness;

import eu.internetpolice.potionhappiness.command.PotionsCommand;
import eu.internetpolice.potionhappiness.datastore.FileDataStore;
import eu.internetpolice.potionhappiness.datastore.IDataStore;
import eu.internetpolice.potionhappiness.listener.RefreshEvents;
import eu.internetpolice.potionhappiness.task.AutoSaveTask;
import eu.internetpolice.potionhappiness.task.RefreshEffectTask;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class PotionHappiness extends JavaPlugin {
    private IDataStore dataStore;
    private PotionManager potionManager;

    /* Running tasks */
    private BukkitTask autoSaveTask;
    private BukkitTask refreshEffectTask;

    @Override
    public void onEnable() {
        dataStore = new FileDataStore(this);
        potionManager = new PotionManager(this);

        getServer().getPluginManager().registerEvents(new RefreshEvents(this), this);
        getCommand("potions").setExecutor(new PotionsCommand(this));

        autoSaveTask = new AutoSaveTask(this).runTaskTimer(this, 20L, 1200L);
        refreshEffectTask = new RefreshEffectTask(this).runTaskTimer(this, 20L, 6000L);
    }

    @Override
    public void onDisable() {
        dataStore.save();

        autoSaveTask.cancel();
        refreshEffectTask.cancel();

        dataStore.save();
    }

    public @NotNull IDataStore getDataStore() {
        return dataStore;
    }

    public @NotNull PotionManager getPotionManager() {
        return potionManager;
    }
}
