package me.unoprojects.unoeconomy.commands.sub;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.DoubleArgument;
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

public class SetSubCommand extends SubCommand<UnoEconomy> {

    private static final String PREFIX = " <gradient:#FA982A:#FFC57A><b>UnoEconomy</b></gradient> <dark_gray>» <gray>";

    public SetSubCommand(UnoEconomy plugin) {
        super(plugin, "set");
    }

    @Override
    protected void setup() {
        withPermission(Permission.COMMAND_SET.getPermission());
        withArguments(
                new EntitySelectorArgument.OnePlayer("target"),
                new DoubleArgument("amount", 0.0),
                new StringArgument("currency")
                        .replaceSuggestions(ArgumentSuggestions.strings(info ->
                                Stream.of(Currency.values()).map(Currency::name).toArray(String[]::new)
                        ))
        );
        executes((sender, args) -> {
            Player targetPlayer = (Player) args.get("target");
            double amount = (double) args.get("amount");
            String currencyName = (String) args.get("currency");
            if (targetPlayer == null || currencyName == null) return;

            Currency currency;
            try {
                currency = Currency.valueOf(currencyName.toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(parse(PREFIX + "<red>Valuta '" + currencyName + "' non valida."));
                return;
            }

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

            econData.setBalance(currency, amount);
            plugin.getEconomyTable().setBalance(unoPlayer.getId(), currency, amount);

            sender.sendMessage(parse(PREFIX + "Impostato il bilancio di <yellow>" + targetPlayer.getName() + "</yellow> per <white>" + currency.getDisplayName() + "</white> a <green>" + currency.format(amount) + "</green>."));
        });
    }
}
