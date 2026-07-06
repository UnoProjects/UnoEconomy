package me.unoprojects.economy.api.database.tables;

import me.unoprojects.unocore.api.database.ConnectionProvider;
import me.unoprojects.unocore.api.database.DatabaseTable;
import me.unoprojects.economy.api.UnoEconomy;
import me.unoprojects.economy.api.economy.Currency;
import org.intellij.lang.annotations.Language;

import java.util.concurrent.CompletableFuture;

public abstract class EconomyTable extends DatabaseTable<UnoEconomy> {

    protected EconomyTable(ConnectionProvider provider, UnoEconomy plugin, @Language("SQL") String... tableQueries) {
        super(provider, plugin, tableQueries);
    }

    public abstract CompletableFuture<Double> getBalance(int playerId, Currency currency);

    public abstract CompletableFuture<Void> setBalance(int playerId, Currency currency, double balance);
}
