package ru.nukkit.dbexample;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.dbexample.jdbc.MySQLExample;
import ru.nukkit.dbexample.jdbc.SQLiteExample;
import ru.nukkit.dbexample.ormlite.ORMLiteExample;
import ru.nukkit.dbexample.sql2o.Sql2oExample;

import java.sql.SQLException;

public class DbExample extends PluginBase {
    public String host;
    public String port;
    public String database;
    public String name;
    public String password;

    private static DbExample plugin;
    public static DbExample getPlugin(){
        return plugin;
    }

    public void loadCfg(){
        this.getDataFolder().mkdirs();
        this.saveResource("config.yml");
        this.reloadConfig();
        this.host = this.getConfig().getString("host","localhost");
        this.port = this.getConfig().getString("port","3306");
        this.database = this.getConfig().getString("database","world");
        this.name = this.getConfig().getString("name","nukkit");
        this.password = this.getConfig().getString("password","tikkun");

    }

    @Override
    public void onEnable(){
        plugin = this;
        loadCfg();
        log("&eDbExample plugin started");
        log("init ORMLite example : "+ ORMLiteExample.init());
        log("init MySQL example   : "+ MySQLExample.init());
        log("init SQLite example  : "+ SQLiteExample.init());
        log("init Sql2o example  : "+ Sql2oExample.init());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            sender.sendMessage(TextFormat.DARK_RED+"You can use this comand in console only!!!");
        } else {
            log("&6Test 1. ORMLite (DBLib configuration");
            ORMLiteExample.runTest();
            log("&6Test 2. MySQL through JDBC");
            try {
                MySQLExample.runTest();
            } catch (SQLException e) {
                log ("&cFAIL!");
                e.printStackTrace();
            }
            log("&6Test 3. SQLite through JDBC");
            try {
                SQLiteExample.runTest();
            } catch (SQLException e) {
                log ("&cFAIL!");
                e.printStackTrace();
            }

            log("&6Test 4. Sql2o (DBlib configuration)");
            try {
                Sql2oExample.runTest();
            } catch (Exception e) {
                log ("&cFAIL!");
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void log (String s){
        getPlugin().getLogger().info(TextFormat.colorize("&a"+s));
    }
}
