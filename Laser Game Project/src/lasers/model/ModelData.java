package lasers.model;

/**
 * Use this class to customize the data you wish to send from the model
 * to the view when the model changes state.
 *
 * @author RIT CS
 * @author Jacob Harman
 * @author Sean Harmon
 */
public class ModelData {

    private int row;
    private int col;
    private String val;
    private boolean error = false;

    /**
     * Used for if the value at this spot becomes a laser beam, true if the laser it originates from is from the left
     * or right of the laser beam
     */
    private boolean laserLR = false;

    /**
     * Used for if the value at this spot becomes a laser beam, true if the laser it originates from is above or below
     * the laser beam
     */
    private boolean laserUD = false;

    /**
     * This constructor takes in the row, column, and the value at these coordinates to be used when updating the GUI
     *
     * @param row the row coordinate for the spot in question
     * @param col the column coordinate for the spot in question
     * @param val the value at the coordinates given
     */
    public ModelData(int row, int col, String val) {
        this.row = row;
        this.col = col;
        this.val = val;
    }

    /**
     * This returns the int representing the row of the coordinates given
     *
     * @return the row as an int
     */
    public int getRow() {
        return row;
    }

    /**
     * This returns the int representing the column of the coordinates given
     *
     * @return the column as an int
     */
    public int getCol() {
        return col;
    }

    /**
     * This returns the String representing the value at the coordinates given in the object already
     *
     * @return the value at the coordinates given as a String
     */
    public String getVal() {
        return val;
    }

    /**
     * This returns true if the spot is in an error state, and false otherwise
     *
     * @return true or false
     */
    public boolean inError() {
        return error;
    }

    /**
     * If the spot becomes a laser beam, this is set to true if the beam originates from a laser to the left or right
     * of the laser beam, and false otherwise
     *
     * @return true or false
     */
    public boolean isLaserLR() {
        return laserLR;
    }

    /**
     * If the spot becomes a laser beam, this is set to true if the beam originates from a laser above or below the
     * laser beam, and false otherwise
     *
     * @return true or false
     */
    public boolean isLaserUD() {
        return laserUD;
    }

    /**
     * This method sets the error state to true or false
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * This method sets laserUD to true if the laser beam at this spot is to the right or the left of the laser it
     * originates from and vice versa
     */
    public void setLaserLR(boolean laserLR) {
        this.laserLR = laserLR;
    }

    /**
     * This method sets laserUD to true if the laser beam at this spot is above or below the laser it originates from
     * and vice versa
     */
    public void setLaserUD(boolean laserUD) {
        this.laserUD = laserUD;
    }
}
