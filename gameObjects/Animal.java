package a4.gameObjects;
import java.util.ArrayList;
import java.util.List;
/* Author: Gary Vang
 * 
 * Assignment: 1
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.util.Random;

/**
 * This class is the base class for all animal objects. In this case, for cats and dogs.
 * This class contains fields and methods that are common amongst all animal objects.
 * 
 * All animal objects have a:
 * Speed - between 0 - 5
 * Direction - between 0 - 360; 0 being North, 90 being east, 180 south, 270 west.
 * 
 * Initialization of fields will be done by its subclasses.
 * 
 * For A3, implements the ICollideable interface, but does not provide
 * concrete implementations for it.
 * 
 * @author Gary
 *
 */
public abstract class Animal extends GameObject implements IMoveable, ICollideable
{
	/**
	 * Declaration of fields.
	 */
	private int speed;
	private int direction;
	
	private List<ICollideable> alreadyCollidingList = new ArrayList<ICollideable>();
	
	//A3
	//**************************************************
	public boolean inColList(ICollideable obj)
	{
		boolean result = false;
		
		if(alreadyCollidingList.contains(obj) == true)
		{
			result = true;
		}	
		return result;
	}
	
	public void addToColList(ICollideable obj)
	{
		alreadyCollidingList.add(obj);
	}
	
	public void removeFromColList(ICollideable obj)
	{
		alreadyCollidingList.remove(obj);
	}
	//*****************************************************
	
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
	
	/**
	 * Sets the direction of an animal using a new direction.
	 */
	public void setDirection(int newDirection)
	{
		direction = newDirection;
	}
	
	/**
	 * Returns an animal's direction.
	 */
	public int getDir()
	{
		return direction;
	}
	
	/**
	 * Randomly subtracts or adds values from an animal's current direction.
	 * Values: between 0 - 10
	 * 
	 * Obtains a random number between 0 and 1.
	 * If 1, then subtracts a random value from the animal's direction.
	 * else, add a random value to the animal's direction.
	 */
	public void varyDir()
	{
		Random rn = new Random();
		if(rn.nextInt(2) == 1)
		{
			direction = (direction - rn.nextInt(11));
			if(direction < 0)
			{
				direction += 360;
			}
		}
		else
		{
			direction = (direction + rn.nextInt(11)) % 360;
		}
	}
}//End of class