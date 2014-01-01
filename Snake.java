/********************
NAME: Stephen Wu
LOGIN: cs11eeu
ID: A11279994
*********************/

public class Snake {

	// Declaring variables
	private int orientation = 2;
	private Coord[] body;
	private int length;

	public Snake(Coord initLoc, int initOrientation, int maxLen) {
		body = new Coord[maxLen + 1];
		body[0] = initLoc;
		orientation = initOrientation;
		length = 1;
	}

	// Creates a duplicate snake
	public Snake(Snake snake) {
		if(snake != null) {
			body = new Coord[snake.getBody().length];
			for(int i = 0; i < snake.getLength(); i++)
				body[i] = new Coord(snake.getBodyIndex(i));
			orientation = snake.getOrientation();
			length = snake.getLength();
		}
	}

	// Moving snake
	private boolean move(int direction) {
		
		// Initializing new head to old head
		Coord newHead = new Coord(body[0]);

		// Moving head in a correct direction
		switch(direction) {
			case 0:	newHead.move(0, -1);
					break;
			case 2:	newHead.move(0, 1);
					break;
			case 1:	newHead.move(1, 0);
					break;
			case 3:	newHead.move(-1, 0);
					break;
			default:	return false;
		}

		body[length-1] = null;
		for (int i = length-1; i > 0; i--)
			body[i] = body[i-1];
		body[0] = newHead;

		return true;
	}

	public boolean advance() {
		return move(orientation);
	}

	// Growing snake by one unit
	public boolean grow() {
		Coord newTail = body[length-1];

		// Checking if move was success to continue
		if (advance()) {
			body[length] = newTail;
			length++;
			return true;
		}
		return false;
	}

	// Turning snake left or right
	public void turn(int turnDirection) {
		orientation = (orientation+turnDirection+4) % 4;
	}

	// Checking if snake has eaten itself
	public boolean eatenSelf() {
		boolean overlapped = false;

		for (int i = 1; i < length; i++) {
			if (body[0].equals(body[i]))
				overlapped = true;
		}
		return overlapped;
	}

	// Getting the length of the snake
	public int getLength() {
		return length;
	}

	// Getting the body of the snake
	public Coord getBodyIndex(int index) {
		return body[index];
	}

	// Getting the length of the snake
	public Coord[] getBody() {
		return body;
	}

	// Getting the orientation of the snake
	public int getOrientation() {
	return orientation;
	}
}
