package net.earomc.astrainventoryview;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class AstraInventoryView extends JavaPlugin {

    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        inventoryManager = new InventoryManager();
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(inventoryManager), this);
    }


}
