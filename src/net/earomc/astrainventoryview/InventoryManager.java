package net.earomc.astrainventoryview;


import net.minecraft.server.v1_15_R1.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InventoryManager {

    private File file;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy/HH:mm:ss");
    private String fileSeparator = System.getProperty("file.separator");

    public InventoryManager() {
        String f = fileSeparator;
        this.file = new File("plugins" + f + "AstraInventoryView" + f + "inventories");
        if (!this.file.exists()) {
            this.file.mkdirs();
        }
    }


    public boolean registerNewPlayer(UUID uuid) {
        File inventoriesFile = getFile(uuid);
        if (inventoriesFile.exists()) {
            return false;
        } else {
            try {
                inventoriesFile.createNewFile();
                System.out.println("Created new file for " + uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void addNewDeath(Player player) {
        try {
            this.saveInventory(player.getInventory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveInventory(PlayerInventory inventory) throws IOException {
        //noinspection ConstantConditions
        UUID uuid = inventory.getHolder().getUniqueId();
        if (!isRegistered(uuid)) {
            throw new IllegalStateException("Cant save inventory when the player is not registered");
        } else {
            File thisFile = getFile(uuid);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(thisFile);
            Date date = new Date();
            config.set(dateFormat.format(date), inventory);
            config.save(thisFile);
        }
    }

    private void saveInventory(YamlConfiguration config, String path, PlayerInventory inventory) {

    }

    private PlayerInventory getInventory(YamlConfiguration config, UUID uuid) {
        return null;
    }

    public PlayerInventory getMostRecentPlayerInventory(UUID uuid) {
        YamlConfiguration config = getConfig(uuid);

        HashMap<Date, PlayerInventory> entries = new HashMap<>();
        config.getValues(false).forEach((s, o) -> entries.put(parseDate(dateFormat, s), (PlayerInventory) o));
        ArrayList<Map.Entry<Date, PlayerInventory>> entryList = new ArrayList<>(entries.entrySet());
        entryList.sort(Map.Entry.comparingByKey());
        return entryList.get(0).getValue();
    }

    private Date parseDate(DateFormat dateFormat, String s) {
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public PlayerInventory getInventory(UUID uuid, Date date) {
        YamlConfiguration config = getConfig(uuid);
        return config.getObject(dateFormat.format(date), PlayerInventory.class);
    }


    public boolean isRegistered(UUID uuid) {
        return getFile(uuid).exists();
    }

    private File getFile(UUID uuid) {
        String filePath = this.file.getPath() + fileSeparator + uuid.toString() + ".yml";

        System.out.println(filePath);
        return new File(filePath);

    }

    private YamlConfiguration getConfig(UUID uuid) {
        return YamlConfiguration.loadConfiguration(getFile(uuid));
    }
}
