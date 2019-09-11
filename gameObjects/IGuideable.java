package a4.gameObjects;
/* Author: Gary Vang
 * 
 * Assignment: 1
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */

/**
 * This interface is for Catcher type objects.
 * It takes in a direction: d (down), u (up), l (left), r (right).
 * Depending on the direction, the net will be moved accordingly.
 * 
 * @author Gary
 *
 */
public interface IGuideable 
{
	/**
	 * Concrete implementation will be needed by net.
	 */
	public void guide(char direction);
}
