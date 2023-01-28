const webSocket = new WebSocket('ws://localhost:8080/gameOfLife');
webSocket.binaryType = 'arraybuffer';

let canvasParent, canvas, ctx;
const AmountOfCellsOnXAxis = 90;
const AmountOfCellsOnYAxis = 90;
const pixelSize = 10;

let currentGeneration = 0;

let canvasClicked = false;
let lastX = 0;
let lastY = 0;

const makeCursor = cursor => document.body.style.cursor = cursor;

const fitCanvasSize = () => {
        canvas.style.width = '100%';
        canvas.style.height='100%';
        canvas.width  = canvas.offsetWidth;
        canvas.height = canvas.offsetHeight;
};

const handleMouseMoveOverCanvas = event => {
    event.preventDefault();
    if (!canvasClicked) return;

    const x = event.clientX;
    const y = event.clientY;

    canvasParent.scrollLeft = lastX - x;
    canvasParent.scrollTop = lastY - y;

    lastX = x;
    lastY = y;
};

const onCanvasClickRelease = () => {
    canvasClicked = false;
    makeCursor('pointer');
};

const onCanvasClick = () => {
    canvasClicked = true;
    makeCursor('grab');
};

addEventListener('DOMContentLoaded', () => {
    canvasParent = document.querySelector(".game-of-live-canvas");
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext("2d");

    makeCursor('pointer');
    fitCanvasSize();

    window.addEventListener('resize', () => fitCanvasSize());
    canvas.addEventListener('mousedown', () => onCanvasClick());
    canvas.addEventListener('mouseup', () => onCanvasClickRelease());
    canvas.addEventListener('mousemove', (event) => handleMouseMoveOverCanvas(event));
});

const getAliveOrDeadRandom = () => 0 === Math.floor(Math.random() * 2);

const generateRandomField = (xSize, ySize) => {
    return [...Array(xSize).keys()].flatMap(x =>
        [...Array(ySize).keys()].flatMap(y => ({
            isAlive: getAliveOrDeadRandom(),
            xCoordinate: x,
            yCoordinate: y
        })));
};
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


const generateWebsocketMessage = cellList => JSON.stringify({
    'field': cellList
});

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