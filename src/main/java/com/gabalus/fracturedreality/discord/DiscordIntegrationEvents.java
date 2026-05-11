package com.gabalus.fracturedreality.discord;

import com.gabalus.fracturedreality.FracturedReality;
import com.gabalus.fracturedreality.rewards.DiscordRewardManager;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Method;

public final class DiscordIntegrationEvents {
    private static final String DISCORD_COMPLETED_EVENT = "commoble.hyperbox.api.discord.DiscordCompletedEvent";

    private DiscordIntegrationEvents() {}

    @SubscribeEvent
    public static void onForgeEvent(Event event) {
        if (!event.getClass().getName().equals(DISCORD_COMPLETED_EVENT)) {
            return;
        }

        try {
            ServerPlayer player = (ServerPlayer) event.getClass().getMethod("getPlayer").invoke(event);
            Object metadata = event.getClass().getMethod("getMetadata").invoke(event);

            RewardContext context = new RewardContext(
                player,
                readDimension(metadata),
                readString(metadata, "theme", "random"),
                readInt(metadata, "tier", 1),
                readString(metadata, "source", "unknown"),
                readBoolean(metadata, "createdByPlayer", false)
            );

            DiscordRewardManager.grantCompletionRewards(context);
        } catch (ReflectiveOperationException | ClassCastException exception) {
            FracturedReality.LOGGER.error("Failed to handle InfiniDungeons Discord completion event", exception);
        }
    }

    @SuppressWarnings("unchecked")
    private static ResourceKey<Level> readDimension(Object metadata) throws ReflectiveOperationException {
        Method method = metadata.getClass().getMethod("dimension");
        return (ResourceKey<Level>) method.invoke(metadata);
    }

    private static String readString(Object metadata, String methodName, String fallback) {
        try {
            Object value = metadata.getClass().getMethod(methodName).invoke(metadata);
            return value instanceof String string ? string : fallback;
        } catch (ReflectiveOperationException exception) {
            return fallback;
        }
    }

    private static int readInt(Object metadata, String methodName, int fallback) {
        try {
            Object value = metadata.getClass().getMethod(methodName).invoke(metadata);
            return value instanceof Integer integer ? integer : fallback;
        } catch (ReflectiveOperationException exception) {
            return fallback;
        }
    }

    private static boolean readBoolean(Object metadata, String methodName, boolean fallback) {
        try {
            Object value = metadata.getClass().getMethod(methodName).invoke(metadata);
            return value instanceof Boolean bool ? bool : fallback;
        } catch (ReflectiveOperationException exception) {
            return fallback;
        }
    }
}
