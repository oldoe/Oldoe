package com.oldoe.plugin.services;

import com.oldoe.plugin.Oldoe;
import org.bukkit.plugin.java.JavaPlugin;

public class ServiceManager {

    private static PlayerService playerService = new PlayerService();
    private static CommandService commandService = new CommandService();
    private static EventService eventService = new EventService();
    private static DataService dataService = new DataService();

    public static PlayerService PlayerService() {
        return playerService;
    }

    public static CommandService CommandService() {
        return commandService;
    }
    public static EventService EventsService() {
        return eventService;
    }

    public static DataService DataService() {
        return dataService;
    }

    public void Register(JavaPlugin instance) {
        commandService.registerCommands(instance);
        eventService.registerEvents(instance);
        dataService.registerService(Oldoe.getInstance());
    }

    public void UnRegister() {
        dataService.DisableService();
    }
}
