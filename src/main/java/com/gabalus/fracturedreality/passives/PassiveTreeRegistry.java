package com.gabalus.fracturedreality.passives;

import com.gabalus.fracturedreality.progression.PlayerProgression;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class PassiveTreeRegistry {
    private static final Map<ResourceLocation, PassiveNodeDefinition> NODES = new HashMap<>();

    private PassiveTreeRegistry() {}

    public static void replaceAll(Collection<PassiveNodeDefinition> nodes) {
        NODES.clear();
        for (PassiveNodeDefinition node : nodes) {
            NODES.put(node.id(), node);
        }
    }

    public static Optional<PassiveNodeDefinition> get(ResourceLocation id) {
        return Optional.ofNullable(NODES.get(id));
    }

    public static Collection<PassiveNodeDefinition> all() {
        return Collections.unmodifiableCollection(NODES.values());
    }

    public static int size() {
        return NODES.size();
    }

    public static AllocationResult canAllocate(PlayerProgression progression, ResourceLocation nodeId) {
        PassiveNodeDefinition node = NODES.get(nodeId);
        if (node == null) {
            return AllocationResult.fail("Unknown passive node: " + nodeId);
        }

        int currentRank = progression.getPassiveNodeRank(nodeId);
        if (currentRank >= node.maxRank()) {
            return AllocationResult.fail("Node is already at max rank.");
        }

        if (node.isAscendancyNode()) {
            if (!progression.hasAscendancy()) {
                return AllocationResult.fail("Choose an ascendancy before allocating ascendancy nodes.");
            }
            if (node.ascendancy() == null || !node.ascendancy().equals(progression.getAscendancyId())) {
                return AllocationResult.fail("This node belongs to another ascendancy.");
            }
            if (progression.getAscendancyPoints() < node.cost()) {
                return AllocationResult.fail("Not enough ascendancy points.");
            }
        } else if (progression.getPassivePoints() < node.cost()) {
            return AllocationResult.fail("Not enough passive points.");
        }

        for (ResourceLocation required : node.requires()) {
            if (!progression.hasPassiveNode(required)) {
                return AllocationResult.fail("Missing prerequisite node: " + required);
            }
        }

        return AllocationResult.success(node);
    }

    public static AllocationResult allocate(PlayerProgression progression, ResourceLocation nodeId) {
        AllocationResult result = canAllocate(progression, nodeId);
        if (!result.success()) {
            return result;
        }

        PassiveNodeDefinition node = result.node();
        if (node.isAscendancyNode()) {
            progression.spendAscendancyPoints(node.cost());
        } else {
            progression.spendPassivePoints(node.cost());
        }
        progression.addPassiveNodeRank(nodeId, 1);
        return result;
    }

    public static Map<String, Double> aggregateStats(PlayerProgression progression) {
        Map<String, Double> totals = new HashMap<>();
        for (Map.Entry<ResourceLocation, Integer> entry : progression.getAllocatedPassiveNodes().entrySet()) {
            PassiveNodeDefinition node = NODES.get(entry.getKey());
            if (node == null) {
                continue;
            }

            int rank = entry.getValue();
            for (PassiveStatModifier modifier : node.modifiers()) {
                totals.merge(modifier.stat(), modifier.value() * rank, Double::sum);
            }
        }
        return totals;
    }

    public static List<PassiveNodeDefinition> getAscendancyNodes(String ascendancyId) {
        List<PassiveNodeDefinition> nodes = new ArrayList<>();
        for (PassiveNodeDefinition node : NODES.values()) {
            if (node.isAscendancyNode() && ascendancyId.equals(node.ascendancy())) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    public record AllocationResult(boolean success, String message, PassiveNodeDefinition node) {
        public static AllocationResult success(PassiveNodeDefinition node) {
            return new AllocationResult(true, "Allocated " + node.id(), node);
        }

        public static AllocationResult fail(String message) {
            return new AllocationResult(false, message, null);
        }
    }
}
