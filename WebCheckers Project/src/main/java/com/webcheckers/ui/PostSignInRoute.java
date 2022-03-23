package com.webcheckers.ui;

import com.webcheckers.util.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;


/**
 * The {@code POST /guess} route handler.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:jrv@se.rit.edu'>Jim Vallino</a>
 */
public class PostSignInRoute implements Route {
    static final String NAME_KEY = "nameKey";
    private TemplateEngine templateEngine;

    public PostSignInRoute(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String handle(Request request, Response response) {
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();

        final String name = request.queryParams("name");

        //TODO: Remove test message
        System.out.println(name);

        Player player = new Player(name);
        response.redirect(WebServer.HOME_URL);

        return null;
    }


}

