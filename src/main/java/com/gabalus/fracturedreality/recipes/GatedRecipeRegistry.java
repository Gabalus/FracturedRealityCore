package com.gabalus.fracturedreality.recipes;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class GatedRecipeRegistry {
    private static final Set<ResourceLocation> GATED_RECIPES = new HashSet<>();

    static {
        register("minecraft:shield");
        register("minecraft:crossbow");
        register("minecraft:enchanting_table");
        register("minecraft:anvil");
        register("minecraft:golden_apple");
    }

    private GatedRecipeRegistry() {}

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
