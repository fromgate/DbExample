package ru.nukkit.dbexample.ormlite;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ormlitetest")
public class DbTestTable {

    @DatabaseField(canBeNull = false, columnName = "name")
    String name;
    @DatabaseField(canBeNull = false, columnName = "lastname")
    String lastname;

    DbTestTable(){
    }

    DbTestTable(String name, String lastname){
        this.name = name;
        this.lastname = lastname;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getLastname(){
        return this.lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

}
