package com.github.kaspiandev.fishybusiness.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "users")
public class User {

    @DatabaseField(id = true, canBeNull = false)
    private UUID uuid;

    @DatabaseField(canBeNull = false)
    private int points;

    public User() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
}
