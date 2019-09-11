package a4.gameObjects;
/* Author: Gary Vang
 * 
 * Assignment: 4
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
/**
 * This class is a hierarchical object for the Dog object.
 * A dog can only have 1 body.
 * This class has its own affine transformation objects and
 * is drawn to its local coordinates.
 * 
 * Any transformations will be done by the parent.
 * 
 * @author Gary
 *
 */
public class Body 
{
	private int myRadius;
	private Color myColor;
	private AffineTransform myTranslation, myRotation, myScale;
	
	/**
	 * Default class constructor that sets the object's size, color
	 * and also initializes 3 affine objects.
	 */
	public Body()
	{
		myRadius = 1;
		myColor = new Color(0,0,128);
		myTranslation = new AffineTransform();
		myRotation = new AffineTransform();
		myScale = new AffineTransform();
	}
	
	/**
	 * Provides the parent with the ability to change its local
	 * coordinates.
	 * 
	 * @param degrees
	 */
	public void rotate(double degrees)
	{
		myRotation.rotate(Math.toRadians(degrees));
	}
	
	/**
	 * Provides the parent with the ability to change its local
	 * coordinates.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void translate(double dx, double dy)
	{
		myTranslation.translate(dx,dy);
	}
	
	/**
	 * Provides the parent with the ability to change its local
	 * coordinates.
	 * 
	 * @param sx
	 * @param sy
	 */
	public void scale(double sx, double sy)
	{
		myScale.scale(sx, sy);
	}
	
	//Setter for the color
	public void setColor(Color newColor)
	{
		myColor = newColor;
	}
	
	//Getter for X coordinate of the translation element.
	public double getLocationX()
	{
		return myTranslation.getTranslateX();
	}
	
	//Getter for Y coordinate of the translation element.
	public double getLocationY()
	{
		return myTranslation.getTranslateY();
	}
	
	//Getter for X coordinate of the scale element.
	public double getScaleX()
	{
		return myScale.getScaleX();
	}
	
	//Getter for Y coordinate of the scale element.
	public double getScaleY()
	{
		return myScale.getScaleY();
	}
	
	/**
	 * When invoked, this method transform the object from local coordinates to world. 
	 * The VTM will then convert it from world to screen.
	 * 
	 * Being selected or not selected will determine how the object is drawn.
	 * -Selected = filled
	 * -Not Selected = unfilled
	 */
	public void draw(Graphics2D g2d)
	{
		AffineTransform saveAT = g2d.getTransform();
		g2d.transform(myTranslation);
		g2d.transform(myRotation);
		g2d.transform(myScale);
		g2d.setColor(myColor);
		
		Point upperLeft = new Point (-myRadius, -myRadius);
		g2d.fillOval(upperLeft.x,  upperLeft.y, myRadius*2, myRadius*2);
		g2d.setTransform(saveAT);
	}

	/**
	 * An overloarded draw method to handle drawing 
	 * when the dog object is selected.
	 * 
	 * @param g2d
	 * @param selected
	 */
	public void draw(Graphics2D g2d, boolean selected) 
	{
		AffineTransform saveAT = g2d.getTransform();
		g2d.transform(myTranslation);
		g2d.transform(myRotation);
		g2d.transform(myScale);
		g2d.setColor(myColor);
		
		Point upperLeft = new Point (-myRadius, -myRadius);
		g2d.drawOval(upperLeft.x,  upperLeft.y, myRadius*2, myRadius*2);
		g2d.setTransform(saveAT);
	}
}