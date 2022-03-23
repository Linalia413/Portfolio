package lasers.ptui;

import lasers.model.LasersModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author RIT CS
 * @author Jacob Harman
 * @author Sean Harmon
 */
public class ControllerPTUI  {
    /** The UI's connection to the lasers.lasers.model */
    private LasersModel model;

    private LasersPTUI ptui;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) throws FileNotFoundException {
        this.model = model;
        ptui = this.model.makePTUI();
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) {
        try {
            String input;
            String[] splt;
            System.out.println(this.toString());

            if (!inputFile.isBlank()) {
                Scanner inputfile = new Scanner(new File(inputFile));
                String[] filesplt;
                while (inputfile.hasNextLine()) {
                    String line = inputfile.nextLine();
                    System.out.println("> " + line);
                    filesplt = line.split(" ");
                    ptui.interpret(line, filesplt);
                }
            }

            System.out.println("a|add r c: Add laser to (r,c)\nd|display: Display safe\n" +
                    "h|help: Print this help message\nq|quit: Exit program\n" +
                    "r|remove r c: Remove laser from (r,c)\n" + "v|verify: Verify safe correctness");
            Scanner in = new Scanner(System.in);
            System.out.print("> ");
            input = in.nextLine();
            splt = input.split(" ");
            while (!splt[0].equals("q") && !splt[0].equals("quit")) {
                ptui.interpret(input, splt);
                System.out.print("> ");
                input = in.nextLine();
                splt = input.split(" ");
            }
        } catch(FileNotFoundException e){
            System.out.println("File cannot be found");
        }
    }
}
