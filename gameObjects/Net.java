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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * This class is the only catcher type object in the game and it contains a concrete implementation of
 * the guided method inherited from the interface IGuided.
 * 
 * Nets are represented as squares. Its size is a length of one of the sides.
 * Its initial size is 100. The net's size can be anywhere between 50 and 500.
 * 
 * Nets have a color that will never change once it is created. Essentially it is an immutable variable.
 * 
 * Initializes:
 * Size
 * Color
 * Location
 * 
 * Currently, there can only be one net in the game.
 * 
 * @author Gary
 *
 */
public class Net extends Catcher
{

	
	//A3 - Map Test
	//******
	private int mapWidth = 875;
	private int mapHeight = 689;
	
	/**
	 * Declaration and initialization of fields
	 */
	//Random number generator
	private Random rn = new Random();
	
	//Holds the object's size
	private int length = 100;//Square: 50 to 500
	
	/**
	 * Initializes the object's spawn location
	 * 
	 * Random.nextInt() generates a random number between 0 and Max-1; Max being whatever is inside nextInt()
	 * The full length is subtracted from Max because I'm adding half the size in order to
	 * make sure that the object spawns entirely inside the object.
	 * 
	 * rn.nextDouble() grabs a random double value between 0 and 1. It then adds it to the coordinate.
	 * 
	 * After finishing, the values are stored inside a Point2D object which is then used to set the Nets's
	 * spawn location.
	 */
	private double curY = rn.nextInt((mapHeight-2) - length + 1) + length/2 + rn.nextDouble();
	private double newY = ((double)Math.round(curY * 10d)/10d);
	private double curX = rn.nextInt((mapWidth-2) - length + 1) + length/2 + rn.nextDouble();
	private double newX = ((double)Math.round(curX * 10d)/10d);
	private Point2D newLoc = new Point2D.Double(newX, newY);
	
	//The net's color cannot change after it is created. Hence the use of final.
	private final Color color = new Color(0,0,0);
	
	//A4
	private AffineTransform myTranslation;
	private AffineTransform myRotation;
	private AffineTransform myScale;
	
	
	/**
	 * The default constructor for Net.
	 * 
	 * Sets the object's color, size, and location.
	 */
	public Net()
	{
		setColor(color);
		setSize(length);
		setLocation(newLoc);
		
		myScale = new AffineTransform();
		myRotation = new AffineTransform();
		myTranslation = new AffineTransform();
		myTranslation.translate(newX, newY);
		//myTranslation.translate(0, 0);
	}
	
	public void rotate(double degrees)
	{
		myRotation.rotate(Math.toRadians(degrees));
	}
	
	public void translate(double dx, double dy)
	{
		myTranslation.translate(dx,dy);
	}
	
	public void scale(double sx, double sy)
	{
		myScale.scale(sx, sy);
	}
	
	/**
	 * Concrete implementation of IGuided's method.
	 * 
	 * The method first checks which direction that it needs to move in.
	 * It then moves the net and checks if it is still within the bounds of the world.
	 * If it is outside the bounds, it then correct moves the net so that it is next to whatever
	 * boundary wall it passed.
	 * 
	 * The net moves in increments of 50.
	 * 
	 * Math.round() is used to keep the values to the tenth place.
	 */
	public void guide(char direction)
	{
		Point2D newLocation = new Point2D.Double();

		if (direction == 'u') //Up
		{
			double newY = ((double)Math.round((getY() + 50) * 10d)/10d);
			newLocation.setLocation(getX(), newY);
			newLocation = outsideWorldFix(newLocation);
			setLocation(newLocation);
			
			myTranslation.translate(0, (newLocation.getY() - myTranslation.getTranslateY()));
		}
		else if ( direction == 'r') //Right
		{
			double newX = ((double)Math.round((getX() + 50) * 10d)/10d);
			newLocation.setLocation(newX, getY());
			newLocation = outsideWorldFix(newLocation);
			setLocation(newLocation);
			
			myTranslation.translate(newLocation.getX() - myTranslation.getTranslateX(), 0);
		}
		else if (direction == 'd')//Down
		{
			double newY = ((double)Math.round((getY() - 50) * 10d)/10d);
			newLocation.setLocation(getX(), newY);
			newLocation = outsideWorldFix(newLocation);
			setLocation(newLocation);
			
			myTranslation.translate(0, - (myTranslation.getTranslateY() - newLocation.getY()));
		}
		else //Left
		{
			double newX = ((double)Math.round((getX() - 50) * 10d)/10d);
			newLocation.setLocation(newX, getY());
			newLocation = outsideWorldFix(newLocation);
			setLocation(newLocation);
			
			myTranslation.translate(- (myTranslation.getTranslateX() - newLocation.getX()), 0);
		}
//		System.out.print("TranslateX: " + myTranslation.getTranslateX() + '\n');
//		System.out.print("TranslateY: " + myTranslation.getTranslateY() + '\n');
	}
	
//	public void boundaryCheck(double axis)
//	{
//		//if ( (axis > 1024)
//	}
	
	/**
	 * Checks and sets the object near the world's boundary if it passes the boundary.
	 * 
	 * The for loop makes 2 passes in order to check both the X and the Y.
	 * 
	 * 
	 * Calculations:
	 * newX = boundary +/- size/2
	 * newY = boundary +/- size/2
	 * + if it passed the boundary at the bottom or left
	 * - if it passed the boundary to the top or right
	 */
	private Point2D outsideWorldFix(Point2D oldPoint)
	{
		for(int i = 0; i < 2; i++) // 2 passes
		{
			if( (oldPoint.getX()-(getSize() / 2)) < 0)//Decide on the format to pick
			{//oldPoint.getX() < (getSize()/2) works also, all about format
				oldPoint.setLocation(getSize()/2, oldPoint.getY());
			}
			else if (oldPoint.getY() < (getSize()/2))
			{
				double newY = ((double)Math.round((getSize()/2) * 10d)/10d);
				oldPoint.setLocation(oldPoint.getX(), newY);
			}
			else if (oldPoint.getX() > mapWidth-(getSize()/2))
			{
				oldPoint.setLocation(mapWidth-(getSize()/2), oldPoint.getY());
			}
			else if (oldPoint.getY() > mapHeight-(getSize()/2))
			{
				double newY = ((double)Math.round((mapHeight-(getSize()/2)) * 10d)/10d);
				oldPoint.setLocation(oldPoint.getX(), newY);
			}
		}
		return oldPoint;
	}//End of outsideWorldFix

	/**
	 * Increases the net's size by 50 as long as it doesn't go over the max of 500.
	 * If increasing the size of the net will make it pass any part of the world's boundaries,
	 * the net will be shifted accordingly.
	 */
	public void expand()//Square: 50 to 500
	{
		Point2D newLocation = new Point2D.Double();
		newLocation.setLocation(getLocation());
		
		if(length + 50 > 500)
		{
			length = 500;
			setSize(length);
			newLocation = outsideWorldFix(newLocation);
			setLocation(newLocation);
			
			myTranslation.translate(newLocation.getX() - myTranslation.getTranslateX(),(newLocation.getY() - myTranslation.getTranslateY()));
		}
		else
		{
			length = length + 50;
			setSize(length);
			newLocation = outsideWorldFix(newLocation);
			setLocation(newLocation);
			
			myTranslation.translate(newLocation.getX() - myTranslation.getTranslateX(),(newLocation.getY() - myTranslation.getTranslateY()));
		}
	}
	
	/**
	 * Decreases the net's size by 50 as long as it doesn't go under the min of 50.
	 */
	public void contract()//Square: 50 to 500
	{
		if(length - 50 < 50)
		{
			length = 50;
			setSize(length);
		}
		else
		{
			length = length - 50;
			setSize(length);
		}
	}
	
	/**
	 * Overridden the default toString() method.
	 * Example output: Net: loc=803.9,100.0 color=[0,0,0] size=100
	 */
	public String toString()
	{
		String myString = "Net: loc=" + getX() + "," + getY() + " color=" + "[" + getColor().getRed() + "," + getColor().getGreen() + "," + getColor().getBlue() + "] size=" + getSize();
		return myString;
	}
	
	/**
	 * A polymorphic method that draws a net.
	 * 
	 * Note: The net's sides are designed so that they can sit on the
	 * 		 edge of the map. Because of this, it might look like it went
	 * 		 out of the map's boundaries.
	 */
	@Override
	public void draw(Graphics g) 
	{
//		int centerX = (int) (getLocation().getX());
//		int centerY = (int) (getLocation().getY());
//		
//		g.drawRect(centerX - getSize()/2, centerY - getSize()/2, getSize(), getSize());
		
		//g.drawRect(0, 0, getSize(), getSize());

		
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform saveAT = g2d.getTransform();
		
		Point upperLeft = new Point(-getSize()/2, -getSize()/2);
		
		g2d.transform(myTranslation);
		g2d.setColor(getColor());
		
//		System.out.print("G2D_2: " + g2d.getTransform() + '\n');
		
		
		g2d.drawRect(upperLeft.x, upperLeft.y, getSize(), getSize());
		g2d.setTransform(saveAT);
	}
	
	
	

	
	
}//End of class