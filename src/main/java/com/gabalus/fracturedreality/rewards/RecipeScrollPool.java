package com.gabalus.fracturedreality.rewards;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeScrollPool {
    private final String id;
    private final List<Entry> entries = new ArrayList<>();

    public RecipeScrollPool(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public void add(ResourceLocation recipeId, int minTier, int weight) {
        if (weight <= 0) {
            return;
        }
        entries.add(new Entry(recipeId, Math.max(1, minTier), weight));
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public ResourceLocation roll(int tier, Random random) {
        List<Entry> eligible = entries.stream()
            .filter(entry -> tier >= entry.minTier())
            .toList();

        if (eligible.isEmpty()) {
            eligible = entries;
        }

        int totalWeight = 0;
        for (Entry entry : eligible) {
            totalWeight += entry.weight();
        }

        if (totalWeight <= 0) {
            return null;
        }

        int roll = random.nextInt(totalWeight);
        for (Entry entry : eligible) {
            roll -= entry.weight();
            if (roll < 0) {
                return entry.recipeId();
            }
        }

        return eligible.get(0).recipeId();
    }

    public record Entry(ResourceLocation recipeId, int minTier, int weight) {}
}
