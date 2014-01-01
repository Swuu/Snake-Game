/********************
NAME: Stephen Wu
LOGIN: cs11eeu
ID: A11279994
*********************/

import objectdraw.*;
import java.awt.*;

public class GameGrid {

	// Declaring variables
	private SnakeGame snakeGame;
	private String gameType;
	private Snake snake;
	private int[][] stage;
	private FilledRect[][] stageVisuals;
	private DrawingCanvas canvas;
	private int	cellSize, xBuffer, yBuffer;

	// Creating GameGrid
	public GameGrid(Coord snakeCoord, int initCellSize, SnakeGame initControl) {
		
		// Initializing variables
		snakeGame = initControl;
		gameType = snakeGame.getGameType();
		canvas = snakeGame.getCanvas();
		cellSize = initCellSize;

		// Determining sizes
		int aWidth = canvas.getWidth()/cellSize;
		xBuffer = (canvas.getWidth()%cellSize)/2;
		int aHeight = canvas.getHeight()/cellSize;
		yBuffer = (canvas.getHeight()%cellSize)/2;
		stage = new int[aWidth][aHeight];
		stageVisuals = new FilledRect[aWidth][aHeight];
		snake = new Snake(snakeCoord, 2, aWidth*aHeight);

		// Drawing
		initStage();
		showSnake(snake, true);
	}

	// Initializing graphics
	private void initStage() {
		canvas.clear();

		// Filling the stage
		for(int y = 0; y < stage[0].length; y++) {
			for(int x = 0; x < stage.length; x++) {
				
				// Making a FilledRect at the slot area and store it in stage
				stageVisuals[x][y] = new FilledRect(xBuffer + x*cellSize,
					yBuffer + y*cellSize, cellSize, cellSize, canvas);
				
				// Then updating its color
				updateSlot(new Coord(x, y));
			}
		}
		
		// Constructing the border
		new FramedRect(xBuffer, yBuffer, stage.length*cellSize - 1,
				stage[0].length*cellSize - 1, canvas);
	}

	// Moving snake if valid move
	public boolean snakeMoveForward() {

		Snake temp = new Snake(snake);
		if(snake.advance() && !gameOver()) {
			if(gameType.equals("Original Snake") && hitObstacle()) {
				snake = temp;
				snake.grow();
				snakeGame.addScore();
			}

			redrawSnake(temp);
			return true;
		}
		else {
			snake = temp;
			return false;
		}
	}

	// Turning snake in given direction
	public boolean turnSnake(int turn) {
		
		// Saving the state of snake
		Snake temp = new Snake(snake);
		snake.turn(turn);

		switch(gameType) {

			// For the case of the PR7 game type
			case "PR7 Game":
				if(snake.grow() && !gameOver()) {
					snakeGame.addScore();
					redrawSnake(temp);
					return true;
				} else {
					snake = temp;
					return false;
				}

			// For the case of the original sname game type
			case "Original Snake":
				return snakeMoveForward();
			default: return true;
		}
	}

	// Drawing or erasing snake
	private void showSnake(Snake aSnake, boolean write) {
		int state;
		if(write) state = 2;
		else state = 0;
		setCoord(aSnake.getBody(), aSnake.getLength(), state);
		if(write) setCoord(aSnake.getBodyIndex(0), 1);
		updateSlot(aSnake.getBody(), aSnake.getLength());
	}

	// Checking if slot if occupied
	private boolean isOccupied() {
		for(int[] array : stage) {
			for(int slot : array) {
				if(slot == 0)
					return false;
			}
		}
		return true;
	}

	// Returning the state
	private int getCoord(Coord coord) {
		return stage[coord.getX()][coord.getY()];
	}

	// Setting the coordinate
	private void setCoord(Coord coord, int state) {
		stage[coord.getX()][coord.getY()] = state;
	}

	// Setting the type of game
	public void setGameType(String newGameType) {
		gameType = newGameType;
	}

	// Setting all coordinates in the array
	private void setCoord(Coord[] array, int index, int state) {
		for(int i = 0; i < index; i++)
			setCoord(array[i], state);
	}

	// Updating the visual graphics that the user sees
	private void updateSlot(Coord coord) {

		Color color;
		switch(getCoord(coord)) {
				case 0:		color = Color.WHITE;
							break;
				case 1:		color = Color.BLUE;
							break;
				case 2:		color = Color.GREEN;
							break;
				case 3:		color = Color.BLACK;
							break;
				default:	color = Color.WHITE;
							break;
		}
		stageVisuals[coord.getX()][coord.getY()].setColor(color);
	}

	// Updating all slots
	private void updateSlot(Coord[] array, int index) {
		for(int i = 0; i < index; i++)
			updateSlot(array[i]);
	}

	// Checking if the snake hits an obstacle
	private boolean hitObstacle() {
		try {
			return getCoord(snake.getBodyIndex(0)) == 3;
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	// Adding an obstacle
	public boolean addObstacle() {
		
		// Checking if the space is occupied
		if(isOccupied())
			return false;

		int empty = 0;
		for(int[] array : stage)
			for(int state : array)
				if(state == 0) empty++;

		int selected = (int) (Math.random() * empty) + 1;
		empty = 0;
		int	x = 0,
			y = 0;
		for(y = 0; y < stage[0].length; y++) {
			for(x = 0; x < stage.length; x++) {
				if(stage[x][y] == 0) empty++;
				if(empty == selected) break;
			}
			if(empty == selected) break;
		}

		Coord randCoord = new Coord(x,y);
		setCoord(randCoord, 3);
		updateSlot(randCoord);

		return true;
	}

	// Checking if snake is off the stage
	private boolean offStage() {
		return	snake.getBodyIndex(0).getX() < 0 ||
				snake.getBodyIndex(0).getX() > stage.length-1 ||
				snake.getBodyIndex(0).getY() < 0 ||
				snake.getBodyIndex(0).getY() > stage[0].length-1;
	}

	// Redrawing snake
	private void redrawSnake(Snake oldSnake) {
		showSnake(oldSnake, false);
		showSnake(snake, true);
	}

	// Checking if snake violated rules and thus game over
	private boolean gameOver() {
		if(offStage()) return true;
		switch(gameType) {
			case "Original Snake":
				return snake.eatenSelf();
			case "PR7 Game":
				return snake.eatenSelf() || hitObstacle();
			default: return false;
		}
	}
}
