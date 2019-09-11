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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * This class is one of two animal objects that exist in the game and contains a concrete implementation
 * of the move method inherited from the interface IMoving.
 * 
 * Dogs are represented as circles. Its size is the circle's diameter.
 * Its size is generated using a random number generator and is between the size 20 - 80.
 * 
 * Dogs have a size that never changes once it is created. Essentially an immutable variable.
 * They also have the same starting color that changes depending on how many scratches it has.
 * 
 * Initializes:
 * Size
 * Color
 * Speed
 * Location
 * Direction
 * 
 * No new dogs will be created after the start of the game.
 * 
 * Note: Overriding setSize() is not necessary because invoking instance methods on final variables
 * will not alter the variable's value.
 * 
 * This class provides a way for the object to draw itself, detect/handle decision, and
 * provides concrete implementations for the ISelectable and ICollideable interface.
 * 
 * A4: This class has been changed to have hierarchical and dynamic object transformations.
 * This means that the use of getLocation is limited to updating the translation affine transform.
 * 
 * 
 * @author Gary
 *
 */
public class Dog extends Animal implements ISelectable
{
	//A3 - Map Test
	//******
	private int mapWidth = 875;
	private int mapHeight = 689;
	private int moveTime = 20; //average time elapsed required for an object to make a full-length move
	
	private boolean isSelected;
	
	/**
	 * Declaration and initialization of fields
	 */
	//Random number generator
	private  Random rn = new Random();
	
	//Holds the object's size
	private  int diameter = rn.nextInt(81) + 20; // Size: 80 to 20
	
	/**
	 * Initializes the object's spawn location
	 * 
	 * Random.nextInt() generates a random number between 0 and Max-1; Max being whatever is inside nextInt()
	 * The full diameter is subtracted from Max because I'm adding half the size in order to
	 * make sure that the object spawns entirely inside the object.
	 * 
	 * rn.nextDouble() grabs a random double value between 0 and 1. It then adds it to the coordinate.
	 * 
	 * After finishing, the values are stored inside a Point2D object which is then used to set the Dog's
	 * spawn location.
	 */
	private double curY = rn.nextInt((mapHeight-2) - diameter + 1) + diameter/2 + rn.nextDouble();
	private double newY = ((double)Math.round(curY * 10d)/10d);
	private double curX = rn.nextInt((mapWidth-2) - diameter + 1) + diameter/2 + rn.nextDouble();
	private double newX = ((double)Math.round(curX * 10d)/10d);
	private Point2D newLoc = new Point2D.Double(newX, newY);
	
	//The number of scratches on the dog. Default is 0. Max is 5.
	private int scratches;
	
	//All dogs are born the same color.
	private Color color = new Color(0,0,128);
	
	//All dogs' size cannot change after they are created. Hence the use of final.
	private final int size = diameter;
	
	//A4
	private AffineTransform myTranslation;
	private AffineTransform myRotation;
	private AffineTransform myScale;
	private AffineTransform inverseVTM;
	
	//Hierarchical objects
	private Body myBody;
	private Leg[] legs;
	private boolean headSpinFlag = false;
	private Leg fr, fl, rr, rl, head;
	
	int headSpinCounter = 0;
	
	/**
	 * The default constructor for Dog.
	 * 
	 * Sets the object's location, speed, direction, and color.
	 */
	public Dog()
	{
		setSelected(false);
		setLocation(newLoc);
		setSpeed(5);
		setDirection(rn.nextInt(360));
		setColor(color);
		
		myScale = new AffineTransform();
		myRotation = new AffineTransform();
		myTranslation = new AffineTransform();
		myTranslation.translate(newX, newY);
		
		myBody = new Body();
		myBody.scale(getSize()/2, getSize());
		legs = new Leg[5];
		
		fr = new Leg(); fr.translate(getSize()/2 -5, getSize()/4); fr.scale(getSize()/8, getSize()/8);
			legs[0] = fr;
		fl = new Leg(); fl.translate(-getSize()/2 -5, getSize()/4); fl.scale(getSize()/8, getSize()/8);
			legs[1] = fl;
		rr = new Leg(); rr.translate(getSize()/2 -5, -getSize()/4); rr.scale(getSize()/8, getSize()/8);
			legs[2] = rr;
		rl = new Leg(); rl.translate(-getSize()/2 -5, -getSize()/4); rl.scale(getSize()/8, getSize()/8);
			legs[3] = rl;
		head = new Leg(); head.translate(0, getSize()); head.scale(getSize()/3, getSize()/8);
			legs[4] = head; head.setColor(Color.BLACK);
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
	 * A4: Stores the new world coordinates into the translation affineTransform object.
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
		
		if(headSpinCounter < 50)
		{
			headSpinCounter++;
		}
		else
		{
			if(headSpinFlag == false)
			{
				head.rotate( Math.toRadians(180));
				head.translate(0, 25);
				headSpinFlag = true;
				headSpinCounter=0;
			}
			else
			{
				head.rotate(-Math.toRadians(180));
				head.translate(0, -25);
				headSpinFlag = false;
				headSpinCounter=0;
			}
		}
	}
	
	/**
	 * Returns the number of scratches on the dog.
	 */
	public int getScratch()
	{
		return scratches;
	}
	
	/**
	 * Invoked whenever there's a fight between a dog and cat.
	 * Checks first to see if the dog has the maximum number of scratches.
	 * If not, increments scratches by 1, changes the color accordingly, and lowers speed.
	 */
	public void fight()
	{
		if (scratches < 5)
		{
			scratches++;
			Color newColor = new Color(scratches*40, 0, 128);
			
			myBody.setColor(newColor);
			setSpeed(5-scratches);
		}
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
	 * Checks to make sure the object is within the boundaries of the world.
	 * 
	 * Calculations:
	 * Y boundary check = centerOfObject +/- sizeOfObject/2
	 * X boundary check = centerObject +/- sizeOfObject/2
	 * 
	 * Returns either true or false.
	 */
	private boolean boundaryCheck(Point2D newPoint)
	{
		if (newPoint.getX() - getSize()/2 < 0 || newPoint.getY() - getSize()/2 < 0)
		{	
			return false;
		}
		else if (newPoint.getX() + getSize()/2 > mapWidth || newPoint.getY() + getSize()/2 > mapHeight)
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
	 * curY = same as above
	 * + if it passed the boundary at the bottom or left
	 * - if it passed the boundary to the top or right
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
			else if (oldPoint.getY() - getSize()/2 < 0)
			{
				double curY = getSize()/2 + (getSize()/2 - oldPoint.getY());
				double newY = ((double)Math.round(curY * 10d)/10d);
				oldPoint.setLocation(oldPoint.getX(), newY);
			}
			else if (oldPoint.getX() + getSize()/2 > mapWidth)
			{
				double curX = mapWidth - getSize()/2 - (oldPoint.getX() + getSize()/2 - mapWidth);
				double newX = ((double)Math.round(curX * 10d)/10d);
				oldPoint.setLocation(newX, oldPoint.getY());
			}
			else if (oldPoint.getY() + getSize()/2 > mapHeight)
			{
				double curY = mapHeight - getSize()/2 - (oldPoint.getY() + getSize()/2 - mapHeight);		
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
		if (oldPoint.getY() - getSize()/2 < 0) //Collision with Bottom
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
		else if (oldPoint.getY() + getSize()/2 > mapHeight) //Collision with Top
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
		else if (oldPoint.getX() + getSize()/2 > mapWidth) //Collision with Right Wall
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
	 * Heals the dog of its scratches and sets its speed back to its max(5).
	 */
	public void heal()
	{
		scratches = 0;
		
		Color newColor = new Color(scratches*40, 0, 128);
		myBody.setColor(newColor);
		setSpeed(5-scratches);
	}
	
	/**
	 * When invoked, this method transform the object from local coordinates to world. 
	 * The VTM will then convert it from world to screen.
	 * 
	 * Being selected or not selected will determine how the object is drawn.
	 * -Selected = filled
	 * -Not Selected = unfilled
	 */
	@Override
	public void draw(Graphics g) 
	{
		if(this.isSelected() != true)
		{
			Graphics2D g2d = (Graphics2D) g;
			
			AffineTransform saveAT = g2d.getTransform();
			try {
				inverseVTM = saveAT.createInverse();
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			g2d.transform(myTranslation);
			g2d.transform(myRotation);
			
			myBody.draw(g2d);
			for(Leg f: legs)
			{
				f.draw(g2d);
			}
			
			g2d.setTransform(saveAT);
		}
		else
		{	
			Graphics2D g2d = (Graphics2D) g;
			
			AffineTransform saveAT = g2d.getTransform();
			
			g2d.transform(myTranslation);
			g2d.transform(myRotation);
			
			myBody.draw(g2d, isSelected());
			for(Leg f: legs)
			{
				f.draw(g2d, isSelected());
			}
	
			g2d.setTransform(saveAT);
		}
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
		int testDir = getDir();
		setDirection(((Animal)otherObject).getDir());
		((Animal)otherObject).setDirection(testDir);
	}
	
	//Setter for the isSelected flag.
	@Override
	public void setSelected(boolean yesNo) 
	{
		isSelected = yesNo;
	}

	//Getter for the isSelected flag.
	@Override
	public boolean isSelected() 
	{
		return isSelected;
	}

	/**
	 * Checks if the Point p is located inside this object.
	 * Returns true if so,
	 * else returns checks the other body parts.
	 */
	@Override
	public boolean contains(Point worldPoint) 
	{
		Point2D p = null;
		try {
			p = inverseVTM.createInverse().transform(worldPoint,null);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int px = (int) p.getX();
		int py = (int) p.getY();
		
		int xLoc = (int) myTranslation.getTranslateX();
		int yLoc = (int) myTranslation.getTranslateY();
		
		int sizeX = (int) myBody.getScaleX()/2;
		int sizeY = (int) myBody.getScaleY();
		
		if((px >= xLoc - sizeX) && (px <= xLoc + sizeX) && (py >= yLoc - sizeY) && (py <= yLoc + sizeY))
			return true;
		else
		{
			int i = 0;
			return contains(worldPoint, i);
		}	
	}
	
	//Overloaded for recursive call
	public boolean contains(Point worldPoint, int index)
	{
		Point2D p = null;
		
		try {
			p = inverseVTM.createInverse().transform(worldPoint,null);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		int px = (int) p.getX();
		int py = (int) p.getY();
		
		Leg f = legs[index];
		
		int xLoc = (int) (myTranslation.getTranslateX());
		int yLoc = (int) (myTranslation.getTranslateY() + f.getLocationY());
		
		Point objectPoint = new Point (xLoc, yLoc);
		myRotation.transform(objectPoint, null);
		
		int sizeX = (int) f.getScaleX();
		int sizeY = (int) f.getScaleY();
		
//		System.out.print("locX: " + xLoc + '\n');
//		System.out.print("locy: " + yLoc + '\n');
//		System.out.print("ScaleX: " +f.getScaleX() +'\n');
//		System.out.print("ScaleY: " +f.getScaleY() +'\n'+'\n');
		
		if((px >= objectPoint.getX() - sizeX) && (px <= objectPoint.getX() + sizeX) && (py >= objectPoint.getY() - sizeY) && (py <= objectPoint.getX() + sizeY))
			return true;
		else if(index < 4)
			return contains(worldPoint, index+1);
		
		return false;
	}
}//End of class