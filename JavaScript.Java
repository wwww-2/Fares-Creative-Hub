const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');

// Load the background image
const backgroundImage = new Image();
backgroundImage.src = 'image/pixel-art-2-pic-1280x720.jpg'; // Replace with the correct path to your background image

// Load the apple image
const appleImage = new Image();
appleImage.src = 'image/output-onlinepngtools.png'; // Replace with the correct path to your apple image

// Load the basket image
const basketImage = new Image();
basketImage.src = 'image/basket.png'; // Replace with the correct path to your basket image

// Background music
const backgroundMusic = document.getElementById('backgroundMusic');

function startBackgroundMusic() {
    backgroundMusic.play();
}

function stopBackgroundMusic() {
    backgroundMusic.pause();
    backgroundMusic.currentTime = 0; // Reset the music to the start
}

// Game state
let gameRunning = false;
let lives = 3;
let highScore = 0;
let score = 0;
let appleInterval; // Store the interval ID

// Basket properties
const basket = {
    width: 80,
    height: 50,
    x: canvas.width / 2 - 40,
    y: canvas.height - 60,
    dx: 5
};

// Apple properties
let apples = [];
const appleRadius = 20; // Adjust the radius for the image size
const appleSpeed = 2;

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

// Touch events for mobile
document.getElementById('moveLeft').addEventListener('touchstart', function() {
    leftPressed = true;
});
document.getElementById('moveLeft').addEventListener('touchend', function() {
    leftPressed = false;
});

document.getElementById('moveRight').addEventListener('mousedown', function() {
    rightPressed = true;
});
document.getElementById('moveRight').addEventListener('mouseup', function() {
    rightPressed = false;
});

// Touch events for mobile
document.getElementById('moveRight').addEventListener('touchstart', function() {
    rightPressed = true;
});
document.getElementById('moveRight').addEventListener('touchend', function() {
    rightPressed = false;
});

// Prevent default drag behavior for images
document.getElementById('moveLeft').addEventListener('dragstart', function(e) {
    e.preventDefault();
});
document.getElementById('moveRight').addEventListener('dragstart', function(e) {
    e.preventDefault();
});

// Create new apple
function createApple() {
    const x = Math.random() * (canvas.width - appleRadius * 2) + appleRadius;
    apples.push({ x: x, y: 0 });
}

// Draw the basket image
function drawBasket() {
    ctx.drawImage(basketImage, basket.x, basket.y, basket.width, basket.height);
}

// Draw an apple image
function drawApple(apple) {
    ctx.drawImage(appleImage, apple.x - appleRadius, apple.y - appleRadius, appleRadius * 2, appleRadius * 2);
}

// Draw lives
function drawLives() {
    document.getElementById('lives').textContent = 'Lives: ' + lives;
}

// Draw high score
function drawHighScore() {
    document.getElementById('highScore').textContent = 'High Score: ' + highScore;
}

// Move basket left or right
function moveBasket() {
    if (rightPressed && basket.x + basket.width < canvas.width) {
        basket.x += basket.dx;
    }
    if (leftPressed && basket.x > 0) {
        basket.x -= basket.dx;
    }
}

// Move apples and check for collision with basket
function moveApples() {
    apples.forEach((apple, index) => {
        apple.y += appleSpeed;

        // Check if apple is caught by the basket
        if (apple.y + appleRadius > basket.y && apple.x > basket.x && apple.x < basket.x + basket.width) {
            apples.splice(index, 1);
            score++;
            document.getElementById('score').textContent = 'Score: ' + score;

            // Update high score
            if (score > highScore) {
                highScore = score;
                localStorage.setItem('highScore', highScore); // Save high score to local storage
                drawHighScore();
            }
        }

        // Remove apple if it goes off the canvas
        if (apple.y > canvas.height) {
            apples.splice(index, 1);
            lives--;
            drawLives();
            if (lives <= 0) {
                gameRunning = false;
                clearInterval(appleInterval); // Clear interval when game stops
                stopBackgroundMusic();
                alert('Game Over! Your score: ' + score);
            }
        }
    });
}

// Game loop
function gameLoop() {
    if (!gameRunning) return;

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Draw background image
    ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);

    drawBasket();
    apples.forEach(drawApple);

    moveBasket();
    moveApples();

    requestAnimationFrame(gameLoop);
}

// Dynamic resizing of the canvas
function resizeCanvas() {
    canvas.width = window.innerWidth * 0.9; // Set width to 90% of window width
    canvas.height = (canvas.width * 400) / 500; // Maintain aspect ratio
}

window.addEventListener('resize', resizeCanvas);
resizeCanvas(); // Call this initially to set the correct size

// Game control buttons
document.getElementById('startGame').addEventListener('click', function() {
    if (!gameRunning) {
        gameRunning = true;
        lives = 3;
        drawLives();
        score = 0;
        document.getElementById('score').textContent = 'Score: ' + score;
        drawHighScore();
        startBackgroundMusic();
        appleInterval = setInterval(createApple, 1500); // Create an apple every 1.5 seconds
        gameLoop();
    }
});

document.getElementById('stopGame').addEventListener('click', function() {
    gameRunning = false;
    clearInterval(appleInterval); // Stop apple creation
    stopBackgroundMusic();
});

// Load high score from local storage
highScore = localStorage.getItem('highScore') || 0;
drawHighScore(); // Initial call to draw high score
