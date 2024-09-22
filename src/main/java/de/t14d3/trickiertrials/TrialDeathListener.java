package de.t14d3.trickiertrials;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class TrialDeathListener implements Listener {

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (event.getEntity().getPersistentDataContainer().has(new NamespacedKey("trickiertrials", "trialspawned"), PersistentDataType.INTEGER)) {
            event.getDrops().clear();
        }
    }
}
