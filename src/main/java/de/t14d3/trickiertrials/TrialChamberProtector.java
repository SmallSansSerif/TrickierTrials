package de.t14d3.trickiertrials;

import io.papermc.paper.event.block.BlockBreakProgressUpdateEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;


public class TrialChamberProtector implements Listener {

    private final JavaPlugin plugin;

    private final Map<Location, BlockData> brokenBlocks = new HashMap<>();
    private final Map<Location, BlockData> placedBlocks = new HashMap<>();

    public TrialChamberProtector(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        block.getChunk().getStructures(Structure.TRIAL_CHAMBERS).forEach(structure -> {
            if (structure.getBoundingBox().contains(blockLocation.toVector()) && TrialChamberBlocks.contains(block.getType())) {

                brokenBlocks.put(blockLocation, block.getBlockData());
                if (!placedBlocks.containsKey(blockLocation)) {
                    event.setDropItems(false);
                }

                if (placedBlocks.containsKey(blockLocation)) {
                    placedBlocks.remove(blockLocation);
                    return;
                }
                // Schedule block restoration after 10 seconds (200 ticks)
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Restore the block
                        Block brokenBlock = blockLocation.getBlock();
                        if (brokenBlocks.containsKey(blockLocation) && !placedBlocks.containsKey(blockLocation)) {
                            brokenBlock.setBlockData(brokenBlocks.get(blockLocation));
                            brokenBlocks.remove(blockLocation); // Remove after restoration
                        }
                    }
                }.runTaskLater(plugin, 200 + (long) (Math.random() * 100));
            }
        });
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        block.getChunk().getStructures(Structure.TRIAL_CHAMBERS).forEach(structure -> {
            if (structure.getBoundingBox().contains(blockLocation.toVector()) && !TrialChamberBlocks.contains(block.getType())) {
                placedBlocks.put(blockLocation, block.getBlockData());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Block placedBlock = blockLocation.getBlock();
                        if (placedBlocks.containsKey(blockLocation)) {
                            placedBlock.setBlockData(Material.AIR.createBlockData());
                            placedBlocks.remove(blockLocation); // Remove after restoration
                        }
                    }
                }.runTaskLater(plugin, 200);
            }
        });
    }

    @EventHandler
    public void onBlockBreakStart(BlockBreakProgressUpdateEvent event) {
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        block.getChunk().getStructures(Structure.TRIAL_CHAMBERS).forEach(structure -> {
            if (structure.getBoundingBox().contains(blockLocation.toVector())) {

                Player player = (Player) event.getEntity();
                player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 10, 1, false, false));
            }
        });
    }


}
