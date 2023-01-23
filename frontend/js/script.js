<!-- TODO before even starting to work on the -> refactor -->
const webSocket = new WebSocket('ws://localhost:8080/gameOfLife');
webSocket.binaryType = 'arraybuffer';

let messageDiv, canvas, ctx;
addEventListener('DOMContentLoaded', () => {
    messageDiv = document.querySelector('#game');
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext("2d");
});


webSocket.onopen = () => {
    let cellList = [];
    for (let i = 0; i < 90; i++) {
        for (let j = 0; j < 90; j++) {
            cellList.push({
                'isAlive': 0===Math.floor(Math.random() * 2),
                'xCoordinate': i,
                'yCoordinate': j
            });
        }
    }
    let s = JSON.stringify({
        'field':
            cellList

    });
    webSocket.send(s);
}

webSocket.onmessage = (event) => {
    const data = JSON.parse(event.data);
    console.log(data.field);

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    if(!data.field) return;

    data.field.forEach(c => {
        ctx.beginPath();
        ctx.fillRect(c.xCoordinate * 10, c.yCoordinate * 10, 10, 10);
        ctx.fill();
        ctx.closePath();

    });

    setTimeout(() => webSocket.send(event.data));
};