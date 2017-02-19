package db.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

/**
 * @author kawasima
 */
public class V5__InsertUser implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        String sql = "INSERT INTO user(user_id, first_name, last_name, email, pass) " +
                "VALUES(?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, 0L);
            stmt.setString(2, "kawasima");
            stmt.setString(3, "");
            stmt.setString(4, "sample@tis.co.jp");
            stmt.setString(5, "pass");
            stmt.executeUpdate();
            connection.commit();
        }
    }
}
