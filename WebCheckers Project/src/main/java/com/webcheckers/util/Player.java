package com.webcheckers.util;

import java.util.Objects;

public class Player {
    private final String playerName;
    private boolean inGame;

    /**
     * Gets the current player name
     *
     * @return : returns the name of the player as a string
     */
    public String getPlayerName(){
        return playerName;
    }

    /**
     * gets whether or not the player is in a game
     *
     * @return : true if they are already in a game, false if not
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * compares the names of 2 players to make sure they aren't the same player
     * @param player2 : player to be compared against
     * @return : true if both names are the same, false if not
     */
    public boolean isEqual(Player player2) {
        return this.getPlayerName().equals(player2.getPlayerName());
    }

    /**
     * Changes you from out of game to in one, and vice-versa
     */
    public void changeGameState(){
        inGame = !inGame;
    }

    /**
     * creates a new instance of the Player class with a given name
     *
     * @param userName : name to give to player class
     */
    public Player ( String userName ){
        this.playerName = userName;
        this.inGame = false;
    }
}
