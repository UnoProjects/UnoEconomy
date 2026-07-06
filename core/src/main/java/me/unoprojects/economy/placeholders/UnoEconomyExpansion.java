package me.unoprojects.economy.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.data.UnoPlayer;
import me.unoprojects.economy.api.UnoEconomy;
import me.unoprojects.economy.api.economy.Currency;
import me.unoprojects.economy.api.economy.EconomyData;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class UnoEconomyExpansion extends PlaceholderExpansion {

    private final UnoEconomy plugin;

    public UnoEconomyExpansion(UnoEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "unoeconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return "UnoProjects";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        if (params.startsWith("balance_")) {
            String rest = params.substring("balance_".length());
            boolean formatted = false;
            if (rest.startsWith("formatted_")) {
                formatted = true;
                rest = rest.substring("formatted_".length());
            }

            String currencyName = rest.toUpperCase();
            Currency currency;
            try {
                currency = Currency.valueOf(currencyName);
            } catch (IllegalArgumentException e) {
                return null;
            }

            if (player.isOnline() && player.getPlayer() != null) {
                UnoPlayer unoPlayer = UnoCore.getInstance().getPlayerDataManager().getPlayer(player.getPlayer()).orElse(null);
                if (unoPlayer != null) {
                    EconomyData data = unoPlayer.getComponent(EconomyData.class);
                    if (data != null) {
                        double balance = data.getBalance(currency);
                        if (formatted) {
                            return currency.format(balance);
                        } else {
                            if (currency.isDecimal()) {
                                return String.format(Locale.US, "%.2f", balance);
                            } else {
                                return String.valueOf((int) balance);
                            }
                        }
                    }
                }
            }
            return "0";
        }

        return null;
    }
}
