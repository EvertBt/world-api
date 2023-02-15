package net.skyrift.worldapi.protection;

import net.skyrift.worldapi.WorldAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class VoidProtect implements Listener {
    private static final Map<String, Integer> lowestYMap = new HashMap<>();
    private static final Map<String, World> protectedWorldMap = new HashMap<>();

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e){

        World world = e.getWorld();

        //Check if world needs a border
        if (world.getPersistentDataContainer().has(new NamespacedKey(WorldAPI.getPlugin(), "void-protection"), PersistentDataType.INTEGER)){
            protectedWorldMap.put(world.getName(), world);
            lowestYMap.put(world.getName(), world.getPersistentDataContainer().getOrDefault(new NamespacedKey(WorldAPI.getPlugin(), "void-protection"), PersistentDataType.INTEGER, -64));
            return;
        }

        //Check if world is in config, this only applies to worlds in the config that weren't loaded initially.
        if (!protectedWorldMap.containsKey(e.getWorld().getName())) return;
        if (protectedWorldMap.get(e.getWorld().getName()) != null) return;

        protectedWorldMap.put(e.getWorld().getName(), e.getWorld());
    }

    public VoidProtect(){
        //Setup protected worlds from config
        for (String worldWithHeightCombination : WorldAPI.getPlugin().getConfig().getStringList("void-protection")){

            //Get world name
            String worldName = worldWithHeightCombination.split("/")[0];

            //Get lowest Y
            int lowestY = Integer.parseInt(worldWithHeightCombination.split("/")[1]);

            //Add to maps
            World world = Bukkit.getWorld(worldName);
            protectedWorldMap.put(worldName, world);
            lowestYMap.put(worldName, lowestY);

            if (world == null) continue;
            world.getPersistentDataContainer().set(new NamespacedKey(WorldAPI.getPlugin(), "void-protection"), PersistentDataType.INTEGER, lowestY);
        }

        //Start task
        start();
    }

    public void start(){
        //Prevent players from leaving the spawn radius
        Bukkit.getScheduler().runTaskTimer(WorldAPI.getPlugin(), () -> {

            //Loop through all worlds
            for (Map.Entry<String, Integer> entry : Map.copyOf(lowestYMap).entrySet()){

                World world = protectedWorldMap.get(entry.getKey());
                Integer lowestY = entry.getValue();
                if (world == null) continue;

                //Loop through all players in the world
                world.getPlayers().forEach(player -> {
                    if (!player.isOnline()) return;

                    //Teleport player to spawn if they are below the lowest Y
                    if (player.getLocation().getY() > lowestY) return;

                    Location spawn = world.getSpawnLocation();
                    spawn.setX(spawn.getX() + 0.5);
                    spawn.setZ(spawn.getZ() + 0.5);

                    player.teleport(spawn);
                });
            }
        }, 0, 5);
    }

    public static void addVoidProtection(World world, int lowestY){
        protectedWorldMap.put(world.getName(), world);
        lowestYMap.put(world.getName(), lowestY);

        world.getPersistentDataContainer().set(new NamespacedKey(WorldAPI.getPlugin(), "void-protection"), PersistentDataType.INTEGER, lowestY);
    }

    public static void removeVoidProtection(World world){
        protectedWorldMap.remove(world.getName());
        lowestYMap.remove(world.getName());

        world.getPersistentDataContainer().remove(new NamespacedKey(WorldAPI.getPlugin(), "void-protection"));
    }
}
