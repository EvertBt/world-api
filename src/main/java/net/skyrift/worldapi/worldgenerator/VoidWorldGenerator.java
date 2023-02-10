package net.skyrift.worldapi.worldgenerator;

import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Consumer;

//This is a simple utility class that will generate a void world for all the maps to be stored in
public class VoidWorldGenerator {

    public static void generate(String name, Consumer<World> world){

        WorldCreator creator = new WorldCreator(name);
        creator.keepSpawnLoaded(TriState.FALSE);

        creator.generator(new ChunkGenerator() {
            @Override
            public @NotNull ChunkData createVanillaChunkData(@NotNull World world, int x, int z) {
                return Bukkit.createChunkData(world);
            }

            @Override
            public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
                return new Location(world, 0, 0, 0);
            }

            @Override
            public boolean shouldGenerateStructures() {
                return false;
            }

            @Override
            public boolean shouldGenerateMobs() {
                return false;
            }
        });

        //Creates / loads the world
        World createdWorld = creator.createWorld();
        world.accept(createdWorld);
    }
}
