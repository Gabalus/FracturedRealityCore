package com.gabalus.fracturedreality.compat.scorched_guns;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.compat.RegistryRewardHelper;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ScorchedGunsCompat implements CompatModule {
    @Override
    public String modId() {
        return "scguns";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        if (!isGunTheme(context.theme())) {
            return;
        }

        int tier = Math.max(1, context.tier());
        if (tier >= 3) {
            RegistryRewardHelper.giveFirstAvailable(context.player(), 1,
                scguns("m3_carabine"),
                scguns("makeshift_rifle"),
                scguns("musket"),
                scguns("flintlock_pistol"));
        } else if (tier == 2) {
            RegistryRewardHelper.giveFirstAvailable(context.player(), 1,
                scguns("makeshift_rifle"),
                scguns("musket"),
                scguns("flintlock_pistol"));
        } else {
            RegistryRewardHelper.giveFirstAvailable(context.player(), 1,
                scguns("flintlock_pistol"),
                scguns("musket"));
        }

        context.player().sendSystemMessage(Component.literal("Scorched Guns arsenal reward rolled.")
            .withStyle(ChatFormatting.RED));
    }

    private static ResourceLocation scguns(String path) {
        return new ResourceLocation("scguns", path);
    }

    private static boolean isGunTheme(String theme) {
        String normalized = theme == null ? "" : theme.toLowerCase();
        return normalized.contains("arsenal") || normalized.contains("gun") || normalized.contains("bullet") || normalized.contains("foundry");
    }
}
