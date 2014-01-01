/********************
NAME: Stephen Wu
LOGIN: cs11eeu
ID: A11279994
*********************/

import java.awt.event.*;

public class SnakeMover extends Thread {

	// Declaring variables
	private SnakeGame snakeGame;
	private GameGrid grid;
	private int	speed, actionCount = 0, cycleCount = 0;
	private boolean	alive = true;

	// Constructing Snake Mover
	public SnakeMover(SnakeGame initControl, GameGrid initGrid, int initSpeed) {
		snakeGame = initControl;
		grid = initGrid;
		speed = initSpeed;
		start();
	}

	// Allowing other classes to turn the snake
	public void turnSnake(int direction) {
		boolean turned = false;
		
		// Turning left on request
		if (direction == -1)
			turned = alive = grid.turnSnake(-1);

		// Turning right on request
		else if (direction == 1)
			turned = alive = grid.turnSnake(1);

		if(turned) {
			updateActionCount(1);
			cycleCount = 0;
		}
	}

	// Setting the speed of snake
	public void setSpeed(int setSpeed) {
		speed = setSpeed;
	}

	// Run method
	public void run() {
		while(alive) {

			if(speed != 0) {
				if(cycleCount*50 < 1000-(speed-1)*50)
					cycleCount++;
				else {
					alive = grid.snakeMoveForward();
					updateActionCount(1);
					cycleCount = 1; // Reset and increase
				}
			}

			try {
				sleep(50);
			}
			catch (Exception e) {
				break;
			}
		}
		snakeGame.gameOver();
	}

	// Changes the action count and runs any actions that should take place when doing so
	private void updateActionCount(int count) {
		actionCount += count;

		// Adding obstacle after 10 moves
		boolean pr7Type = snakeGame.getGameType() == "PR7 Game";
		if(pr7Type && actionCount > 0 && actionCount%10 == 0) {
			grid.addObstacle();
		}
	}
	
	// Returning if snake is alive or not
	public boolean alive() {
		return alive;
	}

	// Stopping the snakemover
	public void dead() {
		alive = false;
		interrupt();
	}
}
