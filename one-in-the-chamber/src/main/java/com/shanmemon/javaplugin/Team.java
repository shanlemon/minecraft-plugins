package com.shanmemon.javaplugin;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Server;
import org.json.simple.JSONObject;

public class Team implements Comparable {
    private int[] teamColor;
    private String textColor;
    private ArrayList<Location> spawnPoints;

    private int teamSize;

    // private ArrayList<Player> players;

    public Team(JSONObject jsonObject, Server server) {
        teamSize = 0;
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

    @Override
    public int compareTo(Object o) {
        if (o instanceof Team) {
            Team team = (Team) o;
            return teamSize - team.teamSize;
        } else {
            return 0;
        }
    }

    public void addPlayer() {
        teamSize++;
    }

    public Location getRandomLocation() {
        Random r = new Random();
        return spawnPoints.get(r.nextInt(spawnPoints.size()));
    }

}