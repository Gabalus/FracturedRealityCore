package com.gabalus.fracturedreality.recipes;

import com.gabalus.fracturedreality.FracturedReality;
import com.gabalus.fracturedreality.rewards.RecipeScrollPoolReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FracturedReality.MODID)
public final class RecipeDataEvents {
    private RecipeDataEvents() {}

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new GatedRecipeReloadListener());
        event.addListener(new RecipeScrollPoolReloadListener());
    }
}
