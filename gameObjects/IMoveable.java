package a4.gameObjects;
/* Author: Gary Vang
 * 
 * Assignment: 4
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */

/**
 * This interface is for Animal type objects.
 * Objects will move depending on their current direction and speed.
 * 
 * At the end of the move method, objects will +/- values from the object's direction.
 * 
 * A4, move now takes in the elapsed time.
 * 
 * @author Gary
 *
 */
public interface IMoveable 
{
	/**
	 * Concrete implementation will be needed for dogs and cats.
	 */
	void move(int elapsedTime);
}
