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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SignTeleport {

    private HashMap<String, Location> arenas = new HashMap<>();
    private Server server;
    private Plugin plugin;

    public SignTeleport(Server s, Plugin p, String jsonSource) throws Exception {
        server = s;
        plugin = p;
        generateArenas(jsonSource);
    }

    private void generateArenas(String jsonSource) throws FileNotFoundException, IOException, ParseException {

        Object obj = new JSONParser().parse(new FileReader(new File(jsonSource).getCanonicalFile()));
        JSONObject jo = (JSONObject) obj;
        JSONArray ja = (JSONArray) jo.get("signs");

        Iterator<JSONObject> it = ja.iterator();
        while (it.hasNext()) {
            JSONObject signData = it.next();
            setSignData(signData);
        }
    }

    private void setSignData(JSONObject signData) {
        String[] fromData = ((String) signData.get("from")).split(" ");
        int fromX = Integer.parseInt(fromData[1]);
        int fromY = Integer.parseInt(fromData[2]);
        int fromZ = Integer.parseInt(fromData[3]);

        Location signLocation = new Location(server.getWorld(fromData[0]), fromX, fromY, fromZ);
        BlockState block = signLocation.getBlock().getState();

        String arenaName = (String) signData.get("arena_name");

        if (block instanceof Sign) {
            Sign sign = (Sign) block;
            sign.setMetadata("arena_name", new FixedMetadataValue(plugin, arenaName));
        }

        String[] toData = ((String) signData.get("to")).split(" ");
        double toX = Double.parseDouble(toData[1]);
        double toY = Double.parseDouble(toData[2]);
        double toZ = Double.parseDouble(toData[3]);
        float toPitch = Float.parseFloat(toData[4]);
        float toYaw = Float.parseFloat(toData[5]);

        arenas.put(arenaName, new Location(server.getWorld(toData[0]), toX, toY, toZ, toYaw, toPitch));
    }

    public void signTeleport(PlayerInteractEvent event) {
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