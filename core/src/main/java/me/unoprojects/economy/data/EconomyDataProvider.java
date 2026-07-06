package me.unoprojects.economy.data;

import me.unoprojects.unocore.api.data.PlayerComponentProvider;
import me.unoprojects.unocore.api.data.UnoPlayer;
import me.unoprojects.economy.api.economy.Currency;
import me.unoprojects.economy.api.economy.EconomyData;
import me.unoprojects.economy.api.database.tables.EconomyTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EconomyDataProvider implements PlayerComponentProvider<EconomyData> {

    private final EconomyTable economyTable;

    public EconomyDataProvider(EconomyTable economyTable) {
        this.economyTable = economyTable;
    }

    @Override
    public Class<EconomyData> getComponentClass() {
        return EconomyData.class;
    }

    @Override
    public EconomyData createDefault(UnoPlayer player) {
        return new EconomyData();
    }

    @Override
    public CompletableFuture<EconomyData> load(UnoPlayer player) {
        EconomyData data = new EconomyData();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Currency currency : Currency.values()) {
            CompletableFuture<Void> future = economyTable.getBalance(player.getId(), currency)
                    .thenAccept(balance -> data.setBalance(currency, balance));
            futures.add(future);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> data);
    }

    @Override
    public CompletableFuture<Void> save(UnoPlayer player, EconomyData component) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Currency currency : Currency.values()) {
            CompletableFuture<Void> future = economyTable.setBalance(player.getId(), currency, component.getBalance(currency));
            futures.add(future);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
}
