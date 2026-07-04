package me.unoprojects.unoeconomy.commands.sub;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.commands.SubCommand;
import me.unoprojects.unocore.api.data.UnoPlayer;
import me.unoprojects.unoeconomy.api.UnoEconomy;
import me.unoprojects.unoeconomy.api.economy.Currency;
import me.unoprojects.unoeconomy.api.economy.EconomyData;
import me.unoprojects.unoeconomy.api.permissions.Permission;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.stream.Stream;

import static me.unoprojects.unocore.api.utils.ColorUtils.parse;
import static net.kyori.adventure.text.Component.empty;

public class ViewSubCommand extends SubCommand<UnoEconomy> {

    private static final String PREFIX = " <gradient:#FA982A:#FFC57A><b>UnoEconomy</b></gradient> <dark_gray>» <gray>";

    public ViewSubCommand(UnoEconomy plugin) {
        super(plugin, "view");
    }

    @Override
    protected void setup() {
        withPermission(Permission.COMMAND_VIEW.getPermission());
        withArguments(
                new EntitySelectorArgument.OnePlayer("target"),
                new StringArgument("currency")
                        .replaceSuggestions(ArgumentSuggestions.strings(info ->
                                Stream.of(Currency.values()).map(Currency::name).toArray(String[]::new)
                        ))
                        .setOptional(true)
        );
        executes((sender, args) -> {
            Player targetPlayer = (Player) args.get("target");
            if (targetPlayer == null) return;

            Optional<UnoPlayer> targetUnoOpt = UnoCore.getInstance().getPlayerDataManager().getPlayer(targetPlayer);
            if (targetUnoOpt.isEmpty()) {
                sender.sendMessage(parse(PREFIX + "<red>Dati giocatore non caricati in memoria."));
                return;
            }

            UnoPlayer unoPlayer = targetUnoOpt.get();
            EconomyData econData = unoPlayer.getComponent(EconomyData.class);
            if (econData == null) {
                sender.sendMessage(parse(PREFIX + "<red>Dati economici non trovati per questo giocatore."));
                return;
            }

            String currencyName = (String) args.get("currency");
            if (currencyName == null) {
                sender.sendMessage(empty());
                sender.sendMessage(parse(" <gradient:#FA982A:#FFC57A:#FA982A><b>ᴜɴᴏ ᴇᴄᴏɴᴏᴍʏ</b></gradient> <dark_gray>| <gray>Bilancio di <yellow>" + targetPlayer.getName()));
                sender.sendMessage(empty());
                for (Currency currency : Currency.values()) {
                    double balance = econData.getBalance(currency);
                    sender.sendMessage(parse("  <dark_gray>» <gray>" + currency.getDisplayName() + ": <green>" + currency.format(balance) + "</green>"));
                }
                sender.sendMessage(empty());
            } else {
                try {
                    Currency currency = Currency.valueOf(currencyName.toUpperCase());
                    double balance = econData.getBalance(currency);
                    sender.sendMessage(parse(PREFIX + "Bilancio di <yellow>" + targetPlayer.getName() + "</yellow> (" + currency.getDisplayName() + "): <green>" + currency.format(balance) + "</green>"));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(parse(PREFIX + "<red>Valuta '" + currencyName + "' non valida."));
                }
            }
        });
    }
}
