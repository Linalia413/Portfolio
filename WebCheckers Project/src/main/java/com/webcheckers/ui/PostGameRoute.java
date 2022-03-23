package com.webcheckers.ui;

import static com.webcheckers.ui.GetGameRoute.CURRENTPLAYER_KEY;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.util.*;
import com.webcheckers.model.Game;
import java.util.Objects;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

/**
 * The Spark Route that handles a user clicking on an available user's name in the home page
 * to start a game. Initializes the Game object and adds it to the user's HTTP session, and to the
 * GameCenter's game list.
 *
 * @author <a href='mailto:cng7819@rit.edu'>Chris Grenfell</a>
 */
public class PostGameRoute implements Route {

  static final String CURRENTGAME_KEY = "currentGame";

  public static final Logger LOG = Logger.getLogger(PostGameRoute.class.getName());

  private final TemplateEngine templateEngine;
  private final GameCenter gameCenter;

  /**
   * Create the Spark Route to handle all {@code POST /game} requests, to set up a game state
   * with the selected player.
   *
   * @param templateEngine
   *    the HTML template rendering engine
   */
  public PostGameRoute(final TemplateEngine templateEngine, final GameCenter gameCenter) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.gameCenter = gameCenter;
    LOG.config("PostGameRoute is initialized.");
  }

  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("PostGameRoute is invoked.");

    final Session httpSession = request.session();
    final String otherPlayerName = request.queryParams("playerName");

    Player redPlayer = httpSession.attribute(CURRENTPLAYER_KEY);
    Player whitePlayer = gameCenter.getPlayerByName(otherPlayerName);

    Game newGame = new Game(redPlayer, whitePlayer);
    httpSession.attribute(CURRENTGAME_KEY, newGame);
    gameCenter.addGame(newGame);

    return null;

  }

}
