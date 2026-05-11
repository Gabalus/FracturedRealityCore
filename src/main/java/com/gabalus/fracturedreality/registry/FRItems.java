package com.gabalus.fracturedreality.registry;

import com.gabalus.fracturedreality.FracturedReality;
import com.gabalus.fracturedreality.codex.FracturedCodexItem;
import com.gabalus.fracturedreality.rewards.RecipeScrollItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FRItems {
    private FRItems() {}

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, FracturedReality.MODID);

    public static final RegistryObject<Item> ECHO_SHARD = ITEMS.register("echo_shard",
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ECHO_DUST = ITEMS.register("echo_dust",
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SEALED_RECIPE_SCROLL = ITEMS.register("sealed_recipe_scroll",
        () -> new RecipeScrollItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> DISCORD_CORE_FRAGMENT = ITEMS.register("discord_core_fragment",
        () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> FRACTURED_CODEX = ITEMS.register("fractured_codex",
        () -> new FracturedCodexItem(new Item.Properties().stacksTo(1)));
}
