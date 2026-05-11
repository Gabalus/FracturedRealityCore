package com.gabalus.fracturedreality.progression;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerProgression {
    private static final String COMPLETED_VOID_TUTORIAL = "completed_void_tutorial";
    private static final String RECEIVED_CODEX = "received_codex";
    private static final String COMPLETED_DISCORD_COUNT = "completed_discord_count";
    private static final String PASSIVE_POINTS = "passive_points";
    private static final String ASCENDANCY_POINTS = "ascendancy_points";
    private static final String UNLOCKED_DISCORD_TIER = "unlocked_discord_tier";
    private static final String ASCENDANCY_ID = "ascendancy_id";
    private static final String KNOWN_RECIPES = "known_recipes";
    private static final String COMPLETED_MILESTONES = "completed_milestones";
    private static final String ALLOCATED_PASSIVE_NODES = "allocated_passive_nodes";
    private static final String NODE_ID = "node_id";
    private static final String NODE_RANK = "rank";

    private boolean completedVoidTutorial;
    private boolean receivedCodex;
    private int completedDiscordCount;
    private int passivePoints;
    private int ascendancyPoints;
    private int unlockedDiscordTier = 1;
    private String ascendancyId = "";
    private final Set<ResourceLocation> knownRecipes = new HashSet<>();
    private final Set<String> completedMilestones = new HashSet<>();
    private final Map<ResourceLocation, Integer> allocatedPassiveNodes = new HashMap<>();

    public boolean hasCompletedVoidTutorial() {
        return completedVoidTutorial;
    }

    public void setCompletedVoidTutorial(boolean completedVoidTutorial) {
        this.completedVoidTutorial = completedVoidTutorial;
    }

    public boolean hasReceivedCodex() {
        return receivedCodex;
    }

    public void setReceivedCodex(boolean receivedCodex) {
        this.receivedCodex = receivedCodex;
    }

    public int getCompletedDiscordCount() {
        return completedDiscordCount;
    }

    public void addCompletedDiscord() {
        completedDiscordCount++;
    }

    public int getPassivePoints() {
        return passivePoints;
    }

    public void addPassivePoints(int amount) {
        passivePoints = Math.max(0, passivePoints + amount);
    }

    public boolean spendPassivePoints(int amount) {
        if (amount <= 0 || passivePoints < amount) {
            return false;
        }
        passivePoints -= amount;
        return true;
    }

    public int getAscendancyPoints() {
        return ascendancyPoints;
    }

    public void addAscendancyPoints(int amount) {
        ascendancyPoints = Math.max(0, ascendancyPoints + amount);
    }

    public boolean spendAscendancyPoints(int amount) {
        if (amount <= 0 || ascendancyPoints < amount) {
            return false;
        }
        ascendancyPoints -= amount;
        return true;
    }

    public int getUnlockedDiscordTier() {
        return unlockedDiscordTier;
    }

    public void setUnlockedDiscordTier(int unlockedDiscordTier) {
        this.unlockedDiscordTier = Math.max(1, unlockedDiscordTier);
    }

    public String getAscendancyId() {
        return ascendancyId;
    }

    public boolean hasAscendancy() {
        return ascendancyId != null && !ascendancyId.isBlank();
    }

    public boolean chooseAscendancy(String ascendancyId) {
        if (hasAscendancy() || ascendancyId == null || ascendancyId.isBlank()) {
            return false;
        }
        this.ascendancyId = ascendancyId;
        return true;
    }

    public void resetAscendancy() {
        ascendancyId = "";
    }

    public boolean knowsRecipe(ResourceLocation recipeId) {
        return knownRecipes.contains(recipeId);
    }

    public boolean unlockRecipe(ResourceLocation recipeId) {
        return knownRecipes.add(recipeId);
    }

    public Set<ResourceLocation> getKnownRecipes() {
        return Collections.unmodifiableSet(knownRecipes);
    }

    public boolean hasMilestone(String milestone) {
        return completedMilestones.contains(milestone);
    }

    public boolean completeMilestone(String milestone) {
        return completedMilestones.add(milestone);
    }

    public int getPassiveNodeRank(ResourceLocation nodeId) {
        return allocatedPassiveNodes.getOrDefault(nodeId, 0);
    }

    public boolean hasPassiveNode(ResourceLocation nodeId) {
        return getPassiveNodeRank(nodeId) > 0;
    }

    public void addPassiveNodeRank(ResourceLocation nodeId, int amount) {
        if (amount <= 0) {
            return;
        }
        allocatedPassiveNodes.put(nodeId, getPassiveNodeRank(nodeId) + amount);
    }

    public boolean removePassiveNodeRank(ResourceLocation nodeId, int amount) {
        int current = getPassiveNodeRank(nodeId);
        if (amount <= 0 || current < amount) {
            return false;
        }
        int next = current - amount;
        if (next <= 0) {
            allocatedPassiveNodes.remove(nodeId);
        } else {
            allocatedPassiveNodes.put(nodeId, next);
        }
        return true;
    }

    public Map<ResourceLocation, Integer> getAllocatedPassiveNodes() {
        return Collections.unmodifiableMap(allocatedPassiveNodes);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(COMPLETED_VOID_TUTORIAL, completedVoidTutorial);
        tag.putBoolean(RECEIVED_CODEX, receivedCodex);
        tag.putInt(COMPLETED_DISCORD_COUNT, completedDiscordCount);
        tag.putInt(PASSIVE_POINTS, passivePoints);
        tag.putInt(ASCENDANCY_POINTS, ascendancyPoints);
        tag.putInt(UNLOCKED_DISCORD_TIER, unlockedDiscordTier);
        tag.putString(ASCENDANCY_ID, ascendancyId == null ? "" : ascendancyId);

        ListTag recipes = new ListTag();
        for (ResourceLocation recipeId : knownRecipes) {
            recipes.add(StringTag.valueOf(recipeId.toString()));
        }
        tag.put(KNOWN_RECIPES, recipes);

        ListTag milestones = new ListTag();
        for (String milestone : completedMilestones) {
            milestones.add(StringTag.valueOf(milestone));
        }
        tag.put(COMPLETED_MILESTONES, milestones);

        ListTag nodes = new ListTag();
        for (Map.Entry<ResourceLocation, Integer> entry : allocatedPassiveNodes.entrySet()) {
            CompoundTag node = new CompoundTag();
            node.putString(NODE_ID, entry.getKey().toString());
            node.putInt(NODE_RANK, entry.getValue());
            nodes.add(node);
        }
        tag.put(ALLOCATED_PASSIVE_NODES, nodes);

        return tag;
    }

    public void load(CompoundTag tag) {
        completedVoidTutorial = tag.getBoolean(COMPLETED_VOID_TUTORIAL);
        receivedCodex = tag.getBoolean(RECEIVED_CODEX);
        completedDiscordCount = tag.getInt(COMPLETED_DISCORD_COUNT);
        passivePoints = tag.getInt(PASSIVE_POINTS);
        ascendancyPoints = tag.getInt(ASCENDANCY_POINTS);
        unlockedDiscordTier = Math.max(1, tag.getInt(UNLOCKED_DISCORD_TIER));
        ascendancyId = tag.getString(ASCENDANCY_ID);

        knownRecipes.clear();
        ListTag recipes = tag.getList(KNOWN_RECIPES, Tag.TAG_STRING);
        for (int i = 0; i < recipes.size(); i++) {
            ResourceLocation recipeId = ResourceLocation.tryParse(recipes.getString(i));
            if (recipeId != null) {
                knownRecipes.add(recipeId);
            }
        }

        completedMilestones.clear();
        ListTag milestones = tag.getList(COMPLETED_MILESTONES, Tag.TAG_STRING);
        for (int i = 0; i < milestones.size(); i++) {
            completedMilestones.add(milestones.getString(i));
        }

        allocatedPassiveNodes.clear();
        ListTag nodes = tag.getList(ALLOCATED_PASSIVE_NODES, Tag.TAG_COMPOUND);
        for (int i = 0; i < nodes.size(); i++) {
            CompoundTag node = nodes.getCompound(i);
            ResourceLocation nodeId = ResourceLocation.tryParse(node.getString(NODE_ID));
            int rank = node.getInt(NODE_RANK);
            if (nodeId != null && rank > 0) {
                allocatedPassiveNodes.put(nodeId, rank);
            }
        }
    }

    public void copyFrom(PlayerProgression other) {
        completedVoidTutorial = other.completedVoidTutorial;
        receivedCodex = other.receivedCodex;
        completedDiscordCount = other.completedDiscordCount;
        passivePoints = other.passivePoints;
        ascendancyPoints = other.ascendancyPoints;
        unlockedDiscordTier = other.unlockedDiscordTier;
        ascendancyId = other.ascendancyId;
        knownRecipes.clear();
        knownRecipes.addAll(other.knownRecipes);
        completedMilestones.clear();
        completedMilestones.addAll(other.completedMilestones);
        allocatedPassiveNodes.clear();
        allocatedPassiveNodes.putAll(other.allocatedPassiveNodes);
    }
}
