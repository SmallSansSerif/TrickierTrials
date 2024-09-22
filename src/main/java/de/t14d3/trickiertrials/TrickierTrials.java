package de.t14d3.trickiertrials;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TrickierTrials extends JavaPlugin {


    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new TrialSpawnerListener(), this);
        this.getServer().getPluginManager().registerEvents(new TrialChamberProtector(this), this);
        this.getServer().getPluginManager().registerEvents(new TrialDeathListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



}
