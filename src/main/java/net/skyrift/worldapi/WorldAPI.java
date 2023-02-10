package net.skyrift.worldapi;

import net.skyrift.core.utils.ReflectUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldAPI extends JavaPlugin {

    private static WorldAPI plugin;

    @Override
    public void onEnable() {
        plugin = this;
        loadConfig();

        setupReflections();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig(){
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    private void setupReflections(){
        ReflectUtils.setupListeners(this);
        ReflectUtils.setupCommands(this);
    }

    public static WorldAPI getPlugin() {
        return plugin;
    }
}
