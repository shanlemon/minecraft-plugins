package com.shanmemon.javaplugin;

/**
 * Hello world!
 *
 */
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class App extends JavaPlugin implements Listener {

    SignTeleport st;

    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!!!!!");
        getServer().getPluginManager().registerEvents(this, this);

        try {
            st = new SignTeleport(getServer(), this, "signs.JSON");
        } catch (Exception e) {
            System.out.println("Failed to parse");
            e.printStackTrace();
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
        st.signTeleport(event);
    }

}
