package com.gabalus.fracturedreality.rewards;

import com.gabalus.fracturedreality.FracturedReality;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class RecipeScrollPoolReloadListener extends SimpleJsonResourceReloadListener {
    public static final String DIRECTORY = "fractured_reality/recipe_scroll_pools";
    private static final Gson GSON = new Gson();

    public RecipeScrollPoolReloadListener() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<String, RecipeScrollPool> pools = new HashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            JsonElement root = entry.getValue();
            if (!root.isJsonObject()) {
                FracturedReality.LOGGER.warn("Skipping recipe scroll pool {} because root is not an object", entry.getKey());
                continue;
            }

            JsonObject object = root.getAsJsonObject();
            String theme = object.has("theme") ? object.get("theme").getAsString().toLowerCase() : entry.getKey().getPath();
            RecipeScrollPool pool = new RecipeScrollPool(theme);

            JsonArray entries = object.getAsJsonArray("entries");
            if (entries == null) {
                FracturedReality.LOGGER.warn("Skipping recipe scroll pool {} because it has no entries array", entry.getKey());
                continue;
            }

            for (JsonElement element : entries) {
                if (!element.isJsonObject()) {
                    continue;
                }

                JsonObject recipeObject = element.getAsJsonObject();
                if (!recipeObject.has("recipe")) {
                    continue;
                }

                ResourceLocation recipeId = ResourceLocation.tryParse(recipeObject.get("recipe").getAsString());
                if (recipeId == null) {
                    FracturedReality.LOGGER.warn("Invalid recipe id in recipe scroll pool {}", entry.getKey());
                    continue;
                }

                int minTier = recipeObject.has("min_tier") ? recipeObject.get("min_tier").getAsInt() : 1;
                int weight = recipeObject.has("weight") ? recipeObject.get("weight").getAsInt() : 1;
                pool.add(recipeId, minTier, weight);
            }

            if (!pool.isEmpty()) {
                pools.put(theme, pool);
            }
        }

        RecipeScrollPoolRegistry.replaceAll(pools);
        FracturedReality.LOGGER.info("Loaded {} Fractured Reality recipe scroll pools", RecipeScrollPoolRegistry.size());
    }
}
