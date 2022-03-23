package com.webcheckers.ui;

import static com.webcheckers.ui.PostGameRoute.CURRENTGAME_KEY;

import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.util.Color;
import com.webcheckers.util.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

/**
 * The UI controller to GET the game page.
 *
 * @author <a href='mailto:cng7819@rit.edu'>Chris Grenfell</a>
 */
public class GetGameRoute implements Route {
  enum ViewMode {
    PLAY,
    SPECTATOR,
    REPLAY
  }

  static final String CURRENTPLAYER_KEY = "dummy value";

  private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route to handle all {@code GET /game} requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetGameRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    LOG.config("GetGameRoute is initialized.");
  }

  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetGameRoute is invoked.");

    // Get the HTTP session.
    final Session httpSession = request.session();

    // Create the view-model map.
    Map<String, Object> vm = new HashMap<>();

    // Add current user to map.
    Player currentUser = httpSession.attribute(CURRENTPLAYER_KEY); //TODO: player class
    vm.put("currentUser", currentUser); // This is only complaining because it doesn't know about
                                        // Player yet, so it isn't sure Player is also an Object.

    // Add view mode to map.
    vm.put("viewMode", ViewMode.PLAY);

    Game currentGame = httpSession.attribute(CURRENTGAME_KEY);

    // Add red and white player to map.
    Player redPlayer = currentGame.getRedPlayer();
    Player whitePlayer = currentGame.getWhitePlayer();
    vm.put("redPlayer", redPlayer);
    vm.put("whitePlayer", whitePlayer);

    // Add gameboard to map.
    Board gameBoard = currentGame.getBoard();
    vm.put("board", gameBoard);

    // Add active player's color to map.
    Color activeColor = currentGame.getCurrentColor();
    vm.put("activeColor", activeColor);

    // Render the page.
    return templateEngine.render(new ModelAndView(vm, "game.ftl"));
  }

}
