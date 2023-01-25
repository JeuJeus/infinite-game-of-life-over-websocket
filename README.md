# infinite-game-of-life-over-websocket

This is a Functional-Programming Implementation of John Conways "Game of Life" written in Java.
The Frontend is written in plain HTML/CSS/JS and communicates to the Backend, holding all Logic, via Websockets.
Beneath Guava, Jetty was used for the Webserver and Websocket Implementation.

## HowToRun
- Either Open the Project in an IDE of your Choice, start the Backends Main Class and open the Frontends index.html in your browser
- or use the Backends Mavenwrapper to build a executbale jar, run that in the target folder with ```java -jar backend-1.0-SNAPSHOT.jar``` and point your browser to _localhost:8080_

## Constraints used during Development
- Functional Programming
- TDD
- Libraries no Frameworks
- Implement with infinite Grid

## Possible Improvements
- Further Memory-Optimizations
- Runtime Optimization, calculating only relevant Cells is Alive-Status not for every Cell

### Game of Life Rules
- Any live cell with two or three live neighbours survives.
- Any dead cell with three live neighbours becomes a live cell.
- All other live cells die in the next generation. Similarly, all other dead cells stay dead.
