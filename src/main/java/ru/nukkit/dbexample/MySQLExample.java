package ru.nukkit.dbexample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLExample {

    private static boolean enabled;
    public static boolean init(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            enabled = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            enabled = false;
        }
        return enabled;
    }


    public static Connection connectToMySQL(){
        return connectToMySQL(DbExample.getPlugin().host,
                DbExample.getPlugin().port,
                DbExample.getPlugin().database,
                DbExample.getPlugin().name,
                DbExample.getPlugin().password);
    }

    public static Connection connectToMySQL(String host, String port, String db, String name, String pwd){
        if (!enabled) return null;
        String url = "jdbc:mysql://"+host+(port.isEmpty()? "" : ":"+port)+"/"+db;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, name,pwd);
        } catch (Exception e) {
            DbExample.log("&cFailed to connect to MySQL");
            DbExample.log("&cURL  : "+url);
            DbExample.log("&cName : "+name);
            DbExample.log("&cPass : "+pwd);
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean executeUpdate (String query) throws SQLException {
        Connection  connection = connectToMySQL();
        if (connection == null) return false;
        Statement statement = null;
        statement = connection.prepareStatement(query);
        statement.executeUpdate(query);
        if (statement!=null) statement.close();
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
        if (executeUpdate(query)) DbExample.log ("Table successfully created");
        else {
            DbExample.log ("&cFailed to create table!");
            return;
        }

        query = "insert into dbtest2 (name,lastname) values ('bob','marley')";
        if (executeUpdate(query)) DbExample.log("Query executed: "+query);
        else DbExample.log("&cFailed to add record #1 to database");
        query = "insert into dbtest2 (name,lastname) values ('john','lennon')";
        if (executeUpdate(query)) DbExample.log("Query executed: "+query);
        else DbExample.log("&cFailed to add record #2 to database");
        query = "insert into dbtest2 (name,lastname) values ('dima','beeline')";
        if (executeUpdate(query)) DbExample.log("Query executed: "+query);
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
