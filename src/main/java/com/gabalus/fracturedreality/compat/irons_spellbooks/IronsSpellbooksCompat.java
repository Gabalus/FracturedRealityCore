package com.gabalus.fracturedreality.compat.irons_spellbooks;

import com.gabalus.fracturedreality.compat.CompatModule;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class IronsSpellbooksCompat implements CompatModule {
    @Override
    public String modId() {
        return "irons_spellbooks";
    }

    @Override
    public void onDiscordCompleted(RewardContext context) {
        if (isMagicTheme(context.theme())) {
            context.player().sendSystemMessage(Component.literal("Iron's Spells compatibility detected: magic Discord reward hook pending.")
                .withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    private static boolean isMagicTheme(String theme) {
        String normalized = theme == null ? "" : theme.toLowerCase();
        return normalized.contains("arcane") || normalized.contains("library") || normalized.contains("astral") || normalized.contains("spell");
    }
}
