package net.earomc.astrainventoryview;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerDeathListener implements Listener {

    private InventoryManager inventoryManager;

    public PlayerDeathListener(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        Player player = event.getEntity();
        InventoryManager im = inventoryManager;
        UUID uuid = player.getUniqueId();
        if (!im.isRegistered(uuid)) {
            im.registerNewPlayer(uuid);
        }
        im.addNewDeath(player);
    }

}
