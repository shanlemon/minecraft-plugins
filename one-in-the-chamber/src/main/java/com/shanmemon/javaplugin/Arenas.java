package com.shanmemon.javaplugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Arenas {

    public HashMap<String, Arena> arenas = new HashMap<>();
    private Server server;
    private Plugin plugin;

    public Arenas(Server s, Plugin p, String jsonSource) throws Exception {
        server = s;
        plugin = p;

        File fileDest = createDataFiles(jsonSource);
        generateArenas(fileDest);
    }

    private File createDataFiles(String jsonSource) {
        File arenaInfoFile = plugin.getDataFolder();
        if (arenaInfoFile.mkdir()) {
            plugin.getLogger().info("Created \\One-In-The-Chamber directory.");
        }
        return new File(arenaInfoFile.toString() + "\\" + jsonSource);
    }

    private void generateArenas(File jsonSource) throws FileNotFoundException, IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(jsonSource));
        JSONObject jo = (JSONObject) obj;
        JSONArray ja = (JSONArray) jo.get("arenas");

        Iterator<JSONObject> it = ja.iterator();
        while (it.hasNext()) {
            JSONObject arenaData = it.next();
            arenas.put((String) arenaData.get("arena_name"), new Arena(arenaData, server));
        }
    }

}