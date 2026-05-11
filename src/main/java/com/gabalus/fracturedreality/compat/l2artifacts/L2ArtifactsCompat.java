package com.gabalus.fracturedreality.compat.l2artifacts;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.registry.FRItems;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class L2ArtifactsCompat implements CompatModule {
    @Override
    public String modId() {
        return "l2artifacts";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        int tier = Math.max(1, context.tier());
        if (tier < 2) {
            return;
        }

        int shards = 2 + tier;
        ItemStack stack = new ItemStack(FRItems.ECHO_SHARD.get(), shards);
        if (!context.player().getInventory().add(stack)) {
            context.player().drop(stack, false);
        }

        context.player().sendSystemMessage(Component.literal("L2Artifacts compatibility detected: bonus Echo Shards awarded as artifact-attunement proxy loot.")
            .withStyle(ChatFormatting.GOLD));
    }
}
