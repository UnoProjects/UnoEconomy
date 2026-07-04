package me.unoprojects.unoeconomy;

import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unoeconomy.api.UnoEconomy;
import me.unoprojects.unoeconomy.api.database.tables.EconomyTable;
import me.unoprojects.unoeconomy.database.tables.BaseEconomyTable;

import me.unoprojects.unoeconomy.commands.UnoEconomyCommand;
import me.unoprojects.unoeconomy.placeholders.UnoEconomyExpansion;
import org.bukkit.Bukkit;

public class DefaultUnoEconomy extends UnoEconomy {

    private EconomyTable economyTable;

    @Override
    protected void onModuleEnable() {
        this.economyTable = new BaseEconomyTable(UnoCore.getInstance().getConnectionProvider(), this);
        UnoCore.getInstance().getPlayerDataManager().registerProvider(new me.unoprojects.unoeconomy.data.EconomyDataProvider(economyTable));

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
