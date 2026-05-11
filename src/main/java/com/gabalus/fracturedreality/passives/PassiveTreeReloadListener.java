package com.gabalus.fracturedreality.passives;

import com.gabalus.fracturedreality.FracturedReality;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PassiveTreeReloadListener extends SimpleJsonResourceReloadListener {
    public static final String DIRECTORY = "fractured_reality/passive_nodes";
    private static final Gson GSON = new Gson();

    public PassiveTreeReloadListener() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
        List<PassiveNodeDefinition> nodes = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            if (!entry.getValue().isJsonObject()) {
                FracturedReality.LOGGER.warn("Skipping passive node {} because root is not an object", entry.getKey());
                continue;
            }

            try {
                PassiveNodeDefinition node = parseNode(entry.getKey(), entry.getValue().getAsJsonObject());
                nodes.add(node);
            } catch (RuntimeException exception) {
                FracturedReality.LOGGER.error("Failed to load passive node {}", entry.getKey(), exception);
            }
        }

        PassiveTreeRegistry.replaceAll(nodes);
        FracturedReality.LOGGER.info("Loaded {} Fractured Reality passive nodes", PassiveTreeRegistry.size());
    }

    private static PassiveNodeDefinition parseNode(ResourceLocation fileId, JsonObject object) {
        ResourceLocation id = object.has("id") ? new ResourceLocation(object.get("id").getAsString()) : fileId;
        String name = string(object, "name", id.toString());
        String description = string(object, "description", "");
        PassiveNodeType type = PassiveNodeType.valueOf(string(object, "type", "SMALL").toUpperCase());
        int maxRank = integer(object, "max_rank", 1);
        int cost = integer(object, "cost", 1);
        int x = integer(object, "x", 0);
        int y = integer(object, "y", 0);
        String ascendancy = string(object, "ascendancy", "");

        List<ResourceLocation> requires = new ArrayList<>();
        if (object.has("requires") && object.get("requires").isJsonArray()) {
            JsonArray array = object.getAsJsonArray("requires");
            for (JsonElement element : array) {
                ResourceLocation required = ResourceLocation.tryParse(element.getAsString());
                if (required != null) {
                    requires.add(required);
                }
            }
        }

        List<PassiveStatModifier> modifiers = new ArrayList<>();
        if (object.has("modifiers") && object.get("modifiers").isJsonArray()) {
            JsonArray array = object.getAsJsonArray("modifiers");
            for (JsonElement element : array) {
                if (!element.isJsonObject()) {
                    continue;
                }
                JsonObject mod = element.getAsJsonObject();
                modifiers.add(new PassiveStatModifier(
                    string(mod, "stat", "unknown"),
                    decimal(mod, "value", 0.0D),
                    string(mod, "operation", "add")
                ));
            }
        }

        return new PassiveNodeDefinition(id, name, description, type, Math.max(1, maxRank), Math.max(1, cost), x, y, ascendancy, requires, modifiers);
    }

    private static String string(JsonObject object, String key, String fallback) {
        return object.has(key) ? object.get(key).getAsString() : fallback;
    }

    private static int integer(JsonObject object, String key, int fallback) {
        return object.has(key) ? object.get(key).getAsInt() : fallback;
    }

    private static double decimal(JsonObject object, String key, double fallback) {
        return object.has(key) ? object.get(key).getAsDouble() : fallback;
    }
}
