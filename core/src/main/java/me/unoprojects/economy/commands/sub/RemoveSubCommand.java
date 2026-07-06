package me.unoprojects.economy.commands.sub;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.commands.SubCommand;
import me.unoprojects.unocore.api.data.UnoPlayer;
import me.unoprojects.economy.api.UnoEconomy;
import me.unoprojects.economy.api.economy.Currency;
import me.unoprojects.economy.api.economy.EconomyData;
import me.unoprojects.economy.api.permissions.Permission;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.stream.Stream;

import static me.unoprojects.unocore.api.utils.ColorUtils.parse;

public class RemoveSubCommand extends SubCommand<UnoEconomy> {

    private static final String PREFIX = " <gradient:#FA982A:#FFC57A><b>UnoEconomy</b></gradient> <dark_gray>» <gray>";

    public RemoveSubCommand(UnoEconomy plugin) {
        super(plugin, "remove");
    }

    @Override
    protected void setup() {
        withPermission(Permission.COMMAND_REMOVE.getPermission());
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

            double newBalance = Math.max(0.0, econData.getBalance(currency) - amount);
            econData.setBalance(currency, newBalance);
            plugin.getEconomyTable().setBalance(unoPlayer.getId(), currency, newBalance);

            sender.sendMessage(parse(PREFIX + "Rimossi <red>" + currency.format(amount) + "</red> a <yellow>" + targetPlayer.getName() + "</yellow> in <white>" + currency.getDisplayName() + "</white>."));
        });
    }
}
