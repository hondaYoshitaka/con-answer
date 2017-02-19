package db.migration;

import java.sql.Connection;
import java.sql.Statement;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

/**
 * @author kawasima
 */
public class V3__CreateSignature implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE signature (" +
                    "signature_id IDENTITY PRIMARY KEY," +
                    "name VARCHAR(30) NOT NULL," +
                    "signature_comment CLOB," +
                    "campaign_id INTEGER NOT NULL," +
                    "FOREIGN KEY (campaign_id) REFERENCES campaign(campaign_id)" +
                    ")"
            );
        }
    }
}
