package com.gabalus.fracturedreality.recipes;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class GatedRecipeRegistry {
    private static final Set<ResourceLocation> GATED_RECIPES = new HashSet<>();
    private static final Set<ResourceLocation> FALLBACK_GATED_RECIPES = Set.of(
        new ResourceLocation("minecraft", "shield"),
        new ResourceLocation("minecraft", "crossbow"),
        new ResourceLocation("minecraft", "enchanting_table"),
        new ResourceLocation("minecraft", "anvil"),
        new ResourceLocation("minecraft", "golden_apple")
    );

    static {
        resetToFallbacks();
    }

    private GatedRecipeRegistry() {}

    public static void resetToFallbacks() {
        GATED_RECIPES.clear();
        GATED_RECIPES.addAll(FALLBACK_GATED_RECIPES);
    }

    public static void replaceAll(Collection<ResourceLocation> recipeIds) {
        GATED_RECIPES.clear();
        GATED_RECIPES.addAll(recipeIds);
        if (GATED_RECIPES.isEmpty()) {
            resetToFallbacks();
        }
    }

    public static void register(String recipeId) {
        ResourceLocation id = ResourceLocation.tryParse(recipeId);
        if (id != null) {
            GATED_RECIPES.add(id);
        }
    }

    public static boolean isGated(ResourceLocation recipeId) {
        return GATED_RECIPES.contains(recipeId);
    }

    public static Set<ResourceLocation> getGatedRecipes() {
        return Collections.unmodifiableSet(GATED_RECIPES);
    }
}
