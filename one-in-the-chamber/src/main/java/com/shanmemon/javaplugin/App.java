package com.shanmemon.javaplugin;

/**
 * Hello world!
 *
 */
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class App extends JavaPlugin implements Listener {

    HashMap<String, Location> arenas = new HashMap<>();

    private void setArenaLocations() {
        arenas.put("arena1", new Location(getServer().getWorld("world"), -166, 65, -93, 90, 90));
    }

    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!!!!!");
        setArenaLocations();
        getServer().getPluginManager().registerEvents(this, this);

        Location signLocation = new Location(getServer().getWorld("world"), -117, 68, -91);
        BlockState block = signLocation.getBlock().getState();

        if (block instanceof Sign) {
            Sign sign = (Sign) block;
            sign.setMetadata("arena_name", new FixedMetadataValue(this, "arena1"));
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje) {
        pje.setJoinMessage("Welcome, " + pje.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            BlockState block = event.getClickedBlock().getState();
            if (block instanceof Sign) {
                Sign sign = (Sign) block;
                if (sign.hasMetadata("arena_name")) {
                    event.getPlayer().teleport(arenas.get(sign.getMetadata("arena_name").get(0).asString()));
                }
            }

        }
    }

}
