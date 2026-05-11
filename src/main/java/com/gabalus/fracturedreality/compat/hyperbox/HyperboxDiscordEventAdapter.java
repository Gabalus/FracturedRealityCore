package com.gabalus.fracturedreality.compat.hyperbox;

import com.gabalus.fracturedreality.FracturedReality;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

import java.lang.reflect.Method;
import java.util.Optional;

public final class HyperboxDiscordEventAdapter {
    public static final String DISCORD_COMPLETED_EVENT_CLASS = "commoble.hyperbox.api.discord.DiscordCompletedEvent";
    public static final String DISCORD_ENTERED_EVENT_CLASS = "commoble.hyperbox.api.discord.DiscordEnteredEvent";

    private HyperboxDiscordEventAdapter() {}

    public static boolean isDiscordCompletedEvent(Event event) {
        return event.getClass().getName().equals(DISCORD_COMPLETED_EVENT_CLASS);
    }

    public static boolean isDiscordEnteredEvent(Event event) {
        return event.getClass().getName().equals(DISCORD_ENTERED_EVENT_CLASS);
    }

    public static Optional<RewardContext> tryReadRewardContext(Event event) {
        try {
            ServerPlayer player = (ServerPlayer) event.getClass().getMethod("getPlayer").invoke(event);
            Object metadata = event.getClass().getMethod("getMetadata").invoke(event);

            return Optional.of(new RewardContext(
                player,
                readDimension(metadata),
                readString(metadata, "theme", "random"),
                readInt(metadata, "tier", 1),
                readString(metadata, "source", "unknown"),
                readBoolean(metadata, "createdByPlayer", false)
            ));
        } catch (ReflectiveOperationException | ClassCastException exception) {
            FracturedReality.LOGGER.error("Failed to read InfiniDungeons Discord event", exception);
            return Optional.empty();
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
