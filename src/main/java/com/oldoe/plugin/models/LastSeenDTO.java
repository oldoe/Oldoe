package com.oldoe.plugin.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class LastSeenDTO {

    private String _name;

    private Timestamp _lastSeen;

    public LastSeenDTO(String name, Timestamp lastSeen) {
        _name = name;
        _lastSeen = lastSeen;
    }

    public String GetName() { return _name; }

    public Timestamp GetLastSeen() { return _lastSeen; }

    public String GetFormattedDateLastSeen() {
        if (_lastSeen != null) {
            String formattedString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(_lastSeen);
            return formattedString;
        } else {
            return "";
        }
    }
}
