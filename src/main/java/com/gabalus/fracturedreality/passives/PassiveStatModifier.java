package com.gabalus.fracturedreality.passives;

public record PassiveStatModifier(
    String stat,
    double value,
    String operation
) {}
