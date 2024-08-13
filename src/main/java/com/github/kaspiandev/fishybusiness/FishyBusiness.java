package com.github.kaspiandev.fishybusiness;

import com.github.kaspiandev.fishybusiness.user.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.UUID;

public final class FishyBusiness extends JavaPlugin {

    //private Config config;

    @Override
    public void onEnable() {
//        try {
//            config = new Config(this);
//        } catch (PluginLoadFailureException ex) {
//            getPluginLoader().disablePlugin(this);
//            throw new RuntimeException(ex);
//        }

        try (JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:myDb.db")) {
            TableUtils.createTableIfNotExists(connectionSource, User.class);

            Dao<User, Long> libraryDao
                    = DaoManager.createDao(connectionSource, User.class);
            User user = new User();
            user.setUuid(UUID.randomUUID());
            user.setPoints(20);
            libraryDao.create(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
