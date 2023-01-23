# infinite-game-of-life-over-websocket

This is a Functional-Programming Implementation of John Conways "Game of Life" written in Java.
The Frontend is written in plain HTML/CSS/JS and communicates to the Backend, holding all Logic, via Websockets.
Beneath Guava, Jetty was used for the Webserver and Websocket Implementation.

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
