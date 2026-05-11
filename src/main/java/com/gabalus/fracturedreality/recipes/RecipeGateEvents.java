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
            removeCraftedOutputFromInventory(player, crafted);
            player.sendSystemMessage(Component.literal("You do not know this recipe yet: " + recipeId)
                .withStyle(ChatFormatting.RED));
            player.sendSystemMessage(Component.literal("Find and use a Sealed Recipe Scroll for this recipe. The crafted output was removed.")
                .withStyle(ChatFormatting.YELLOW));
        });
    }

    private static void removeCraftedOutputFromInventory(ServerPlayer player, ItemStack crafted) {
        if (crafted.isEmpty()) {
            return;
        }

        int remaining = crafted.getCount();
        for (int slot = 0; slot < player.getInventory().getContainerSize() && remaining > 0; slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (ItemStack.isSameItemSameTags(stack, crafted)) {
                int removed = Math.min(remaining, stack.getCount());
                stack.shrink(removed);
                remaining -= removed;
                player.getInventory().setChanged();
            }
        }
    }
}
