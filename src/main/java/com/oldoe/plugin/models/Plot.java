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
}
