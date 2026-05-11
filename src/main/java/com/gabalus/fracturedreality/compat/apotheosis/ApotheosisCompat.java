package com.gabalus.fracturedreality.compat.apotheosis;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.registry.FRItems;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ApotheosisCompat implements CompatModule {
    @Override
    public String modId() {
        return "apotheosis";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        int tier = Math.max(1, context.tier());
        if (tier < 2) {
            return;
        }

        int fragments = tier >= 4 ? 3 : tier >= 3 ? 2 : 1;
        ItemStack stack = new ItemStack(FRItems.DISCORD_CORE_FRAGMENT.get(), fragments);
        if (!context.player().getInventory().add(stack)) {
            context.player().drop(stack, false);
        }

        context.player().sendSystemMessage(Component.literal("Apotheosis compatibility detected: bonus Discord Core Fragments awarded as affix-crafting proxy loot.")
            .withStyle(ChatFormatting.DARK_AQUA));
    }
}
