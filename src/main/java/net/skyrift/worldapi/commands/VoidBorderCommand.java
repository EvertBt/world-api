package net.skyrift.worldapi.commands;

import net.skyrift.core.commands.SkyriftCommand;
import net.skyrift.core.constants.Colors;
import net.skyrift.worldapi.border.VoidBorder;
import net.skyrift.worldapi.commands.tabcompleters.VoidBorderTab;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VoidBorderCommand extends SkyriftCommand {
    @Override
    public String getName() {
        return "voidborder";
    }

    @Override
    public TabCompleter getTabCompleter() {
        return new VoidBorderTab();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length < 1) {
            player.sendRichMessage("<red>Usage: " + command.getUsage());
            return true;
        }

        boolean set = args[0].equalsIgnoreCase("set");

        if (!set && !args[0].equalsIgnoreCase("remove")) {
            player.sendRichMessage("<red>Usage: " + command.getUsage());
            return true;
        }

        if (args.length < 2 && !set){
            VoidBorder.removeBorder(player.getWorld());
            player.sendRichMessage(Colors.info+"Void border removed!");
            return true;
        }

        if (args.length < 2) {
            player.sendRichMessage("<red>Usage: " + command.getUsage());
            return true;
        }

        int radius;

        try {
            radius = Integer.parseInt(args[1]);
            if (radius < 0) {
                player.sendRichMessage("<red>Radius must be positive.");
                return true;
            }
        }catch (NumberFormatException e) {
            player.sendRichMessage("<red>Radius must be a number.");
            return true;
        }

        if (set) VoidBorder.addBorder(player.getWorld(), radius);

        player.sendRichMessage(Colors.info+"Void border set to "+Colors.off_orange+radius+Colors.info+"!");
        return true;
    }
}
