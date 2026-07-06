package me.unoprojects.economy.api;

import me.unoprojects.unocore.api.modules.UnoPlugin;
import me.unoprojects.economy.api.database.tables.EconomyTable;

public abstract class UnoEconomy extends UnoPlugin {

    public abstract EconomyTable getEconomyTable();
}
