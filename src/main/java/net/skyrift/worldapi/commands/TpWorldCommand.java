package net.skyrift.worldapi.commands;

import net.skyrift.core.commands.SkyriftCommand;
import net.skyrift.core.constants.Colors;
import net.skyrift.core.files.YmlLoader;
import net.skyrift.worldapi.WorldAPI;
import net.skyrift.worldapi.commands.tabcompleters.WorldsTab;
import net.skyrift.worldapi.worldgenerator.VoidWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class TpWorldCommand extends SkyriftCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length < 1) return true;

        double x = 0.0;
        double y = 0.0;
        double z = 0.0;

        World world = Bukkit.getWorld(args[0]);
        AtomicReference<Location> location = new AtomicReference<>(new Location(world, x, y, z));

        try{
            if (args.length >= 4){
                location.get().setX(Double.parseDouble(args[1]));
                location.get().setY(Double.parseDouble(args[2]));
                location.get().setZ(Double.parseDouble(args[3]));
            }
        }catch (NumberFormatException e){
            player.sendRichMessage("<red>Invalid coordinates");
            return true;
        }


        //Load world if it isn't loaded, then teleport player
        if (world == null){
            if (YmlLoader.getConfig(WorldAPI.getPlugin(), "worlds.yml").getStringList("worlds").contains(args[0])){
                VoidWorldGenerator.generate(args[0], (loadedWorld)->{
                    if (args.length < 4)  location.set(loadedWorld.getSpawnLocation());

                    Location finalLocation = location.get();
                    finalLocation.setWorld(loadedWorld);
                    finalLocation.setX(finalLocation.getBlockX() + 0.5);
                    finalLocation.setZ(finalLocation.getBlockZ() + 0.5);

                    player.teleportAsync(finalLocation);
                    player.sendRichMessage(Colors.info+"Teleporting to world: "+Colors.off_orange+loadedWorld.getName());
                });
            }else{
                player.sendRichMessage("<red>This world does not exist");
            }
            return true;
        }

        //If world was loaded, teleport player
        if (args.length < 4) location.set(world.getSpawnLocation());

        Location finalLocation = location.get();
        finalLocation.setX(finalLocation.getBlockX() + 0.5);
        finalLocation.setZ(finalLocation.getBlockZ() + 0.5);
        player.teleportAsync(finalLocation);
        player.sendRichMessage(Colors.info+"Teleporting to world: "+Colors.off_orange+world.getName());
        return true;
    }

    @Override
    public String getName() {
        return "tpworld";
    }

    @Override
    public TabCompleter getTabCompleter() {
        return new WorldsTab();
    }
}
