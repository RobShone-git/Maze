/**
 * Creates Relic objects and allows them to use methods.
 * 
 * @author Robert Shone
 */
public class Relic {

	private final String colour;
	private final String num;
	private boolean visible;

	/**
	 * Constructor that initializes objects and gives them attributes
	 * 
	 * @param c color of the relic
	 * @param n number that the relic is visible on
	 * @see #colour
	 * @see #num
	 * @see #visible
	 */
	public Relic(String c, String n) {
		colour = c;
		num = n;
		if (num.equals("1")) {
			visible = true;
		} else {
			visible = false;
		}

	}

	/**
	 * returns the color of the relic
	 * 
	 * @return {@code colour}
	 * @see #colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * returns if the relic is visible or not
	 * 
	 * @return {@code visible}
	 * @see #visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * returns the number that the relic is visible on
	 * 
	 * @return {@code num}
	 * @see #num
	 */
	public String getNum() {
		return num;
	}

	/**
	 * returns if there is a relic or not by checking the color
	 * 
	 * @return {@code true} if there is a relic
	 * @see #colour
	 */
	public boolean isRelic() {
		if (colour.equals("x")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * sets the visibility of the the relic to the boolean given
	 * 
	 * @param b the boolean that the visible variable is set to
	 * @see #visible
	 */
	public void setVisible(boolean b) {
		visible = b;
	}

}
