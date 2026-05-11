# Fractured Reality Core

Forge 1.20.1 integration/progression glue for the Fractured Reality / Discords project.

## Correct role

`InfiniDungeons` is the Discord dungeon engine.

`Passive Skill Tree` is the enormous passive tree, class, and ascendancy foundation.

`Iron's Spells`, `Scorched Guns`, `Epic Fight`, `Apotheosis`, and `L2Artifacts` are the main mechanical pillars.

`FracturedRealityCore` should glue those systems together. It should not replace them.

## Responsibilities

FracturedRealityCore owns:

- Echo Shards
- Recipe Scrolls
- Discord Core Fragments
- Fractured Codex
- player Discord/progression milestones
- Discord completion rewards
- recipe knowledge gates
- optional integration hooks
- reward routing into the selected content mods

FracturedRealityCore should not own:

- a duplicate giant passive tree
- a duplicate ascendancy system
- replacement magic systems
- replacement gun systems
- replacement combat systems

## Selected mod roles

- `InfiniDungeons`: dynamic protected Discord dimensions and rift entry/return
- `Passive Skill Tree`: enormous ARPG passive tree and ascendancy system
- `Iron's Spells`: magic progression, spell rewards, arcane Discords
- `Scorched Guns`: firearm progression, arsenal Discords, blueprint rewards
- `Epic Fight`: combat identity, boss arenas, melee mastery
- `Apotheosis`: affix/loot depth and item scaling
- `L2Artifacts`: artifact rewards and rare build-defining items

## Current MVP

This skeleton includes:

- Forge 1.20.1 Gradle setup
- core mod entrypoint
- item registry
- `echo_shard`
- `echo_dust`
- `sealed_recipe_scroll`
- `discord_core_fragment`
- `fractured_codex`
- Discord completion listener bridge
- Discord reward manager
- player Discord/progression milestone capability
- recipe-scroll knowledge system
- recipe gating
- optional compat modules

## Important correction

The earlier standalone Fractured passive tree work is deprecated. The project should move ARPG passive/ascendancy expansion into `Gabalus/Passive-Skill-Tree` on the `1.20.1` branch, then use FracturedRealityCore only to award points, unlock classes/ascendancies, and connect Discord progression to that mod.

## Next steps

1. Inspect `Gabalus/Passive-Skill-Tree` 1.20.1 internals.
2. Add or expose a small API for awarding skill/passive points from FracturedRealityCore.
3. Add Fractured Reality passive tree data/classes/ascendancies to Passive Skill Tree, not this core mod.
4. Keep FracturedRealityCore focused on Discord rewards, recipe scrolls, Codex, and cross-mod reward routing.
