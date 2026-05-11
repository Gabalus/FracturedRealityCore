package com.gabalus.fracturedreality.compat.scorched_guns;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ScorchedGunsCompat implements CompatModule {
    @Override
    public String modId() {
        return "scguns";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        if (isGunTheme(context.theme())) {
            context.player().sendSystemMessage(Component.literal("Scorched Guns compatibility detected: arsenal reward hook pending.")
                .withStyle(ChatFormatting.RED));
        }
    }

    private static boolean isGunTheme(String theme) {
        String normalized = theme == null ? "" : theme.toLowerCase();
        return normalized.contains("arsenal") || normalized.contains("gun") || normalized.contains("bullet") || normalized.contains("foundry");
    }
}
