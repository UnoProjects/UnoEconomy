package me.unoprojects.unoeconomy.api;

import me.unoprojects.unocore.api.modules.UnoPlugin;
import me.unoprojects.unoeconomy.api.database.tables.EconomyTable;

public abstract class UnoEconomy extends UnoPlugin {

    public abstract EconomyTable getEconomyTable();
}
