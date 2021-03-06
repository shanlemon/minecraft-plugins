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

public class Teleporter {

    private HashMap<String, Location> endPoints = new HashMap<>();
    private Server server;
    private Plugin plugin;

    private final float RELATIVE_INDICATOR = 360.0f;

    public Teleporter(Server s, Plugin p, String jsonSource) throws Exception {
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

        if (!(block instanceof Sign)) {
            System.out.println("JSON Error, Given coordinate is not a sign");
        } else {
            Sign sign = (Sign) block;
            sign.setMetadata("arena_name", new FixedMetadataValue(plugin, arenaName));
        }

        if (!((boolean) signData.get("tp_to_arena"))) {
            assignToData(signData, arenaName);
        }
    }

    private void assignToData(JSONObject signData, String arenaName) {
        String[] toData = ((String) signData.get("to")).split(" ");
        double toX = Double.parseDouble(toData[1]);
        double toY = Double.parseDouble(toData[2]);
        double toZ = Double.parseDouble(toData[3]);

        if (!((boolean) signData.get("keep_direction"))) {
            float toPitch = Float.parseFloat(toData[4]);
            float toYaw = Float.parseFloat(toData[5]);
            endPoints.put(arenaName, new Location(server.getWorld(toData[0]), toX, toY, toZ, toYaw, toPitch));
        } else {
            endPoints.put(arenaName,
                    new Location(server.getWorld(toData[0]), toX, toY, toZ, RELATIVE_INDICATOR, RELATIVE_INDICATOR));
        }
    }

    public void signTeleport(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            BlockState block = event.getClickedBlock().getState();
            if (block instanceof Sign) {
                Sign sign = (Sign) block;
                if (sign.hasMetadata("arena_name")) {
                    String arenaName = sign.getMetadata("arena_name").get(0).asString();
                    Location toLocation = endPoints.get(arenaName);
                    if (toLocation == null) {
                        Arena arenaToTeleportTo = App.ARENAS.arenas.get(arenaName);

                        Location endPoint = arenaToTeleportTo.tryJoinGame(event.getPlayer());

                        if (endPoint == null) {
                            event.getPlayer().sendMessage("Failed to Join Arena: " + arenaName);
                        } else {
                            event.getPlayer().sendMessage("You have joined: " + arenaName);
                            event.getPlayer().teleport(endPoint);
                        }

                    } else {
                        Location playerLocation = event.getPlayer().getLocation();

                        float pitch = toLocation.getPitch() == RELATIVE_INDICATOR ? playerLocation.getPitch()
                                : toLocation.getPitch();
                        float yaw = toLocation.getYaw() == RELATIVE_INDICATOR ? playerLocation.getYaw()
                                : toLocation.getYaw();

                        event.getPlayer().teleport(new Location(toLocation.getWorld(), toLocation.getX(),
                                toLocation.getY(), toLocation.getZ(), yaw, pitch));
                    }
                }
            }
        }
    }
}