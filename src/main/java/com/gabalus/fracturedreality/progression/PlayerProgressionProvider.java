package com.gabalus.fracturedreality.progression;

import com.gabalus.fracturedreality.FracturedReality;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerProgressionProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PlayerProgression> PLAYER_PROGRESSION =
        CapabilityManager.get(new CapabilityToken<>() {});

    public static final ResourceLocation ID = new ResourceLocation(FracturedReality.MODID, "player_progression");

    private final PlayerProgression progression = new PlayerProgression();
    private final LazyOptional<PlayerProgression> optional = LazyOptional.of(() -> progression);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == PLAYER_PROGRESSION) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return progression.save();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        progression.load(nbt);
    }
}
