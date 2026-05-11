package com.gabalus.fracturedreality.rewards;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public final class RecipeScrollPoolRegistry {
    private static final Map<String, RecipeScrollPool> POOLS_BY_THEME = new HashMap<>();
    private static final Random RANDOM = new Random();

    private RecipeScrollPoolRegistry() {}

    public static void replaceAll(Map<String, RecipeScrollPool> pools) {
        POOLS_BY_THEME.clear();
        POOLS_BY_THEME.putAll(pools);
        ensureFallbackPools();
    }

    public static ResourceLocation rollRecipe(String theme, int tier) {
        RecipeScrollPool pool = findPool(theme).orElseGet(() -> POOLS_BY_THEME.get("default"));
        if (pool == null || pool.isEmpty()) {
            return new ResourceLocation("minecraft", tier >= 2 ? "shield" : "chest");
        }
        ResourceLocation rolled = pool.roll(Math.max(1, tier), RANDOM);
        return rolled == null ? new ResourceLocation("minecraft", "chest") : rolled;
    }

    public static int size() {
        return POOLS_BY_THEME.size();
    }

    private static Optional<RecipeScrollPool> findPool(String theme) {
        if (theme == null || theme.isBlank()) {
            return Optional.empty();
        }

        String normalized = theme.toLowerCase();
        RecipeScrollPool exact = POOLS_BY_THEME.get(normalized);
        if (exact != null) {
            return Optional.of(exact);
        }

        for (Map.Entry<String, RecipeScrollPool> entry : POOLS_BY_THEME.entrySet()) {
            if (!entry.getKey().equals("default") && normalized.contains(entry.getKey())) {
                return Optional.of(entry.getValue());
            }
        }

        return Optional.empty();
    }

    private static void ensureFallbackPools() {
        if (!POOLS_BY_THEME.containsKey("default")) {
            RecipeScrollPool fallback = new RecipeScrollPool("default");
            fallback.add(new ResourceLocation("minecraft", "chest"), 1, 10);
            fallback.add(new ResourceLocation("minecraft", "shield"), 2, 5);
            POOLS_BY_THEME.put("default", fallback);
        }
    }
}
