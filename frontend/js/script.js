<!-- TODO before even starting to work on the -> refactor -->
const webSocket = new WebSocket('ws://localhost:8080/gameOfLife');
webSocket.binaryType = 'arraybuffer';

let messageDiv, canvas, ctx;
addEventListener('DOMContentLoaded', () => {
    messageDiv = document.querySelector('#game');
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext("2d");
});

let s = JSON.stringify({
    'field': [
        {
            'isAlive': true,
            'xCoordinate': 0,
            'yCoordinate': 0
        }, {
            'isAlive': true,
            'xCoordinate': 0,
            'yCoordinate': 1
        }, {
            'isAlive': true,
            'xCoordinate': 0,
            'yCoordinate': 2
        }
    ]
});
webSocket.onopen = () => {
    webSocket.send(s);
}

webSocket.onmessage = (event) => {
    const data = JSON.parse(event.data);
    console.log(data.field);

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    data.field.forEach(c => ctx.fillRect(c.xCoordinate * 10, c.yCoordinate * 10, 10, 10))

    setTimeout(() => webSocket.send(event.data), 1000);
};