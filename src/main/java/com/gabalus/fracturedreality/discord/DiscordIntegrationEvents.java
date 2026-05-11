package com.gabalus.fracturedreality.discord;

import com.gabalus.fracturedreality.compat.hyperbox.HyperboxDiscordEventAdapter;
import com.gabalus.fracturedreality.rewards.DiscordRewardManager;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class DiscordIntegrationEvents {
    private DiscordIntegrationEvents() {}

    @SubscribeEvent
    public static void onForgeEvent(Event event) {
        if (!HyperboxDiscordEventAdapter.isDiscordCompletedEvent(event)) {
            return;
        }

        HyperboxDiscordEventAdapter.tryReadRewardContext(event)
            .ifPresent(DiscordRewardManager::grantCompletionRewards);
    }
}
