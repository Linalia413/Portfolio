package lasers.model;

import lasers.ptui.LasersPTUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The model of the lasers safe.  You are free to change this class however
 * you wish, but it should still follow the MVC architecture.
 *
 * @author RIT CS
 * @author Jacob Harman
 * @author Sean Harmon
 */
public class LasersModel {

    /** the observers who are registered with this model */
    private List<Observer<LasersModel, ModelData>> observers;

    /**
     * The total number of rows
     */
    private int rows;

    /**
     * The total number of columns
     */
    private int cols;

    /**
     * The String representation of the safe, [][] is row col coordinates
     */
    private String[][] safe;

    /**
     * The String given in the constructor that represents the file name of the safe text file
     */
    private String fileName;

    /**
     * This constructor starts the model of the GUI by initializing the observers list and building the String
     * representation of the safe to be used by the PTUI and the GUI
     *
     * @param filename this is the file name of the safe file given to build the String safe
     */
    public LasersModel(String filename) throws FileNotFoundException {
        this.observers = new LinkedList<>();
        fileName = filename;
        this.buildSafe(filename);
    }

    /**
     * This method makes a PTUI from the initial file given to the model. This is only used if Lasers goes into PTUI
     * mode.
     *
     * @throws FileNotFoundException thrown if file made in LasersPTUI constructor is not found
     */
    public LasersPTUI makePTUI() throws FileNotFoundException {
        return new LasersPTUI(fileName);
    }

    /**
     * Add a new observer.
     *
     * @param observer the new observer
     */
    public void addObserver(Observer<LasersModel, ModelData > observer) {
        this.observers.add(observer);
    }

    /**
     * Notify observers the model has changed.
     *
     * @param data optional data the model can send to the view
     */
    private void notifyObservers(ModelData data){
        for (Observer<LasersModel, ModelData> observer: observers) {
            observer.update(this, data);
        }
    }

    /**
     * Gets the total number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the total number of columns.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get a value from the safe PTUI board based on coordinates.
     *
     * @param row the row
     * @param col the column
     * @return the value
     * @rit.pre the coordinates are valid
     */
    public String getValueAtSpot(int row, int col) {
        return this.safe[row][col];
    }

    /**
     * Set a value from the safe PTUI board based on coordinates as well as notify the observers of the change.
     *
     * @param row the row
     * @param col the column
     * @rit.pre the coordinates are valid
     */
    public void setValueAtSpot(int row, int col, String val) {
        this.safe[row][col] = val;
        notifyObservers(new ModelData(row, col, this.safe[row][col]));
    }

    /**
     * This method uses the file taken from the main and builds the array representing the safe
     *
     * @param filename this comes from the command line arguments in the PTUI main, the file itself is where the
     *                 dimensions and the safe itself are taken from
     */
    public void buildSafe(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename));
        String[] splt;
        splt = in.nextLine().split(" ");
        rows = Integer.parseInt(splt[0]);
        cols = Integer.parseInt(splt[1]);
        safe = new String[rows][cols];
        int rowCounter = 0;
        int colCounter = 0;
        while (in.hasNextLine() && !(rowCounter >= rows)) {
            splt = in.nextLine().split(" ");
            for(String s : splt){
                safe[rowCounter][colCounter] = s;
                colCounter++;
            }
            colCounter = 0;
            rowCounter++;
        }
    }

    /**
     * This method adds a laser to the spot specified if possible with an L then shoots its "beams" using *'s
     *
     * @param row the row of the spot the laser is being added to and its beam originates from
     * @param col the column of the spot the laser is being added to and its beam originates from
     * @return a String that represents the result of what happened to be put on the topLabel in the GUI
     */
    public String addLaser(int row, int col, boolean ptui){
        ModelData data;
        if((safe[row][col].equals(".") || safe[row][col].equals("*"))){
            safe[row][col] = "L";
            if(!ptui){
                notifyObservers(new ModelData(row, col, safe[row][col]));
            }
            for(int c = col - 1; c >= 0; c--){
                if(safe[row][c].equals(".")){
                    safe[row][c] = "*";
                    if(!ptui){
                        data = new ModelData(row, c, safe[row][c]);
                        data.setLaserLR(true);
                        notifyObservers(data);
                    }
                }else{
                    break;
                }
            }
            for(int c = col + 1 ; c < cols; c++){
                if(safe[row][c].equals(".")){
                    safe[row][c] = "*";
                    if(!ptui){
                        data = new ModelData(row, c, safe[row][c]);
                        data.setLaserLR(true);
                        notifyObservers(data);
                    }
                }else{
                    break;
                }
            }
            for(int r = row - 1; r >= 0; r--){
                if(safe[r][col].equals(".")){
                    safe[r][col] = "*";
                    if(!ptui){
                        data = new ModelData(r, col, safe[r][col]);
                        data.setLaserUD(true);
                        notifyObservers(data);
                    }
                }else{
                    break;
                }
            }
            for(int r = row + 1; r < rows; r++){
                if(safe[r][col].equals(".")){
                    safe[r][col] = "*";
                    if(!ptui){
                        data = new ModelData(r, col, safe[r][col]);
                        data.setLaserUD(true);
                        notifyObservers(data);
                    }
                }else{
                    break;
                }
            }
        }
        else{
            return "Error adding laser at: (" + row + ", " + col + ")";
        }
        return "Laser added at: (" + row + ", " + col + ")";
    }

    /**
     * This method removes the laser at the spot specified if possible and removes the * beam, *'s are only removed
     * if there is no overlap between lasers. This also has a distinction of whether what it's being called from is a
     * PTUI or a GUI, since the PTUI has its own update that isn't compatible with the GUI's.
     *
     * @param row the row of the spot the laser will be removed from along with its beam
     * @param col the column of the spot the laser will be removed from along with its beam
     * @param ptui a boolean true if the object this method is called from is a PTUI, and false otherwise
     * @return a String that represents the result of what happened to be put on the topLabel in the GUI
     */
    public String removeLaser(int row, int col, boolean ptui){
        ModelData data;
        if(safe[row][col].equals("L")){
            if(notAnotherLaser(row, col, true)){
                safe[row][col] = ".";
                if(!ptui){
                    notifyObservers(new ModelData(row, col, safe[row][col]));
                }
            }
            for(int c = col - 1; c >= 0; c--){
                if(safe[row][c].equals("*")){
                    if(notAnotherLaser(row, c, true)){
                        safe[row][c] = ".";
                        if(!ptui){
                            data = new ModelData(row, c, safe[row][c]);
                            data.setLaserLR(false);
                            notifyObservers(data);
                        }
                    }
                }
            }
            for(int c = col + 1 ; c < cols; c++){
                if(safe[row][c].equals("*")){
                    if(notAnotherLaser(row, c, true)){
                        safe[row][c] = ".";
                        if(!ptui){
                            data = new ModelData(row, c, safe[row][c]);
                            data.setLaserLR(false);
                            notifyObservers(data);
                        }
                    }
                }
            }
            for(int r = row - 1; r >= 0; r--){
                if(safe[r][col].equals("*")){
                    if(notAnotherLaser(r, col, true)){
                        safe[r][col] = ".";
                        if(!ptui){
                            data = new ModelData(r, col, safe[r][col]);
                            data.setLaserUD(false);
                            notifyObservers(data);
                        }
                    }
                }
            }
            for(int r = row + 1; r < rows; r++){
                if(safe[r][col].equals("*")){
                    if(notAnotherLaser(r, col, true)){
                        safe[r][col] = ".";
                        if(!ptui){
                            data = new ModelData(r, col, safe[r][col]);
                            data.setLaserUD(false);
                            notifyObservers(data);
                        }
                    }
                }
            }
        }
        else{
            return "Error removing laser at: (" + row + ", " + col + ")";
        }
        return "Laser removed at: (" + row + ", " + col + ")";
    }

    /**
     * This method checks to make sure there is no other laser that is overlapping the * being removed. This also has a
     * distinction of whether what it's being called from is a PTUI or a GUI, since the PTUI has its own update that
     * isn't compatible with the GUI's.
     *
     * @param row the row of the spot of the * being checked
     * @param col the column of the spot of the * being checked
     * @param ptui a boolean true if the object this method is called from is a PTUI, and false otherwise
     * @return a boolean that is true if there is no other laser and false otherwise
     */
    public boolean notAnotherLaser(int row, int col, boolean ptui){
        ModelData data;
        int lasers = 0;
        for(int c = col - 1; c >= 0; c--){
            if(!(safe[row][c].equals(".")||safe[row][c].equals("*")||safe[row][c].equals("L"))){
                break;
            }else if(safe[row][c].equals("L")){
                lasers++;
                data = new ModelData(row, col, "*");
                if(!ptui){
                    data.setLaserLR(true);
                    notifyObservers(data);
                }
                safe[row][col] = "*";
            }
        }
        for(int c = col + 1 ; c < cols; c++){
            if(!(safe[row][c].equals(".")||safe[row][c].equals("*")||safe[row][c].equals("L"))){
                break;
            }else if(safe[row][c].equals("L")){
                lasers++;
                data = new ModelData(row, col, "*");
                if(!ptui){
                    data.setLaserLR(true);
                    notifyObservers(data);
                }
                safe[row][col] = "*";
            }
        }
        for(int r = row - 1; r >= 0; r--){
            if(!(safe[r][col].equals(".")||safe[r][col].equals("*")||safe[r][col].equals("L"))){
                break;
            }else if(safe[r][col].equals("L")){
                lasers++;
                data = new ModelData(row, col, "*");
                if(!ptui){
                    data.setLaserUD(true);
                    notifyObservers(data);
                }
                safe[row][col] = "*";
            }
        }
        for(int r = row + 1; r < rows; r++){
            if(!(safe[r][col].equals(".")||safe[r][col].equals("*")||safe[r][col].equals("L"))){
                break;
            }else if(safe[r][col].equals("L")){
                lasers++;
                data = new ModelData(row, col, "*");
                if(!ptui){
                    data.setLaserUD(true);
                    notifyObservers(data);
                }
                safe[row][col] = "*";
            }
        }
        return lasers == 0;

    }

    /**
     * This method checks to see if the safe puzzle has been solved and prints either a success message or a failure
     * message that prints the coordinates of the first mistake found. This also has a distinction of whether what
     * it's being called from is a PTUI or a GUI, since the PTUI has its own update that isn't compatible with the
     * GUI's.
     *
     * @param ptui a boolean true if the object this method is called from is a PTUI, and false otherwise
     * @return a String that represents the result of what happened to be put on the topLabel in the GUI
     */
    public String verify(boolean ptui){
        ModelData data;
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                if(!ptui){
                    notifyObservers(new ModelData(r, c, safe[r][c]));
                }
                if(safe[r][c].equals(".")){
                    return "Error verifying at: ("+r+", "+c+")";
                } else if(safe[r][c].equals("L")){
                    if(notAnotherLaser(r,c, false)){
                        continue;
                    } else {
                        if(!ptui){
                            data = new ModelData(r, c, safe[r][c]);
                            data.setError(true);
                            notifyObservers(data);
                        }
                        return "Error verifying at: ("+r+", "+c+")";
                    }
                } else if(safe[r][c].equals("*")||safe[r][c].equals("X")){
                    continue;
                } else {
                    int lasers = Integer.parseInt(safe[r][c]);
                    if (r!=0){
                        if (safe[r-1][c].equals("L")){
                            lasers--;
                        }
                    }
                    if (r!=rows-1){
                        if (safe[r+1][c].equals("L")){
                            lasers--;
                        }
                    }
                    if (c!=0){
                        if (safe[r][c-1].equals("L")){
                            lasers--;
                        }
                    }
                    if (c!=cols-1){
                        if (safe[r][c+1].equals("L")){
                            lasers--;
                        }
                    }

                    if (lasers!=0){
                        if(!ptui){
                            data = new ModelData(r, c, safe[r][c]);
                            data.setError(true);
                            notifyObservers(data);
                        }
                        return "Error verifying at: ("+r+", "+c+")";
                    }
                }
            }
        }
        return "Safe is fully verified!";
    }

    /**
     * This method redoes the entire safe after solve has been called in the GUI, as well as make it visible on the
     * GUI
     *
     * @param newSafe this is the safe received from the solver that has the solution to be written onto the GUI
     */
    public void redoSafeFromSolve(String[][] newSafe){
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                if(newSafe[row][col].equals("L")){
                    addLaser(row, col, false);
                }
                else{
                    notifyObservers(new ModelData(row, col, newSafe[row][col]));
                }
            }
        }
    }

    /**
     * An overridden toString to be able to print out what the safe looks like
     *
     * @return the safe turned into a String representation of itself
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("   ");
        int colCount = 0;
        for (int col = 0; col<this.cols; col++) {
            if(colCount >= 10){
                colCount = colCount % 10;
            }
            str.append(colCount).append(" ");
            colCount++;
        }
        str.append("\n");
        int rowCount = 0;
        for (int row = 0; row<this.rows; row++) {
            if(rowCount >= 10){
                rowCount = rowCount % 10;
            }
            str.append(rowCount).append("|").append(" ");
            for (int col = 0; col<this.cols; col++) {
                str.append(this.safe[row][col]).append(" ");
            }
            str.append("\n");
            rowCount++;
        }
        return str.toString();
    }
}
