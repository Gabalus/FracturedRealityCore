package com.gabalus.fracturedreality.codex;

import com.gabalus.fracturedreality.progression.PlayerProgressionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FracturedCodexItem extends Item {
    public FracturedCodexItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            showCodex(serverPlayer);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static void showCodex(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("Fractured Codex").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
        player.sendSystemMessage(Component.literal("Discords are protected dungeon pockets. Clear them to recover recipes, Echo Shards, and progression keys.").withStyle(ChatFormatting.GRAY));
        player.sendSystemMessage(Component.literal(" "));

        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresentOrElse(progression -> {
            player.sendSystemMessage(Component.literal("Progression").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("Void Tutorial: " + (progression.hasCompletedVoidTutorial() ? "Complete" : "Incomplete"))
                .withStyle(progression.hasCompletedVoidTutorial() ? ChatFormatting.GREEN : ChatFormatting.YELLOW));
            player.sendSystemMessage(Component.literal("Discords Cleared: " + progression.getCompletedDiscordCount()).withStyle(ChatFormatting.GRAY));
            player.sendSystemMessage(Component.literal("Passive Points: " + progression.getPassivePoints()).withStyle(ChatFormatting.AQUA));
            player.sendSystemMessage(Component.literal("Unlocked Discord Tier: " + progression.getUnlockedDiscordTier()).withStyle(ChatFormatting.GRAY));
            player.sendSystemMessage(Component.literal("Known Recipes: " + progression.getKnownRecipes().size()).withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.literal(" "));
            showNextHint(player, progression.hasCompletedVoidTutorial(), progression.getCompletedDiscordCount());
        }, () -> player.sendSystemMessage(Component.literal("Progression data is not attached.").withStyle(ChatFormatting.RED)));
    }

    private static void showNextHint(ServerPlayer player, boolean tutorialComplete, int completedDiscords) {
        player.sendSystemMessage(Component.literal("Next Step").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));

        if (!tutorialComplete) {
            player.sendSystemMessage(Component.literal("Complete the Void Tutorial to unlock normal Overworld progression.").withStyle(ChatFormatting.YELLOW));
            return;
        }

        if (completedDiscords == 0) {
            player.sendSystemMessage(Component.literal("Find an Overworld rift and clear your first Discord.").withStyle(ChatFormatting.YELLOW));
            return;
        }

        if (completedDiscords < 3) {
            player.sendSystemMessage(Component.literal("Clear 3 Tier 1 Discords to begin preparing for deeper progression layers.").withStyle(ChatFormatting.YELLOW));
            return;
        }

        player.sendSystemMessage(Component.literal("Start collecting Echo Shards, recipe scrolls, and Discord Core Fragments for advanced progression.").withStyle(ChatFormatting.YELLOW));
    }
}
