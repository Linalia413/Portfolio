package com.webcheckers.model;

import com.webcheckers.util.Color;
import com.webcheckers.util.Type;

/**
 * The structure for a checkers piece
 *
 * @author <a href='mailto:amf3379@rit.edu'>Alex Frankel</a>
 */
public class Piece {
  // enum of whether the piece is a single or king (only 2 values are 'SINGLE' and 'KING')
  private Type type;
  // enum of whether the piece is red or white (only 2 values are 'RED' and 'WHITE')
  private Color color;

  /**
   * Creates a new piece with the given type and color
   *
   * @param type whether the piece is a single piece or a king piece
   * @param color whether the piece is a red or white piece
   */
  public Piece( Type type, Color color){
    this.type = type;
    this.color = color;
  }

  /**
   * @return the type of piece ('SINGLE' or 'KING')
   */
  public Type getType(){
    return type;
  }

  /**
   * @return the color of the piece ('RED' or 'WHITE')
   */
  public Color getColor() {
    return color;
  }
}
