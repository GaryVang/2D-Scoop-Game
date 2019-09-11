package a4.gameObjects;
/* Author: Gary Vang
 * 
 * Assignment: 4
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.Random;
/**
 * This class is a new gameobject that is created upon scooping an animal object.
 * The class has a finite life that ticks down.
 * Once the end of its life has been reached, it is removed from the
 * gameworld.
 * 
 * This object is a bezier curve.
 * 
 * @author Gary
 *
 */
public class ShockWave extends GameObject implements IMoveable
{
	private  Random rn = new Random();
	private int speed;
	private int direction;
	private int moveTime = 20;
	
	private Point p0, p1, p2, p3;

	private AffineTransform myTranslation, myRotation, myScale;
	
	private int maxLifeTime = 200;
	private int lifeTimeCounter = 0;
	private boolean aliveFlag = true;
	
	/**
	 * This default construct creates an object at
	 * the location of the net and sets it to a random
	 * speed and direction.
	 * 
	 * @param xLoc
	 * @param yLoc
	 */
	public ShockWave(double xLoc, double yLoc)
	{
		setSpeed(rn.nextInt(6) + 3);
		setDirection(rn.nextInt(360));
		
		myScale = new AffineTransform();
		myRotation = new AffineTransform();
		myTranslation = new AffineTransform();
		
		p0 = new Point(-rn.nextInt(320), -rn.nextInt(320));
		p1 = new Point(-rn.nextInt(160) + 50, 0);
		p2 = new Point(rn.nextInt(160) + 50, 0);
		p3 = new Point(rn.nextInt(320), -rn.nextInt(320));
		
		myTranslation.setToIdentity();
		myTranslation.translate(xLoc, yLoc);
		myRotation.rotate(Math.toRadians(90 - getDir()));
	}
	
	//Flag for keeping track of object's status
	public boolean isAlive()
	{
		return aliveFlag;
	}
	
	/**
	 * Sets the speed of an animal using a new speed.
	 */
	public void setSpeed(int newSpeed)
	{
		speed = newSpeed;
	}
	
	/**
	 * Returns an animal's speed.
	 */
	public int getSpeed()
	{
		return speed;
	}
	
	//A setter for the direction
	public void setDirection(int newDirection)
	{
		direction = newDirection;
	}
	
	/**
	 * Returns the object's direction.
	 */
	public int getDir()
	{
		return direction;
	}
	
	/**
	 * Draws the object using 4 points.
	 * Prior to drawing, the object is transformed to
	 * screen coordinate using the vtm.
	 */
	@Override
	public void draw(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform saveAT = g2d.getTransform();
		
		g2d.transform(myTranslation);
		g2d.transform(myRotation);
		
		CubicCurve2D.Double bezCurve;
		
		bezCurve = new CubicCurve2D.Double( p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, p3.x,p3.y);
		
		g2d.draw(bezCurve);
		
		g2d.setTransform(saveAT);
	}

	/**
	 * Concrete implementation of the IMoving's method.
	 * 
	 * The method first calculates the new location after the object moves.
	 * It then checks to see if this new location is within the bounds of the world.
	 * If it is outside the bounds, it then correctly bounces the object off whatever boundary wall it
	 * passed.
	 * 
	 * Calculations:
	 * Radians = (90 - direction)
	 * deltaX = cos(theta) * speed
	 * deltaY = sin(theta) * speed
	 * newLocation(x,y) = oldLocation + (deltaX, deltaY)
	 * 
	 * Unlike the other GameObject's move method,
	 * this one does not use getLocation and instead,
	 * stores the new world coordinate into the translation transformation object.
	 * 
	 * It also increments the objects lifetimer and checks if it is still alive.
	 */
	@Override
	public void move(int elapsedTime) 
	{
		double radians = Math.toRadians(90 - getDir());
		double deltaX = Math.cos(radians) * getSpeed() * (elapsedTime/moveTime);
		double deltaY = Math.sin(radians) * getSpeed() * (elapsedTime/moveTime);
		
		double newX = ((double)Math.round((myTranslation.getTranslateX() + deltaX) * 10d)/10d);
		double newY = ((double)Math.round((myTranslation.getTranslateX() + deltaY) * 10d)/10d);

		myTranslation.setToIdentity();
		myTranslation.translate(newX, newY);
		
		if (lifeTimeCounter >= maxLifeTime)
			aliveFlag = false;
		else
			lifeTimeCounter++;
	}
}