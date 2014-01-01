/********************
NAME: Stephen Wu
LOGIN: cs11eeu
ID: A11279994
*********************/

import java.util.Random;

public class Coord {

	// Declaring variables
	private int	x, y;

	// Constructs a coord at newCoord
	public Coord(Coord newCoord) {
		if(newCoord != null) {
			x = newCoord.getX();
			y = newCoord.getY();
		}
	}

	// Constructing a coord
	public Coord(int i, int j) {
		x = i;
		y = j;
	}

	// Equating coords
	public boolean equals(Coord other) {
		return	(x == other.getX() && y == other.getY());
	}

	// Moving coords
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

	//Returning values
	public int getY() {
		return y;
	}
		public int getX() {
		return x;
	}

	// Constructing a random coord
	public static Coord getRandom(int x, int y) {
		Random random = new Random();
		return new Coord(random.nextInt(x),random.nextInt(y));
	}
}
