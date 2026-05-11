package com.gabalus.fracturedreality.progression;

import com.gabalus.fracturedreality.FracturedReality;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FracturedReality.MODID)
public final class ProgressionEvents {
    private ProgressionEvents() {}

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerProgression.class);
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(PlayerProgressionProvider.ID, new PlayerProgressionProvider());
        }
    }

    @SubscribeEvent
    public static void copyPlayerData(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();
        oldPlayer.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(oldProgression ->
            newPlayer.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(newProgression ->
                newProgression.copyFrom(oldProgression)));
        oldPlayer.invalidateCaps();
    }
}
