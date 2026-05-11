package com.gabalus.fracturedreality.compat;

import com.gabalus.fracturedreality.FracturedReality;
import com.gabalus.fracturedreality.compat.apotheosis.ApotheosisCompat;
import com.gabalus.fracturedreality.compat.epicfight.EpicFightCompat;
import com.gabalus.fracturedreality.compat.irons_spellbooks.IronsSpellbooksCompat;
import com.gabalus.fracturedreality.compat.l2artifacts.L2ArtifactsCompat;
import com.gabalus.fracturedreality.compat.passive_skill_tree.PassiveSkillTreeCompat;
import com.gabalus.fracturedreality.compat.scorched_guns.ScorchedGunsCompat;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public final class CompatManager {
    private static final List<CompatModule> ACTIVE_MODULES = new ArrayList<>();
    private static boolean initialized;

    private CompatManager() {}

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        tryRegister(new IronsSpellbooksCompat());
        tryRegister(new ScorchedGunsCompat());
        tryRegister(new PassiveSkillTreeCompat());
        tryRegister(new ApotheosisCompat());
        tryRegister(new L2ArtifactsCompat());
        tryRegister(new EpicFightCompat());

        FracturedReality.LOGGER.info("Fractured Reality active compat modules: {}", ACTIVE_MODULES.size());
    }

    public static void onDiscordCompleted(RewardContext context) {
        for (CompatModule module : ACTIVE_MODULES) {
            try {
                module.onDiscordCompleted(context);
            } catch (RuntimeException exception) {
                FracturedReality.LOGGER.error("Fractured Reality compat module {} failed during Discord completion", module.modId(), exception);
            }
        }
    }

    private static void tryRegister(CompatModule module) {
        if (ModList.get().isLoaded(module.modId())) {
            ACTIVE_MODULES.add(module);
            FracturedReality.LOGGER.info("Enabled Fractured Reality compat module for {}", module.modId());
        }
    }
}
