package com.gabalus.fracturedreality.passives;

import com.gabalus.fracturedreality.FracturedReality;
import com.gabalus.fracturedreality.progression.PlayerProgressionProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = FracturedReality.MODID)
public final class PassiveTreeCommands {
    private PassiveTreeCommands() {}

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fractured")
            .then(Commands.literal("passives")
                .then(Commands.literal("list")
                    .executes(ctx -> listNodes(ctx.getSource().getPlayerOrException())))
                .then(Commands.literal("allocate")
                    .then(Commands.argument("node", StringArgumentType.string())
                        .executes(ctx -> allocate(
                            ctx.getSource().getPlayerOrException(),
                            StringArgumentType.getString(ctx, "node")))))
                .then(Commands.literal("stats")
                    .executes(ctx -> showStats(ctx.getSource().getPlayerOrException())))
                .then(Commands.literal("choose_ascendancy")
                    .then(Commands.argument("ascendancy", StringArgumentType.string())
                        .executes(ctx -> chooseAscendancy(
                            ctx.getSource().getPlayerOrException(),
                            StringArgumentType.getString(ctx, "ascendancy")))))));
    }

    private static int listNodes(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("Fractured Passive Tree Nodes: " + PassiveTreeRegistry.size())
            .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));

        int shown = 0;
        for (PassiveNodeDefinition node : PassiveTreeRegistry.all()) {
            if (shown >= 20) {
                player.sendSystemMessage(Component.literal("...more nodes loaded. Use datapack files or future GUI for full view.").withStyle(ChatFormatting.GRAY));
                break;
            }
            player.sendSystemMessage(Component.literal(node.id() + " | " + node.name() + " | " + node.type() + " | cost " + node.cost())
                .withStyle(node.isAscendancyNode() ? ChatFormatting.GOLD : ChatFormatting.GRAY));
            shown++;
        }
        return 1;
    }

    private static int allocate(ServerPlayer player, String nodeText) {
        ResourceLocation nodeId = ResourceLocation.tryParse(nodeText);
        if (nodeId == null) {
            player.sendSystemMessage(Component.literal("Invalid node id: " + nodeText).withStyle(ChatFormatting.RED));
            return 0;
        }

        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            PassiveTreeRegistry.AllocationResult result = PassiveTreeRegistry.allocate(progression, nodeId);
            if (result.success()) {
                player.sendSystemMessage(Component.literal(result.message()).withStyle(ChatFormatting.GREEN));
            } else {
                player.sendSystemMessage(Component.literal(result.message()).withStyle(ChatFormatting.RED));
            }
        });
        return 1;
    }

    private static int showStats(ServerPlayer player) {
        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            player.sendSystemMessage(Component.literal("Fractured Passive Stats").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("Passive Points: " + progression.getPassivePoints()).withStyle(ChatFormatting.GRAY));
            player.sendSystemMessage(Component.literal("Ascendancy: " + (progression.hasAscendancy() ? progression.getAscendancyId() : "none")).withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.literal("Ascendancy Points: " + progression.getAscendancyPoints()).withStyle(ChatFormatting.GRAY));
            player.sendSystemMessage(Component.literal("Allocated Nodes: " + progression.getAllocatedPassiveNodes().size()).withStyle(ChatFormatting.GRAY));

            Map<String, Double> stats = PassiveTreeRegistry.aggregateStats(progression);
            if (stats.isEmpty()) {
                player.sendSystemMessage(Component.literal("No passive stats allocated.").withStyle(ChatFormatting.DARK_GRAY));
                return;
            }

            for (Map.Entry<String, Double> entry : stats.entrySet()) {
                player.sendSystemMessage(Component.literal(entry.getKey() + ": " + entry.getValue()).withStyle(ChatFormatting.GRAY));
            }
        });
        return 1;
    }

    private static int chooseAscendancy(ServerPlayer player, String ascendancy) {
        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            if (progression.chooseAscendancy(ascendancy)) {
                progression.addAscendancyPoints(2);
                player.sendSystemMessage(Component.literal("Ascendancy chosen: " + ascendancy + ". Gained 2 ascendancy points.").withStyle(ChatFormatting.GOLD));
            } else {
                player.sendSystemMessage(Component.literal("Could not choose ascendancy. You may already have one.").withStyle(ChatFormatting.RED));
            }
        });
        return 1;
    }
}
