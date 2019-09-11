package a4;


/* Author: Gary Vang
 * 
 * Assignment: 2
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */

/**
 * This is the collection interface.
 * This class contains two methods: add and getIterator.
 * 		add - adds an object to the collection
 * 		getIterator - returns an iterator for the collection
 * 
 * @author Gary
 *
 */
public interface Collection 
{
	public void add(Object newObject);
	
	public Iterator getIterator();
}
