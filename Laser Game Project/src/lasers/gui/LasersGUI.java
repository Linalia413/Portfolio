package lasers.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import lasers.backtracking.Backtracker;
import lasers.backtracking.Configuration;
import lasers.backtracking.SafeConfig;
import lasers.model.*;

/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the lasers.lasers.model
 * and receives updates from it.
 *
 * @author RIT CS
 * @author Jacob Harman
 * @author Sean Harmon
 */
public class LasersGUI extends Application implements Observer<LasersModel, ModelData> {

    /**
     * This board of gridButtons is created to know what is at each spot according to the safe given in the PTUI,
     * [][] is [row][col]
     */
    private GridButton[][] board;

    /** The UI's connection to the lasers.lasers.model */
    private LasersModel model;

    /** this can be removed - it is used to demonstrates the button toggle */
    private static boolean status = true;

    /**
     * This is the label that tells the user what's going after events happen
     */
    private Label topLabel;

    /**
     * This is the file name given for the safe file given in the command line arguments or loaded in from the load
     * button
     */
    private String fileName;

    /**
     * This is the shortened file name given for the safe file given in the command line arguments or loaded in from
     * the load button
     */
    private String shorterFileName;

    /**
     * This is the config created by the given file of the safe for use by the solver
     */
    private SafeConfig config;

    /**
     * This is the BackTracker to allow the use of the solvers
     */
    private Backtracker back;

    /**
     * This enum gives all the file paths for the images being used for use with Image creation:
     * new Image(getClass().getResourceAsStream([insert file path here]));
     */
    private enum ImageFilePaths {
        TRAVOLTA("/lasers/gui/resources/confused-travolta(for-blank-space).gif"),
        LASERLR("/lasers/gui/resources/laser-left-right.gif"),
        LASERUD("/lasers/gui/resources/laser-up-down.gif"),
        STORMSHOOTING("/lasers/gui/resources/stormtrooper-shooting.gif"),
        STORMSHOT("/lasers/gui/resources/stormtrooper-shot.gif");

        private String filePath;

        public String getFilePath() {
            return filePath;
        }

        ImageFilePaths(String flPath){
            filePath = flPath;
        }
    }

    @Override
    public void init(){
        try {
            Parameters params = getParameters();
            fileName = params.getRaw().get(0);
            shorterFileName = fileName.substring(6);
            this.model = new LasersModel(fileName);
            this.config = new SafeConfig(fileName);
            this.board = new GridButton[model.getRows()][model.getCols()];
            this.back = new Backtracker(false);
            this.topLabel = new Label(shorterFileName + " successfully loaded");
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    /**
     * This method takes the String val that has a number between 0-4 or an X and gets the pillar image for that
     * value. There is also a distinction with if it's "destroyed" or not, a pillar is destroyed if an error comes
     * up on it from verify.
     *
     * @param val this is a String that is either a number from 0-4 or an X that is taken from the PTUI
     * @param destroyed if a pillar is "destroyed" then an error occurred on it in verify, param is true if this is so
     *                  and false otherwise
     * @return an Image file type to be used to change the image on the PokemonButtons
     */
    public Image getPillarImage(String val, boolean destroyed){
        Image image;
        if(destroyed){
            image = new Image(getClass().getResourceAsStream(
                    "/lasers/gui/resources/destroyed-pillar-" + val + ".png"));
        }
        else{
            image = new Image(getClass().getResourceAsStream(
                    "/lasers/gui/resources/new-pillar-" + val + ".png"));
        }
        return image;
    }

    /**
     * This class, which extends the Button class, creates a special button that knows the row and column it's in on
     * the board.
     */
    private class GridButton extends Button {
        private int row;
        private int col;
        private String val;
        public GridButton(int row, int col) {
            this.row = row;
            this.col = col;
            this.val = model.getValueAtSpot(this.row, this.col);
            Image image;
            if(val.equals(".")){
                image = new Image(getClass().getResourceAsStream(ImageFilePaths.TRAVOLTA.getFilePath()));
            }
            else{
                image = getPillarImage(val, false);
            }
            this.setGraphic(new ImageView(image));
            setButtonBackground(this, "white.png");
        }

        /**
         * This method ensures that the value most currently at the spot is taken for use
         */
        public String getVal() {
            return model.getValueAtSpot(this.row, this.col);
        }
    }

    /**
     * This method creates a GridPane for the scene, as well as creates a board of the buttons to keep track of them
     * and sets the event that happens when the button is pressed, if anything.
     *
     * @return the GridPane that will be used in the scene
     */
    private GridPane makeGridPane(){
        GridPane gridPane = new GridPane();
        for (int row=0; row<model.getRows(); ++row) {
            for (int col=0; col<model.getCols(); ++col) {
                GridButton button = new GridButton(row, col);
                this.board[row][col] = button;
                button.setOnAction(event -> {
                    if(button.getVal().equals(".") || button.getVal().equals("*")){
                        topLabel.setText(model.addLaser(button.row, button.col, false));
                    }
                    else if(button.getVal().equals("L")){
                        topLabel.setText(model.removeLaser(button.row, button.col, false));
                    }
                });
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    /**
     * This method creates a HBox for the scene, which consists of five buttons, as well as define the events that
     * happen when those buttons are clicked.
     *
     * @return the HBox that will be used in the scene
     */
    private HBox makeHBox(Stage stage){
        HBox hBox;
        Button check = new Button("Check");
        check.setOnAction(event -> {
            topLabel.setText(model.verify(false));
        });
        Button hint = new Button("Hint");
        hint.setOnAction(event -> {
            topLabel.setText("This is not functional");
        });
        Button solve = new Button("Solve");
        solve.setOnAction(event -> {
            Optional<Configuration> solution = back.solve(config);
            if (solution.isPresent()) {
                SafeConfig sol = (SafeConfig) solution.get();
                model.redoSafeFromSolve(sol.getSafe());
                topLabel.setText(shorterFileName + " solved");
            } else {
                topLabel.setText("No Solution!");
            }
        });
        Button restart = new Button("Restart");
        restart.setOnAction(event -> {
            for (int row=0; row<model.getRows(); ++row) {
                for (int col = 0; col < model.getCols(); ++col) {
                    if(model.getValueAtSpot(row, col).equals("*") || model.getValueAtSpot(row, col).equals("L")){
                        model.setValueAtSpot(row, col, ".");
                        topLabel.setText(shorterFileName + " has been reset");
                    }
                    else if(!model.getValueAtSpot(row, col).equals(".")){
                        Image image = getPillarImage(model.getValueAtSpot(row, col), false);
                        board[row][col].setGraphic(new ImageView(image));
                        setButtonBackground(board[row][col], "white.png");
                    }
                }
            }
        });
        Button load = new Button("Load");
        load.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
            fileChooser.setTitle("Open Safe File");
            File file = fileChooser.showOpenDialog(stage);
            if(file != null){
                fileName = file.getPath();
                shorterFileName = file.getName();
                try {
                    model.buildSafe(fileName);
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                    System.exit(-1);
                }
                this.board = new GridButton[model.getRows()][model.getCols()];
                init(stage);
                topLabel.setText(shorterFileName + " successfully loaded");
            }
        });
        hBox = new HBox(check, hint, solve, restart, load);
        return hBox;
    }

    /**
     * The initialization of all GUI component happens here.
     *
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage) {
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = makeGridPane();
        HBox hBox = makeHBox(stage);
        borderPane.setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setTop(this.topLabel);
        BorderPane.setAlignment(topLabel, Pos.CENTER);
        borderPane.setBottom(hBox);
        hBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(borderPane);
        stage.setTitle("ConcentrationGUI");
        stage.setScene(scene);
    }

    /**
     * This overridden method simply calls the second initialization then shows the stage
     *
     * @param stage the stage for which everything is to be put on and seen by the user
     */
    @Override
    public void start(Stage stage) {
        init(stage);
        stage.show();
    }

    /**
     * This method handles all the changes done to the GUI that the user can see. This is mainly changes to the board
     * of buttons.
     *
     * @param data this is the data received from the update method
     */
    private void refresh(ModelData data){
        Image image;
        switch (data.getVal()) {
            case "*":
                if (data.isLaserLR()) {
                    image = new Image(getClass().getResourceAsStream(ImageFilePaths.LASERLR.getFilePath()));
                    board[data.getRow()][data.getCol()].setGraphic(new ImageView(image));
                    setButtonBackground(board[data.getRow()][data.getCol()], "white.png");
                } else if (data.isLaserUD()) {
                    image = new Image(getClass().getResourceAsStream(ImageFilePaths.LASERUD.getFilePath()));
                    board[data.getRow()][data.getCol()].setGraphic(new ImageView(image));
                    setButtonBackground(board[data.getRow()][data.getCol()], "white.png");
                }
                break;
            case "L":
                if(data.inError()){
                    image = new Image(getClass().getResourceAsStream(ImageFilePaths.STORMSHOT.getFilePath()));
                }
                else{
                    image = new Image(getClass().getResourceAsStream(ImageFilePaths.STORMSHOOTING.getFilePath()));
                }
                board[data.getRow()][data.getCol()].setGraphic(new ImageView(image));
                data.setError(false);
                break;
            case ".":
                image = new Image(getClass().getResourceAsStream(ImageFilePaths.TRAVOLTA.getFilePath()));
                board[data.getRow()][data.getCol()].setGraphic(new ImageView(image));
                if(data.inError()){
                    setButtonBackground(board[data.getRow()][data.getCol()], "red.png");
                }
                else{
                    setButtonBackground(board[data.getRow()][data.getCol()], "white.png");
                }
                data.setError(false);
                break;
            default:
                if(data.inError()){
                    image = getPillarImage(data.getVal(), true);
                }
                else{
                    image = getPillarImage(data.getVal(), false);
                }
                board[data.getRow()][data.getCol()].setGraphic(new ImageView(image));
                if(data.inError()){
                    setButtonBackground(board[data.getRow()][data.getCol()], "red.png");
                }
                else{
                    setButtonBackground(board[data.getRow()][data.getCol()], "white.png");
                }
                data.setError(false);
                break;
        }
    }

    /**
     * This overridden method makes sure everything stops properly when the user closes the window
     */
    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * This overridden method simply sends the data given to it to the refresh method in a Platform.runLater buffer
     *
     * @param model the model from which information can be taken from
     * @param data the data that will be used to assess any changes needed to the GUI in the refresh method
     */
    @Override
    public void update(LasersModel model, ModelData data) {
        Platform.runLater(() -> this.refresh(data));
    }
}
