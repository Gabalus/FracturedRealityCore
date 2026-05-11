package com.gabalus.fracturedreality.rewards;

import com.gabalus.fracturedreality.progression.PlayerProgressionProvider;
import com.gabalus.fracturedreality.registry.FRItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class DiscordRewardManager {
    private DiscordRewardManager() {}

    public static void grantCompletionRewards(RewardContext context) {
        ServerPlayer player = context.player();
        int tier = Math.max(1, context.tier());
        ResourceLocation recipeReward = RecipeScrollPoolRegistry.rollRecipe(context.theme(), tier);

        giveOrDrop(player, new ItemStack(FRItems.ECHO_SHARD.get(), 4 + tier * 4));
        giveOrDrop(player, RecipeScrollItem.create(recipeReward, context.theme(), tier));

        if (tier >= 2) {
            giveOrDrop(player, new ItemStack(FRItems.DISCORD_CORE_FRAGMENT.get(), tier - 1));
        }

        player.giveExperiencePoints(25 * tier);
        awardProgression(player, tier);

        player.sendSystemMessage(Component.literal("Fractured Reality rewards granted for " + context.theme() + " Tier " + tier + ".")
            .withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    private static void awardProgression(ServerPlayer player, int tier) {
        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            progression.addCompletedDiscord();

            int completed = progression.getCompletedDiscordCount();
            int passivePoints = 0;

            if (completed == 1) {
                passivePoints += 1;
            }

            if (completed % 3 == 0) {
                passivePoints += 1;
            }

            if (tier >= 3 && completed % 2 == 0) {
                passivePoints += 1;
            }

            if (passivePoints > 0) {
                progression.addPassivePoints(passivePoints);
                player.sendSystemMessage(Component.literal("Gained " + passivePoints + " passive point" + (passivePoints == 1 ? "" : "s") + ". Total: " + progression.getPassivePoints())
                    .withStyle(ChatFormatting.AQUA));
            }
        });
    }

    private static void giveOrDrop(ServerPlayer player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
}
