package me.unoprojects.economy;

import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.economy.api.UnoEconomy;
import me.unoprojects.economy.api.database.tables.EconomyTable;
import me.unoprojects.economy.database.tables.BaseEconomyTable;

import me.unoprojects.economy.commands.UnoEconomyCommand;
import me.unoprojects.economy.placeholders.UnoEconomyExpansion;
import org.bukkit.Bukkit;

public class DefaultUnoEconomy extends UnoEconomy {

    private EconomyTable economyTable;

    @Override
    protected void onModuleEnable() {
        this.economyTable = new BaseEconomyTable(UnoCore.getInstance().getConnectionProvider(), this);
        UnoCore.getInstance().getPlayerDataManager().registerProvider(new me.unoprojects.economy.data.EconomyDataProvider(economyTable));

        registerCommands(new UnoEconomyCommand(this).get());

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new UnoEconomyExpansion(this).register();
        }
        
        getLogger().info("UnoEconomy caricato con successo!");
    }

    @Override
    protected void onModuleDisable() {
        getLogger().info("UnoEconomy disattivato.");
    }

    @Override
    public EconomyTable getEconomyTable() {
        return economyTable;
    }
}
