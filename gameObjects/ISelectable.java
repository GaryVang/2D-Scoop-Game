package a4.gameObjects;
/* Author: Gary Vang
 * 
 * Assignment: 3
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.Graphics;
import java.awt.Point;

/** This interface defines the services (methods) provided
* by an object which is “Selectable” on the screen.
* 
* Description was taken from the powerpoint slides.
*/
public interface ISelectable 
{
	// a way to mark an object as “selected” or not
	public void setSelected(boolean yesNo);
	
	// a way to test whether an object is selected
	public boolean isSelected();
	
	// a way to determine if a mouse point is “in” an object
	public boolean contains(Point p);
	
	// a way to “draw” the object that knows about drawing
	// different ways depending on “isSelected”
	public void draw(Graphics g);
}