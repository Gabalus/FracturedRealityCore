package com.gabalus.fracturedreality.compat.passive_skill_tree;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.progression.PlayerProgressionProvider;
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
        int tier = Math.max(1, context.tier());
        context.player().getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            int bonusPoints = calculateBridgePoints(tier, context.createdByPlayer());
            if (bonusPoints <= 0) {
                return;
            }

            progression.addPassivePoints(bonusPoints);
            progression.completeMilestone("skilltree_bridge_discord_rewards");
            context.player().sendSystemMessage(Component.literal("Passive Skill Tree bridge: gained " + bonusPoints + " Fractured passive point" + (bonusPoints == 1 ? "" : "s") + ".")
                .withStyle(ChatFormatting.AQUA));
        });
    }

    private static int calculateBridgePoints(int tier, boolean createdByPlayer) {
        int points = 0;
        if (tier >= 2) {
            points += 1;
        }
        if (tier >= 4) {
            points += 1;
        }
        if (createdByPlayer && tier >= 3) {
            points += 1;
        }
        return points;
    }
}
