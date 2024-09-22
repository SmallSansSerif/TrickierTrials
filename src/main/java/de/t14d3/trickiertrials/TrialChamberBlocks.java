package de.t14d3.trickiertrials;

import org.bukkit.Material;

public enum TrialChamberBlocks{
    COPPER_BLOCK,
    WAXED_COPPER_BLOCK,
    WEATHERED_COPPER_BLOCK,
    WAXED_WEATHERED_COPPER_BLOCK,
    EXPOSED_COPPER_BLOCK,
    WAXED_EXPOSED_COPPER_BLOCK,
    OXIDIZED_COPPER_BLOCK,
    WAXED_OXIDIZED_COPPER_BLOCK,
    CUT_COPPER_BLOCK,
    WAXED_CUT_COPPER_BLOCK,
    EXPOSED_CUT_COPPER_BLOCK,
    WAXED_EXPOSED_CUT_COPPER_BLOCK,
    WEATHERED_CUT_COPPER_BLOCK,
    WAXED_WEATHERED_CUT_COPPER_BLOCK,
    OXIDIZED_CUT_COPPER_BLOCK,
    WAXED_OXIDIZED_CUT_COPPER_BLOCK,


    ;

    public Material toMaterial() {
        return Material.valueOf(this.name());
    }

    public static boolean contains(Material material) {
        for (TrialChamberBlocks block : TrialChamberBlocks.values()) {
            if (block.toMaterial() == material) {
                return true;
            }
        }
        return false;
    }
}
