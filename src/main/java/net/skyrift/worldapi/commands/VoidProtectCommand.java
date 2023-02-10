package net.skyrift.worldapi.commands;

import net.skyrift.core.commands.SkyriftCommand;
import net.skyrift.core.constants.Colors;
import net.skyrift.worldapi.commands.tabcompleters.VoidProtectTab;
import net.skyrift.worldapi.protection.VoidProtect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VoidProtectCommand extends SkyriftCommand {
    @Override
    public String getName() {
        return "voidprotect";
    }

    @Override
    public TabCompleter getTabCompleter() {
        return new VoidProtectTab();
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
            VoidProtect.removeVoidProtection(player.getWorld());
            player.sendRichMessage(Colors.info+"Void protection removed!");
            return true;
        }

        if (args.length < 2) {
            player.sendRichMessage("<red>Usage: " + command.getUsage());
            return true;
        }

        int lowestY;

        try {
            lowestY = Integer.parseInt(args[1]);
        }catch (NumberFormatException e) {
            player.sendRichMessage("<red>Y value must be a number.");
            return true;
        }

        if (set) VoidProtect.addVoidProtection(player.getWorld(), lowestY);

        player.sendRichMessage(Colors.info+"Void protection set to "+Colors.off_orange+lowestY+Colors.info+"!");
        return true;
    }
}
