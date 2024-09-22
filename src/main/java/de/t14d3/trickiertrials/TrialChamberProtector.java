package de.t14d3.trickiertrials;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;


public class TrialChamberProtector implements Listener {

    private final JavaPlugin plugin;

    private Map<Location, BlockData> brokenBlocks = new HashMap<>();

    public TrialChamberProtector(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        block.getChunk().getStructures(Structure.TRIAL_CHAMBERS).forEach(structure -> {
            if (structure.getBoundingBox().contains(blockLocation.toVector())) {

                brokenBlocks.put(blockLocation, block.getBlockData());
                event.setDropItems(false);
                // Schedule block restoration after 10 seconds (200 ticks)
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Restore the block
                        Block brokenBlock = blockLocation.getBlock();
                        if (brokenBlocks.containsKey(blockLocation)) {
                            brokenBlock.setBlockData(brokenBlocks.get(blockLocation));
                            brokenBlocks.remove(blockLocation); // Remove after restoration
                        }
                    }
                }.runTaskLater(plugin, 200);
            }
        });
    }


}
