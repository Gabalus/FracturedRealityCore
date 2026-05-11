package com.gabalus.fracturedreality.recipes;

import com.gabalus.fracturedreality.FracturedReality;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GatedRecipeReloadListener extends SimpleJsonResourceReloadListener {
    public static final String DIRECTORY = "fractured_reality/gated_recipes";
    private static final Gson GSON = new Gson();

    public GatedRecipeReloadListener() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
        Set<ResourceLocation> loadedRecipes = new HashSet<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            JsonElement element = entry.getValue();
            if (!element.isJsonObject()) {
                FracturedReality.LOGGER.warn("Skipping gated recipe file {} because root is not an object", entry.getKey());
                continue;
            }

            JsonObject object = element.getAsJsonObject();
            JsonArray recipes = object.getAsJsonArray("recipes");
            if (recipes == null) {
                FracturedReality.LOGGER.warn("Skipping gated recipe file {} because it has no recipes array", entry.getKey());
                continue;
            }

            for (JsonElement recipeElement : recipes) {
                if (!recipeElement.isJsonPrimitive()) {
                    continue;
                }

                ResourceLocation recipeId = ResourceLocation.tryParse(recipeElement.getAsString());
                if (recipeId != null) {
                    loadedRecipes.add(recipeId);
                } else {
                    FracturedReality.LOGGER.warn("Invalid gated recipe id '{}' in {}", recipeElement.getAsString(), entry.getKey());
                }
            }
        }

        GatedRecipeRegistry.replaceAll(loadedRecipes);
        FracturedReality.LOGGER.info("Loaded {} gated Fractured Reality recipe ids", GatedRecipeRegistry.getGatedRecipes().size());
    }
}
