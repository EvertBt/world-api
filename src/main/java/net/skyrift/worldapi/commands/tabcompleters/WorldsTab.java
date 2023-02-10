package net.skyrift.worldapi.commands.tabcompleters;

import net.skyrift.core.files.YmlLoader;
import net.skyrift.worldapi.WorldAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldsTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> results = new ArrayList<>();

        if (args.length == 1){
            Set<String> worlds = new HashSet<>();
            Bukkit.getWorlds().forEach(world -> worlds.add(world.getName()));
            worlds.addAll(YmlLoader.getConfig(WorldAPI.getPlugin(), "worlds.yml").getStringList("worlds"));
            results.addAll(worlds);
        }

        switch (args.length) {
            case 2 -> results.add("x");
            case 3 -> results.add("y");
            case 4 -> results.add("z");
        }

        return results;
    }
}
