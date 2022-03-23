package lasers.ptui;

import java.io.FileNotFoundException;

import lasers.model.LasersModel;
import lasers.model.ModelData;
import lasers.model.Observer;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author Jacob Harman
 * @author Sean Harmon
 */
public class LasersPTUI implements Observer<LasersModel, ModelData> {
    /**
     * The UI's connection to the model
     */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the LasersModel and initialize the view.
     *
     * @param filename the safe file name
     */
    public LasersPTUI(String filename) {
        try {
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * Accessor for the model the PTUI create.
     *
     * @return the model
     */
    public LasersModel getModel() { return this.model; }

    /**
     * This method interprets what's inputted by the user and does the action appropriate to the input.
     *
     * @param input this is the plain input from the user
     * @param splt  this is the input received from the user from the main method split apart into an array for easier
     *              usage
     */
    public void interpret(String input, String[] splt) {
        if (splt[0].equals("h") || splt[0].equals("help")) {
            System.out.println("a|add r c: Add laser to (r,c)\nd|display: Display safe\n" +
                    "h|help: Print this help message\nq|quit: Exit program\nr|remove r c: Remove laser from (r,c)\n" +
                    "v|verify: Verify safe correctness");
        } else {
            if (splt[0].equals("d") || splt[0].equals("display")) {
                update(model, null);
            } else {
                if (splt[0].equals("a") || splt[0].equals("add")) {
                    if (splt.length != 3) {
                        System.out.println("Incorrect coordinates (too many or too little)");
                    } else {
                        try {
                            int row;
                            int col;
                            row = Integer.parseInt(splt[1]);
                            col = Integer.parseInt(splt[2]);
                            if(!(row < 0) && !(col < 0) && !(row>model.getRows()-1) && !(col>model.getCols()-1)){
                                System.out.println(model.addLaser(row, col, true));
                                update(model, new ModelData(row, col, model.getValueAtSpot(row, col)));
                            }
                            else{
                                System.out.println("Error adding laser at: (" + row + ", " + col + ")");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Incorrect coordinates (parsable number(s) not given)");
                        }
                    }
                } else {
                    if (splt[0].equals("r") || splt[0].equals("remove")) {
                        if (splt.length != 3) {
                            System.out.println("Incorrect coordinates (too many or too little)");
                        } else {
                            try {
                                int row;
                                int col;
                                row = Integer.parseInt(splt[1]);
                                col = Integer.parseInt(splt[2]);
                                if(!(row < 0) && !(col < 0) && !(row>model.getRows()-1) && !(col>model.getCols()-1)){
                                    System.out.println(model.removeLaser(row, col, true));
                                    update(model, new ModelData(row, col, model.getValueAtSpot(row, col)));
                                }
                                else{
                                    System.out.println("Error adding laser at: (" + row + ", " + col + ")");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Incorrect coordinates (parsable number(s) not given)");
                            }
                        }
                    }
                    else{
                        if (splt[0].equals("v") || splt[0].equals("verify")) {
                            System.out.println(model.verify(true));
                            update(model, null);
                        }
                        else{
                            System.out.println("Unrecognized command: " + input);
                        }
                    }
                }
            }
        }
    }

    /**
     * This overridden method simply prints out the board for the PTUI
     *
     * @param model the model from which information can be taken from
     * @param data the data that will be used to assess any changes (not used)
     */
    @Override
    public void update(LasersModel model, ModelData data) {
        System.out.println(model.toString());
    }
}
