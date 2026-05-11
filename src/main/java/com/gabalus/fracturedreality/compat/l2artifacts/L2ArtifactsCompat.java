package com.gabalus.fracturedreality.compat.l2artifacts;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class L2ArtifactsCompat implements CompatModule {
    @Override
    public String modId() {
        return "l2artifacts";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        if (context.tier() >= 2) {
            context.player().sendSystemMessage(Component.literal("L2Artifacts compatibility detected: artifact reward hook pending.")
                .withStyle(ChatFormatting.GOLD));
        }
    }
}
