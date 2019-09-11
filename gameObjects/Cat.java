package a4.gameObjects;
/* Author: Gary Vang
 * 
 * Assignment: 3
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Random;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * This class is one of two animal objects that exist in the game and contains a concrete implementation
 * of the move method inherited from the interface IMoving.
 * 
 * Cats are represented as equilateral triangles. Its size is a length of one of the triangle's sides.
 * Its size is generated using a random number generator and is between the size 20 - 40.
 * 
 * Cats have a size and color that never changes once it is created. Essentially they are immutable variables.
 * They all also have the same color.
 * 
 * Initializes:
 * Size
 * Color
 * Speed
 * Location
 * Direction
 * 
 * New cats can be created after the start of the game.
 * 
 * Note: Overriding setSize() and setColor() is not necessary because invoking instance methods on final variables
 * will not alter the variable's value.
 * 
 * This class provides a way for the object to draw itself, detect/handle decision, and
 * provides concrete implementations for the ISelectable and ICollideable interface.
 * 
 * @author Gary
 *
 */
public class Cat extends Animal
{
	//A3
	//******
	private int mapWidth = 875;
	private int mapHeight = 689;
	private int moveTime = 20; //average time elapsed required for an object to make a full-length move
	
	private boolean kittenFlag = false;
	private int age = 500; //A counter used to determine when a kitten becomes a cat
	
	/**
	 * Declaration and initialization of fields
	 */
	//Random number generator
	private Random rn = new Random();
	
	//Holds the object's size
	private int side = rn.nextInt(41) + 20; // Size: 40 to 20
	
	/**
	 * Initializes the object's spawn location
	 * 
	 * Random.nextInt() generates a random number between 0 and Max-1; Max being whatever is inside nextInt()
	 * The full size is subtracted from Max because I'm adding half the size in order to
	 * make sure that the object spawns entirely inside the object.
	 * 
	 * rn.nextDouble() grabs a random double value between 0 and 1. It then adds it to the coordinate.
	 * 
	 * After finishing, the values are stored inside a Point2D object which is then used to set the Cats's
	 * spawn location.
	 * 
	 * Note: Random.nextInt() can only take integers as arguments so instead of using the square root of 3,
	 * I used an integer near its value. In this case, 2.
	 */
	private double curY = rn.nextInt((mapHeight-2) - (side * 2) + 1) + side + rn.nextDouble();
	private double newY = ((double)Math.round(curY * 10d)/10d);
	private double curX = rn.nextInt((mapWidth-2) - (side * 2) + 1) + side + rn.nextDouble();
	private double newX = ((double)Math.round(curX * 10d)/10d);
	private Point2D newLoc = new Point2D.Double(newX, newY);
	
	//All cats' size and color cannot change after they are created. Hence the use of final.
	private final Color color = new Color(255, 0, 0);
	private final int size = side;
	
	//A4
	private AffineTransform myTranslation;
	private AffineTransform myRotation;
	private AffineTransform myScale;
	
	/**
	 * The default constructor for Cat.
	 * 
	 * Sets the object's location, speed, and direction.
	 */
	public Cat()
	{
		setLocation(newLoc);
		setSpeed(5);
		setDirection(rn.nextInt(360));
		
		myScale = new AffineTransform();
		myRotation = new AffineTransform();
		myTranslation = new AffineTransform();
		myTranslation.translate(newX, newY);
		myRotation.rotate(Math.toRadians(90 - getDir()));
	}
	
	/**
	 * New overloaded class constructor for specifically creating kittens.
	 * 
	 * @param kittenParent
	 */
	public Cat(Cat kittenParent)
	{
		double newX = ((double)Math.round(( kittenParent.getX() + kittenParent.getSize() + 10.0) * 10d)/10d);
		double newY = ((double)Math.round(( kittenParent.getY() + kittenParent.getSize() + 10.0) * 10d)/10d);
						
		Point2D kittenLocation = new Point2D.Double();
		kittenLocation.setLocation(newX,  newY);
		
		setLocation(kittenLocation);
		setSpeed(5);
		setDirection(rn.nextInt(360));
		
		kittenFlag = true;
		age = 0;	
		
		myScale = new AffineTransform();
		myRotation = new AffineTransform();
		myTranslation = new AffineTransform();
		myTranslation.translate(newX, newY);
		myRotation.rotate(Math.toRadians(90 - getDir()));
	}
	
	/**
	 * Concrete implementation of IMoving's method.
	 * 
	 * The method first calculates the new location after the object moves.
	 * It then checks to see if this new location is within the bounds of the world.
	 * If it is outside the bounds, it then correctly bounces the object off whatever boundary wall it
	 * passed.
	 * 
	 * At the end, the object's direction is changed slightly to make it so that
	 * the object isn't moving in a straight line.
	 * 
	 * Calculations:
	 * Radians = (90 - direction)
	 * deltaX = cos(theta) * speed
	 * deltaY = sin(theta) * speed
	 * newLocation(x,y) = oldLocation + (deltaX, deltaY)
	 * 
	 * Math.round() is used to keep the values to the tenth place.
	 * 
	 * Move takes in the elapsedTime and uses it to calculate
	 * how far the object needs to move.
	 * 
	 * Stores the new world coordinates into the translation affineTransform object.
	 */
	public void move(int elapsedTime)
	{	
		double radians = Math.toRadians(90 - getDir());
		double deltaX = Math.cos(radians) * getSpeed() * (elapsedTime/moveTime);
		double deltaY = Math.sin(radians) * getSpeed() * (elapsedTime/moveTime);
		
		double newX = ((double)Math.round((getX() + deltaX) * 10d)/10d);
		double newY = ((double)Math.round((getY() + deltaY) * 10d)/10d);
		Point2D newLocation = new Point2D.Double();
				
		newLocation.setLocation(newX, newY);

		setLocation(newLocation);
		
		if(boundaryCheck(newLocation) == true)
		{
			setLocation(newLocation);
			
			myTranslation.setToIdentity();
			myTranslation.translate(newLocation.getX(), newLocation.getY());
		}
		else //It is out of bounds
		{
			setDirection(directionFix(getDir(), newLocation));
			newLocation = outsideWorldFix(newLocation);
			setLocation(newLocation);
			
			myTranslation.setToIdentity();
			myTranslation.translate(newLocation.getX(), newLocation.getY());
		}
		varyDir();
		myRotation.setToIdentity();
		myRotation.rotate(Math.toRadians(360 - getDir()));
		
		if(age < 500)//Kittens will stay kittens for 10 seconds
			age++;
		else
			kittenFlag = false;
	}
	
	/**
	 * Checks if the cat object is a kitten.
	 * 
	 * Returns true if the kittenFlag is true, else 
	 * returns false.
	 * @return
	 */
	public boolean isKitten()
	{
		if(kittenFlag == true)
		{
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Overrides parent's getSize().
	 * Needed because all animals have a size that won't change and
	 * invoking a child's instance method on a parent's final variable
	 * will not work.
	 * The instance method in this case is the random number generator (rng).
	 */
	public int getSize()
	{
		return size;
	}
	
	/**
	 * Overrides parent's getColor().
	 * Needed because all cats have a color that won't change and
	 * invoking a child's instance method on a parent's final variable
	 * will not work.
	 * The instance method in this case is the random number generator (rng).
	 */
	public Color getColor()
	{
		return color;
	}
	
	/**
	 * Checks to make sure the object is within the boundaries of the world.
	 * 
	 * Calculations:
	 * X boundary check = centerObject +/- sizeOfObject/2
	 * Y boundary check = centerOfObject +/- (sizeOfObject/2 * Math.sqrt(3))
	 * 
	 * Note again: Cats are represented as equilateral triangles.
	 * 
	 * Returns either true or false.
	 */
	private boolean boundaryCheck(Point2D newPoint)
	{
		if (newPoint.getX() - getSize()/2 < 0 || newPoint.getY() - (getSize() * (Math.sqrt(3)/2)) < 0)
		{	
			return false;
		}
		else if (newPoint.getX() + getSize()/2 > mapWidth || newPoint.getY() + (getSize() * (Math.sqrt(3)/2)) > mapHeight)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Bounces the object off the world's boundaries.
	 * 
	 * The for loop makes 2 passes in order to check both the X and the Y.
	 * 
	 * Variables:
	 * curX = the unrounded new X value
	 * curY = the unrounded new Y value
	 * 
	 * Calculations:
	 * curX = halfSizeOfObject +/- howMuchItPassedTheBoundary
	 * curY = halfOfTheHeightOfObject +/- howMuchItPassedTheBoundary
	 * + if it passed the boundary at the bottom or left
	 * - if it passed the boundary to the top or right
	 * 
	 * Note: the height is calculated as: (length of triangle's side) * (square root of 3)
	 * because it is an equilateral triangle.
	 */
	private Point2D outsideWorldFix(Point2D oldPoint)
	{
		for(int i = 0; i < 2; i++) // 2 passes
		{
			if (oldPoint.getX() - getSize()/2 < 0) 
			{
				double curX = getSize()/2 + (getSize()/2 - oldPoint.getX());
				double newX = ((double)Math.round(curX * 10d)/10d);
				oldPoint.setLocation(newX, oldPoint.getY());
				
			}
			else if (oldPoint.getY() - (getSize() * (Math.sqrt(3)/2))< 0)
			{
				double curY = (getSize() * Math.sqrt(3)/2) + (getSize() * Math.sqrt(3)/2) - oldPoint.getY();
				double newY = ((double)Math.round(curY * 10d)/10d);
				oldPoint.setLocation(oldPoint.getX(), newY);
			}
			else if (oldPoint.getX() + getSize()/2 > mapWidth)
			{
				double curX = mapWidth - getSize()/2 - (oldPoint.getX() + getSize()/2 - mapWidth);
				double newX = ((double)Math.round(curX * 10d)/10d);
				oldPoint.setLocation(newX, oldPoint.getY());
			}
			else if (oldPoint.getY() + (getSize() * (Math.sqrt(3)/2)) > mapHeight) 
			{
				double curY = mapHeight - ( (getSize() * Math.sqrt(3) ) /2) - (oldPoint.getY() + (getSize() * (Math.sqrt(3)/2)) - mapHeight);
				double newY = ((double)Math.round(curY * 10d)/10d);
				oldPoint.setLocation(oldPoint.getX(), newY);
			}
		}
		return oldPoint;
	}
	
	/**
	 * Fixes the direction based on the old direction and which boundary the
	 * object collided with.
	 * 
	 * Calculations for checking collision is the same as the previous method.
	 */
	private int directionFix(int oldDir, Point2D oldPoint)
	{
		if (oldPoint.getY() - (getSize() * (Math.sqrt(3)/2)) < 0) //Collision with Bottom
		{
			if (getDir() <= 180)
			{
				int newDir = 180 - oldDir; // 90 - (oldDir - 90) 
				return newDir;
			}
			else
			{
				int newDir = 270 + (270 - oldDir);
				return newDir;
			}			
		}
		else if (oldPoint.getY() + (getSize() * (Math.sqrt(3)/2)) > mapHeight) //Collision with Top
		{
			if (getDir() <= 90)
			{
				int newDir = 180 - oldDir;
				return newDir;
			}
			else
			{
				int newDir = 270 + (270 - oldDir);
				return newDir;
			}
		}
		else if (oldPoint.getX() - getSize()/2 < 0) //Collision with Left Wall
		{
			if (oldDir <= 270)
			{
				int newDir = 90 + 270 - oldDir;
				return newDir;
			}
			else
			{
				int newDir = (360 - oldDir);// 360 + (360 - oldDir) -360...-360 instead of using modulus because modulus returns a negative
				return newDir;
			}
		}
		else if (oldPoint.getX() > mapHeight) //Collision with Right Wall
		{
			if (oldDir <= 90)
			{
				int newDir = 360 - oldDir;
				return newDir;
			}
			else 
			{
				int newDir = 90 + 270 - oldDir;
				return newDir;
			}
		}
		return 0;
	}

	/**
	 * When invoked, this method draws the object at its current location, size, and color. 
	 * Being selected or not selected will determine how the object is drawn.
	 * -Selected = filled
	 * -Not Selected = unfilled
	 */
	@Override
	public void draw(Graphics g) 
	{	
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform saveAT = g2d.getTransform();
		
		g2d.transform(myTranslation);
		g2d.transform(myRotation);
		g2d.setColor(getColor());
		
		int[] x = {-getSize()/2, 0, getSize()/2};
		int[] y = { (int) (-getSize() * (Math.sqrt(3)/2)), (int) (getSize()*(Math.sqrt(3)/2)), (int) (-getSize()*(Math.sqrt(3)/2))};
		
		g.fillPolygon(new Polygon(x, y, 3));
		
		g2d.setTransform(saveAT);
	}

	/**
	 * This method checks to see if this object and another object has collided.
	 * 
	 * This is done by comparing the distance between the center points squared
	 * and (radius1 + radius2)^2
	 */
	@Override
	public boolean collidesWith(ICollideable otherObject) 
	{
		boolean result = false;
		int thisCenterX = (int) (getX() + getSize()/2);
		int thisCenterY = (int) (getY() + getSize()/2);
		int otherCenterX = (int) (((Animal) otherObject).getX() + ((Animal) otherObject).getSize()/2);
		int otherCenterY = (int) (((Animal) otherObject).getY() + ((Animal) otherObject).getSize()/2);
		
		int dx = thisCenterX - otherCenterX;
		int dy = thisCenterY - otherCenterY;
		int distBetweenCentersSqr = (dx*dx + dy*dy);
		
		int thisRadius = getSize()/2;
		int otherRadius = ((Animal) otherObject).getSize()/2;
		int radiiSqr = (thisRadius*thisRadius + 2*thisRadius*otherRadius + otherRadius*otherRadius);
		
		if(distBetweenCentersSqr <= radiiSqr)
		{
			result = true;
		}
		return result;
	}
	
	/**
	 * This method handles the collision response.
	 * 
	 * The collision response is done by swapping the direction of the colliding
	 * objects.
	 * This is due to the speed factor being constant.
	 */
	@Override
	public void handleCollision(ICollideable otherObject) 
	{
		int curObjDir = getDir();
		setDirection(((Animal)otherObject).getDir());
		((Animal)otherObject).setDirection(curObjDir);
	}
}//End of class