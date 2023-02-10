package net.skyrift.worldapi.commands;

import net.skyrift.core.commands.SkyriftCommand;
import net.skyrift.core.commands.tabcompleters.EmptyTab;
import net.skyrift.core.constants.Colors;
import net.skyrift.core.files.YmlLoader;
import net.skyrift.worldapi.WorldAPI;
import net.skyrift.worldapi.worldgenerator.VoidWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class CreateVoidWorldCommand extends SkyriftCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1){
            sender.sendRichMessage("<red>Please provide a name");
            return true;
        }

        String name = args[0];

        if (Bukkit.getWorld(name) != null){
            sender.sendRichMessage("<red>This world already exists!");
            return true;
        }

        VoidWorldGenerator.generate(name, (world)->{
            sender.sendRichMessage(Colors.info+"new world: "+Colors.off_orange+name+Colors.info+" has been created!");
        });

        //Save new world name to list
        FileConfiguration worldsConfig = YmlLoader.getConfig(WorldAPI.getPlugin(), "worlds.yml");
        List<String> worlds =worldsConfig.getStringList("worlds");
        worlds.add(name);
        worldsConfig.set("worlds", worlds);
        try {
            worldsConfig.save("worlds.yml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public String getName() {
        return "createvoidworld";
    }

    @Override
    public TabCompleter getTabCompleter() {
        return new EmptyTab();
    }
}
