package com.shanmemon.javaplugin;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Arena {

    private String name;
    private boolean ffa;
    private ArrayList<Team> teams;

    private Server server;

    public Arena(JSONObject arenaInfo, Server server) {
        this.server = server;
        name = (String) arenaInfo.get("arena_name");
        ffa = (boolean) arenaInfo.get("ffa");
        teams = createTeams((JSONArray) arenaInfo.get("teams"));
    }

    private ArrayList<Team> createTeams(JSONArray teams) {
        Iterator<JSONObject> it = teams.iterator();

        ArrayList<Team> output = new ArrayList<>();

        while (it.hasNext()) {
            output.add(new Team(it.next(), server));
        }

        return output;
    }

    public Location tryJoinGame(Player player) {
        teams.sort(null);
        Team chosen = teams.get(0);
        chosen.addPlayer(player);

        return chosen.getRandomLocation();
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

}