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
import javax.swing.JOptionPane;
/**
 * This class is the quit command object.
 * 
 * The primary purpose of this object is open
 * a confirm dialog message box.
 * 
 * Depending on what the user selects(Yes or No),
 * the game will either exit the program or return
 * to the current state of the game.
 * 
 * @author Gary
 *
 */
public class QuitCommand extends AbstractAction
{
	/**
	 * A constructor for the class.
	 * 
	 * Also overrides the labels of the command holders.
	 * 
	 * @param gameWorldModel
	 */
	public QuitCommand()
	{
		super("Quit");
	}
	
	/**
	 * Opens a message box.
	 * Depending on the user's selection, either exit program
	 * or return to its current state.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		int result = JOptionPane.showConfirmDialog
				(null,
				"Are you sure you want to exit?",
				"Confirm Exit",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		
		if(result == JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
		return;
	}
}