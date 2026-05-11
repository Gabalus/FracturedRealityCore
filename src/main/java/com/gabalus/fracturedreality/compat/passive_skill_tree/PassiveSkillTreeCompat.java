package com.gabalus.fracturedreality.compat.passive_skill_tree;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class PassiveSkillTreeCompat implements CompatModule {
    @Override
    public String modId() {
        return "skilltree";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        context.player().sendSystemMessage(Component.literal("Passive Skill Tree compatibility detected: external passive reward hook pending.")
            .withStyle(ChatFormatting.AQUA));
    }
}
