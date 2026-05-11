package com.gabalus.fracturedreality.compat.battleroyale;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class BattleRoyaleCompat implements CompatModule {
    @Override
    public String modId() {
        return "battleroyale";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        if (context.source().toLowerCase().contains("battle")) {
            context.player().sendSystemMessage(Component.literal("BattleRoyale compatibility detected: PvP reward hook pending.")
                .withStyle(ChatFormatting.DARK_GREEN));
        }
    }
}
