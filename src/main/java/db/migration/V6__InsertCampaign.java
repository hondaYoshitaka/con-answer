package db.migration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.stream.Collectors;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

/**
 * @author kawasima
 */
public class V6__InsertCampaign implements JdbcMigration {
    private String read(InputStream in) throws IOException {
        try (BufferedReader buf = new BufferedReader(new InputStreamReader(in))) {
            return buf.lines().collect(Collectors.joining("\n"));
        }
    }

    @Override
    public void migrate(Connection connection) throws Exception {
        String sql = "INSERT INTO campaign(campaign_id, title, statement, goal, create_user_id) " +
                "VALUES(?,?,?,?,?)";
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, 1L);
            stmt.setString(2, "開発に必要なサイトはプロキシでのブロックをやめたい！");
            stmt.setString(3, processor.markdownToHtml(
                    "## 我々の手でエンジニアを守りましょう！\n" +
                            "\n" +
                            "エンジニアを絶滅から救うために、以下のことを実現させましょう！\n" +
                            "\n" +
                            "- 全社で使えるチャットを導入します。\n" +
                            "- フリーWi-Fiを使えるスポットを増やします。\n" +
                            "- PC完備の研修室を作ります。\n"
            ));
            stmt.setLong(4, 1000L);
            stmt.setLong(5, 0L);
            stmt.executeUpdate();
            connection.commit();
        }
    }
}
