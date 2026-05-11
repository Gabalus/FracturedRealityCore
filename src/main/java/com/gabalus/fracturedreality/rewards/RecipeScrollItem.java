package com.gabalus.fracturedreality.rewards;

import com.gabalus.fracturedreality.progression.PlayerProgressionProvider;
import com.gabalus.fracturedreality.registry.FRItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecipeScrollItem extends Item {
    public static final String RECIPE_ID_TAG = "RecipeId";
    public static final String SOURCE_THEME_TAG = "SourceTheme";
    public static final String TIER_TAG = "Tier";

    public RecipeScrollItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(ResourceLocation recipeId, String sourceTheme, int tier) {
        ItemStack stack = new ItemStack(FRItems.SEALED_RECIPE_SCROLL.get());
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(RECIPE_ID_TAG, recipeId.toString());
        tag.putString(SOURCE_THEME_TAG, sourceTheme == null || sourceTheme.isBlank() ? "unknown" : sourceTheme);
        tag.putInt(TIER_TAG, Math.max(1, tier));
        return stack;
    }

    public static @Nullable ResourceLocation getRecipeId(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(RECIPE_ID_TAG)) {
            return null;
        }
        return ResourceLocation.tryParse(tag.getString(RECIPE_ID_TAG));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            ResourceLocation recipeId = getRecipeId(stack);
            if (recipeId == null) {
                serverPlayer.sendSystemMessage(Component.literal("This recipe scroll is blank. It dissolves into Echo Dust.").withStyle(ChatFormatting.YELLOW));
                consumeOneAndGiveDust(serverPlayer, stack, 1);
                return InteractionResultHolder.sidedSuccess(stack, false);
            }

            serverPlayer.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresentOrElse(progression -> {
                if (progression.unlockRecipe(recipeId)) {
                    stack.shrink(1);
                    serverPlayer.sendSystemMessage(Component.literal("Recipe knowledge recovered: " + recipeId).withStyle(ChatFormatting.GOLD));
                } else {
                    serverPlayer.sendSystemMessage(Component.literal("You already know " + recipeId + ". The scroll becomes Echo Dust.").withStyle(ChatFormatting.GRAY));
                    consumeOneAndGiveDust(serverPlayer, stack, 2);
                }
            }, () -> serverPlayer.sendSystemMessage(Component.literal("Progression data is not attached.").withStyle(ChatFormatting.RED)));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ResourceLocation recipeId = getRecipeId(stack);
        CompoundTag tag = stack.getTag();

        if (recipeId == null) {
            tooltip.add(Component.literal("Blank scroll").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Right-click to convert into Echo Dust.").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }

        tooltip.add(Component.literal("Recipe: " + recipeId).withStyle(ChatFormatting.GOLD));
        if (tag != null && tag.contains(SOURCE_THEME_TAG)) {
            tooltip.add(Component.literal("Source: " + tag.getString(SOURCE_THEME_TAG)).withStyle(ChatFormatting.GRAY));
        }
        if (tag != null && tag.contains(TIER_TAG)) {
            tooltip.add(Component.literal("Tier: " + Math.max(1, tag.getInt(TIER_TAG))).withStyle(ChatFormatting.GRAY));
        }
    }

    private static void consumeOneAndGiveDust(ServerPlayer player, ItemStack scrollStack, int dustAmount) {
        scrollStack.shrink(1);
        ItemStack dust = new ItemStack(FRItems.ECHO_DUST.get(), Math.max(1, dustAmount));
        if (!player.getInventory().add(dust)) {
            player.drop(dust, false);
        }
    }
}
