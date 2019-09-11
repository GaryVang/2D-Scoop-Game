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
/**
 * This class is the create new game command object.
 * 
 * Currently, the primary purpose of this object is to
 * output something to the console.
 * 
 * @author Gary
 *
 */
public class SaveGameCommand extends AbstractAction
{
	/**
	 * A constructor for the class.
	 * 
	 * Also overrides the labels of the command holders.
	 * 
	 * @param gameWorldModel
	 */
	public SaveGameCommand()
	{
		super("Save");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		System.out.print("Save selected\n");
	}
}