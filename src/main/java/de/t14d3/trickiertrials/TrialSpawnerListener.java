package de.t14d3.trickiertrials;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.TrialSpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class TrialSpawnerListener implements Listener {

    private final boolean strengthenTrialMobs;

    public TrialSpawnerListener(JavaPlugin plugin, boolean strengthenTrialMobs) {
        this.strengthenTrialMobs = strengthenTrialMobs;
    }

    public enum TrialTiers {
        DEFAULT,
        NORMAL,
        HARD,
        EXTREME
    }

    public void healthMultiplier(LivingEntity entity, double healthMultiplier) {
        AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        maxHealth.setBaseValue(maxHealth.getBaseValue() * healthMultiplier);

        entity.setHealth(maxHealth.getValue());
    }

    public void assignArmorBasedOnDifficulty(LivingEntity entity, TrialTiers trialTiers) {
        Random random = new Random();

        switch (trialTiers) {
            case EXTREME:
                // 80% chance for 1-2 diamond armor pieces, 50% chance for 1 netherite piece
                assignArmorPiece(entity, 0.80, 0.50, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET, "helmet");
                assignArmorPiece(entity, 0.80, 0.50, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE, "chestplate");
                assignArmorPiece(entity, 0.80, 0.50, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS, "leggings");
                assignArmorPiece(entity, 0.80, 0.50, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS, "boots");
                break;

            case HARD:
                // 60% for 2-3 iron armor pieces, 20% for leather, 5% for diamond
                assignArmorPiece(entity, 0.60, 0.05, Material.IRON_HELMET, Material.DIAMOND_HELMET, Material.LEATHER_HELMET, 0.20, "helmet");
                assignArmorPiece(entity, 0.60, 0.05, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.LEATHER_CHESTPLATE, 0.20, "chestplate");
                assignArmorPiece(entity, 0.60, 0.05, Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.LEATHER_LEGGINGS, 0.20, "leggings");
                assignArmorPiece(entity, 0.60, 0.05, Material.IRON_BOOTS, Material.DIAMOND_BOOTS, Material.LEATHER_BOOTS, 0.20, "boots");
                break;

            case NORMAL:
                // 20% for 1-2 leather, 10% for iron
                assignArmorPiece(entity, 0.20, 0.10, Material.LEATHER_HELMET, Material.IRON_HELMET, "helmet");
                assignArmorPiece(entity, 0.20, 0.10, Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, "chestplate");
                assignArmorPiece(entity, 0.20, 0.10, Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, "leggings");
                assignArmorPiece(entity, 0.20, 0.10, Material.LEATHER_BOOTS, Material.IRON_BOOTS, "boots");
                break;

            case DEFAULT:
            default:
                // 25% chance for a single leather armor piece
                assignArmorPiece(entity, 0.25, Material.LEATHER_HELMET, "helmet");
                assignArmorPiece(entity, 0.25, Material.LEATHER_CHESTPLATE, "chestplate");
                assignArmorPiece(entity, 0.25, Material.LEATHER_LEGGINGS, "leggings");
                assignArmorPiece(entity, 0.25, Material.LEATHER_BOOTS, "boots");
                break;
        }
    }

    // Method to assign specific armor pieces to their respective slots
    public void assignArmorPiece(LivingEntity entity, double primaryChance, double secondaryChance, Material primaryArmor, Material secondaryArmor, String armorSlot) {
        Random random = new Random();
        if (random.nextDouble() < primaryChance) {
            equipArmor(entity, primaryArmor, armorSlot);
        } else if (random.nextDouble() < secondaryChance) {
            equipArmor(entity, secondaryArmor, armorSlot);
        }
    }

    // Overloaded method to include a third armor piece (like leather for HARD)
    public void assignArmorPiece(LivingEntity entity, double primaryChance, double secondaryChance, Material primaryArmor, Material secondaryArmor, Material tertiaryArmor, double tertiaryChance, String armorSlot) {
        Random random = new Random();
        if (random.nextDouble() < primaryChance) {
            equipArmor(entity, primaryArmor, armorSlot);
        } else if (random.nextDouble() < secondaryChance) {
            equipArmor(entity, secondaryArmor, armorSlot);
        } else if (random.nextDouble() < tertiaryChance) {
            equipArmor(entity, tertiaryArmor, armorSlot);
        }
    }

    // Simplified method for single-piece assignment
    public void assignArmorPiece(LivingEntity entity, double chance, Material armor, String armorSlot) {
        Random random = new Random();
        if (random.nextDouble() < chance) {
            equipArmor(entity, armor, armorSlot);
        }
    }

    // Helper method to equip the armor to the correct slot
    public void equipArmor(LivingEntity entity, Material armor, String armorSlot) {
        switch (armorSlot) {
            case "helmet":
                entity.getEquipment().setHelmet(new ItemStack(armor));
                break;
            case "chestplate":
                entity.getEquipment().setChestplate(new ItemStack(armor));
                break;
            case "leggings":
                entity.getEquipment().setLeggings(new ItemStack(armor));
                break;
            case "boots":
                entity.getEquipment().setBoots(new ItemStack(armor));
                break;
        }
    }

    @EventHandler
    public void onSpawn(TrialSpawnerSpawnEvent event) {
        if (!strengthenTrialMobs) {
            return;
        }
        final int[] gearScore = {0};
        event.getTrialSpawner().getTrackedPlayers().forEach(player -> {
                    Arrays.stream(player.getEquipment().getArmorContents())
                            .filter(Objects::nonNull)
                            .forEach(itemStack -> {
                                switch (itemStack.getType()) {
                                    case ELYTRA:
                                        gearScore[0] += 10;
                                        break;
                                    case NETHERITE_CHESTPLATE:
                                    case NETHERITE_HELMET:
                                    case NETHERITE_LEGGINGS:
                                    case NETHERITE_BOOTS:
                                        gearScore[0] += 5;
                                        break;
                                    case DIAMOND_CHESTPLATE:
                                    case DIAMOND_HELMET:
                                    case DIAMOND_LEGGINGS:
                                    case DIAMOND_BOOTS:
                                        gearScore[0] += 4;
                                        break;
                                    case IRON_CHESTPLATE:
                                    case IRON_HELMET:
                                    case IRON_LEGGINGS:
                                    case IRON_BOOTS:
                                    case CHAINMAIL_CHESTPLATE:
                                    case CHAINMAIL_HELMET:
                                    case CHAINMAIL_LEGGINGS:
                                    case CHAINMAIL_BOOTS:
                                        gearScore[0] += 3;
                                        break;
                                    case GOLDEN_CHESTPLATE:
                                    case GOLDEN_HELMET:
                                    case GOLDEN_LEGGINGS:
                                    case GOLDEN_BOOTS:
                                        gearScore[0] += 2;
                                        break;
                                    case LEATHER_CHESTPLATE:
                                    case LEATHER_HELMET:
                                    case LEATHER_LEGGINGS:
                                    case LEATHER_BOOTS:
                                        gearScore[0] += 1;
                                        break;
                                    default:
                                        break;
                                }
                            });
                    Arrays.stream(player.getInventory().getContents())
                            .filter(Objects::nonNull)
                            .forEach(itemStack -> {
                                switch (itemStack.getType()) {

                                    case NETHERITE_SWORD:
                                    case NETHERITE_AXE:
                                    case NETHERITE_PICKAXE:
                                    case NETHERITE_SHOVEL:
                                        gearScore[0] += 10;
                                        break;
                                    case DIAMOND_SWORD:
                                    case DIAMOND_AXE:
                                    case DIAMOND_PICKAXE:
                                    case DIAMOND_SHOVEL:
                                        gearScore[0] += 5;
                                        break;
                                }
                            });
                });

        TrialSpawnerListener.TrialTiers TrialTiers;
        if (gearScore[0] >= 35) {
            TrialTiers = TrialSpawnerListener.TrialTiers.EXTREME;
        } else if (gearScore[0] >= 20) {
            TrialTiers = TrialSpawnerListener.TrialTiers.HARD;
        } else if (gearScore[0] >= 10) {
            TrialTiers = TrialSpawnerListener.TrialTiers.NORMAL;
        } else {
            TrialTiers = TrialSpawnerListener.TrialTiers.DEFAULT;
        }

        event.getEntity().setGlowing(true);

        LivingEntity entity = (LivingEntity) event.getEntity();
        switch (TrialTiers) {
            case NORMAL:
                healthMultiplier(entity, 1.5);  // Adjust health for NORMAL difficulty
                assignArmorBasedOnDifficulty(entity, TrialSpawnerListener.TrialTiers.NORMAL);  // Assign armor based on NORMAL difficulty
                break;

            case HARD:
                healthMultiplier(entity, 2);  // Adjust health for HARD difficulty
                assignArmorBasedOnDifficulty(entity, TrialSpawnerListener.TrialTiers.HARD);  // Assign armor based on HARD difficulty
                break;

            case EXTREME:
                healthMultiplier(entity, 3);  // Adjust health for EXTREME difficulty
                assignArmorBasedOnDifficulty(entity, TrialSpawnerListener.TrialTiers.EXTREME);  // Assign armor based on EXTREME difficulty
                break;

            default:
                assignArmorBasedOnDifficulty(entity, TrialSpawnerListener.TrialTiers.DEFAULT);  // Assign minimal armor for default case
                break;
        }
        entity.getPersistentDataContainer().set(new NamespacedKey("trickiertrials", "trialspawned"), PersistentDataType.INTEGER, 1);
        //entity.getServer().getLogger().info(TrialTiers.toString() + " , " + gearScore[0]);
    }



}
