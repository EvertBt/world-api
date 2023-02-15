package net.skyrift.worldapi.border;

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

public class VoidBorder implements Listener {

    private static final Map<String, Integer> borderRadiusMap = new HashMap<>();
    private static final Map<String, World> worldMap = new HashMap<>();

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e){

        World world = e.getWorld();

        //Check if world needs a border
        if (world.getPersistentDataContainer().has(new NamespacedKey(WorldAPI.getPlugin(), "void-border"), PersistentDataType.INTEGER)){
            int radius = world.getPersistentDataContainer().getOrDefault(new NamespacedKey(WorldAPI.getPlugin(), "void-border"), PersistentDataType.INTEGER, 1000);
            worldMap.put(world.getName(), world);
            borderRadiusMap.put(world.getName(), radius);
            return;
        }

        //Check if world is in config, this only applies to worlds in the config that weren't loaded initially.
        if (!worldMap.containsKey(e.getWorld().getName())) return;
        if (worldMap.get(e.getWorld().getName()) != null) return;

        worldMap.put(e.getWorld().getName(), e.getWorld());
    }

    public VoidBorder(){
        //Setup worlds from config
        for (String worldRadiusCombo : WorldAPI.getPlugin().getConfig().getStringList("void-borders")){

            //Get world name
            String worldName = worldRadiusCombo.split("/")[0];

            //Get radius
            int radius = Integer.parseInt(worldRadiusCombo.split("/")[1]);

            //Add to maps
            worldMap.put(worldName, Bukkit.getWorld(worldName));
            borderRadiusMap.put(worldName, radius);
        }

        //Start task
        start();
    }

    public void start(){
        //Prevent players from leaving the spawn radius
        Bukkit.getScheduler().runTaskTimer(WorldAPI.getPlugin(), () -> {

            //Loop through all worlds
            for (Map.Entry<String, Integer> entry : Map.copyOf(borderRadiusMap).entrySet()){

                World world = worldMap.get(entry.getKey());
                int spawnRadius = entry.getValue();

                if (world == null) continue;

                //Loop through all players in the world
                world.getPlayers().forEach(player -> {
                    if (!player.isOnline()) return;

                    Location location = player.getLocation();
                    if (location.getX() > spawnRadius || location.getX() < -spawnRadius || location.getZ() > spawnRadius || location.getZ() < -spawnRadius){

                        //Teleport player back to the edge of the border
                        if (location.getX() > spawnRadius) location.setX(spawnRadius - 1);
                        else if (location.getX() < -spawnRadius) location.setX(-spawnRadius + 1);

                        if (location.getZ() > spawnRadius) location.setZ(spawnRadius - 1);
                        else if (location.getZ() < -spawnRadius) location.setZ(-spawnRadius + 1);

                        player.teleport(location);
                    }
                });
            }
        }, 0, 5);
    }

    public static void addBorder(World world, int radius){
        worldMap.put(world.getName(), world);
        borderRadiusMap.put(world.getName(), radius);

        world.getPersistentDataContainer().set(new NamespacedKey(WorldAPI.getPlugin(), "void-border"), PersistentDataType.INTEGER, radius);
    }

    public static void removeBorder(World world){
        worldMap.remove(world.getName());
        borderRadiusMap.remove(world.getName());

        world.getPersistentDataContainer().remove(new NamespacedKey(WorldAPI.getPlugin(), "void-border"));
    }
}
