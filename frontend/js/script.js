const webSocket = new WebSocket('ws://localhost:8080/gameOfLife');
webSocket.binaryType = 'arraybuffer';

let messageDiv, canvas, ctx;
const AmountOfCellsOnXAxis = 90;
const AmountOfCellsOnYAxis = 90;
const pixelSize = 10;

let currentGeneration = 0;

const resizeCanvas = () => {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
};

addEventListener('DOMContentLoaded', () => {
    messageDiv = document.querySelector('#game');
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext("2d");

    resizeCanvas();
    window.addEventListener('resize', () => resizeCanvas())
});

const getAliveOrDeadRandom = () => 0 === Math.floor(Math.random() * 2);

const generateRandomField = (xSize, ySize) => {
    let cellList = [];
    for (let i = 0; i < xSize; i++) {
        for (let j = 0; j < ySize; j++) {
            cellList.push({
                'isAlive': getAliveOrDeadRandom(),
                'xCoordinate': i,
                'yCoordinate': j
            });
        }
    }
    return cellList;
};

const generateWebsocketMessage = cellList => JSON.stringify({
    'field': cellList
});

const drawCellOnCanvas = c => {
    ctx.beginPath();
    ctx.fillStyle = "black";
    ctx.fillRect(c.xCoordinate * pixelSize, c.yCoordinate * pixelSize, pixelSize, pixelSize);
    ctx.fill();
    ctx.closePath();
};


const drawCurrentGeneration = () => {
    ctx.beginPath();
    ++currentGeneration;
    ctx.font = "40px monospace";
    ctx.fillStyle = "red";
    ctx.textAlign = "top";
    ctx.fillText(currentGeneration, 0, 30);
    ctx.closePath();
};

const drawField = field => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    field.filter(c => c.isAlive).forEach(c => drawCellOnCanvas(c));
    drawCurrentGeneration();
};

webSocket.onopen = () => {
    let cellList = generateRandomField(AmountOfCellsOnXAxis, AmountOfCellsOnYAxis);
    let gameState = generateWebsocketMessage(cellList);
    webSocket.send(gameState);
}

webSocket.onmessage = (event) => {
    console.timeEnd('generationCalculationDurationWas');

    const rawGameState = event.data;
    const gameState = JSON.parse(rawGameState);
    const field = gameState.field;
    if (!field) return;

    console.log(field.length);
    drawField(field);

    webSocket.send(rawGameState);

    console.time('generationCalculationDurationWas');
};