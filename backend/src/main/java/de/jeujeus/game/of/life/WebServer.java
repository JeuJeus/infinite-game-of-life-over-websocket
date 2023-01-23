package de.jeujeus.game.of.life;

import de.jeujeus.game.of.life.websocket.ConnectionEndpoint;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;

import java.net.URL;
import java.util.Objects;


public class WebServer {
    private final Server server;
    private final ServerConnector connector;

    @SneakyThrows
    public WebServer() {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");

        JavaxWebSocketServletContainerInitializer
                .configure(contextHandler, (servletContext, wsContainer) -> {
                    wsContainer.setDefaultMaxTextMessageBufferSize(65535);
                    wsContainer.addEndpoint(ConnectionEndpoint.class);
                });

        ResourceHandler resourceHandler = new ResourceHandler();
        String webAppDir =
                Objects.requireNonNull(Main.class.getClassLoader()
                                .getResource("META-INF/resources"))
                        .toURI()
                        .toString();
        resourceHandler.setResourceBase(webAppDir);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setDirectoriesListed(false);


        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, contextHandler, new DefaultHandler()});
        server.setHandler(handlers);

    }

    public void setPort(int port) {
        connector.setPort(port);
    }

    public void start() throws Exception {
        server.start();
    }

    public void join() throws InterruptedException {
        server.join();
    }
}
