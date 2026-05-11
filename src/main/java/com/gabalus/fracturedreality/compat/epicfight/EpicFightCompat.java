package com.gabalus.fracturedreality.compat.epicfight;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class EpicFightCompat implements CompatModule {
    @Override
    public String modId() {
        return "epicfight";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        if (isCombatTheme(context.theme())) {
            context.player().sendSystemMessage(Component.literal("Epic Fight compatibility detected: combat encounter hook pending.")
                .withStyle(ChatFormatting.DARK_RED));
        }
    }

    private static boolean isCombatTheme(String theme) {
        String normalized = theme == null ? "" : theme.toLowerCase();
        return normalized.contains("catacombs") || normalized.contains("arena") || normalized.contains("boss") || normalized.contains("war");
    }
}
