/**
* Creates Tile objects and allows them to use methods.
* @author Robert Shone
*/
public class Tile {

	private String type;
	private String up;
	private String down;
	private String left;
	private String right;
	private String player[];
	private Relic relic;
	
    /**
    * Constructor that initializes objects and gives them attributes 
    * @param  t type of tile
    * @param  p players that are on the tile
    * @param  c color of the relic that is on the tile
    * @param  n number that the relic is visible on 
    * @see Relic
    */
	public Tile(String t, String p[], String c, String n) {
		type = t;
		up = t.substring(0, 1);
		right = t.substring(1, 2);
		down = t.substring(2, 3);
		left = t.substring(3, 4);
		player = p;
		relic = new Relic(c, n);
	}
	
    /**
	 * returns the relic object of the tile
	 * 
	 * @return {@code relic}
	 */
	public Relic getRelic() {
		return relic;
	}
	
    /**
    * returns the player requested on the tile
    * @param  num  the number for the specific player in the array
    * @return  {@code player[num]}
    */
	public String getPlayer(int num) {
			return player[num];
		}
	
    /**
    * returns the type of the tile
    * @see #up
    * @return  {@code type}
    */
		public String getType() {
				return this.type;
			}
	
    /**
    * sets the player in the player array at position given to the new value that is also given.
    * @param  p  the number for the specific player in the array
    * @param  to  the new player that is inserted into position
    */
	public void setPlayer(int p, String to) {
		player[p] = to;
	}
	
    /**
    * returns the special character unique if tile is open in the up direction
    * @see Tile
    * @see Tile#specialCharacters(String t)
    * @return  {@code MovingMaze.PATH_NS} if up is open, otherwise return {@code " "}
    */
	public String goUp() {
		specialCharacters(type);
		if(up.equals("1")) {
			return MovingMaze.PATH_NS;
		}else {
			return " ";
		}
	}
	
    /**
    * returns the special character unique if tile is open in the down direction
    * @see Tile
    * @see Tile#specialCharacters(String t)
    * @return  {@code MovingMaze.PATH_NS} if down is open, otherwise return {@code " "}
    */
	public String goDown() {
		specialCharacters(type);
		if(down.equals("1")) {
			return MovingMaze.PATH_NS;
		}else {
			return " ";
		}
	}
	
    /**
    * returns the special character unique if tile is open in the left direction
    * @see Tile
    * @see Tile#specialCharacters(String t)
    * @return  {@code MovingMaze.PATH_EW+MovingMaze.PATH_EW+MovingMaze.PATH_EW} if left is open, otherwise return {@code " "}
    */
	public String goLeft() {
		specialCharacters(type);
		if(left.equals("1")) {
			return MovingMaze.PATH_EW+MovingMaze.PATH_EW+MovingMaze.PATH_EW;
		}else {
			return "   ";
		}
	}
	
    /**
    * returns the special character unique if tile is open in the right direction
    * @see Tile
    * @see Tile#specialCharacters(String t)
    * @return  {@code MovingMaze.PATH_EW+MovingMaze.PATH_EW+MovingMaze.PATH_EW} if left is open, otherwise return {@code " "}
    */
	public String goRight() {
		specialCharacters(type);
		if(right.equals("1")) {
			return MovingMaze.PATH_EW+MovingMaze.PATH_EW+MovingMaze.PATH_EW;
		}else {
			return "   ";
		}
	}
	
    /**
    * returns the String for the up variable
    * @see #up
    * @return  {@code up}
    */
	public String getUp() {
		return up;
	}
	
    /**
    * returns the String for the down variable
    * @see #down
    * @return  {@code down}
    */
	public String getDown() {
		return down;
	}
	
    /**
    * returns the String for the left variable
    * @see #left
    * @return  {@code left}
    */
	public String getLeft() {
		return left;
	}
	
    /**
    * returns the String for the right variable
    * @see #right
    * @return  {@code right}
    */
	public String getRight() {
		return right;
	}
	
    /**
    * Checks to see if the tile is open in the up direction and the tile given is open in the down direction.
    * @param  t  the tile to be inspected 
    * @return  {@code true} if the player can move up from this tile to the tile given
    * @see Tile#getDown()
    * @see Tile#getUp()
    */
	public boolean isUpOpen(Tile t) {
		specialCharacters(type);
		if(t.getDown().equals("1") && this.getUp().equals("1")) {
			return true;
		}else {
			return false;
		}
	}
	
    /**
    * Checks to see if the tile is open in the down direction and the tile given is open in the up direction.
    * @param  t  the tile to be inspected 
    * @return  {@code true} if the player can move down from this tile to the tile given
    * @see Tile#getDown()
    * @see Tile#getUp()
    */
	public boolean isDownOpen(Tile t) {
		specialCharacters(type);
		if(t.getUp().equals("1") && this.getDown().equals("1")) {
			return true;
		}else {
			return false;
		}
	}
	
    /**
    * Checks to see if the tile is open in the left direction and the tile given is open in the right direction.
    * @param  t  the tile to be inspected 
    * @return  {@code true} if the player can move left from this tile to the tile given
    * @see Tile#getRight()
    * @see Tile#getLeft()
    */
	public boolean isLeftOpen(Tile t) {
		specialCharacters(type);
		if(t.getRight().equals("1") && this.getLeft().equals("1")) {
			return true;
		}else {
			return false;
		}
	}
	
    /**
    * Checks to see if the tile is open in the right direction and the tile given is open in the left direction.
    * @param  t  the tile to be inspected 
    * @return  {@code true} if the player can move right from this tile to the tile given
    * @see Tile#getRight()
    * @see Tile#getLeft()
    */
	public boolean isRightOpen(Tile t) {
		specialCharacters(type);
		if(t.getLeft().equals("1") && this.getRight().equals("1")) {
			return true;
		}else {
			return false;
		}
	}
	
    /**
    * Takes string given and compares to cases in switch statement and returns special character and sets the up, down, left and right variables
    * @param  t  the String to be compared  
    * @return  {@code MovingMaze.PATH_**} with the stars representing the specific directions for each string type to return the special character
    * @see #up
    * @see #down
    * @see #left
    * @see #right
    */
	public String specialCharacters(String t) {
		switch(t) {
		  case "0101":
			  up = "0";
			  right = "1";
			  down = "0";
		      left = "1";
		      return MovingMaze.PATH_EW;
		  case "1010":
			  up = "1";
			  right = "0";
			  down = "1";
			  left = "0";
			  return MovingMaze.PATH_NS;
		  case "0110":
			  up = "0";
			  right = "1";
			  down = "1";
			  left = "0";
			  return MovingMaze.PATH_ES;
		  case "0011":
			  up = "0";
			  right = "0";
			  down = "1";
			  left = "1";
			  return MovingMaze.PATH_SW;
		  case "1100":
			  up = "1";
			  right = "1";
			  down = "0";
			  left = "0";
			  return MovingMaze.PATH_NE;
		  case "1001":
			  up = "1";
			  right = "0";
			  down = "0";
			  left = "1";
			  return MovingMaze.PATH_NW;
		  case "1110":
			  up = "1";
			  right = "1";
			  down = "1";
			  left = "0";
			  return MovingMaze.PATH_NES;
		  case "1011":
			  up = "1";
			  right = "0";
			  down = "1";
			  left = "1";
			  return MovingMaze.PATH_NSW;
		  case "0111":
			  up = "0";
			  right = "1";
			  down = "1";
			  left = "1";
			  return MovingMaze.PATH_ESW;
		  case "1101":
			  up = "1";
			  right = "1";
			  down = "0";
			  left = "1";
			  return MovingMaze.PATH_NEW;
		  case "1111":
			  up = "1";
			  right = "1";
			  down = "1";
			  left = "1";
			  return MovingMaze.PATH_NESW;
		  default:
		    return "Not path";
		}
	}
	
    /**
    * Rotates the tile either left or right depending on the integer given and sets the tile with a new type
    * @param  n  the integer that chooses which direction to rotate 
    * @return  {@code specialCharacters(***)} returns the special character that is the rotated version of the tile
    * @see Tile#specialCharacters(String t)
    */
	public String Rotate(int n) {
		int num = 0;
		for (int i = 0;i < type.length(); i++){
		    num = num + Integer.parseInt(type.charAt(i)+"");
		}
		
		String arr2[] = {"0110", "0011", "1001", "1100"};
		String arr3[] = {"1110", "0111", "1011", "1101"};
		
		if(num == 2) {
			if(type.equals("0101")) {
				type = "1010";
				return specialCharacters("1010");
			}else if(type.equals("1010")) {
				type = "0101";
				return specialCharacters("0101");
			}else {
				for(int x = 0; x < arr2.length; x++) {
					if(arr2[x].equals(type)) {
						int pos = x;
						pos = pos + n;
						if(pos == -1) {
							pos = 3;
						}else if(pos == 4) {
							pos = 0;
						}
						type = arr2[pos];
						return specialCharacters(arr2[pos]);
					}
				}
			}
			
		}else if(num == 3) {
			for(int x = 0; x < arr3.length; x++) {
				if(arr3[x].equals(type)) {
					int pos = x;
					pos = pos + n;
					if(pos == -1) {
						pos = 3;
					}else if(pos == 4) {
						pos = 0;
					}
					type = arr3[pos];
					return specialCharacters(arr3[pos]);
				}
			}
		}else {
			return toString();
		}
		return "Something went wrong";
	} 
	
	
    /**
    * returns the either the color of the relic if its visible or the special character of the tile if its not
    * @return  {@code relic.getColour()} if relic is visible for the tile and returns {@code specialCharacters(type)} if not visible
    * @see Relic
    * @see Relic#getColour()
    * @see Tile#specialCharacters(String t)
    */
	public String toString() {
		if(relic.isVisible() == true) {
			return relic.getColour();
		}else {
			return specialCharacters(type);
		}
		
	}
	
}
