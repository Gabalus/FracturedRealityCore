package com.gabalus.fracturedreality.rewards;

import com.gabalus.fracturedreality.registry.FRItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class DiscordRewardManager {
    private DiscordRewardManager() {}

    public static void grantCompletionRewards(RewardContext context) {
        ServerPlayer player = context.player();
        int tier = Math.max(1, context.tier());

        giveOrDrop(player, new ItemStack(FRItems.ECHO_SHARD.get(), 4 + tier * 4));
        giveOrDrop(player, new ItemStack(FRItems.SEALED_RECIPE_SCROLL.get(), 1));

        if (tier >= 2) {
            giveOrDrop(player, new ItemStack(FRItems.DISCORD_CORE_FRAGMENT.get(), tier - 1));
        }

        player.giveExperiencePoints(25 * tier);
        player.sendSystemMessage(Component.literal("Fractured Reality rewards granted for " + context.theme() + " Tier " + tier + ".")
            .withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    private static void giveOrDrop(ServerPlayer player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
}
