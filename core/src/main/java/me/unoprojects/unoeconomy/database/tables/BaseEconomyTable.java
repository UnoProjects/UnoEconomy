package me.unoprojects.unoeconomy.database.tables;

import me.unoprojects.unocore.api.database.ConnectionProvider;
import me.unoprojects.unoeconomy.api.UnoEconomy;
import me.unoprojects.unoeconomy.api.database.tables.EconomyTable;
import me.unoprojects.unoeconomy.api.economy.Currency;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class BaseEconomyTable extends EconomyTable {

    @Language("SQL")
    private static final String TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS unoeconomy_balances (
                player_id INT NOT NULL,
                currency VARCHAR(16) NOT NULL,
                balance DOUBLE NOT NULL DEFAULT 0.0,
                PRIMARY KEY (player_id, currency),
                FOREIGN KEY (player_id) REFERENCES unocore_players(id) ON DELETE CASCADE
            );
            """;

    @Language("SQL")
    private static final String SELECT = """
            SELECT balance
            FROM unoeconomy_balances
            WHERE player_id = ? AND currency = ?;
            """;

    @Language("SQL")
    private static final String UPSERT = """
            INSERT INTO unoeconomy_balances (player_id, currency, balance)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE balance = ?;
            """;

    public BaseEconomyTable(ConnectionProvider provider, UnoEconomy plugin) {
        super(provider, plugin, TABLE_QUERY);
    }

    @Override
    public CompletableFuture<Double> getBalance(int playerId, Currency currency) {
        return supplyAsync(() -> {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SELECT)) {
                stmt.setInt(1, playerId);
                stmt.setString(2, currency.name());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("balance");
                    }
                }
            } catch (SQLException e) {
                logError(e, "Error fetching balance for player_id " + playerId + " and currency " + currency);
            }
            return 0.0;
        });
    }

    @Override
    public CompletableFuture<Void> setBalance(int playerId, Currency currency, double balance) {
        return runAsync(() -> {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(UPSERT)) {
                stmt.setInt(1, playerId);
                stmt.setString(2, currency.name());
                stmt.setDouble(3, balance);
                stmt.setDouble(4, balance);
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e, "Error saving balance for player_id " + playerId + " and currency " + currency);
            }
        });
    }
}
