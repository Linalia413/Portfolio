package com.webcheckers.model;

/**
 * The structure for a space on the gameboard
 *
 * @author <a href='mailto:amf3379@rit.edu'>Alex Frankel</a>
 */
public class Space {
  // The index the space is at on the overall board
  private int fullIndex;
  // The index the space is at in its row
  private int cellIndex;
  // The piece on the space
  private Piece piece;
  // Whether the space is a dark square or not
  private Boolean isDark;

  /**
   * Creates a space based off of the full index given. Determines other values based on
   * the full index being of a chessboard
   *
   * @param fullIndex the index of the space in the whole board
   */
  public Space( int fullIndex ){
    this.fullIndex = fullIndex;
    this.cellIndex = fullIndex % 8;
    this.piece = null;
    isDark = fullIndex % 2 != cellIndex % 2;
  }

  /**
   * Creates a deep copy of the given space
   *
   * @param that the space to copy
   */
  public Space( Space that ){
    this.fullIndex = that.getFullIndex();
    this.cellIndex = that.getCellIdx();
    this.isDark = that.isDarkSquare();
    Piece thatPiece = that.getPiece();
    if(thatPiece == null){
      this.piece = null;
    } else {
      this.piece = new Piece(thatPiece.getType(), thatPiece.getColor());
    }
  }

  /**
   * @return the piece on the square
   */
  public Piece getPiece() {
    return piece;
  }

  /**
   * @return the index of the square in the row
   */
  public int getCellIdx() {
    return cellIndex;
  }

  /**
   * @return the index of the square in the board
   */
  public int getFullIndex() {
    return fullIndex;
  }

  /**
   * @return whether the space is a dark square or not
   */
  public Boolean isDarkSquare(){
    return isDark;
  }

  /**
   * A space is valid if it is a dark square and has no piece on it
   *
   * @return if the space is valid or not
   */
  public boolean isValid(){
    return (isDark && piece == null);
  }

  /**
   * Removes the piece currently on the space
   *
   * @return the piece that was removed (null if no piece was on the space)
   */
  public Piece removePiece(){
    if(this.piece == null){ return null; }
    Piece removed = this.piece;
    this.piece = null;
    return removed;
  }

  /**
   * Adds the given piece to the space the space is valid (dark and no piece currently on it)
   * TODO: make the function return something if the square is not valid
   *
   * @param piece the piece to add to the space
   */
  public void addPiece(Piece piece){
    if(this.isValid()) {
      this.piece = piece;
    }
  }
}
