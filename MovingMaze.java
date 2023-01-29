
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/* Stellenbosch University CS144 Project 2022
 * MovingMaze.java
 * Name and surname: Robert Shone
 * Student number: 25132687
 */

/**
 * The main file for the game Moving Maze.
 * 
 * @author Robert Shone
 */
public class MovingMaze {

	// ==========================================================
	// Constants
	// ==========================================================

	// Move these where you'll use them, or refer to them with e.g.
	// MovingMaze.PATH_NESW

	// ═ ║ ╔ ╗ ╚ ╝ ╠ ╣ ╦ ╩ ╬
	// ─ │ ┌ ┐ └ ┘ ├ ┤ ┬ ┴ ┼

	public static final String PATH_EW = "═";
	public static final String PATH_NS = "║";
	public static final String PATH_ES = "╔";
	public static final String PATH_SW = "╗";
	public static final String PATH_NE = "╚";
	public static final String PATH_NW = "╝";
	public static final String PATH_NES = "╠";
	public static final String PATH_NSW = "╣";
	public static final String PATH_ESW = "╦";
	public static final String PATH_NEW = "╩";
	public static final String PATH_NESW = "╬";

	public static final String BORDER_HORI = "─";
	public static final String BORDER_VERT = "│";
	public static final String BORDER_TOPLEFT = "┌";
	public static final String BORDER_TOPRIGHT = "┐";
	public static final String BORDER_BOTTOMLEFT = "└";
	public static final String BORDER_BOTTOMRIGHT = "┘";
	public static final String BORDER_LEFTEDGE = "├";
	public static final String BORDER_RIGHTEDGE = "┤";
	public static final String BORDER_TOPEDGE = "┬";
	public static final String BORDER_BOTTOMEDGE = "┴";
	public static final String BORDER_MIDDLE = "┼";

	/**
	 * Declares the floating tile of the game to be accessed anywhere in the
	 * program.
	 */
	public static Tile floater = new Tile("XXXX", null, "", "");

	// ==========================================================
	// Main function
	// ==========================================================

	/**
	 * Used scanner to read tiles and format of game from file path given in
	 * arguments. Sets the games width, length and number of relics per player.
	 * Creates tiles and adds them to a 2d array. Creates game with the format given
	 * by arguments.
	 * 
	 * @param arg1 arguments given to the program.
	 * @see Game
	 * @see Games#play()
	 */
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File(args[0]));
			int width = sc.nextInt();
			int length = sc.nextInt();
			int numRelics = sc.nextInt();
			String f = sc.next();
			String startf[] = { " ", " ", " ", " " };
			Tile floating = new Tile(f.substring(0, 4), startf, f.substring(4, 5), f.substring(5));
			floater = floating;

			Tile board[][] = new Tile[length][width];

			for (int y = 0; y < length; y++) {
				for (int x = 0; x < width; x++) {
					String cur = sc.next();
					if (y == 0 && x == 0) {
						String startG[] = { "G", " ", " ", " " };
						board[y][x] = new Tile(cur.substring(0, 4), startG, cur.substring(4, 5), cur.substring(5));
					} else if (y == 0 && x == (width - 1)) {
						String startY[] = { " ", "Y", " ", " " };
						board[y][x] = new Tile(cur.substring(0, 4), startY, cur.substring(4, 5), cur.substring(5));
					} else if (y == (length - 1) && x == 0) {
						String startR[] = { " ", " ", "R", " " };
						board[y][x] = new Tile(cur.substring(0, 4), startR, cur.substring(4, 5), cur.substring(5));
					} else if (y == (length - 1) && x == (width - 1)) {
						String startB[] = { " ", " ", " ", "B" };
						board[y][x] = new Tile(cur.substring(0, 4), startB, cur.substring(4, 5), cur.substring(5));
					} else {
						String start[] = { " ", " ", " ", " " };
						board[y][x] = new Tile(cur.substring(0, 4), start, cur.substring(4, 5), cur.substring(5));
					}

				}
			}
			String mode = args[1];
			if (args[1].equals("text") || args[1].equals("gui")) {

			} else {
				System.out.println("Unknown visual mode. ");
				System.exit(0);
			}

			Game play = new Game(width, length, board, numRelics, mode);
			play.Play();

		} catch (FileNotFoundException e) {
			System.out.println("The game board file does not exist." + e);
			System.exit(0);
		}

	}

	// ==========================================================
	// Test API functions
	// ==========================================================

	// Populate these with high-level code that references your codebase.

	// ----------------------------------------------------------
	// First hand-in

	/**
	 * Returns true if the tile given is open in the direction given and false if
	 * not.
	 * {@link https://learn.sun.ac.za/pluginfile.php/3476782/mod_resource/content/6/main.pdf}.
	 * 
	 * @param tileEncoding the tile to be inspected
	 * @param dir          the direction to check if its open
	 * @return {@code true} if the tile is open in the direction given
	 * @see Tile
	 * @see Tile#isUpOpen(Tile t)
	 * @see Tile#isDownOpen(Tile t)
	 * @see Tile#isRightOpen(Tile t)
	 * @see Tile#isLeftOpen(Tile t)
	 */
	public static boolean isTileOpenToSide(String tileEncoding, char dir) {
		Tile cur = new Tile(tileEncoding.substring(0, 4), null, "", "");
		Tile fake = new Tile("1111", null, "", "");
		switch (dir) {
		case 'n':
			return cur.isUpOpen(fake);
		case 's':
			return cur.isDownOpen(fake);
		case 'e':
			return cur.isRightOpen(fake);
		case 'w':
			return cur.isLeftOpen(fake);
		}
		return false;
	}

	/**
	 * Take a tile given and rotates it once clockwise
	 * {@link https://learn.sun.ac.za/pluginfile.php/3476782/mod_resource/content/6/main.pdf}.
	 * 
	 * @param tileEncoding the tile to be inspected
	 * @return {@code true} in a boolean array for each direction if that side is
	 *         open
	 * @see Tile
	 * @see Tile#Rotate(int n)
	 */
	public static boolean[] rotateTileClockwise(String tileEncoding) {
		Tile cur = new Tile(tileEncoding.substring(0, 4), null, "", "");
		Tile fake = new Tile("1111", null, "", "");
		cur.Rotate(1);
		return new boolean[] { cur.isUpOpen(fake), cur.isRightOpen(fake), cur.isDownOpen(fake), cur.isLeftOpen(fake) };
	}

	/**
	 * Take a tile given and rotates it once counterclockwise
	 * {@link https://learn.sun.ac.za/pluginfile.php/3476782/mod_resource/content/6/main.pdf}.
	 * 
	 * @param tileEncoding the tile to be inspected
	 * @return {@code true} in a boolean array for each direction if that side is
	 *         open
	 * @see Tile
	 * @see Tile#Rotate(int n)
	 */
	public static boolean[] rotateTileCounterclockwise(String tileEncoding) {
		Tile cur = new Tile(tileEncoding.substring(0, 4), null, "", "");
		Tile fake = new Tile("1111", null, "", "");
		cur.Rotate(-1);
		return new boolean[] { cur.isUpOpen(fake), cur.isRightOpen(fake), cur.isDownOpen(fake), cur.isLeftOpen(fake) };
	}

	/**
	 * Take the floating tile given, insert it at the sliding position given into
	 * the maze encoded with the 2D array that is also given Checks each side of the
	 * new floating tile and returns if each side is open or not
	 * {@link https://learn.sun.ac.za/pluginfile.php/3476782/mod_resource/content/6/main.pdf}.
	 * 
	 * @param mazeTileEncodings    2D array to be used.
	 * @param floatingTileEncoding floating tile to be inserted into maze.
	 * @param slidingIndicator     position where the floating tile must be
	 *                             inserted.
	 * @return {@code true} in a boolean array for each direction if that side is
	 *         open for the floating tile.
	 * @see Tile
	 * @see Game
	 * @see Maze
	 * @see Game#Sliding(String p, Maze m, int nrg)
	 */
	public static boolean[] slideTileIntoMaze1(String[][] mazeTileEncodings, String floatingTileEncoding,
			String slidingIndicator) {
		Tile arr[][] = new Tile[mazeTileEncodings.length][mazeTileEncodings[0].length];
		Tile fake = new Tile("1111", null, "", "");
		String fraud[] = { " ", " ", " ", " " };
		for (int k = 0; k < mazeTileEncodings.length; k++) {
			for (int y = 0; y < mazeTileEncodings[0].length; y++) {
				Tile cur = new Tile(mazeTileEncodings[k][y].substring(0, 4), fraud, "", "");
				arr[k][y] = cur;
			}
		}
		floater = new Tile(floatingTileEncoding.substring(0, 4), fraud, "", "");
		Maze m = new Maze(mazeTileEncodings[0].length, mazeTileEncodings.length, arr);
		Game g = new Game(mazeTileEncodings[0].length, mazeTileEncodings.length, arr, 0, "text");
		g.Sliding(slidingIndicator, m, 0);
		return new boolean[] { floater.isUpOpen(fake), floater.isRightOpen(fake), floater.isDownOpen(fake),
				floater.isLeftOpen(fake) };
	}

	/**
	 * Returns true if the new tile can be stepped to from the current tile if new
	 * tile is open from direction given and the current tile is open in the
	 * direction given.
	 * {@link https://learn.sun.ac.za/pluginfile.php/3476782/mod_resource/content/6/main.pdf}.
	 * 
	 * @param curTileEncoding current tile to be inspected.
	 * @param newTileEncoding new tile to be inspected.
	 * @param dir             direction to check if the old tile can move to the new
	 *                        tile
	 * @return {@code true} if you can move from current tile to the new tile in the
	 *         direction.
	 * @see Tile
	 * @see Tile#isUpOpen(Tile t)
	 * @see Tile#isDownOpen(Tile t)
	 * @see Tile#isRightOpen(Tile t)
	 * @see Tile#isLeftOpen(Tile t)
	 */
	public static boolean canMoveInDirection(String curTileEncoding, String newTileEncoding, char dir) {
		Tile cur = new Tile(curTileEncoding.substring(0, 4), null, "", "");
		Tile newCur = new Tile(newTileEncoding.substring(0, 4), null, "", "");
		switch (dir) {
		case 'n':
			return cur.isUpOpen(newCur);
		case 's':
			return cur.isDownOpen(newCur);
		case 'e':
			return cur.isRightOpen(newCur);
		case 'w':
			return cur.isLeftOpen(newCur);
		}
		return false;
	}

	/**
	 * Returns true if an adventurer starting in the top-left corner can
	 * successfully complete the steps in the directions given in the array.
	 * {@link https://learn.sun.ac.za/pluginfile.php/3476782/mod_resource/content/6/main.pdf}.
	 * 
	 * @param mazeTileEncodings 2D array to be used.
	 * @param steps             an array of directions to move tiles
	 * @return {@code false} if any step in the path cannot be taken for any reason,
	 *         Otherwise {@code true}.
	 * @see Tile
	 * @see Game
	 * @see Maze
	 * @see Game#Moving(String p, int num, String player, Maze m, int ssg)
	 */
	public static boolean canMoveAlongPath(String[][] mazeTileEncodings, char[] steps) {
		String fraud[] = { " ", " ", " ", " " };

		Tile arr[][] = new Tile[mazeTileEncodings.length][mazeTileEncodings[0].length];
		for (int k = 0; k < mazeTileEncodings.length; k++) {
			for (int y = 0; y < mazeTileEncodings[0].length; y++) {
				Tile cur = new Tile(mazeTileEncodings[k][y].substring(0, 4), fraud, "", "");
				arr[k][y] = cur;
			}
		}
		Maze m = new Maze(mazeTileEncodings[0].length, mazeTileEncodings.length, arr);
		Game g = new Game(mazeTileEncodings[0].length, mazeTileEncodings.length, arr, 0, "text");
		for (int b = 0; b < steps.length; b++) {
			g.Moving(steps[b] + "", 0, "", m, 0);
		}

		return g.checkNum(steps.length);
	}

	// ----------------------------------------------------------
	// Second hand-in

	/**
	 * Checks to see if tile given has a relic on it or not.
	 * {@link https://learn.sun.ac.za/pluginfile.php/3476782/mod_resource/content/6/main.pdf}.
	 * 
	 * @param tileEncoding the tile to be inspected
	 * @return {@code true} if the tile has a relic on it.
	 * @see Tile
	 * @see Tile#getRelic()
	 * @see Relic
	 * @see Relic#.isRelic()
	 */
	public static boolean tileHasRelic(String tileEncoding) {
		Tile cur = new Tile(tileEncoding.substring(0, 4), null, tileEncoding.substring(4, 5),
				tileEncoding.substring(5));
		return cur.getRelic().isRelic();
	}

	/**
	 * Take the floating tile given, insert it at the sliding position given into
	 * the maze encoded with the 2D array that is also given Takes the floating tile
	 * and returns the relic of it or 'x' if there is no relic.
	 * {@link https://learn.sun.ac.za/pluginfile.php/3476782/mod_resource/content/6/main.pdf}.
	 * 
	 * @param mazeTileEncodings    2D array to be used.
	 * @param floatingTileEncoding floating tile to be inserted into maze.
	 * @param slidingIndicator     position where the floating tile must be
	 *                             inserted.
	 * @return {@code out} a char the represents the relic on the floating tile.
	 * @see Tile
	 * @see Game
	 * @see Maze
	 * @see floater
	 * @see Game#Sliding(String p, Maze m, int nrg)
	 * @see Tile#getRelic()
	 * @see Relic#getColour()
	 */
	public static char slideTileIntoMaze2(String[][] mazeTileEncodings, String floatingTileEncoding,
			String slidingIndicator) {
		Tile arr[][] = new Tile[mazeTileEncodings.length][mazeTileEncodings[0].length];
		String fraud[] = { " ", " ", " ", " " };
		for (int k = 0; k < mazeTileEncodings.length; k++) {
			for (int y = 0; y < mazeTileEncodings[0].length; y++) {
				Tile cur = new Tile(mazeTileEncodings[k][y].substring(0, 4), fraud,
						mazeTileEncodings[k][y].substring(4, 5), mazeTileEncodings[k][y].substring(5));
				arr[k][y] = cur;
			}
		}
		floater = new Tile(floatingTileEncoding.substring(0, 4), fraud, "", "");
		Maze m = new Maze(mazeTileEncodings[0].length, mazeTileEncodings.length, arr);
		Game g = new Game(mazeTileEncodings[0].length, mazeTileEncodings.length, arr, 0, "text");
		g.Sliding(slidingIndicator, m, 0);
		char out = floater.getRelic().getColour().charAt(0);
		return out;
	}

}