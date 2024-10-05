package de.t14d3.trickiertrials;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class TrickierTrials extends JavaPlugin {

    private List<Material> trialChamberMaterials;

    @Override
    public void onEnable() {
        // Load configuration
        saveDefaultConfig(); // Creates the config file with default values if it doesn't exist
        loadTrialChamberMaterials(); // Load trial chamber materials from config
        loadConfigurationOptions(); // Load decay and regeneration settings

        // Register event listeners
        this.getServer().getPluginManager().registerEvents(new TrialSpawnerListener(this, strengthenTrialMobs), this);
        this.getServer().getPluginManager().registerEvents(new TrialChamberProtector(this, getTrialChamberMaterials(), decayPlacedBlocks, regenerateBrokenBlocks), this);
        this.getServer().getPluginManager().registerEvents(new TrialDeathListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadTrialChamberMaterials() {
        FileConfiguration config = getConfig();
        List<String> blockNames = config.getStringList("blocks-to-protect");
        trialChamberMaterials = new ArrayList<>();

        for (String name : blockNames) {
            try {
                Material material = Material.valueOf(name);
                trialChamberMaterials.add(material);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Invalid material in config: " + name);
            }
        }
    }

    private boolean decayPlacedBlocks;
    private boolean regenerateBrokenBlocks;
    private boolean strengthenTrialMobs;

    private void loadConfigurationOptions() {
        FileConfiguration config = getConfig();
        decayPlacedBlocks = config.getBoolean("decay-placed-blocks", true);
        regenerateBrokenBlocks = config.getBoolean("regenerate-broken-blocks", true);
        strengthenTrialMobs = config.getBoolean("strengthen-trial-mobs", true);
    }

    public List<Material> getTrialChamberMaterials() {
        return trialChamberMaterials;
    }
}