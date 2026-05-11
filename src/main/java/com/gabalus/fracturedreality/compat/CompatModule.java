package com.gabalus.fracturedreality.compat;

import com.gabalus.fracturedreality.rewards.RewardContext;

public interface CompatModule {
    String modId();

    default void onDiscordCompleted(RewardContext context) {}
}
