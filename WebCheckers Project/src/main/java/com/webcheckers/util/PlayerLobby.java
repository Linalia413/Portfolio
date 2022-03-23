package com.webcheckers.util;


import java.util.*;

/**
 *
 * The list of all available players currently logged into the web server
 *
 * @author sshar413   Sean Harmon
 *
 */

public class PlayerLobby {
  private List<Player> list = new ArrayList<>();

  /**
   * Adds a new player to the list of players as long as they don't already exist.
   *
   * @param newPlayer : Player object to be added to the list
   * @return : true if player was added successfully, false if not
   */
  public boolean addPlayer (Player newPlayer) {
    if (isInList(newPlayer)){
      return false;
    }else{
      return list.add(newPlayer);
    }
  }

  /**
   * Checks to see if a given player is already in the list
   *
   * @param player : Player class to be cross-referenced against the contents of the list
   * @return : true if player is in the list already, false if not
   */
  public boolean isInList(Player player){
    return list.contains(player);
  }

  /**
   * returns the full ist of connected players for display on the homepage after sign in
   *
   * @return : list of players
   */
  public List<Player> getList(){
    return list;
  }

  /**
   * Removes a player from the list of connected players
   *
   * @param player : player to be removed
   * @return : true if player was removed correctly or false if the player wasn't in the list
   */
  public boolean removePlayer(Player player){
    if (isInList(player)){
      list.remove(player);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets the list of available players who you can start a game with
   *
   * @return : list of players not already in a game
   */
  public List<Player> getAvailable (){
    List<Player> availableList = new ArrayList<>();
    for (Player p : list) {
      if (!p.isInGame()){
        availableList.add(p);
      }
    }
    return availableList;
  }

}
