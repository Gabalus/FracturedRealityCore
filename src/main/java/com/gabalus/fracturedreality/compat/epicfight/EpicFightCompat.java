package com.gabalus.fracturedreality.compat.epicfight;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.registry.FRItems;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class EpicFightCompat implements CompatModule {
    @Override
    public String modId() {
        return "epicfight";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        if (!isCombatTheme(context.theme())) {
            return;
        }

        int tier = Math.max(1, context.tier());
        ItemStack echoes = new ItemStack(FRItems.ECHO_SHARD.get(), 2 * tier);
        if (!context.player().getInventory().add(echoes)) {
            context.player().drop(echoes, false);
        }

        if (tier >= 3) {
            ItemStack core = new ItemStack(FRItems.DISCORD_CORE_FRAGMENT.get(), 1);
            if (!context.player().getInventory().add(core)) {
                context.player().drop(core, false);
            }
        }

        context.player().sendSystemMessage(Component.literal("Epic Fight compatibility detected: bonus combat mastery rewards granted.")
            .withStyle(ChatFormatting.DARK_RED));
    }

    private static boolean isCombatTheme(String theme) {
        String normalized = theme == null ? "" : theme.toLowerCase();
        return normalized.contains("catacombs") || normalized.contains("arena") || normalized.contains("boss") || normalized.contains("war") || normalized.contains("duel");
    }
}
