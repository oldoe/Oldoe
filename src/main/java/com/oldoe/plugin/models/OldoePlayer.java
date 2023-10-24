package com.oldoe.plugin.models;

import org.bukkit.Location;

import java.time.Instant;

public class OldoePlayer {

    private int dbID;
    private int permission = 0;
    private boolean isStaff = false;
    private boolean isHidden = false;
    private boolean pvpEnabled = false;
    private boolean lockMode = false;
    private String uuid;
    private String name;
    private String lastDmName;
    private boolean borderEnabled = false;
    private boolean staffEnabled = false;
    private boolean tagEnabled = true;
    private Plot currentPlot;

    private Instant lastMovement;
    private Instant lastPlotMessage;
    private Instant loginTime;

    private Location staffStartLocation;


    public OldoePlayer(int id, String uuid, String displayName) {
        this.dbID = id;
        this.uuid = uuid;
        this.name = displayName;
        this.lastMovement = Instant.now();
        this.lastPlotMessage = Instant.now();
        this.loginTime = Instant.now();
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

    public Instant getLastPlotMessageTime() {
        return lastPlotMessage;
    }

    public Instant getLoginTime() {
        return loginTime;
    }

    public void setLastMovementNow() {
        this.lastMovement = Instant.now();
    }

    public void setLastPlotMessageNow() {
        this.lastPlotMessage = Instant.now();
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

    public void setLastStaffLocation(Location loc) {
        this.staffStartLocation = loc;
    }

    public void setStaff() {
        this.isStaff = true;
    }

    public void setStaffEnabled(boolean enabled) { this.staffEnabled = enabled; }

    public void setHidden(boolean hide) { this.isHidden = hide; }

    public void setShowTag(boolean enabled) { this.tagEnabled = enabled; }

    public boolean isStaff() { return this.isStaff; }

    public boolean isHidden() { return this.isHidden; }

    public boolean isTagEnabled() { return this.tagEnabled; }

    public boolean isPvpEnabled() { return this.pvpEnabled; }

    public void setPVP(Boolean isEnabled) { this.pvpEnabled = isEnabled; }

    public boolean isLockMode() { return this.lockMode; }

    public void setLockMode(Boolean isEnabled) { this.lockMode = isEnabled; }

    public String getUUID() {
        return uuid;
    }

    public int getID() {
        return dbID;
    }

    public Location getStaffStartLocation() {
        return staffStartLocation;
    }

    public boolean borderEnabled() { return borderEnabled; }

    public boolean isStaffEnabled() { return staffEnabled; }

    public void toggleBorder() {
        this.borderEnabled = !borderEnabled;
    }

    public Plot getCurrentPlot() { return currentPlot; }

    public void setCurrentPlot(Plot plot) { this.currentPlot = plot; }
}
