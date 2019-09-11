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
/**
 * This class is the Clock has Ticked command object.
 * 
 * The primary purpose of this is to invoke a method
 * in the gameworld object.
 * 
 * @author Gary
 *
 */
public class ClockTickCommand extends AbstractAction
{
	private GameWorld gw;
	//private int elapsedTime;
	
	/**
	 * A constructor for the class.
	 * Sets the target of the command as the GameWorld Object passed into it.
	 * 
	 * Also overrides the labels of the command holders.
	 * 
	 * @param gameWorldModel
	 */
	public ClockTickCommand(GameWorld gameWorldModel)
	{
		super("Tick");
		gw = gameWorldModel;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//gw.tick(elapsedTime);
	}
	
	public void actionPerformed(ActionEvent e, int elapsedTime)
	{
		gw.tick(elapsedTime);
	}
}