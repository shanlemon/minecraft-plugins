package com.shanmemon.javaplugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Team implements Comparable {
    private int[] teamColor;
    private String textColor;
    private ArrayList<Location> spawnPoints;

    private int teamSize;

    private ArrayList<Player> players;

    public Team(JSONObject teamInfo, Server server) {
        teamSize = 0;
        spawnPoints = new ArrayList<>();
        String[] colors = ((String) teamInfo.get("team_color")).split(" ");
        this.teamColor = new int[colors.length];
        players = new ArrayList<>();

        for (int i = 0; i < colors.length; i++) {
            this.teamColor[i] = Integer.parseInt(colors[i]);
        }

        this.textColor = (String) teamInfo.get("text_color");

        JSONArray spawnLocations = (JSONArray) teamInfo.get("spawn_locations");

        for (Object o : spawnLocations) {
            String[] locationInfo = ((String) o).split(" ");
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

    public void addPlayer(Player player) {
        players.add(player);
        teamSize++;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Location getRandomLocation() {
        Random r = new Random();
        return spawnPoints.get(r.nextInt(spawnPoints.size()));
    }

}