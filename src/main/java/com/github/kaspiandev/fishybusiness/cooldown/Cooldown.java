package com.github.kaspiandev.fishybusiness.cooldown;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Cooldown<T> {

    private final Map<T, Instant> cooldown;

    public Cooldown() {
        this.cooldown = new HashMap<>();
    }

    public boolean isOnCooldown(T entity) {
        if (!cooldown.containsKey(entity)) return false;

        Instant now = Instant.now();
        return (now.isAfter(cooldown.get(entity)));
    }

    public void putOnCooldown(T entity, long value, ChronoUnit unit) {
        cooldown.put(entity, Instant.now().plus(value, unit));
    }

}
