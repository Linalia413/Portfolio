package com.webcheckers.appl;

import com.webcheckers.model.Game;
import com.webcheckers.util.*;
import java.util.ArrayList;


/**
 * Coordinates the state of the application.
 *
 * @author <a href='mailto:cng7819@rit.edu'>Chris Grenfell</a>
 */
public class GameCenter {

  private PlayerLobby playerLobby;
  private ArrayList<Game> gameList;

  public GameCenter() {
    playerLobby = new PlayerLobby();
    gameList = new ArrayList<Game>();
  }

  public Player getPlayerByName(String name) {
    for (Player p : playerLobby.getList()) {
      if (p.toString().equals(name)) return p;
    }
    return null;
  }

  public Game getGameByPlayer(Player player) {
    for (Game g : gameList) {
      if (g.getRedPlayer().isEqual(player) || g.getWhitePlayer().isEqual(player)) return g;
    }
    return null;
  }

  public PlayerLobby getPlayerLobby() {
    return playerLobby;
  }

  public ArrayList<Game> getGameList() {
    return gameList;
  }

  public void addGame(Game g) {
    gameList.add(g);
  }

}
