package ru.nukkit.dbexample;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ru.nukkit.dblib.DbLib;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ORMLiteExample {

    private static boolean enabled;

    private static ConnectionSource connectionSource;
    private static Dao<DbTestTable,String> dao;

    public static boolean init(){
        enabled = false;
        connectionSource = DbLib.getConnectionSource();
        if (connectionSource == null) {
            DbExample.log("&cFailed to connect to ORMLite");
            return false;
        }
        try {
            dao = DaoManager.createDao(connectionSource,DbTestTable.class);
            TableUtils.createTableIfNotExists(connectionSource,DbTestTable.class);
        } catch (SQLException e) {
            DbExample.log("&cFailed to create table");
            e.printStackTrace();
            return false;
        }
        enabled = true;
        return true;
    }

    public static void runTest (){
        addEntry("bob","marley");
        addEntry("john","lennon");
        addEntry("dima","beeline");
        List<String> list = getByName("dima");
        if (list.isEmpty()) DbExample.log("&cNo records found in table");
        else for (String s : list) DbExample.log (s);
    }


    public static void addEntry (String name, String lastname){
        if (!enabled) return;
        DbTestTable record = new DbTestTable(name,lastname);
        try {
            dao.create(record);
            DbExample.log("Record saved to database ("+name+", "+lastname+")");
        } catch (SQLException e) {
            DbExample.log("&cFailed to save record to database ("+name+", "+lastname+")");
            e.printStackTrace();
        }
    }

    public static List<String> getByName (String name){
        List<String> list = new ArrayList<String>();
        if (!enabled) return list;
        List<DbTestTable> records = null;
        try {
            records = dao.queryForEq("name",name);
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
        for (DbTestTable record: records)
            list.add(record.getName()+" "+record.getLastname());

        return list;
    }




}
