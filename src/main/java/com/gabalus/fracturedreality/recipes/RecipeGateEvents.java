package com.gabalus.fracturedreality.recipes;

import com.gabalus.fracturedreality.FracturedReality;
import com.gabalus.fracturedreality.progression.PlayerProgressionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FracturedReality.MODID)
public final class RecipeGateEvents {
    private RecipeGateEvents() {}

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        Recipe<?> recipe = event.getRecipe();
        if (recipe == null) {
            return;
        }

        ResourceLocation recipeId = recipe.getId();
        if (!GatedRecipeRegistry.isGated(recipeId)) {
            return;
        }

        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression -> {
            if (progression.knowsRecipe(recipeId)) {
                return;
            }

            ItemStack crafted = event.getCrafting();
            removeCraftedStack(player, crafted);
            player.sendSystemMessage(Component.literal("You do not know this recipe yet: " + recipeId)
                .withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("Find and use a Sealed Recipe Scroll for this recipe.")
                .withStyle(ChatFormatting.YELLOW));
        });
    }

    private static void removeCraftedStack(ServerPlayer player, ItemStack crafted) {
        if (crafted.isEmpty()) {
            return;
        }

        ItemStack toRemove = crafted.copy();
        toRemove.setCount(crafted.getCount());
        player.getInventory().clearOrCountMatchingItems(stack -> ItemStack.isSameItemSameTags(stack, toRemove), toRemove.getCount(), player.inventoryMenu.getCraftSlots());
        player.getInventory().clearOrCountMatchingItems(stack -> ItemStack.isSameItemSameTags(stack, toRemove), toRemove.getCount(), player.getInventory());
    }
}
