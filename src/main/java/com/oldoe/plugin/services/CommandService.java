package com.oldoe.plugin.services;

import com.oldoe.plugin.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandService {

    public void registerCommands(JavaPlugin instance) {
        instance.getCommand("spawn").setExecutor(new SpawnCommand());
        instance.getCommand("sethome").setExecutor(new SetHomeCommand());
        instance.getCommand("home").setExecutor(new HomeCommand());
        instance.getCommand("plot").setExecutor(new PlotCommand());
        instance.getCommand("cash").setExecutor(new CashCommand());
        instance.getCommand("msg").setExecutor(new MsgCommand());
        instance.getCommand("border").setExecutor(new BorderCommand());
        instance.getCommand("help").setExecutor(new HelpCommand());
        instance.getCommand("pvp").setExecutor(new PvpCommand());
        instance.getCommand("staff").setExecutor(new StaffCommand());
        instance.getCommand("s").setExecutor(new StaffChatCommand());

        instance.getCommand("help").setTabCompleter(new HelpCommand());
        instance.getCommand("plot").setTabCompleter(new PlotCommand());
        instance.getCommand("staff").setTabCompleter(new StaffCommand());
    }
}
