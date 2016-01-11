package ru.nukkit.dbexample;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteExample {

    private static boolean enabled;
    public static boolean init(){
        try {
            Class.forName("org.sqlite.JDBC");
            enabled = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            enabled = false;
        }
        return enabled;
    }

    public static Connection connectToSQLite() throws SQLException {
        return connectToSQLite("test.db");
    }

    public static Connection connectToSQLite(String filename) throws SQLException {
        if (!enabled) return null;
        String url = "jdbc:sqlite:"+DbExample.getPlugin().getDataFolder()+ File.separator+filename;
        return DriverManager.getConnection(url);
    }

    public static boolean executeUpdate (String query) throws SQLException {
        Connection  connection = connectToSQLite();
        if (connection == null) return false;
        connection.createStatement().executeUpdate(query);
        if (connection != null) connection.close();
        return true;
    }


    public static List<String> executeSelect(String query) throws SQLException {
        List<String> list = new ArrayList<String>();
        Connection  connection = connectToSQLite();
        if (connection == null) return list;
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        if (resultSet == null) return null;
        while (resultSet.next()) {
            list.add(resultSet.getString("name")+" "+resultSet.getString("lastname"));
        }
        if (connection != null) connection.close();
        return list;
    }


    public static void runTest() throws SQLException {
        if (!enabled) {
            DbExample.log("Failed to execute SQLite test");
            return;
        }
        String query = "create table if not exists dbtest (name varchar(20), lastname varchar(20))";
        if (executeUpdate(query)) DbExample.log ("Table successfully created");
        else {
            DbExample.log ("&cFailed to create table!");
            return;
        }

        query = "insert into dbtest (name,lastname) values ('bob','marley')";
        if (executeUpdate(query)) DbExample.log("Query executed: "+query);
        else DbExample.log("&cFailed to add record #1 to database");
        query = "insert into dbtest (name,lastname) values ('john','lennon')";
        if (executeUpdate(query)) DbExample.log("Query executed: "+query);
        else DbExample.log("&cFailed to add record #2 to database");
        query = "insert into dbtest (name,lastname) values ('dima','beeline')";
        if (executeUpdate(query)) DbExample.log("Query executed: "+query);
        else DbExample.log("&cFailed to add record #3 to database");

        query = "select * from dbtest where name='dima'";
        List<String> list = executeSelect(query);
        if (list.isEmpty()) DbExample.log("&cNo records found in table");
        else {
            DbExample.log("select * from dbtest2 where name='dima'");
            for (String s : list) DbExample.log(s);
        }
    }


}
