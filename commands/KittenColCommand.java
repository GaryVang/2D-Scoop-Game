package a4.commands;
/* Author: Gary Vang
 * 
 * Assignment: 4
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import a4.GameWorld;
import a4.gameObjects.Cat;
/**
 * This class is the kitten collision command object.
 * 
 * The primary purpose of this is to invoke a method
 * in the gameworld object.
 * 
 * @author Gary
 *
 */
public class KittenColCommand extends AbstractAction
{
	private GameWorld gw;
	
	/**
	 * A constructor for the class.
	 * Sets the target of the command as the GameWorld Object passed into it.
	 * 
	 * Also overrides the labels of the command holders.
	 * 
	 * @param gameWorldModel
	 */
	public KittenColCommand(GameWorld gameWorldModel)
	{
		super("Kitten Collision");
		gw = gameWorldModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		//Remove before submitting
		//gw.mate(DELAY_IN_MSEC);
	}
	
	//Invokes the kitten creation method in gameworld.
	public void actionPerformed(ActionEvent e, Cat catObject) 
	{
		gw.mate(catObject);
	}
	
}