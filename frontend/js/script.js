const webSocket = new WebSocket('ws://localhost:8080/gameOfLife');
webSocket.binaryType = 'arraybuffer';

let messageDiv, canvas, ctx;
addEventListener('DOMContentLoaded', () => {
    messageDiv = document.querySelector('#game');
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext("2d");
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

webSocket.onopen = () => {
    let cellList = generateRandomField(90,90);
    let gameState = JSON.stringify({
        'field':
            cellList

    });
    webSocket.send(gameState);
}

const drawCellOnCanvas = c => {
    ctx.beginPath();
    ctx.fillRect(c.xCoordinate * 10, c.yCoordinate * 10, 10, 10);
    ctx.fill();
    ctx.closePath();
};

const drawField = field => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    field.forEach(c => drawCellOnCanvas(c));
};

webSocket.onmessage = (event) => {
    console.timeEnd('generationCalculationDurationWas');

    const rawGameState = event.data;
    const gameState = JSON.parse(rawGameState);
    const field = gameState.field;
    if(!field) return;

    console.log(field.length);
    drawField(field);

    webSocket.send(rawGameState);

    console.time('generationCalculationDurationWas');
};