package com.david.cedict.db;

import java.sql.*;

public class EmbeddedH2 {
    public EmbeddedH2() {}

    public void createDB() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:./data/cedictdb", "sa", "");
            Statement statement = connection.createStatement();
            CEDictDB cedictdb = new CEDictDB(statement);
            cedictdb.parse();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createVSM() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:./data/vsm", "sa", "");
            Statement vsmStatement = connection.createStatement();
            vsmStatement.execute("CREATE TABLE IF NOT EXISTS vsm");
            connection = DriverManager.getConnection("jdbc:h2:./data/cedictdb", "sa", "");
            Statement dbStatement = connection.createStatement();
            VectorSpaceModelDB vsm = new VectorSpaceModelDB(vsmStatement, dbStatement);
            vsm.runVSM();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}