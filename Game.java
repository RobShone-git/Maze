

/**
 * Creates Game object and allows them to use methods.
 * @author Robert Shone
 */
public class Game {

	private static int width;
	private static int length;
	private final String mode;
	private String lastFloatPosition = "";
	private int curX = 0;
	private int curY = 0;
	
	public static int totalRelics;
	public static int numRelics[] = { 1, 1, 1, 1 };
	public static int count = 0;
	public static String ActivePLayer = "";
	public static int ActivePLayerNum = 0;
	public static boolean sliding = true;
	public static boolean isPath = false;
	public static boolean pathFinderArr[][];
	public static Tile board[][];

	/**
	 * Constructor that initializes objects and gives them attributes
	 * 
	 * @param w the width of the board
	 * @param l the length of the board
	 * @param b the array of tile objects
	 * @param n number of total relics per player
	 * @see #width
	 * @see #length
	 * @see #startBoard
	 * @see #totalRelics
	 */
	public Game(int w, int l, Tile b[][], int n, String m) {
		width = w;
		length = l;
		board = b;
		totalRelics = n + 1;
		mode = m;
		pathFinderArr = new boolean[l][w];
	}

	/**
	 * returns the scoreboard of the game in a string
	 * 
	 * @return {@code out}
	 * @see #totalRelics
	 * @see #numRelics
	 */
	public String Scoreboard() {
		String out = "Relics collected /" + (totalRelics - 1) + ":";
		out += "\n- Green  " + (numRelics[0] - 1);
		out += "\n- Yellow " + (numRelics[1] - 1);
		out += "\n- Red    " + (numRelics[2] - 1);
		out += "\n- Blue   " + (numRelics[3] - 1);

		return out;
	}

	/**
	 * sliding phase of the game that repeats until floating tile is placed into
	 * maze allows users to rotates the floating tile with their input allows users
	 * to slide floating tile into maze at the position they inputed.
	 * 
	 * @param p   the player represented as the color they are
	 * @param m   the maze object
	 * @param nrg number that allows if the print function should work
	 * @see MovingMaze#floater
	 * @see Tile#Rotate(int n)
	 * @see Tile#setPlayer(int p, String to)
	 */
	public void Sliding(String p, Maze m, int nrg) {

		while (true) {
			String move = "";
			if (nrg == 1) {
				System.out.print("\n[" + p + "] Rotate and slide the floating tile:\n> ");
				move = StdIn.readString();
			} else {
				move = p;
			}

			if (move.equals("r")) {
				if (nrg == 1) {
					System.out.println("Rotating right.");
				}
				MovingMaze.floater.Rotate(1);
				if (nrg == 1) {
					System.out.println("\n" + m.GameBoard());
				}

			} else if (move.equals("l")) {
				if (nrg == 1) {
					System.out.println("Rotating left.");
				}

				MovingMaze.floater.Rotate(-1);
				if (nrg == 1) {
					System.out.println("\n" + m.GameBoard());
				}

			} else if (move.equals("quit")) {
				if (nrg == 1) {
					System.out.println("Game has been quit.");
					System.out.println(Scoreboard());
				}
				System.exit(0);
			} else {
				String side = move.substring(0, 1);
				int num = Integer.parseInt(move.substring(1, 2)) - 1;

				if ((num + 1) % 2 != 0) {
					if (nrg == 1) {
						System.out.print("Cannot slide into odd positions.");
					}
					continue;
				}

				if (move.equals(lastFloatPosition)) {
					if (nrg == 1) {
						System.out.print("Cannot slide into last exit point.");
					}
					continue;
				}

				if (side.equals("n")) {
					Tile last = board[length - 1][num];
					for (int y = length - 2; y >= 0; y--) {
						board[y + 1][num] = board[y][num];
					}
					board[0][num] = MovingMaze.floater;
					for (int b = 0; b < 4; b++) {
						board[0][num].setPlayer(b, last.getPlayer(b));
					}
					MovingMaze.floater = last;
					lastFloatPosition = "s" + (num + 1);

				} else if (side.equals("s")) {
					Tile first = board[0][num];
					for (int y = 1; y < length; y++) {
						board[y - 1][num] = board[y][num];
					}
					board[length - 1][num] = MovingMaze.floater;
					for (int b = 0; b < 4; b++) {
						board[length - 1][num].setPlayer(b, first.getPlayer(b));
					}
					MovingMaze.floater = first;
					lastFloatPosition = "n" + (num + 1);

				} else if (side.equals("e")) {
					Tile first = board[num][0];
					for (int x = 1; x < width; x++) {
						board[num][x - 1] = board[num][x];
					}
					board[num][width - 1] = MovingMaze.floater;
					for (int b = 0; b < 4; b++) {
						board[num][width - 1].setPlayer(b, first.getPlayer(b));
					}
					MovingMaze.floater = first;
					lastFloatPosition = "w" + (num + 1);

				} else if (side.equals("w")) {
					Tile last = board[num][width - 1];
					for (int x = width - 2; x >= 0; x--) {
						board[num][x + 1] = board[num][x];
					}
					board[num][0] = MovingMaze.floater;
					for (int b = 0; b < 4; b++) {
						board[num][0].setPlayer(b, last.getPlayer(b));
					}
					MovingMaze.floater = last;
					lastFloatPosition = "e" + (num + 1);
				}
				if (nrg == 1) {
					System.out.println("Inserting at " + move + ".");
				}

				checkRelic(board, 0,  nrg, m);

				break;
			}
		}
	}

	/**
	 * moving phase of the game that repeats until relic is collected or player
	 * inputs quit allows users to move their player through the maze
	 * 
	 * @param p      the player represented as the color they are
	 * @param m      the maze object
	 * @param num    number representing the player that is currently playing
	 * @param player the player but just as the the first letter
	 * @param ssg    number that allows if the print function should work
	 * @see MovingMaze#floater
	 * @see Tile#setPlayer(int p, String to)
	 */
	public void Moving(String p, int num, String player, Maze m, int ssg) {

		boolean placing = true;
		while (placing) {
			String move = "";
			Tile cur = new Tile("XXXX", null, "", "");

			if (ssg == 1) {
				System.out.print("\n[" + p + "] Move your adventurer:\n> ");
				move = StdIn.readString();

				for (int y = 0; y < length; y++) {
					for (int x = 0; x < width; x++) {
						if (board[y][x].getPlayer(num).equals(" ")) {
							continue;
						} else {
							cur = board[y][x];
							curX = x;
							curY = y;
						}
					}
				}
			} else {
				move = p;
				cur = board[curY][curX];
			}

			switch (move) {
			case "n":

				if (cur.getUp().equals("1") && curY == 0) {
					if (ssg == 1) {
						System.out.print("Cannot move north: off the board.");
					}
					break;
				} else if (cur.getUp().equals("0") && curY == 0) {
					if (ssg == 1) {
						System.out.print("Cannot move north: no path.");
					}
					break;
				}

				if (cur.isUpOpen(board[curY - 1][curX])) {
					if (ssg == 1) {
						System.out.println("Moving north.\n");
					}
					cur.setPlayer(num, " ");
					board[curY][curX] = cur;
					board[curY - 1][curX].setPlayer(num, player);
					count++;
					curY--;

					if (checkRelic(board, 1, ssg, m) == true) {
						placing = false;
						break;
					}

					Win(board);

				} else {
					if (ssg == 1) {
						System.out.print("Cannot move north: no path.");
					}
				}
				break;
			case "e":

				if (cur.getRight().equals("1") && curX == width - 1) {
					if (ssg == 1) {
						System.out.print("Cannot move east: off the board.");
					}
					break;
				} else if (cur.getRight().equals("0") && curX == width - 1) {
					if (ssg == 1) {
						System.out.print("Cannot move east: no path.");
					}
					break;
				}

				if (cur.isRightOpen(board[curY][curX + 1])) {
					if (ssg == 1) {
						System.out.println("Moving east.\n");
					}
					cur.setPlayer(num, " ");
					board[curY][curX] = cur;
					board[curY][curX + 1].setPlayer(num, player);
					count++;
					curX++;
					if (checkRelic(board, 1, ssg, m) == true) {
						placing = false;
						break;
					}

					Win(board);

				} else {
					if (ssg == 1) {
						System.out.print("Cannot move east: no path.");
					}
				}
				break;
			case "s":

				if (cur.getDown().equals("1") && curY == length - 1) {
					if (ssg == 1) {
						System.out.print("Cannot move south: off the board.");
					}
					break;
				} else if (cur.getDown().equals("0") && curY == length - 1) {
					if (ssg == 1) {
						System.out.print("Cannot move south: no path.");
					}
					break;
				}

				if (cur.isDownOpen(board[curY + 1][curX])) {
					if (ssg == 1) {
						System.out.println("Moving south.\n");
					}
					cur.setPlayer(num, " ");
					board[curY][curX] = cur;
					board[curY + 1][curX].setPlayer(num, player);
					count++;
					curY++;
					if (checkRelic(board, 1, ssg, m) == true) {
						placing = false;
						break;
					}

					Win(board);

				} else {
					if (ssg == 1) {
						System.out.print("Cannot move south: no path.");
					}
				}
				break;
			case "w":
				if (cur.getLeft().equals("1") && curX == 0) {
					if (ssg == 1) {
						System.out.print("Cannot move west: off the board.");
					}
					break;
				} else if (cur.getLeft().equals("0") && curX == 0) {
					if (ssg == 1) {
						System.out.print("Cannot move west: no path.");
					}
					break;
				}
				if (cur.isLeftOpen(board[curY][curX - 1])) {
					if (ssg == 1) {
						System.out.println("Moving west.\n");
					}
					cur.setPlayer(num, " ");
					board[curY][curX] = cur;
					board[curY][curX - 1].setPlayer(num, player);
					count++;
					curX--;
					if (checkRelic(board, 1, ssg, m) == true) {
						placing = false;
						break;
					}

					Win(board);

				} else {
					if (ssg == 1) {
						System.out.print("Cannot move west: no path.");
					}

				}
				break;
			case "quit":
				if (ssg == 1) {
					System.out.println("Game has been quit.");
					System.out.println(Scoreboard());
				}
				System.exit(0);
			case "done":
				break;
			default:
				int x = Integer.parseInt(move.substring(0, 1)) - 1;
				int y = Integer.parseInt(move.substring(2)) - 1;
				isPath = false;
				for (int j = 0; j < length; j++) {
					for (int b = 0; b < width; b++) {
						pathFinderArr[j][b] = false;
					}
				}

				if (x >= 0 && x < width && y >= 0 && y < length) {

					if (pathFinder(curX, curY, x, y) == true) {

						if (ssg == 1) {
							System.out.println("Moving to " + (x + 1) + "," + (y + 1) + ".\n");
						}

						cur.setPlayer(num, " ");
						board[curY][curX] = cur;
						board[y][x].setPlayer(num, player);
						count++;
						curX = x;
						curY = y;
						if (checkRelic(board, 1, ssg, m) == true) {
							placing = false;
							break;
						}

						Win(board);
					} else {
						if (ssg == 1) {
							System.out.print("Cannot move to " + (x + 1) + "," + (y + 1) + ": no path.");
						}
					}
				} else {
					if (ssg == 1) {
						System.out.print("Cannot move to " + (x + 1) + "," + (y + 1) + ": no path.");
					}
				}
			}

			if (move.equals("done")) {
				break;
			}
			if (ssg == 0) {
				break;
			}
		}
	}

	/**
	 * Checks if a player is on a tile that has a relic of same color of player and that is visible.
	 * Sets that relic to not be visible and then sets the next relic in line to be visible
	 * 
	 * @param board  2D array of tile objects
	 * @param m      the maze object
	 * @param num    number representing the phase at which this method is being called
	 * @param ssg    number that allows if the print function should work
	 * @see Tile#getRelic(int p, String to)
	 * @return  {@code out} which is either true if there is relic collection otherwise false
	 */
	public boolean checkRelic(Tile[][] board, int num, int ssg, Maze m) {

		boolean out = false;
		boolean cur = false;
		String arr[] = {"Green", "Yellow", "Red", "Blue"};
		for (int t = 0; t < 4; t++) {
			for (int y = 0; y < length; y++) {
				for (int x = 0; x < width; x++) {
					if (board[y][x].getRelic().isVisible() == true) {
						if (board[y][x].getRelic().getColour().equals(arr[t].substring(0, 1).toLowerCase()) && board[y][x].getPlayer(t).equals(arr[t].substring(0, 1))) {
							out = true;
							numRelics[t]++;
							cur = true;
							board[y][x].getRelic().setVisible(false);
						}

					}
				}
			}

			for (int y = 0; y < length; y++) {
				for (int x = 0; x < width; x++) {
					if (board[y][x].getRelic().getNum().equals(numRelics[t] + "")
							&& board[y][x].getRelic().getColour().equals(arr[t].substring(0, 1).toLowerCase())) {
						board[y][x].getRelic().setVisible(true);
					}
				}
			}

			if (MovingMaze.floater.getRelic().getNum().equals(numRelics[t] + "")
					&& MovingMaze.floater.getRelic().getColour().equals(arr[t].substring(0, 1).toLowerCase())) {
				MovingMaze.floater.getRelic().setVisible(true);
			}

			if (ssg == 1) {
				if(num == 1 && out == true) {
				System.out.println(m.GameBoard());
				}
				if(out == true) {
				if(num == 1) {
					System.out.println("");
				}
				System.out.println(arr[t] + " has collected a relic.");
				}
				}

			if (numRelics[t] == totalRelics && cur == true) {
				if (ssg == 1) {
					System.out.println(arr[t] + " has all their relics.");
					if(num == 0) {
						System.out.println(Scoreboard());
						}
				}
			}
			
			if(out == true) {
				break;
			}
		}
		if (ssg == 1) {
			if(num == 0) {
			System.out.println("\n"+m.GameBoard());
			} 
			if(num == 1 && out == false){
				System.out.println(m.GameBoard());	
			}
			}
		
		return out;

	}

	/**
	 * checks to see if the player has won the game by collecting all their relics
	 * and returned to their starting position, then exits program if true
	 * 
	 * @param board the 2D array of tile objects
	 */
	public void Win(Tile[][] board) {
		if (totalRelics == 1) {

		} else {
			if (numRelics[0] == totalRelics && board[0][0].getPlayer(0).equals("G")) {
				System.out.println("\nGreen has won.");
				System.out.println(Scoreboard());
				System.exit(0);
			} else if (numRelics[1] == totalRelics && board[0][width - 1].getPlayer(1).equals("Y")) {
				System.out.println("\nYellow has won.");
				System.out.println(Scoreboard());
				System.exit(0);
			} else if (numRelics[2] == totalRelics && board[length - 1][0].getPlayer(2).equals("R")) {
				System.out.println("\nRed has won.");
				System.out.println(Scoreboard());
				System.exit(0);
			} else if (numRelics[3] == totalRelics && board[length - 1][width - 1].getPlayer(3).equals("B")) {
				System.out.println("\nBlue has won.");
				System.out.println(Scoreboard());
				System.exit(0);
			}
		}

	}

	/**
	 * Checks if the number of moves taken is equal to the number given in the parameter
	 * 
	 * @param n the number given to compare with the number of moves
	 * @see count
	 * @return  {@code true} if the number of moves taken is equal to n
	 */
	public boolean checkNum(int n) {
		if (count == n) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * checks to see if a active player is able to move from its current position to position inputed by user via the maze
	 * 
	 * @param x  current column of active player
	 * @param y  current row of active player
	 * @param endX column that active player wants to go
	 * @param endY row that active player wants to go
	 * @see Tile.isUpOpen(Tile t)
	 * @see Tile.isDownOpen(Tile t)
	 * @see Tile.isRightOpen(Tile t)
	 * @see Tile.isLeftOpen(Tile t)
	 * @see Tile.isUpOpen(Tile t)
	 * @see board
	 * @return  {@code true} if there is a path available from active player to the end position given 
	 */
	public static boolean pathFinder(int x, int y, int endX, int endY) {
		pathFinderArr[y][x] = true;

		if (board[y][x] == board[endY][endX]) { // base class
			isPath = true;
			return true;
		}

		if (y >= 1 && isPath == false) {
			if (board[y][x].isUpOpen(board[y - 1][x]) && pathFinderArr[y - 1][x] == false) {
				pathFinder(x, y - 1, endX, endY);
			}
		}

		if (x <= width - 2 && isPath == false) {
			if (board[y][x].isRightOpen(board[y][x + 1]) && pathFinderArr[y][x + 1] == false) {
				pathFinder(x + 1, y, endX, endY);
			}
		}

		if (y <= length - 2 && isPath == false) {
			if (board[y][x].isDownOpen(board[y + 1][x]) && pathFinderArr[y + 1][x] == false) {
				pathFinder(x, y + 1, endX, endY);
			}
		}

		if (x >= 1 && isPath == false) {
			if (board[y][x].isLeftOpen(board[y][x - 1]) && pathFinderArr[y][x - 1] == false) {
				pathFinder(x - 1, y, endX, endY);
			}
		}

		if (isPath == false) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * function that allows the game to be played
	 * 
	 * @see MovingMaze#floater
	 * @see Game#Sliding(String p, Maze m, int nrg)
	 * @see Game#Moving(String p, int num, String player, Maze m, int ssg)
	 */
	public void Play() {

		if (mode.equals("gui")) {
			Gui gui = new Gui(width, length, board);
			gui.DisplayGui();

			boolean running = true;
			String players[] = { "Green", "Yellow", "Red", "Blue" };

			while (running) {
				for (int p = 0; p < 4; p++) {

					while (Gui.won == true) {
						System.out.print("");
					}
					ActivePLayer = players[p];
					while (sliding == true) {
						System.out.print("");
					}
					ActivePLayerNum = p;
					while (sliding == false) {
						System.out.print("");
					}

				}
			}

		} else {

			Maze m = new Maze(width, length, board);
			System.out.println("--------------------------------------------------");
			System.out.println("Moving Maze");
			System.out.println("Relic goal: " + (totalRelics - 1));
			System.out.println("--------------------------------------------------\n");
			System.out.println(m.GameBoard());

			boolean running = true;
			String players[] = { "Green", "Yellow", "Red", "Blue" };
			String player = "";
			while (running) {
				for (int p = 0; p < 4; p++) {
					switch (players[p]) {
					case "Green":
						player = "G";
						break;
					case "Yellow":
						player = "Y";
						break;
					case "Red":
						player = "R";
						break;
					case "Blue":
						player = "B";
						break;
					}
					ActivePLayerNum = p;
					Sliding(players[p], m, 1);
					Moving(players[p], p, player, m, 1);
					System.out.println("End of " + players[p] + "'s turn.");
					System.out.println(Scoreboard());
					System.out.println("\n" + m.GameBoard());
				}
			}
		}
	}

}
