package a4.commands;
/* Author: Gary Vang
 * 
 * Assignment: 2
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import a4.GameWorld;
import a4.gameObjects.Dog;
/**
 * This class is the fight command object.
 * 
 * The primary purpose of this is to invoke a method
 * in the gameworld object.
 * 
 * @author Gary
 *
 */
public class CatDogFightCommand extends AbstractAction
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
	public CatDogFightCommand(GameWorld gameWorldModel)
	{
		super("Cat/Dog Fight");
		gw = gameWorldModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		//gw.fight();
	}
	
	public void actionPerformed(ActionEvent e, Dog dogObject)
	{
		gw.fight(dogObject);
	}
}