package lasers.backtracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the lasers.model
 * package and/or incorporate it into another class.
 *
 * @author RIT CS
 * @author Jacob Harman
 * @author Sean Harmon
 */
public class SafeConfig implements Configuration {
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
     * array of the current coordinates in the safe, current[0]=row current[1]=column
     */

    private int[] current=new int[2];

    /**
     * boolean array of past checked spaces
     */

    private boolean[][] checked;

    /**
     * list of all steps up to the goal
     */

    private List<Configuration> lasers;

    /**
     * Constructor for the safe from outside the file, gets called once to make the main parent safe
     *
     * @param filename = file to be read from to make the safe
     */

    public SafeConfig(String filename) {
        try {
            Scanner in = new Scanner(new File(filename));
            String[] splt;
            splt = in.nextLine().split(" ");
            rows = Integer.parseInt(splt[0]);
            cols = Integer.parseInt(splt[1]);
            this.safe = new String[rows][cols];
            this.checked = new boolean[rows][cols];
            int rowCounter = 0;
            int colCounter = 0;
            this.lasers= new LinkedList<>();
            while (in.hasNextLine() && !(rowCounter >= rows)) {
                splt = in.nextLine().split(" ");
                for(String s : splt){
                    safe[rowCounter][colCounter] = s;
                    colCounter++;
                }
                colCounter = 0;
                rowCounter++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File cannot be found");
        }
    }

    /**
     * duplicator constructor for the successors from within the class
     *
     * @param other = SafeConfig to be duplicated
     */

    private SafeConfig(SafeConfig other){
        this.cols=other.cols;
        this.rows=other.rows;
        this.current=other.current;
        this.safe= new String[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.safe[r][c]=other.safe[r][c];
            }
        }
        this.checked= new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.checked[r][c]=other.checked[r][c];
            }
        }
        this.lasers=new LinkedList<>(other.lasers);

    }

    /**
     * This method returns the safe contained in the config that was written to in the solvers.
     *
     * @return the safe
     */
    public String[][] getSafe() {
        return safe;
    }

    /**
     * checks all pillars have the correct number of lasers adjacent to them
     *
     * @return true if all are correct, false otherwise
     */

    private boolean checkAllPillars(){
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (safe[r][c].equals("1")||safe[r][c].equals("2")||safe[r][c].equals("3")||safe[r][c].equals("4")){
                    if (!checkPillarFull(r,c)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * creates the successors for the backtracking algorithm to run though
     *
     * @return collection of successors to be ran through in the backtracker
     */
    @Override
    public Collection<Configuration> getSuccessors() {
        outerloop:
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (this.safe[r][c].equals(".")&& !this.checked[r][c]){
                    this.current[0]=r;
                    this.current[1]=c;
                    this.checked[r][c]=true;
                    break outerloop;
                }
            }
        }




        Collection<Configuration> successors = new LinkedList<>();

        SafeConfig successor2 = new SafeConfig(this);
        successor2.safe[current[0]][current[1]]="L";
        successor2.safe[current[0]][current[1]] = "L";
        int[] laserPlacement=new int[2];

        for(int c = current[1] - 1; c >= 0; c--){
            if(successor2.safe[current[0]][c].equals(".")||successor2.safe[current[0]][c].equals("*")){
                successor2.safe[current[0]][c] = "*";
            }else{
                break;
            }
        }
        for(int c = current[1] + 1 ; c < cols; c++){
            if(successor2.safe[current[0]][c].equals(".")||successor2.safe[current[0]][c].equals("*")){
                successor2.safe[current[0]][c] = "*";
            }else{
                break;
            }
        }
        for(int r = current[0] - 1; r >= 0; r--){
            if(successor2.safe[r][current[1]].equals(".")||successor2.safe[r][current[1]].equals("*")){
                successor2.safe[r][current[1]] = "*";
            }else{
                break;
            }
        }
        for(int r = current[0] + 1; r < rows; r++) {
            if (successor2.safe[r][current[1]].equals(".")||successor2.safe[r][current[1]].equals("*")) {
                successor2.safe[r][current[1]] = "*";
            } else {
                break;
            }
        }
        System.arraycopy(current, 0, laserPlacement, 0, 2);
        SafeConfig successor2lasers = new SafeConfig(successor2);
        successor2.lasers.add(successor2lasers);
        successors.add(successor2);

        SafeConfig successor1 = new SafeConfig(this);
        successor1.safe[current[0]][current[1]]=".";
        successors.add(successor1);

        return successors;
    }

    /**
     * checks if a single pillar is completely full
     * @param row = row coordinate
     * @param col= column coordinate
     * @return true if pillar is full
     */

    private boolean checkPillarFull(int row, int col){
        int hasLaser = 0;
        if (row>0){
            if (safe[row-1][col].equals("L")){
                hasLaser++;
            }
        }
        if (row<rows-1){
            if (safe[row+1][col].equals("L")){
                hasLaser++;
            }
        }
        if (col>0){
            if (safe[row][col-1].equals("L")){
                hasLaser++;
            }
        }
        if (col<cols-1){
            if (safe[row][col+1].equals("L")){
                hasLaser++;
            }
        }
        return hasLaser==Integer.parseInt(safe[row][col]);
    }


    /**
     * checks if pillar can hold that laser
     * @param row = row coordinate of pillar
     * @param col = column coordinate of pillar
     * @return true if pillar is not over capacity
     */

    private boolean checkPillar(int row, int col){
        int hasLaser = 0;
        if (row>0){
            if (safe[row-1][col].equals("L")){
                hasLaser++;
            }
        }
        if (row<rows-1){
            if (safe[row+1][col].equals("L")){
                hasLaser++;
            }
        }
        if (col>0){
            if (safe[row][col-1].equals("L")){
                hasLaser++;
            }
        }
        if (col<cols-1){
            if (safe[row][col+1].equals("L")){
                hasLaser++;
            }
        }
        return hasLaser<=Integer.parseInt(safe[row][col]);
    }


    /**
     * checks to make sure the called successor is valid and doesn't disobey the rules of the safe
     *
     * @return true if placement is a valid placement
     */

    @Override
    public boolean isValid() {
        boolean stillUnchecked=false;
        int numDots=0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (safe[r][c].equals(".")){
                    numDots++;
                }
                if (safe[r][c].equals(".")&&!checked[r][c]){
                    stillUnchecked=true;
                }
            }
        }

        if (!stillUnchecked &&numDots!=0){
            return false;
        }
        if (!checkAllPillars()&&numDots==0) {
            return false;
        }


        if (safe[current[0]][current[1]].equals(".")) {
            return true;
        } else if (safe[current[0]][current[1]].equals("L")){
            if (current[0]!=0){
                if (!(safe[current[0]-1][current[1]].equals("X")||safe[current[0]-1][current[1]].equals("*"))){
                    if(!(checkPillar(current[0]-1, current[1]))){
                        return false;
                    }
                }
            }
            if (current[0]!=rows-1){
                if (!(safe[current[0]+1][current[1]].equals("X")||safe[current[0]+1][current[1]].equals("*"))){
                    if(!(checkPillar(current[0]+1, current[1]))){
                        return false;
                    }
                }
            }
            if (current[1]!=0){
                if (!(safe[current[0]][current[1]-1].equals("X")||safe[current[0]][current[1]-1].equals("*"))){
                    if(!(checkPillar(current[0], current[1]-1))){
                        return false;
                    }
                }
            }
            if (current[1]!=cols-1){
                if (!(safe[current[0]][current[1]+1].equals("X")||safe[current[0]][current[1]+1].equals("*"))){
                    if(!(checkPillar(current[0], current[1]+1))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * checks to see if safe is the goal safe
     *
     * @return true if every space on the safe is valid and complete
     */

    @Override
    public boolean isGoal() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (safe[r][c].equals(".")){
                    return false;
                } else if (safe[r][c].equals("1")||safe[r][c].equals("2")||safe[r][c].equals("3")||safe[r][c].equals("4")){
                    if (!checkPillarFull(r,c)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * returns the path to get to the current laser configuration
     *
     * @return List of configurations leading to current state
     */

    @Override
    public List<Configuration> getPath(){
        return this.lasers;
    }


    /**
     * prints safe out in readable format
     *
     * @return String format of safe
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
