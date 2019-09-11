package a4.gameObjects;
/* Author: Gary Vang
 * 
 * Assignment: 1
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * This class is the base class for all of the game objects and contains fields and methods that
 * are common amongst all the objects.
 * 
 * All game objects have a:
 * Location - represented in x, y
 * Color - base on what they are
 * Size - depends on the object
 * 
 * Initialization of fields will be done by its subclasses.
 * 
 * The methods in this class are either setters or getters.
 * 
 * @author Gary
 *
 */

public abstract class GameObject implements IDrawable
{//Remove private double x,y;
	/**
	 * Declaration of fields.
	 */
	private Point2D location;
	private Color color;
	private int size;
	
//	public GameObject()
//	{
//		color = new Color(0,0,0);
//		location = new Point2D.Double(0.0,0.0);
//	}
	
	/**
	 * Returns the location of an object.
	 */
	public Point2D getLocation()
	{
		return location;
	}
	
	/**
	 * Returns the X value of a point.
	 */
	public Double getX()
	{
		return location.getX();
	}
	
	/**
	 * Returns the Y value of a point.
	 */
	public Double getY()
	{
		return location.getY();
	}
	
	/**
	 * Sets a new location for an object using a new point.
	 */
	public void setLocation (Point2D newLoc)
	{
		location = newLoc;
	}
	
	/**
	 * Returns an object's color.
	 */
	public Color getColor()
	{
		return color;
	}
	
	/**
	 * Sets a new color for an object using a new color.
	 */
	public void setColor(Color newColor)
	{
		color = newColor;
	}
	
	/**
	 * Sets a new size for an object using a new size.
	 */
	public void setSize(int newSize)
	{
		size = newSize;
	}
	
	/**
	 * Returns an object's size.
	 */
	public int getSize()
	{
		return size;
	}
}
