package me.unoprojects.unoeconomy.api.database.tables;

import me.unoprojects.unocore.api.database.ConnectionProvider;
import me.unoprojects.unocore.api.database.DatabaseTable;
import me.unoprojects.unoeconomy.api.UnoEconomy;
import me.unoprojects.unoeconomy.api.economy.Currency;
import org.intellij.lang.annotations.Language;

import java.util.concurrent.CompletableFuture;

public abstract class EconomyTable extends DatabaseTable<UnoEconomy> {

    protected EconomyTable(ConnectionProvider provider, UnoEconomy plugin, @Language("SQL") String... tableQueries) {
        super(provider, plugin, tableQueries);
    }

    public abstract CompletableFuture<Double> getBalance(int playerId, Currency currency);

    public abstract CompletableFuture<Void> setBalance(int playerId, Currency currency, double balance);
}
