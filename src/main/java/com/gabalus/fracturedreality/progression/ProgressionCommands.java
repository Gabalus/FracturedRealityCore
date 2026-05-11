package com.gabalus.fracturedreality.progression;

import com.gabalus.fracturedreality.FracturedReality;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FracturedReality.MODID)
public final class ProgressionCommands {
    private ProgressionCommands() {}

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fractured")
            .then(Commands.literal("progression")
                .then(Commands.literal("get")
                    .executes(ctx -> showProgression(ctx.getSource().getPlayerOrException())))
                .then(Commands.literal("add_passive_points")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                        .executes(ctx -> addPassivePoints(
                            ctx.getSource().getPlayerOrException(),
                            IntegerArgumentType.getInteger(ctx, "amount")))))
                .then(Commands.literal("complete_tutorial")
                    .requires(source -> source.hasPermission(2))
                    .executes(ctx -> completeTutorial(ctx.getSource().getPlayerOrException())))
                .then(Commands.literal("reset_tutorial")
                    .requires(source -> source.hasPermission(2))
                    .executes(ctx -> resetTutorial(ctx.getSource().getPlayerOrException())))));
    }

    private static int showProgression(ServerPlayer player) {
        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            player.sendSystemMessage(Component.literal("Fractured Reality Progression").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("Tutorial completed: " + progression.hasCompletedVoidTutorial()).withStyle(ChatFormatting.GRAY));
            player.sendSystemMessage(Component.literal("Codex received: " + progression.hasReceivedCodex()).withStyle(ChatFormatting.GRAY));
            player.sendSystemMessage(Component.literal("Completed Discords: " + progression.getCompletedDiscordCount()).withStyle(ChatFormatting.GRAY));
            player.sendSystemMessage(Component.literal("Passive Points: " + progression.getPassivePoints()).withStyle(ChatFormatting.AQUA));
            player.sendSystemMessage(Component.literal("Unlocked Discord Tier: " + progression.getUnlockedDiscordTier()).withStyle(ChatFormatting.GRAY));
            player.sendSystemMessage(Component.literal("Known Recipes: " + progression.getKnownRecipes().size()).withStyle(ChatFormatting.GRAY));
        });
        return 1;
    }

    private static int addPassivePoints(ServerPlayer player, int amount) {
        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            progression.addPassivePoints(amount);
            player.sendSystemMessage(Component.literal("Added " + amount + " passive point(s). Total: " + progression.getPassivePoints()).withStyle(ChatFormatting.AQUA));
        });
        return 1;
    }

    private static int completeTutorial(ServerPlayer player) {
        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            progression.setCompletedVoidTutorial(true);
            progression.completeMilestone("void_tutorial_completed");
            player.sendSystemMessage(Component.literal("Void tutorial marked complete.").withStyle(ChatFormatting.GREEN));
        });
        return 1;
    }

    private static int resetTutorial(ServerPlayer player) {
        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            progression.setCompletedVoidTutorial(false);
            player.sendSystemMessage(Component.literal("Void tutorial reset.").withStyle(ChatFormatting.YELLOW));
        });
        return 1;
    }
}
