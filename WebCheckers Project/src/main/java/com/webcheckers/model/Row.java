package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The structure for a row of the gameboard
 *
 * @author <a href='mailto:amf3379@rit.edu'>Alex Frankel</a>
 */
public class Row{
  // index of the row on the board (0-7)
  private int index;
  // list containing the 8 spaces in the row
  private List<Space> spaces;

  /**
   * Creates a new row with the given index and deeply copies 8 pieces from the given
   * spaces list starting at the index given
   *
   * @param rowIndex the index of the row
   * @param spaces the list of spaces to deeply copy from
   * @param startIndex the index to start the copying from
   */
  public Row( int rowIndex, List<Space> spaces, int startIndex ){
    this.index = rowIndex;
    this.spaces = new ArrayList<>();
    for(int i = 0; i < 8; i++){
      Space space = new Space(spaces.get(startIndex + i));
      this.spaces.add(space);
    }
  }

  /**
   * @return the index of the row
   */
  public int getIndex(){
    return index;
  }

  /**
   * Creates an iterable containing the 8 spaces in the row
   *
   * @return the iterable of spaces
   */
  public Iterator<Space> iterator(){
    return spaces.iterator();
  }
}
