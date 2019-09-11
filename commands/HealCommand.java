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

/**
 * This command has a reference to the gameworld when created.
 * The sole purpose of this command is the invoke the heal method
 * in gameworld.
 * 
 * @author Gary
 *
 */
public class HealCommand extends AbstractAction
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
	public HealCommand(GameWorld gameWorldModel)
	{
		super("Heal");
		gw = gameWorldModel;
	}
	
	//Invokes the heal method in gameworld
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		gw.heal();	
	}
}