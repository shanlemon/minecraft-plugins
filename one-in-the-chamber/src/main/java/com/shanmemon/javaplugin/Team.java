package com.shanmemon.javaplugin;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Server;
import org.json.simple.JSONObject;

public class Team {
    private int[] teamColor;
    private String textColor;
    private ArrayList<Location> spawnPoints;

    public Team(JSONObject jsonObject, Server server) {
        String[] colors = ((String) jsonObject.get("team_color")).split(" ");
        this.teamColor = new int[colors.length];

        for (int i = 0; i < colors.length; i++) {
            this.teamColor[i] = Integer.parseInt(colors[i]);
        }

        this.textColor = (String) jsonObject.get("text_color");

        for (String locationText : (String[]) jsonObject.get("spawn_locations")) {
            String[] locationInfo = locationText.split(" ");

            String worldName = locationInfo[0];
            double x = Double.parseDouble(locationInfo[1]);
            double y = Double.parseDouble(locationInfo[2]);
            double z = Double.parseDouble(locationInfo[3]);
            float yaw = Float.parseFloat(locationInfo[4]);
            float pitch = Float.parseFloat(locationInfo[5]);

            Location loc = new Location(server.getWorld(worldName), x, y, z, yaw, pitch);
            spawnPoints.add(loc);
        }
    }

}