package com.gabalus.fracturedreality.codex;

import com.gabalus.fracturedreality.FracturedReality;
import com.gabalus.fracturedreality.progression.PlayerProgressionProvider;
import com.gabalus.fracturedreality.registry.FRItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FracturedReality.MODID)
public final class CodexEvents {
    private CodexEvents() {}

    @SubscribeEvent
    public static void giveCodexOnLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            if (progression.hasReceivedCodex()) {
                return;
            }

            ItemStack codex = new ItemStack(FRItems.FRACTURED_CODEX.get());
            if (!player.getInventory().add(codex)) {
                player.drop(codex, false);
            }

            progression.setReceivedCodex(true);
            player.sendSystemMessage(Component.literal("You received the Fractured Codex.").withStyle(ChatFormatting.LIGHT_PURPLE));
        });
    }
}
