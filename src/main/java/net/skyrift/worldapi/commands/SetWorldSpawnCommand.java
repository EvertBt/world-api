package net.skyrift.worldapi.commands;

import net.skyrift.core.commands.SkyriftCommand;
import net.skyrift.core.commands.tabcompleters.EmptyTab;
import net.skyrift.core.constants.Colors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetWorldSpawnCommand extends SkyriftCommand {
    @Override
    public String getName() {
        return "setworldspawn";
    }

    @Override
    public TabCompleter getTabCompleter() {
        return new EmptyTab();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        player.getWorld().setSpawnLocation(player.getLocation());
        player.sendRichMessage(Colors.info+"World spawn set for world: "+Colors.off_orange+player.getWorld().getName()+Colors.info+"!");

        return true;
    }
}
