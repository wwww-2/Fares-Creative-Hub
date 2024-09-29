// Basket properties (already defined)
const basket = {
    width: 80,
    height: 50,  
    x: canvas.width / 2 - 40,
    y: canvas.height - 60,
    dx: 5
};

// Controls
let rightPressed = false;
let leftPressed = false;

// Event listeners for keyboard controls
document.addEventListener('keydown', function (e) {
    if (e.key === 'ArrowRight') rightPressed = true;
    if (e.key === 'ArrowLeft') leftPressed = true;
});
document.addEventListener('keyup', function (e) {
    if (e.key === 'ArrowRight') rightPressed = false;
    if (e.key === 'ArrowLeft') leftPressed = false;
});

// Event listeners for button controls
document.getElementById('moveLeft').addEventListener('mousedown', function() {
    leftPressed = true;
});
document.getElementById('moveLeft').addEventListener('mouseup', function() {
    leftPressed = false;
});

document.getElementById('moveRight').addEventListener('mousedown', function() {
    rightPressed = true;
});
document.getElementById('moveRight').addEventListener('mouseup', function() {
    rightPressed = false;
});

// Move basket left or right
function moveBasket() {
    if (rightPressed && basket.x + basket.width < canvas.width) {
        basket.x += basket.dx;
    }
    if (leftPressed && basket.x > 0) {
        basket.x -= basket.dx;
    }
}

// Then, make sure this `moveBasket` function is called in the game loop to update the basket's position:
function gameLoop() {
    if (!gameRunning) return;

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // Draw background, basket, and apples here...
    
    moveBasket(); // Move the basket based on current input

    requestAnimationFrame(gameLoop); // Continue the game loop
}
