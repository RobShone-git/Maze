/**
 * This class is used to create the board of the game and return it
 * 
 * @author Robert Shone
 */
public class Maze {

	private final int width;
	private final int length;

	/**
	 * Constructor that initializes objects and gives them attributes
	 * 
	 * @param w   the width of the board
	 * @param l   the length of the board
	 * @param arr the array of tile objects
	 * @see #width
	 * @see #length
	 * @see #board
	 */
	public Maze(int w, int l, Tile arr[][]) {
		width = w;
		length = l;
	}

	/**
	 * creates the board by adding special characters, spaces, relics and players to
	 * a string and returns it
	 * 
	 * @return {@code out}
	 * @see #board
	 * @see #width
	 * @see #length
	 * @see Tile#goLeft()
	 * @see Tile#goUp()
	 * @see Tile#goDown()
	 * @see Tile#goRight()
	 * @see Tile#getPlayer(int num)
	 * @see MovingMaze
	 * @see MovingMaze#BORDER_TOPLEFT
	 * @see MovingMaze#BORDER_HORI
	 * @see MovingMaze#BORDER_TOPRIGHT
	 * @see MovingMaze#BORDER_TOPEDGE
	 * @see MovingMaze#BORDER_VERT
	 * @see MovingMaze#BORDER_BOTTOMLEFT
	 * @see MovingMaze#BORDER_LEFTEDGE
	 * @see MovingMaze#BORDER_BOTTOMRIGHT
	 * @see MovingMaze#BORDER_RIGHTEDGE
	 * @see MovingMaze#BORDER_BOTTOMEDGE
	 * @see MovingMaze#BORDER_MIDDLE
	 * @see MovingMaze#floater
	 */
	public String GameBoard() {
		String out = "     ";
		int numX = 1;

		for (int j = 0; j < width; j++) {
			out += numX + "       ";
			numX++;
		}

		out += "\n " + MovingMaze.BORDER_TOPLEFT;

		for (int k = 0; k < width; k++) {
			out += MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI
					+ MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI;
			if (k == (width - 1)) {
				out += MovingMaze.BORDER_TOPRIGHT;
			} else {
				out += MovingMaze.BORDER_TOPEDGE;
			}
		}

		int numY = 1;

		for (int y = 0; y < length; y++) {
			out += "\n " + MovingMaze.BORDER_VERT;
			for (int x = 0; x < width; x++) {
				out += " " + Game.board[y][x].getPlayer(0) + " " + Game.board[y][x].goUp() + " "
						+ Game.board[y][x].getPlayer(1) + " " + MovingMaze.BORDER_VERT;
			}
			out += "\n" + numY + MovingMaze.BORDER_VERT;
			for (int x = 0; x < width; x++) {
				out += Game.board[y][x].goLeft() + Game.board[y][x].toString() + Game.board[y][x].goRight()
						+ MovingMaze.BORDER_VERT;
			}
			out += numY + "\n " + MovingMaze.BORDER_VERT;
			for (int x = 0; x < width; x++) {
				out += " " + Game.board[y][x].getPlayer(2) + " " + Game.board[y][x].goDown() + " "
						+ Game.board[y][x].getPlayer(3) + " " + MovingMaze.BORDER_VERT;
			}

			if (y == (length - 1)) {
				out += "\n " + MovingMaze.BORDER_BOTTOMLEFT;
			} else {
				out += "\n " + MovingMaze.BORDER_LEFTEDGE;
			}

			for (int k = 0; k < width; k++) {
				out += MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI
						+ MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI;
				if (k == (width - 1)) {
					if (y == (length - 1)) {
						out += MovingMaze.BORDER_BOTTOMRIGHT;
					} else {
						out += MovingMaze.BORDER_RIGHTEDGE;
					}
				} else {
					if (y == (length - 1)) {
						out += MovingMaze.BORDER_BOTTOMEDGE;
					} else {
						out += MovingMaze.BORDER_MIDDLE;
					}
				}
			}
			numY++;
		}

		numX = 1;
		out += "\n     ";
		for (int j = 0; j < width; j++) {
			out += numX + "       ";
			numX++;
		}

		out += "\n\n" + MovingMaze.BORDER_TOPLEFT + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI
				+ MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI
				+ MovingMaze.BORDER_HORI + MovingMaze.BORDER_TOPRIGHT;
		out += "\n" + MovingMaze.BORDER_VERT + "   " + MovingMaze.floater.goUp() + "   " + MovingMaze.BORDER_VERT;
		out += "\n" + MovingMaze.BORDER_VERT + MovingMaze.floater.goLeft() + MovingMaze.floater.toString()
				+ MovingMaze.floater.goRight() + MovingMaze.BORDER_VERT;
		out += "\n" + MovingMaze.BORDER_VERT + "   " + MovingMaze.floater.goDown() + "   " + MovingMaze.BORDER_VERT;
		out += "\n" + MovingMaze.BORDER_BOTTOMLEFT + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI
				+ MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI + MovingMaze.BORDER_HORI
				+ MovingMaze.BORDER_HORI + MovingMaze.BORDER_BOTTOMRIGHT;

		return out;
	}

}
