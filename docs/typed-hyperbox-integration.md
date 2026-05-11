# Typed InfiniDungeons / Hyperbox Integration

The current integration uses `HyperboxDiscordEventAdapter` and reflection so FracturedRealityCore can compile before the InfiniDungeons jar is available locally.

Once you have a built InfiniDungeons jar containing the Discord API events, use this path.

## 1. Add the jar

Put the jar here:

```text
libs/InfiniDungeons-1.20.1.jar
```

Then uncomment in `build.gradle`:

```gradle
implementation fg.deobf(files('libs/InfiniDungeons-1.20.1.jar'))
```

## 2. Replace reflection listener with typed listener

Current listener:

```java
@SubscribeEvent
public static void onForgeEvent(Event event) {
    if (!HyperboxDiscordEventAdapter.isDiscordCompletedEvent(event)) {
        return;
    }

    HyperboxDiscordEventAdapter.tryReadRewardContext(event)
        .ifPresent(DiscordRewardManager::grantCompletionRewards);
}
```

Typed version:

```java
package com.gabalus.fracturedreality.discord;

import commoble.hyperbox.api.discord.DiscordCompletedEvent;
import commoble.hyperbox.api.discord.DiscordMetadata;
import com.gabalus.fracturedreality.rewards.DiscordRewardManager;
import com.gabalus.fracturedreality.rewards.RewardContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class DiscordIntegrationEvents {
    private DiscordIntegrationEvents() {}

    @SubscribeEvent
    public static void onDiscordCompleted(DiscordCompletedEvent event) {
        DiscordMetadata metadata = event.getMetadata();

        RewardContext context = new RewardContext(
            event.getPlayer(),
            metadata.dimension(),
            metadata.theme(),
            metadata.tier(),
            metadata.source(),
            metadata.createdByPlayer()
        );

        DiscordRewardManager.grantCompletionRewards(context);
    }
}
```

## 3. Later event uses

You can also listen to:

```java
DiscordEnteredEvent
```

Use cases:

- show Discord title/subtitle
- display tier/theme information
- apply temporary modifiers
- start timed objectives
- initialize party state
