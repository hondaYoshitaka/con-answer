package db.migration;

import java.sql.Connection;
import java.sql.Statement;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

/**
 * @author kawasima
 */
public class V2__CreateCampaign implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE campaign (" +
                    "campaign_id INTEGER DEFAULT 1 NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(30) NOT NULL," +
                    "statement CLOB NOT NULL," +
                    "goal INTEGER," +
                    "create_user_id INTEGER NOT NULL," +
                    "FOREIGN KEY (create_user_id) REFERENCES user(user_id)" +
                    ")"
            );
        }
    }
}
