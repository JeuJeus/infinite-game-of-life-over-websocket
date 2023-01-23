package de.jeujeus.game.of.life;

public class Main {
    public static void main(String[] args) throws Exception {
        //TODO introduce root pom + copy step for frontend + serve here

        WebServer server = new WebServer();
        server.setPort(8080);
        server.start();
        server.join();
    }


}