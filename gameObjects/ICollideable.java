package a4.gameObjects;
/* Author: Gary Vang
 * 
 * Assignment: 3
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */

/**
 * This interface defines the methods provided by an object that is "Collideable."
 * 
 * @author Gary
 *
 */
public interface ICollideable 
{
	//A way for an object to determine if it collided with another object
	public boolean collidesWith(ICollideable otherObject);
	
	//A way for an object to handle collisions
	public void handleCollision(ICollideable otherObject);
	
	//A way for an object to keep track of objects that it has already collided with
	public boolean inColList(ICollideable otherObject);
	
	//A way to add to the list of objects that it has collided with
	public void addToColList(ICollideable otherObject);
	
	//A way to remove objects from the collided with list
	public void removeFromColList(ICollideable otherObject);
}