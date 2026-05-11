package com.gabalus.fracturedreality.rewards;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public record RewardContext(
    ServerPlayer player,
    ResourceKey<Level> discordDimension,
    String theme,
    int tier,
    String source,
    boolean createdByPlayer
) {}
