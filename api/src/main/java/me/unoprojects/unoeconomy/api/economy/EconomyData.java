package me.unoprojects.unoeconomy.api.economy;

import me.unoprojects.unocore.api.data.PlayerComponent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EconomyData implements PlayerComponent {

    private final Map<Currency, Double> balances = new ConcurrentHashMap<>();

    public EconomyData() {
        for (Currency currency : Currency.values()) {
            balances.put(currency, 0.0);
        }
    }

    public double getBalance(Currency currency) {
        if (currency == null) return 0.0;
        return balances.getOrDefault(currency, 0.0);
    }

    public void setBalance(Currency currency, double balance) {
        if (currency == null) return;
        balances.put(currency, balance);
    }
}
