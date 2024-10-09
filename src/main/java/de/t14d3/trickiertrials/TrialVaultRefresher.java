package de.t14d3.trickiertrials;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

public class TrialVaultRefresher implements Listener {

    private final JavaPlugin plugin;
    private long trialVaultResetTime;

    public TrialVaultRefresher(JavaPlugin plugin, Long trialVaultResetTime) {
        this.plugin = plugin;
        this.trialVaultResetTime = trialVaultResetTime;
    }

    @EventHandler
    public void onVaultInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || trialVaultResetTime == -1L) {
            return;
        }
        if (event.getClickedBlock().getType() == Material.VAULT) {

            int x = event.getClickedBlock().getX();
            int y = event.getClickedBlock().getY();
            int z = event.getClickedBlock().getZ();

            BlockPos blockPos = new BlockPos(x, y, z);
            ServerLevel serverLevel = ((CraftWorld) event.getClickedBlock().getWorld()).getHandle();
            BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
            VaultBlockEntity vaultBlockEntity = (VaultBlockEntity) blockEntity;
            VaultServerData vaultServerData = vaultBlockEntity.getServerData();

            NamespacedKey key = new NamespacedKey(plugin, event.getPlayer().getUniqueId().toString());
            long timestamp = System.currentTimeMillis();
            PersistentDataContainer dataContainer = blockEntity.persistentDataContainer;
            if (dataContainer.getKeys().contains(key)) {
                if (timestamp >= dataContainer.get(key, PersistentDataType.LONG) + trialVaultResetTime) { //86400000L = 24h

                    try {
                        Field f = VaultServerData.class.getDeclaredField("e");
                        f.setAccessible(true);
                        Set<UUID> players = (Set<UUID>) f.get(vaultServerData);
                        if (players.contains(event.getPlayer().getUniqueId())) {
                            players.remove(event.getPlayer().getUniqueId());
                            event.setCancelled(true);
                            event.callEvent();
                            dataContainer.remove(key);
                        }
                    }
                    catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
            else {dataContainer.set(key, PersistentDataType.LONG, timestamp);}
        }
    }
}
