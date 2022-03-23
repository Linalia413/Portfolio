package com.webcheckers.model;

import com.webcheckers.util.Color;
import com.webcheckers.util.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The structure for the gameboard itself
 *
 * @author <a href='mailto:amf3379@rit.edu'>Alex Frankel</a>
 */
public class Board {
  // A list representing the spaces on the gameboard
  private List<Space> spaces;

  /**
   * Creates a new board with a full 64 length array of spaces
   */
  public Board(){
    this.spaces = new ArrayList<Space>();
    for(int i = 0; i < 64; i++){
      Space space = new Space(i);
      spaces.add(space);
    }
  }

  /**
   * Initializes the board with player pieces in starting positions with
   * white pieces in the first 3 rows and red pieces in the last 3 rows
   */
  public void initializeBoard(){
    for(int i = 1; i < 22; i++){
      if(spaces.get(i).isDarkSquare()){
        Piece piece = new Piece(Type.SINGLE, Color.WHITE);
        spaces.get(i).addPiece(piece);
      }
    }
    for(int i = 36; i < 64; i++){
      if(spaces.get(i).isDarkSquare()){
        Piece piece = new Piece(Type.SINGLE, Color.RED);
        spaces.get(i).addPiece(piece);
      }
    }
  }

  /**
   * Creates an iterator of row iterators of spaces representing the board
   * These spaces are deeply copied
   *
   * @return an iterator of rows
   */
  public Iterator<Row> iterator(){
    List<Row> rows = new ArrayList<>();
    for(int i = 0; i < 8; i++){
      Row row = new Row(i, spaces, (i * 8));
      rows.add(row);
    }
    return rows.iterator();
  }
}
