package com.gabalus.fracturedreality.compat;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class RegistryRewardHelper {
    private RegistryRewardHelper() {}

    public static boolean giveItem(ServerPlayer player, ResourceLocation itemId, int count) {
        Item item = BuiltInRegistries.ITEM.get(itemId);
        if (item == Items.AIR) {
            return false;
        }

        ItemStack stack = new ItemStack(item, Math.max(1, count));
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
        return true;
    }

    public static void giveFirstAvailable(ServerPlayer player, int count, ResourceLocation... itemIds) {
        for (ResourceLocation itemId : itemIds) {
            if (giveItem(player, itemId, count)) {
                player.sendSystemMessage(Component.literal("Received compatibility reward: " + itemId)
                    .withStyle(ChatFormatting.GRAY));
                return;
            }
        }
    }
}
