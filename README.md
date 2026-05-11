# Fractured Reality Core

Forge 1.20.1 progression and integration core for the Fractured Reality / Discords project.

## Role

`InfiniDungeons` is the Discord dungeon engine.

`FracturedRealityCore` is the progression brain:

- Echo Shards
- Recipe Scrolls
- Discord Core Fragments
- Fractured Codex
- player progression data
- Discord completion rewards
- optional integrations with Iron's Spells, Passive Skill Tree, Epic Fight, Apotheosis, Scorched Guns, L2Artifacts, and BattleRoyale

## Current MVP

This initial skeleton adds:

- Forge 1.20.1 Gradle setup
- core mod entrypoint
- item registry
- `echo_shard`
- `echo_dust`
- `sealed_recipe_scroll`
- `discord_core_fragment`
- `fractured_codex`
- optional Discord completion listener skeleton
- reward manager skeleton

## Architecture

The first integration listener intentionally avoids hard compile dependency on InfiniDungeons. It listens to Forge events generically and checks for the InfiniDungeons event class name at runtime.

Later, once the dependency jar is available in the development environment, this can be replaced with a typed listener:

```java
@SubscribeEvent
public static void onDiscordCompleted(DiscordCompletedEvent event) {
    // typed integration
}
```

## Next steps

1. Build/publish InfiniDungeons with the Discord API events.
2. Add it as a dev dependency.
3. Replace the reflection listener with typed imports.
4. Add player progression capability.
5. Add Codex GUI.
6. Add recipe-scroll unlock logic.
7. Add optional compat modules.
