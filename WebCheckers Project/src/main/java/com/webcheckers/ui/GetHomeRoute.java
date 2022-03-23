package com.webcheckers.ui;

import static com.webcheckers.ui.PostGameRoute.CURRENTGAME_KEY;
import static spark.Spark.halt;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final TemplateEngine templateEngine;
  private final GameCenter gameCenter;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine, final GameCenter gameCenter) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.gameCenter = gameCenter;
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");
    //
    // First we check if this user is part of an existing Game.
    // If so, we add that Game to their http session and send them to GetGameRoute.
    // We also set their status to be in game.
    Player currentUser = request.session().attribute("currentUser");
    if (currentUser == null) {
      Game currentGame = gameCenter.getGameByPlayer(currentUser);
      if (currentGame != null) {
        if (!currentUser.isInGame()) currentUser.changeGameState(); // Do we want this to be a flip-flop?
        request.session().attribute(CURRENTGAME_KEY, currentGame);
        response.redirect(WebServer.GAME_URL);
        halt();
        return null;
      }
    }

    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);

    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
