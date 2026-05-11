package com.gabalus.fracturedreality.passives;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record PassiveNodeDefinition(
    ResourceLocation id,
    String name,
    String description,
    PassiveNodeType type,
    int maxRank,
    int cost,
    int x,
    int y,
    String ascendancy,
    List<ResourceLocation> requires,
    List<PassiveStatModifier> modifiers
) {
    public boolean isAscendancyNode() {
        return type == PassiveNodeType.ASCENDANCY_SMALL
            || type == PassiveNodeType.ASCENDANCY_NOTABLE
            || type == PassiveNodeType.ASCENDANCY_KEYSTONE;
    }
}
