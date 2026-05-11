package com.gabalus.fracturedreality.codex;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FracturedCodexItem extends Item {
    public FracturedCodexItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(Component.literal("Fractured Codex")
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
            serverPlayer.sendSystemMessage(Component.literal("Discords are protected dungeon pockets. Clear them to recover recipes, Echo Shards, and progression keys.")
                .withStyle(ChatFormatting.GRAY));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
