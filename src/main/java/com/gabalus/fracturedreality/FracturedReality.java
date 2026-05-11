package com.gabalus.fracturedreality;

import com.gabalus.fracturedreality.discord.DiscordIntegrationEvents;
import com.gabalus.fracturedreality.registry.FRItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(FracturedReality.MODID)
public class FracturedReality {
    public static final String MODID = "fractured_reality";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FracturedReality() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        FRItems.ITEMS.register(modBus);

        MinecraftForge.EVENT_BUS.register(DiscordIntegrationEvents.class);

        LOGGER.info("Fractured Reality Core loaded. Hyperbox/InfiniDungeons present: {}", ModList.get().isLoaded("hyperbox"));
    }
}
