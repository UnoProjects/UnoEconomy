package me.unoprojects.economy.commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.unoprojects.economy.api.UnoEconomy;
import me.unoprojects.unocore.api.commands.CommandSupplier;
import me.unoprojects.economy.commands.sub.AddSubCommand;
import me.unoprojects.economy.commands.sub.RemoveSubCommand;
import me.unoprojects.economy.commands.sub.SetSubCommand;
import me.unoprojects.economy.commands.sub.ViewSubCommand;

import me.unoprojects.economy.api.permissions.Permission;

import static me.unoprojects.unocore.api.utils.ColorUtils.parse;
import static net.kyori.adventure.text.Component.empty;

public class UnoEconomyCommand extends CommandSupplier {

    private final UnoEconomy plugin;

    public UnoEconomyCommand(UnoEconomy plugin) {
        super("unoeconomy");
        this.plugin = plugin;
    }

    @Override
    public CommandAPICommand[] get() {
        return new CommandAPICommand[]{
                create(name)
                        .withPermission(Permission.COMMAND.getPermission())
                        .withAliases("unoecon", "econ")
                        .withSubcommands(
                                new ViewSubCommand(plugin),
                                new SetSubCommand(plugin),
                                new AddSubCommand(plugin),
                                new RemoveSubCommand(plugin)
                        )
                        .executes((sender, args) -> {
                            sender.sendMessage(empty());
                            sender.sendMessage(parse("  <gradient:#FA982A:#FFC57A:#FA982A><b>ᴜɴᴏ ᴇᴄᴏɴᴏᴍʏ</b></gradient> <dark_gray>- <gray>ɢᴜɪᴅᴀ ᴄᴏᴍᴀɴᴅɪ"));
                            sender.sendMessage(empty());
                            sender.sendMessage(parse("  <dark_gray>» <white>/unoeconomy view <target> [currency] <dark_gray>- <gray>Visualizza i bilanci"));
                            sender.sendMessage(parse("  <dark_gray>» <white>/unoeconomy set <target> <quantità> <currency> <dark_gray>- <gray>Imposta un bilancio"));
                            sender.sendMessage(parse("  <dark_gray>» <white>/unoeconomy add <target> <quantità> <currency> <dark_gray>- <gray>Aggiunge al bilancio"));
                            sender.sendMessage(parse("  <dark_gray>» <white>/unoeconomy remove <target> <quantità> <currency> <dark_gray>- <gray>Rimuove dal bilancio"));
                            sender.sendMessage(empty());
                        })
        };
    }
}
