package com.gabalus.fracturedreality.compat.apotheosis;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ApotheosisCompat implements CompatModule {
    @Override
    public String modId() {
        return "apotheosis";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        if (context.tier() >= 2) {
            context.player().sendSystemMessage(Component.literal("Apotheosis compatibility detected: affix reward hook pending.")
                .withStyle(ChatFormatting.DARK_AQUA));
        }
    }
}
