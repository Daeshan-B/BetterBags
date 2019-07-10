package com.daeshan.betterbags.utils;

import com.daeshan.betterbags.BetterBagsCore;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.dizitart.no2.Cursor;
import org.dizitart.no2.Document;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.exceptions.NitriteIOException;

import java.util.HashMap;

import static org.dizitart.no2.Document.createDocument;
import static org.dizitart.no2.filters.Filters.and;
import static org.dizitart.no2.filters.Filters.eq;

public class NitRiteData {

    private BetterBagsCore plugin;

    public NitRiteData(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    public Nitrite database;
    public NitriteCollection collection;

    public void setup() {
        String username = plugin.getConfig().getString("Nitrite_Database.username");
        String password = plugin.getConfig().getString("Nitrite_Database.password");

        try {
            database = Nitrite.builder()
                    .compressed()
                    .filePath(plugin.getDataFolder() + "/player-bag-data.db")
                    .openOrCreate(username, password);
            collection = database.getCollection("PlayerBagData");
            MessageManager.debug("Nitrite database has connected.");

        } catch (NitriteIOException e) {
            MessageManager.errorConsole("Issue creating database db location.");
        }
        saveChanges();
    }

    private void saveChanges() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (database.hasUnsavedChanges()) {
                    database.commit();
                    MessageManager.debug("Database has been committed.");
                }
            }
        }.runTaskTimer(plugin, 0, 60);
    }

    public void closeConnection() {
        database.commit();
        try {
            database.close();
        } catch (NitriteIOException e) {
            MessageManager.errorConsole("Could not properly shutdown the connection.");
        }
    }

    public void createPlayer(Player player) {
        Cursor cursor = collection.find(and(eq("uuid", player.getUniqueId().toString())));

        if (cursor.size() == 0) {
            Document document = createDocument("uuid", player.getUniqueId().toString());
            MessageManager.debug("No player was found with the uuid: " + player.getUniqueId().toString());
            document.put("bag.count", 0);
            document.put("bag.locations", 0);
            document.put("inventories", new HashMap<String, Inventory>());
            collection.insert(document);
            MessageManager.debug("Player inserted with uuid: " + player.getUniqueId().toString());

        }

    }


    /**
     * This will return the value of a field in the {@link Nitrite}
     *
     * @param key   is the search field (i.e "uuid")
     * @param value is the field value you are looking from from Key
     * @param field is the field value you want
     * @return the requested field value
     */
    public Object getFromDocument(String key, String value, String field) {
        Cursor cursor = collection.find(and(eq(key, value)));
        if (cursor.size() != 0) {
            return cursor.firstOrDefault().get(field);
        }
        return null;
    }

    /**
     * This will set the value of a field in the {@link Nitrite}
     *
     * @param key        is the search field (i.e "uuid")
     * @param value      is the field value you are looking from from Key
     * @param field      is the field value you want to set
     * @param fieldvalue is the value of field you are setting.
     */
    public void setInDocument(String key, String value, String field, Object fieldvalue) {
        Cursor cursor = collection.find(and(eq(key, value)));
        if (cursor.size() != 0) {
            if (cursor.size() != 0) {
                MessageManager.debug("Value set in document.");
                collection.update(eq(key, value), createDocument(field, fieldvalue));
            }

        }
    }
}
