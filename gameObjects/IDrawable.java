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

/**
 * This interface defines the methods provided by an object that is "Drawable."
 * 
 * @author Gary
 *
 */
public interface IDrawable 
{
	// A way for an object to draw itself
	public void draw(Graphics g);
}