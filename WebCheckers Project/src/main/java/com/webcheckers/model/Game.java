package com.webcheckers.model;

import com.webcheckers.util.Color;
import com.webcheckers.util.*;

/**
 * Represents an ongoing game.
 *
 * @author <a href='mailto:cng7819@rit.edu'>Chris Grenfell</a>
 */
public class Game {

  private Board board;
  private Player redPlayer;
  private Player whitePlayer;
  private Color currentColor;

  public Game(Player redPlayer, Player whitePlayer) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.board = new Board();
    this.currentColor = Color.RED;
  }

  public Board getBoard() {
    return board;
  }

  public Player getWhitePlayer() {
    return whitePlayer;
  }

  public Player getRedPlayer() {
    return redPlayer;
  }

  public Color getCurrentColor() {
    return currentColor;
  }

}
