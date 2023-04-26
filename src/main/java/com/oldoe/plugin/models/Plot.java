package com.oldoe.plugin.models;

import java.util.ArrayList;
import java.util.List;

public class Plot {
    public String name;
    public int dbID = -1;
    public int OwnerID = -1;
    public int X = -1;
    public int Z = -1;
    public List<Integer> dbMembers = new ArrayList<>();

    public boolean isPublic() {
        if (dbID == -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasPerms(int userID) {
        if (dbMembers.contains(userID) || dbID == -1 || OwnerID == userID) {
            return  true;
        }
        else {
            return false;
        }
    }
}
