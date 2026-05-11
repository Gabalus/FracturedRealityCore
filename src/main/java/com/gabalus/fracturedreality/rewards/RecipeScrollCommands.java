package com.gabalus.fracturedreality.rewards;

import com.gabalus.fracturedreality.FracturedReality;
import com.gabalus.fracturedreality.progression.PlayerProgressionProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FracturedReality.MODID)
public final class RecipeScrollCommands {
    private RecipeScrollCommands() {}

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fractured")
            .then(Commands.literal("recipe")
                .then(Commands.literal("give_scroll")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("recipe", StringArgumentType.string())
                        .executes(ctx -> giveScroll(
                            ctx.getSource().getPlayerOrException(),
                            StringArgumentType.getString(ctx, "recipe"),
                            "debug",
                            1))
                        .then(Commands.argument("theme", StringArgumentType.string())
                            .executes(ctx -> giveScroll(
                                ctx.getSource().getPlayerOrException(),
                                StringArgumentType.getString(ctx, "recipe"),
                                StringArgumentType.getString(ctx, "theme"),
                                1))
                            .then(Commands.argument("tier", IntegerArgumentType.integer(1))
                                .executes(ctx -> giveScroll(
                                    ctx.getSource().getPlayerOrException(),
                                    StringArgumentType.getString(ctx, "recipe"),
                                    StringArgumentType.getString(ctx, "theme"),
                                    IntegerArgumentType.getInteger(ctx, "tier")))))))
                .then(Commands.literal("knows")
                    .then(Commands.argument("recipe", StringArgumentType.string())
                        .executes(ctx -> knowsRecipe(
                            ctx.getSource().getPlayerOrException(),
                            StringArgumentType.getString(ctx, "recipe")))))));
    }

    private static int giveScroll(ServerPlayer player, String recipeText, String theme, int tier) {
        ResourceLocation recipeId = ResourceLocation.tryParse(recipeText);
        if (recipeId == null) {
            player.sendSystemMessage(Component.literal("Invalid recipe id: " + recipeText).withStyle(ChatFormatting.RED));
            return 0;
        }

        ItemStack scroll = RecipeScrollItem.create(recipeId, theme, tier);
        if (!player.getInventory().add(scroll)) {
            player.drop(scroll, false);
        }

        player.sendSystemMessage(Component.literal("Created recipe scroll for " + recipeId + ".").withStyle(ChatFormatting.GOLD));
        return 1;
    }

    private static int knowsRecipe(ServerPlayer player, String recipeText) {
        ResourceLocation recipeId = ResourceLocation.tryParse(recipeText);
        if (recipeId == null) {
            player.sendSystemMessage(Component.literal("Invalid recipe id: " + recipeText).withStyle(ChatFormatting.RED));
            return 0;
        }

        player.getCapability(PlayerProgressionProvider.PLAYER_PROGRESSION).ifPresent(progression ->
            player.sendSystemMessage(Component.literal("Known recipe " + recipeId + ": " + progression.knowsRecipe(recipeId))
                .withStyle(progression.knowsRecipe(recipeId) ? ChatFormatting.GREEN : ChatFormatting.YELLOW)));

        return 1;
    }
}
