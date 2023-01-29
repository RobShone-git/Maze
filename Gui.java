
/**
* This class is used to create the GUI of the game and display it
* @author Robert Shone
*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui extends JPanel implements KeyListener {

	private final int width;
	private final int length;
	private JFrame frame = new JFrame();;
	private GridBagConstraints c = new GridBagConstraints();
	private static int floatX = 0;
	private static int floatY = 0;
	private String lastFloatPosition = "";
	private int curX = 0;
	private int curY = 0;

	/**
	 * Declares a boolean if a player has won that can be accessed anywhere in the
	 * program.
	 */
	public static boolean won = false;

	/**
	 * Constructor that initializes objects and gives them attributes
	 * 
	 * @param w   the width of the board
	 * @param l   the length of the board
	 * @param arr the array of tile objects
	 * @see #width
	 * @see #length
	 * @see Game#board
	 * @see Game#ActivePLayerNum
	 * @see Game#ActivePLayer
	 */
	public Gui(int w, int l, Tile arr[][]) {
		width = w;
		length = l;
		frame.addKeyListener(this);
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = (int) (((e.getX() - 18) / 50) - (Math.ceil(12.0 / width)));
				int y = (int) (((e.getY() - 40) / 50) - (Math.ceil(12.0 / length)));

				Game.isPath = false;
				for (int j = 0; j < length; j++) {
					for (int b = 0; b < width; b++) {
						Game.pathFinderArr[j][b] = false;
					}
				}

				for (int r = 0; r < length; r++) {
					for (int s = 0; s < width; s++) {
						if (Game.board[r][s].getPlayer(Game.ActivePLayerNum).equals(" ")) {
							continue;
						} else {
							curX = s;
							curY = r;
						}
					}
				}

				if (x >= 0 && y >= 0 && x < width && y < length) {
					if (Game.pathFinder(curX, curY, x, y) == true && Game.sliding == false && won == false) {

						Game.board[curY][curX].setPlayer(Game.ActivePLayerNum, " ");
						Game.board[y][x].setPlayer(Game.ActivePLayerNum,
								Game.ActivePLayer.substring(0, 1).toUpperCase());
						curX = x;
						curY = y;
						if (checkRelic() == true) {
							Game.sliding = true;
						}
						Win();
						DisplayGui();
					}
				}
			}
		});
	}

	/**
	 * paint method that draws: the board of tiles, players, relics, and scoreboard
	 * on a JPanel
	 * 
	 * @param g Graphics method that will be called to draw
	 * @see #width
	 * @see #length
	 * @see Game#board
	 * @see Game#ActivePLayer
	 */
	public void paint(Graphics g) {
		super.paint(g);
		BufferedImage border = null;
		try {
			border = ImageIO.read(new File("src/TilePics/"+"blank.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int j = 0; j < width + 2; j++) { // border right
			g.drawImage(border, 50 * (j) + (10 / width) * (600 / 11), 50 * (0) + (10 / length) * (600 / 11), null);
			g.drawImage(border, 50 * (j) + (10 / width) * (600 / 11), 50 * (length + 1) + (10 / length) * (600 / 11),
					null);
			if (j % 2 == 0 && j != 0 && j != width + 1) {
				g.setColor(Color.GRAY);
				g.fillRect(50 * (j) + (10 / width) * (600 / 11) + 19, 50 * (0) + (10 / length) * (600 / 11) + 19, 12,
						12);
				g.fillRect(50 * (j) + (10 / width) * (600 / 11) + 19,
						50 * (length + 1) + (10 / length) * (600 / 11) + 19, 12, 12);
			}
		}
		for (int y = 0; y < length; y++) { // boarder down
			g.drawImage(border, 50 * (0) + (10 / width) * (600 / 11), 50 * (y + 1) + (10 / length) * (600 / 11), null);
			g.drawImage(border, 50 * (width + 1) + (10 / width) * (600 / 11), 50 * (y + 1) + (10 / length) * (600 / 11),
					null);
			if (y % 2 == 1 && y != 0 && y != length + 1) {
				g.setColor(Color.GRAY);
				g.fillRect(50 * (0) + (10 / width) * (600 / 11) + 19, 50 * (y + 1) + (10 / length) * (600 / 11) + 19,
						12, 12);
				g.fillRect(50 * (width + 1) + (10 / width) * (600 / 11) + 19,
						50 * (y + 1) + (10 / length) * (600 / 11) + 19, 12, 12);
			}
		}

		for (int y = 0; y < length; y++) {
			for (int x = 0; x < width; x++) {
				BufferedImage tile = null;
				try {
					tile = ImageIO.read(new File("src/TilePics/"+setTile(Game.board[y][x].getType())));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.drawImage(tile, 50 * (x + 1) + (10 / width) * (600 / 11), 50 * (y + 1) + (10 / length) * (600 / 11),
						null);
				c.gridx = 1 + x;
				c.gridy = 1 + y;

			}
		}

		BufferedImage floatingTile = null;
		try {
			floatingTile = ImageIO.read(new File("src/TilePics/"+setTile(MovingMaze.floater.getType())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(floatingTile, 50 * (floatX) + (10 / width) * (600 / 11), 50 * (floatY) + (10 / length) * (600 / 11),
				null);

		if (MovingMaze.floater.getRelic().isVisible()) {
			switch (MovingMaze.floater.getRelic().getColour()) {
			case "g":
				g.setColor(Color.GREEN);
				break;
			case "y":
				g.setColor(Color.YELLOW);
				break;
			case "r":
				g.setColor(Color.RED);
				break;
			case "b":
				g.setColor(Color.BLUE);
				break;
			default:
				break;
			}
			g.fillRect(50 * (floatX - 1) + (10 / width) * (600 / 11) + 69,
					50 * (floatY - 1) + (10 / length) * (600 / 11) + 69, 12, 12);
		}

		for (int y = 0; y < length; y++) {
			for (int x = 0; x < width; x++) {
				if (Game.board[y][x].getPlayer(0).equals("G")) {
					g.setColor(Color.GREEN);
					g.fillOval(50 * (x) + (10 / width) * (600 / 11) + 51, 50 * (y) + (10 / length) * (600 / 11) + 51,
							15, 15);
				}

				if (Game.board[y][x].getPlayer(1).equals("Y")) {
					g.setColor(Color.YELLOW);
					g.fillOval(50 * (x) + (10 / width) * (600 / 11) + 83, 50 * (y) + (10 / length) * (600 / 11) + 51,
							15, 15);
				}
				if (Game.board[y][x].getPlayer(2).equals("R")) {
					g.setColor(Color.RED);
					g.fillOval(50 * (x) + (10 / width) * (600 / 11) + 51, 50 * (y) + (10 / length) * (600 / 11) + 83,
							15, 15);
				}
				if (Game.board[y][x].getPlayer(3).equals("B")) {
					g.setColor(Color.BLUE);
					g.fillOval(50 * (x) + (10 / width) * (600 / 11) + 83, 50 * (y) + (10 / length) * (600 / 11) + 83,
							15, 15);
				}

				if (Game.board[y][x].getRelic().isVisible() == true) {
					if (Game.board[y][x].getRelic().getColour().equals("g")) {
						g.setColor(Color.GREEN);
						g.fillRect(50 * (x) + (10 / width) * (600 / 11) + 69,
								50 * (y) + (10 / length) * (600 / 11) + 69, 12, 12);
					}
					if (Game.board[y][x].getRelic().getColour().equals("y")) {
						g.setColor(Color.YELLOW);
						g.fillRect(50 * (x) + (10 / width) * (600 / 11) + 69,
								50 * (y) + (10 / length) * (600 / 11) + 69, 12, 12);
					}
					if (Game.board[y][x].getRelic().getColour().equals("r")) {
						g.setColor(Color.RED);
						g.fillRect(50 * (x) + (10 / width) * (600 / 11) + 69,
								50 * (y) + (10 / length) * (600 / 11) + 69, 12, 12);
					}
					if (Game.board[y][x].getRelic().getColour().equals("b")) {
						g.setColor(Color.BLUE);
						g.fillRect(50 * (x) + (10 / width) * (600 / 11) + 69,
								50 * (y) + (10 / length) * (600 / 11) + 69, 12, 12);
					}
				}

			}
		}
		if (Game.sliding == true) {
			g.setColor(Color.BLACK);
			g.drawString(Game.ActivePLayer + "'s turn to slide", 240, 50);
		} else if (won == true) {
			g.setColor(Color.BLACK);
			g.drawString(Game.ActivePLayer + " has won!!", 260, 50);
		} else {
			g.setColor(Color.BLACK);
			g.drawString(Game.ActivePLayer + "'s turn to move", 240, 50);
		}

		g.setColor(Color.GREEN);
		g.drawString("Green   " + (Game.numRelics[0] - 1), 170, 520);
		g.setColor(Color.YELLOW);
		g.drawString("Yellow   " + (Game.numRelics[1] - 1), 360, 520);
		g.setColor(Color.RED);
		g.drawString("Red       " + (Game.numRelics[2] - 1), 170, 540);
		g.setColor(Color.BLUE);
		g.drawString("Blue       " + (Game.numRelics[3] - 1), 360, 540);

	}

	/**
	 * Relates the type of the tile to the same tile png
	 * 
	 * @param t the type of the tile
	 * @see MovingMaze#floater
	 * @return {@code *} where the star represents the correct name of the png that
	 *         relates to the type of tile
	 */
	public String setTile(String t) {

		switch (t) {
		case "0101":
			return "EW.png";
		case "1010":
			return "NS.png";
		case "0110":
			return "ES.png";
		case "0011":
			return "SW.png";
		case "1100":
			return "NE.png";
		case "1001":
			return "NW.png";
		case "1110":
			return "NES.png";
		case "1011":
			return "NSW.png";
		case "0111":
			return "ESW.png";
		case "1101":
			return "NEW.png";
		case "1111":
			return "NESW.png";
		default:
			return "blank.png";
		}
	}

	/**
	 * creates the JFrame and adds the painted JPanel to the frame and sets the
	 * frame to visible
	 * 
	 * @see #frame
	 */
	public void DisplayGui() {

		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new Gui(width, length, Game.board));
		frame.setVisible(true);

	}

	/**
	 * Checks if a player is on a tile that has a relic of same color of player and
	 * that is visible. Sets that relic to not be visible and then sets the next
	 * relic in line to be visible
	 * 
	 * @see Tile#getRelic(int p, String to)
	 * @see Game#board
	 * @see MovingMaze#floater
	 * @return {@code out} which is either true if there is relic collection
	 *         otherwise false
	 */
	public boolean checkRelic() {
		boolean out = false;
		String arr[] = { "Green", "Yellow", "Red", "Blue" };
		for (int t = 0; t < 4; t++) {
			for (int y = 0; y < length; y++) {
				for (int x = 0; x < width; x++) {
					if (Game.board[y][x].getRelic().isVisible() == true) {
						if (Game.board[y][x].getRelic().getColour().equals(arr[t].substring(0, 1).toLowerCase())
								&& Game.board[y][x].getPlayer(t).equals(arr[t].substring(0, 1))) {
							out = true;
							Game.numRelics[t]++;
							Game.board[y][x].getRelic().setVisible(false);
						}

					}
				}
			}

			for (int y = 0; y < length; y++) {
				for (int x = 0; x < width; x++) {
					if (Game.board[y][x].getRelic().getNum().equals(Game.numRelics[t] + "")
							&& Game.board[y][x].getRelic().getColour().equals(arr[t].substring(0, 1).toLowerCase())) {
						Game.board[y][x].getRelic().setVisible(true);
					}
				}
			}

			if (MovingMaze.floater.getRelic().getNum().equals(Game.numRelics[t] + "")
					&& MovingMaze.floater.getRelic().getColour().equals(arr[t].substring(0, 1).toLowerCase())) {
				MovingMaze.floater.getRelic().setVisible(true);
			}
		}
		return out;
	}

	/**
	 * checks to see if the player has won the game by collecting all their relics
	 * and returned to their starting position, then sets won variable to true
	 * 
	 * @see Game#board the 2D array of tile objects
	 * @see #width
	 * @see #length
	 * @see Game#numRelics
	 * @see Game#totalRelics
	 */
	public void Win() {
		if (Game.totalRelics == 1) {

		} else {
			if (Game.numRelics[0] == Game.totalRelics && Game.board[0][0].getPlayer(0).equals("G")) {
				won = true;
			} else if (Game.numRelics[1] == Game.totalRelics && Game.board[0][width - 1].getPlayer(1).equals("Y")) {
				won = true;
			} else if (Game.numRelics[2] == Game.totalRelics && Game.board[length - 1][0].getPlayer(2).equals("R")) {
				won = true;
			} else if (Game.numRelics[3] == Game.totalRelics
					&& Game.board[length - 1][width - 1].getPlayer(3).equals("B")) {
				won = true;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	/**
	 * Checks the key that was released on the keyboard and changes the board array
	 * and displays updated gui depending on inputs
	 * 
	 * @param e the KeyEvent method to capture keys released
	 * @see #width
	 * @see #length
	 * @see Game#numRelics
	 * @see Game#totalRelics
	 * @see Game#board
	 * @see Game#ActivePLayerNum
	 * @see Game#ActivePLayer
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (won == false) {
			if (Game.sliding == true) {
				switch (e.getKeyChar()) {
				case 'r':
					MovingMaze.floater.Rotate(1);
					DisplayGui();
					break;
				case 'l':
					MovingMaze.floater.Rotate(-1);
					DisplayGui();
					break;
				case 'w':

					if (floatY != 0) {
						if ((floatX == 0 || floatX == width + 1)) {
							floatY--;
							DisplayGui();

						} else {

							if ((floatX) % 2 != 0) {
								break;
							}

							if (("s" + (floatX - 1)).equals(lastFloatPosition)) {
								break;
							}

							Tile first = Game.board[0][floatX - 1];
							for (int y = 1; y < length; y++) {
								Game.board[y - 1][floatX - 1] = Game.board[y][floatX - 1];
							}
							Game.board[length - 1][floatX - 1] = MovingMaze.floater;
							for (int b = 0; b < 4; b++) {
								Game.board[length - 1][floatX - 1].setPlayer(b, first.getPlayer(b));
							}
							MovingMaze.floater = first;
							lastFloatPosition = "n" + (floatX - 1);

							floatY = 0;
							Game.sliding = false;

						}
					}

					break;
				case 'a':

					if (floatX != 0) {
						if ((floatY == 0 || floatY == length + 1)) {

							floatX--;
							DisplayGui();
						} else {

							if ((floatY) % 2 != 0) {
								break;
							}

							if (("e" + (floatY)).equals(lastFloatPosition)) {
								break;
							}

							Tile first = Game.board[floatY - 1][0];
							for (int x = 1; x < width; x++) {
								Game.board[floatY - 1][x - 1] = Game.board[floatY - 1][x];
							}
							Game.board[floatY - 1][width - 1] = MovingMaze.floater;
							for (int b = 0; b < 4; b++) {
								Game.board[floatY - 1][width - 1].setPlayer(b, first.getPlayer(b));
							}
							MovingMaze.floater = first;
							lastFloatPosition = "w" + (floatY);

							floatX = 0;
							Game.sliding = false;

						}
					}
					break;
				case 's':

					if (floatY != length + 1) {
						if ((floatX == 0 || floatX == width + 1)) {

							floatY++;
							DisplayGui();

						} else {

							if ((floatX) % 2 != 0) {
								break;
							}

							if (("n" + (floatX - 1)).equals(lastFloatPosition)) {
								break;
							}

							Tile last = Game.board[length - 1][floatX - 1];
							for (int y = length - 2; y >= 0; y--) {
								Game.board[y + 1][floatX - 1] = Game.board[y][floatX - 1];
							}
							Game.board[0][floatX - 1] = MovingMaze.floater;
							for (int b = 0; b < 4; b++) {
								Game.board[0][floatX - 1].setPlayer(b, last.getPlayer(b));
							}
							MovingMaze.floater = last;
							lastFloatPosition = "s" + (floatX - 1);

							floatY = length + 1;
							Game.sliding = false;

						}
					}
					break;
				case 'd':

					if (floatX != width + 1) {
						if ((floatY == 0 || floatY == length + 1)) {

							floatX++;
							DisplayGui();
						} else {

							if ((floatY) % 2 != 0) {
								break;
							}

							if (("w" + (floatY)).equals(lastFloatPosition)) {
								break;
							}

							Tile last = Game.board[floatY - 1][width - 1];
							for (int x = width - 2; x >= 0; x--) {
								Game.board[floatY - 1][x + 1] = Game.board[floatY - 1][x];
							}
							Game.board[floatY - 1][0] = MovingMaze.floater;
							for (int b = 0; b < 4; b++) {
								Game.board[floatY - 1][0].setPlayer(b, last.getPlayer(b));
							}
							MovingMaze.floater = last;
							lastFloatPosition = "e" + (floatY);

							floatX = width + 1;
							Game.sliding = false;

						}
					}
					break;
				case 'q':
					System.exit(0);

				}

				checkRelic();
				DisplayGui();

			} else {

				Tile cur = new Tile("XXXX", null, "", "");

				for (int y = 0; y < length; y++) {
					for (int x = 0; x < width; x++) {
						if (Game.board[y][x].getPlayer(Game.ActivePLayerNum).equals(" ")) {
							continue;
						} else {
							cur = Game.board[y][x];
							curX = x;
							curY = y;
						}
					}
				}

				switch (e.getKeyChar()) {
				case 'w':

					if (cur.getUp().equals("1") && curY == 0) {// off board up
						break;
					} else if (cur.getUp().equals("0") && curY == 0) { // no path up
						break;
					}

					if (cur.isUpOpen(Game.board[curY - 1][curX])) {
						cur.setPlayer(Game.ActivePLayerNum, " ");
						Game.board[curY][curX] = cur;
						Game.board[curY - 1][curX].setPlayer(Game.ActivePLayerNum,
								Game.ActivePLayer.substring(0, 1).toUpperCase());
						Game.count++;
						curY--;

						DisplayGui();

						if (checkRelic() == true) {
							Game.sliding = true;
							break;
						}

						Win();

					}
					break;
				case 'd':

					if (cur.getRight().equals("1") && curX == width - 1) {
						break;
					} else if (cur.getRight().equals("0") && curX == width - 1) {
						break;
					}

					if (cur.isRightOpen(Game.board[curY][curX + 1])) {
						cur.setPlayer(Game.ActivePLayerNum, " ");
						Game.board[curY][curX] = cur;
						Game.board[curY][curX + 1].setPlayer(Game.ActivePLayerNum,
								Game.ActivePLayer.substring(0, 1).toUpperCase());
						Game.count++;
						curX++;

						DisplayGui();

						if (checkRelic() == true) {
							Game.sliding = true;
							break;
						}

						Win();

					}
					break;
				case 's':

					if (cur.getDown().equals("1") && curY == length - 1) {
						break;
					} else if (cur.getDown().equals("0") && curY == length - 1) {
						break;
					}

					if (cur.isDownOpen(Game.board[curY + 1][curX])) {
						cur.setPlayer(Game.ActivePLayerNum, " ");
						Game.board[curY][curX] = cur;
						Game.board[curY + 1][curX].setPlayer(Game.ActivePLayerNum,
								Game.ActivePLayer.substring(0, 1).toUpperCase());
						Game.count++;
						curY++;

						DisplayGui();

						if (checkRelic() == true) {
							Game.sliding = true;
							break;
						}

						Win();

					}
					break;
				case 'a':
					if (cur.getLeft().equals("1") && curX == 0) {
						break;
					} else if (cur.getLeft().equals("0") && curX == 0) {
						break;
					}
					if (cur.isLeftOpen(Game.board[curY][curX - 1])) {
						cur.setPlayer(Game.ActivePLayerNum, " ");
						Game.board[curY][curX] = cur;
						Game.board[curY][curX - 1].setPlayer(Game.ActivePLayerNum,
								Game.ActivePLayer.substring(0, 1).toUpperCase());
						Game.count++;
						curX--;

						DisplayGui();

						if (checkRelic() == true) {
							Game.sliding = true;
							break;
						}

						Win();

					}
					break;
				case 'q':
					System.exit(0);
				case 'n':
					Game.sliding = true;
					DisplayGui();

				}
			}
		}
	}
}
