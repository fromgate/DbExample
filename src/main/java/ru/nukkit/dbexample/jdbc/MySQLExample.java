package ru.nukkit.dbexample.jdbc;

import cn.nukkit.Server;
import ru.nukkit.dbexample.DbExample;
import ru.nukkit.dblib.DbLib;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLExample {

    private static boolean enabled;

    public static boolean init() {
        enabled = (Server.getInstance().getPluginManager().getPlugin("DbLib") != null);
        return enabled;
    }


    public static Connection connectToMySQL() {
        return connectToMySQL(DbExample.getPlugin().host,
                DbExample.getPlugin().port,
                DbExample.getPlugin().database,
                DbExample.getPlugin().name,
                DbExample.getPlugin().password);
    }

    public static Connection connectToMySQL(String host, String port, String db, String name, String pwd) {
        if (!enabled) return null;
        Connection connection = DbLib.getMySqlConnection(host, (port == null || port.isEmpty() ? -1 : Integer.parseInt(port)),
                db, name, pwd);

        if (connection == null) enabled = false;
        return connection;
    }

    public static boolean executeUpdate(String query) throws SQLException {
        Connection connection = connectToMySQL();
        if (connection == null) return false;
        Statement statement = connection.prepareStatement(query);
        statement.executeUpdate(query);
        if (statement != null) statement.close();
        if (connection != null) connection.close();
        return true;
    }

    public static List<String> executeSelect(String query) throws SQLException {
        List<String> list = new ArrayList<String>();
        Connection connection = connectToMySQL();
        if (connection == null) return list;
        Statement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet == null) return null;
        while (resultSet.next()) list.add(resultSet.getString("name") + " " + resultSet.getString("lastname"));
        if (statement != null) statement.close();
        if (connection != null) connection.close();
        return list;
    }


    public static void runTest() throws SQLException {
        if (!enabled) {
            DbExample.log("Failed to execute MySQL test");
            return;
        }
        String query = "create table if not exists dbtest2 (name varchar(20), lastname varchar(20))";
        if (executeUpdate(query)) DbExample.log("Table successfully created");
        else {
            DbExample.log("&cFailed to create table!");
        }

        query = "insert into dbtest2 (name,lastname) values ('bob','marley')";
        if (executeUpdate(query)) DbExample.log("Query executed: " + query);
        else DbExample.log("&cFailed to add record #1 to database");
        query = "insert into dbtest2 (name,lastname) values ('john','lennon')";
        if (executeUpdate(query)) DbExample.log("Query executed: " + query);
        else DbExample.log("&cFailed to add record #2 to database");
        query = "insert into dbtest2 (name,lastname) values ('dima','beeline')";
        if (executeUpdate(query)) DbExample.log("Query executed: " + query);
        else DbExample.log("&cFailed to add record #3 to database");

        query = "select * from dbtest2 where name='dima'";
        List<String> list = executeSelect(query);
        if (list.isEmpty()) DbExample.log("&cNo records found in table");
        else {
            DbExample.log("select * from dbtest2 where name='dima'");
            for (String s : list) DbExample.log(s);
        }

    }


}
