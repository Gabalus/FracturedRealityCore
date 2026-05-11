package com.gabalus.fracturedreality.compat.irons_spellbooks;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.compat.RegistryRewardHelper;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class IronsSpellbooksCompat implements CompatModule {
    @Override
    public String modId() {
        return "irons_spellbooks";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        if (!isMagicTheme(context.theme())) {
            return;
        }

        int tier = Math.max(1, context.tier());

        RegistryRewardHelper.giveFirstAvailable(context.player(), Math.max(1, tier),
            irons("scroll"),
            irons("common_ink"));

        if (tier >= 2) {
            RegistryRewardHelper.giveFirstAvailable(context.player(), 1,
                irons("uncommon_ink"),
                irons("common_ink"));
        }

        if (tier >= 3) {
            RegistryRewardHelper.giveFirstAvailable(context.player(), 1,
                irons("iron_spell_book"),
                irons("copper_spell_book"),
                irons("rare_ink"));
        } else if (tier >= 2) {
            RegistryRewardHelper.giveFirstAvailable(context.player(), 1,
                irons("copper_spell_book"),
                irons("uncommon_ink"));
        }

        context.player().sendSystemMessage(Component.literal("Iron's Spells arcane reward rolled.")
            .withStyle(ChatFormatting.DARK_PURPLE));
    }

    private static ResourceLocation irons(String path) {
        return new ResourceLocation("irons_spellbooks", path);
    }

    private static boolean isMagicTheme(String theme) {
        String normalized = theme == null ? "" : theme.toLowerCase();
        return normalized.contains("arcane") || normalized.contains("library") || normalized.contains("astral") || normalized.contains("spell");
    }
}
