package de.t14d3.trickiertrials;

import org.bukkit.Material;

public enum TrialChamberBlocks{
    COPPER_BLOCK,
    WAXED_COPPER_BLOCK,
    WEATHERED_COPPER,
    WAXED_WEATHERED_COPPER,
    EXPOSED_COPPER,
    WAXED_EXPOSED_COPPER,
    OXIDIZED_COPPER,
    WAXED_OXIDIZED_COPPER,
    CUT_COPPER,
    WAXED_CUT_COPPER,
    OXIDIZED_CUT_COPPER,
    WAXED_OXIDIZED_CUT_COPPER,
    WAXED_CHISELED_COPPER,
    WAXED_OXIDIZED_CHISELED_COPPER,
    POLISHED_TUFF,
    TUFF_BRICKS,
    CHISELED_TUFF_BRICKS,
    CHISELED_TUFF,
    WAXED_CUT_COPPER_STAIRS,
    WAXED_OXIDIZED_CUT_COPPER_STAIRS,
    WAXED_OXIDIZED_CUT_COPPER_SLAB,
    WAXED_CUT_COPPER_SLAB,
    WAXED_COPPER_GRATE,
    WAXED_OXIDIZED_COPPER_GRATE,
    LADDER,
    LIGHT_GRAY_STAINED_GLASS,
    WAXED_COPPER_BULB,
    WAXED_EXPOSED_COPPER_BULB,
    WAXED_WEATHERED_COPPER_BULB,
    WAXED_OXIDIZED_COPPER_BULB,



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
