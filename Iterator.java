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
 * This class is my implementation of the Iterator java class.
 * Contains 3 methods that will need implementation:
 * hasNext() - which returns true or false based on if the collection has another element
 * getNext() - gets the next element in the collection
 * remove() - removes the current element and shifts all subsequent elements to the left
 * 
 * @author Gary
 *
 */
public interface Iterator 
{
	public boolean hasNext();
	
	public Object getNext();
	
	public void remove();
}