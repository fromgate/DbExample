package ru.nukkit.dbexample.sql2o;

import cn.nukkit.Server;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.nukkit.dbexample.DbExample;
import ru.nukkit.dblib.DbLib;

import java.util.List;

/**
 * Created by Igor on 31.07.2016.
 */
public class Sql2oExample {

    private static boolean enabled;

    private final static String createSQL = "create table if not exists sql2o_test (name varchar(20), lastname varchar(20))";
    private final static String insetQuery = "insert into sql2o_test (name, lastname) values (:name, :lastname)";
    private final static String selectQuery = "select * from sql2o_test where name=:name";



    private static Sql2o sql2o;
    public static boolean init(){
        enabled = (Server.getInstance().getPluginManager().getPlugin("DbLib")!=null);
        if (!enabled) return false;
        sql2o = DbLib.getSql2o();
        enabled = (sql2o!=null);
        return enabled;
    }



    public static void insertData(String name, String lastName){
        try (Connection con = sql2o.open()){
            con.createQuery(insetQuery).addParameter("name",name).addParameter("lastname",lastName).executeUpdate();
            con.close();
            DbExample.log ("Insert query: "+name+" "+lastName);
        }
    }


    public static void runTest(){
        if (!enabled) {
            DbExample.log("Failed to execute Sql2o test");
            return;
        }
        try (Connection con = sql2o.open()){
            con.createQuery(createSQL).executeUpdate();
            con.close();
            DbExample.log ("Table successfully created");
        }


        insertData("bob", "marley");

        insertData("john", "lennon");

        insertData("dima", "beeline");



        List<Sql2oTable> result = null;
        try (Connection con = sql2o.open()){
            result = con.createQuery(selectQuery).addParameter("name","dima").executeAndFetch(Sql2oTable.class);
            con.close();
        }
        if (result == null) DbExample.log ("Select query returned null");
        else if (result.isEmpty()) DbExample.log ("Select query returned nothing");
        else {
            DbExample.log ("Select query result:");
            for (Sql2oTable t: result) DbExample.log (t.name+" "+t.lastname);
        }


        String query = "select lastname from sql2o_test where name=:name";
        List<String> lastNames= null;
        try (Connection con = sql2o.open()){
            lastNames = con.createQuery(query).addParameter("name","dima").executeAndFetch(String.class);
            con.close();
        }



    }

}
