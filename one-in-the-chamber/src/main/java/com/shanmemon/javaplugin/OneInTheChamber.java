package com.shanmemon.javaplugin;

import java.net.http.WebSocket.Listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

public class OneInTheChamber extends Arena {
    boolean alreadyStarted = false;

    public OneInTheChamber(JSONObject arenaInfo, Server server) {
        super(arenaInfo, server);

    }

    private void startGame() {
        giveItemsToPlayers();
    }

    private void giveItemsToPlayers() {
        System.out.println("GIVING ITEMS TO PLAYER");
        for (Team team : getTeams()) {
            for (Player p : team.getPlayers()) {
                p.getInventory().clear();

                ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
                ItemMeta swordMeta = sword.getItemMeta();
                swordMeta.setDisplayName("Kniev");
                swordMeta.setUnbreakable(true);
                sword.setItemMeta(swordMeta);

                p.getInventory().addItem(sword);

                ItemStack[] items = { sword, new ItemStack(Material.BOW), new ItemStack(Material.ARROW) };
                p.getInventory().addItem(items);
            }
        }
    }

    public Location tryJoinGame(Player player) {
        Location ret = super.tryJoinGame(player);

        if (ret != null && !alreadyStarted) {
            // change after event
            startGame();
            alreadyStarted = true;
        }

        return ret;

    }

}