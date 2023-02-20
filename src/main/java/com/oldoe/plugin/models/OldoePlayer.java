package com.oldoe.plugin.models;

import java.time.Instant;

public class OldoePlayer {

    private int dbID;
    private int permission = 0;
    private String uuid;
    private String name;
    private boolean borderEnabled = false;

    private Instant lastMovement;
    private String lastDmName;

    public OldoePlayer(String uuid, String displayName) {
        this.uuid = uuid;
        this.name = displayName;
        this.lastMovement = Instant.now();
    }

    public void SetPermission(int level) {
        this.permission = level;
    }

    public int GetPermission() {
        return this.permission;
    }

    public Instant getLastMovement() {
        return lastMovement;
    }

    public void setLastMovementNow() {
        this.lastMovement = Instant.now();
    }

    public String getName() {
        return this.name;
    }

    public String getLastMessaged() {
        return lastDmName;
    }

    public void setLastMessaged(String name) {
         this.lastDmName = name;
    }

    public String getUUID() {
        return uuid;
    }

    public int getID() {
        return dbID;
    }

    public boolean borderEnabled() {
        return borderEnabled;
    }

    public void toggleBorder() {
        this.borderEnabled = !borderEnabled;
    }
}
