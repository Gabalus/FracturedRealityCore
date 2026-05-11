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

        giveOrDrop(player, new ItemStack(FRItems.ECHO_SHARD.get(), 4 + tier * 4));
        giveOrDrop(player, RecipeScrollItem.create(selectRecipeReward(context), context.theme(), tier));

        if (tier >= 2) {
            giveOrDrop(player, new ItemStack(FRItems.DISCORD_CORE_FRAGMENT.get(), tier - 1));
        }

        player.giveExperiencePoints(25 * tier);
        awardProgression(player, tier);

        player.sendSystemMessage(Component.literal("Fractured Reality rewards granted for " + context.theme() + " Tier " + tier + ".")
            .withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    private static ResourceLocation selectRecipeReward(RewardContext context) {
        String theme = context.theme().toLowerCase();
        int tier = Math.max(1, context.tier());

        if (theme.contains("arsenal") || theme.contains("gun") || theme.contains("bullet")) {
            return new ResourceLocation("minecraft", tier >= 2 ? "crossbow" : "arrow");
        }

        if (theme.contains("arcane") || theme.contains("library") || theme.contains("astral")) {
            return new ResourceLocation("minecraft", tier >= 2 ? "enchanting_table" : "book");
        }

        if (theme.contains("machine") || theme.contains("forge") || theme.contains("foundry")) {
            return new ResourceLocation("minecraft", tier >= 2 ? "anvil" : "furnace");
        }

        if (theme.contains("garden") || theme.contains("beast") || theme.contains("grove")) {
            return new ResourceLocation("minecraft", tier >= 2 ? "golden_apple" : "bread");
        }

        return new ResourceLocation("minecraft", tier >= 2 ? "shield" : "chest");
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
