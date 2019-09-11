package a4;
/* Author: Gary Vang
 * 
 * Assignment: 2
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.util.Vector;

import a4.gameObjects.GameObject;

/**
 * This class is the primary collection class.
 * It uses a vector to store GameObjects and implements methods
 * from the Collection interface.
 * 
 * It also implements the Iterator interface and uses the 
 * field, currElementIndex to keep track of the iterator's position in the vector.
 * 
 * @author Gary
 *
 */
public class GameObjectCollection implements Collection
{
	//The collection
	private Vector theCollection;
	
	//Default class constructor creates a collection when created
	public GameObjectCollection()
	{
		theCollection = new Vector();
	}
	
	/**
	 * Adds an Object to the collection.
	 * 
	 * Currently, any object can be added.
	 * 
	 * In the future, if I wanted to force it to only take GameObjects,
	 * change "Object" to "GameObject" and provide a concrete implementation
	 * of add(Object obj) due to implementing the Collection interface.
	 * 
	 * @param newObject
	 */
	public void add(Object newObject)//Just Recently Added
	{
		theCollection.addElement(newObject);
	}
	
	/**
	 * Returns an iterator for the collection
	 */
	public Iterator getIterator()
	{
		return new GameObjectVectorIterator(); 
	}
	
	/**
	 * An iterator for the collection.
	 */
	private class GameObjectVectorIterator implements Iterator
	{
		//Keeps track of the index
		private int currElementIndex;
		
		//Default constructor
		public GameObjectVectorIterator()
		{
			currElementIndex = -1;
		}
		
		/**
		 * Checks if the the collection has another object.
		 * 
		 * Returns true or false.
		 */
		public boolean hasNext()
		{
			if (theCollection.size() <= 0)
			{
				return false;
			}
			
			if (currElementIndex == theCollection.size() - 1)
			{
				return false;
			}
			return true;
		}
		
		/**
		 * Returns next object in the collection.
		 */
		public Object getNext () 
		{
			currElementIndex++;
			return(theCollection.elementAt(currElementIndex));
		}
		
		public void remove()
		{
			theCollection.remove(currElementIndex);
		}
	}//end private iterator class
}//end class
