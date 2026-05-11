package com.gabalus.fracturedreality;

import com.gabalus.fracturedreality.compat.CompatManager;
import com.gabalus.fracturedreality.discord.DiscordIntegrationEvents;
import com.gabalus.fracturedreality.registry.FRItems;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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
        modBus.addListener(this::onBuildCreativeModeTabContents);

        CompatManager.init();
        MinecraftForge.EVENT_BUS.register(DiscordIntegrationEvents.class);

        LOGGER.info("Fractured Reality Core loaded. Hyperbox/InfiniDungeons present: {}", ModList.get().isLoaded("hyperbox"));
    }

    private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(FRItems.ECHO_SHARD);
            event.accept(FRItems.ECHO_DUST);
            event.accept(FRItems.SEALED_RECIPE_SCROLL);
            event.accept(FRItems.DISCORD_CORE_FRAGMENT);
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(FRItems.FRACTURED_CODEX);
        }
    }
}
