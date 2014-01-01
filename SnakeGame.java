/********************
NAME: Stephen Wu
LOGIN: cs11eeu
ID: A11279994
*********************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import objectdraw.*;

public class SnakeGame implements KeyListener, Runnable, ChangeListener, ActionListener {

	// Declaring final strings that will never be changed
	private static final String[] GAME_TYPES = {"PR7 Game",
												"Original Snake"};

	// Declaring GUI components
	private JLabel	scoreText = new JLabel("Score: 0"),
					highScoreText = new JLabel("High Score: 0"),
					gameOverText = new JLabel("");
	private JButton	newGameButton = new JButton("New Game"),
			resetButton = new JButton("Reset");
	private JSlider	speedSlider;
	private JComboBox<String> gameTypeDropDown = new JComboBox<String>(GAME_TYPES);
	private JDrawingCanvas jCanvas = new JDrawingCanvas();

	// Declaring game variables
	private GameGrid grid;
	private SnakeMover mover;
	private int	currentScore = 0,
			highScore = 0;
	private String gameType;

	// Createing a SnakeGame
	public SnakeGame(String initGameType) {
		gameType = initGameType;
		boolean foundType = false;
		for(int i = 0; i < GAME_TYPES.length; i++) {
			if(gameType.equals(GAME_TYPES[i])) {
				foundType = true;
				gameTypeDropDown.setSelectedIndex(i);
				break;
			}
		}
		if(!foundType) System.exit(1);
	}

	public SnakeGame() {
		this("PR7 Game");
	}

	public void run() {
		JFrame window = new JFrame("Snake Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = window.getContentPane();
		pane.setLayout(new BorderLayout());

		// Establishing GUI interface

       	// Setting the bottom panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(newGameButton);
		newGameButton.addActionListener(this);
		bottomPanel.add(resetButton);
		resetButton.addActionListener(this);
		JPanel longControls = new JPanel(new GridLayout(2,0));
		speedSlider = new JSlider(JSlider.HORIZONTAL, 1,20, 1);
		speedSlider.addChangeListener(this);
		gameTypeDropDown.addActionListener(this);
		bottomPanel.add(speedSlider);
		pane.add(bottomPanel, BorderLayout.SOUTH);

		// Setting the top panel
		JPanel topPanel = new JPanel(new GridLayout(2,3));
		topPanel.add(highScoreText);
		topPanel.add(gameOverText);
		topPanel.add(scoreText);
		topPanel.add(gameTypeDropDown);
		pane.add(topPanel, BorderLayout.NORTH);

		pane.add(jCanvas, BorderLayout.CENTER);
		jCanvas.addKeyListener(this);
		jCanvas.requestFocusInWindow();
		window.pack();
		window.setVisible(true);
	}
	
	// Running game and setting rules
	public static void main(String[] args) {

		String type = "PR7 Game";
		if(args.length >= 1) {
			type = args[0];
		}
		SwingUtilities.invokeLater(new SnakeGame(type));
	}

	// Adding score
	public void addScore() {
		currentScore += 10;
		if(highScore < currentScore)
			highScore = currentScore;
			
		updateScore();

		// Depending on game type
		switch(gameType) {
						
			case "Original Snake":
				grid.addObstacle();
				if(currentScore%20 == 0) {
					raiseSpeed((currentScore/20)+1);
				}
				break;

			case "PR7 Game":
				if(currentScore%100 == 0) {
					raiseSpeed((currentScore/100)+1);
				}
				break;
			default: break;
		}
	}

	// Raising speed accordingly
	private void raiseSpeed(int compareTo) {
		boolean biggerSpeed = compareTo > speedSlider.getValue();
		boolean maxed = speedSlider.getValue() >= 20;
		if(biggerSpeed && !maxed)
			speedSlider.setValue(speedSlider.getValue() + 1);
	}

	// Updating the score
	private void updateScore() {
		scoreText.setText("Score: " + currentScore);
		highScoreText.setText("High Score: "+highScore);
	}

	// Getting the canvas
	public DrawingCanvas getCanvas() {
		return jCanvas;
	}

	public void gameOver() {
		gameOverText.setText("GAME OVER!");
	}

	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == speedSlider && mover != null)
			mover.setSpeed(speedSlider.getValue());
		jCanvas.requestFocusInWindow();
	}

	// Empty methods
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	// Performing actions when key is pressed
	public void keyPressed(KeyEvent e) {
		if(mover != null && mover.alive()) {
			switch(e.getKeyChar()) {
				case 'j':	mover.turnSnake(-1);
						break;
				case 'l':	mover.turnSnake(1);
						break;
			}
		}
	}

	// Getting the game type
	public String getGameType() {
		return gameType;
	}
	
	// Performing actions when mouse clicked
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == newGameButton)
		{
			if(mover != null && mover.alive())
			mover.dead();
			currentScore = 0;
			jCanvas.clear();

			grid = new GameGrid(new Coord(jCanvas.getWidth()/(20),0), 10, this);
			mover = new SnakeMover(this, grid, speedSlider.getValue());

			updateScore();
			gameOverText.setText("");

			if(gameType.equals("Original Snake"))
				grid.addObstacle();
		}

		if(e.getSource() == resetButton)
		{
			highScore = 0;
			updateScore();
			speedSlider.setValue(1);
			if(mover != null)
				mover.dead();
		}

		if(e.getSource() == gameTypeDropDown) {
			gameType = GAME_TYPES[gameTypeDropDown.getSelectedIndex()];
				if(grid != null)
					grid.setGameType(gameType);
		}
		jCanvas.requestFocusInWindow();
	}
}
