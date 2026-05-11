package com.gabalus.fracturedreality.compat.passive_skill_tree;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.progression.PlayerProgressionProvider;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public class PassiveSkillTreeCompat implements CompatModule {
    private static final String AWARD_EVENT_CLASS = "daripher.skilltree.api.fractured.FracturedPassivePointAwardEvent";
    private static final String SOURCE_ENUM_CLASS = "daripher.skilltree.api.fractured.FracturedProgressionSource";

    @Override
    public String modId() {
        return "skilltree";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        int tier = Math.max(1, context.tier());
        int bonusPoints = calculateBridgePoints(tier, context.createdByPlayer());
        if (bonusPoints <= 0) {
            return;
        }

        if (postNativeSkillTreeEvent(context, bonusPoints)) {
            context.player().sendSystemMessage(Component.literal("Passive Skill Tree: Discord reward sent to native skill points.")
                .withStyle(ChatFormatting.AQUA));
            return;
        }

        context.player().getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            progression.addPassivePoints(bonusPoints);
            progression.completeMilestone("skilltree_bridge_discord_rewards");
            context.player().sendSystemMessage(Component.literal("Passive Skill Tree fallback bridge: gained " + bonusPoints + " Fractured passive point" + (bonusPoints == 1 ? "" : "s") + ".")
                .withStyle(ChatFormatting.AQUA));
        });
    }

    private static boolean postNativeSkillTreeEvent(RewardContext context, int points) {
        try {
            Class<?> sourceClass = Class.forName(SOURCE_ENUM_CLASS);
            Object source = Enum.valueOf((Class<Enum>) sourceClass.asSubclass(Enum.class), "DISCORD_CLEAR");
            Class<?> eventClass = Class.forName(AWARD_EVENT_CLASS);
            Object event = eventClass
                .getConstructor(net.minecraft.server.level.ServerPlayer.class, int.class, sourceClass, String.class)
                .newInstance(context.player(), points, source, "Tier " + Math.max(1, context.tier()) + " Discord completed");
            MinecraftForge.EVENT_BUS.post((Event) event);
            return true;
        } catch (ReflectiveOperationException | LinkageError | ClassCastException exception) {
            return false;
        }
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
