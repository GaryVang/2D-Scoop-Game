package a4.commands;
/* Author: Gary Vang
 * 
 * Assignment: 3
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * This class is the about command object.
 * 
 * The primary purpose of this is pop up a message dialog box
 * when it is called.
 * 
 * @author Gary
 *
 */
public class AboutCommand extends AbstractAction
{
	/**
	 * A constructor for the class.
	 * 
	 * Also overrides the labels of the command holders.
	 */
	public AboutCommand()
	{
		super("About"); //Overrides the labels of command holders
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JOptionPane.showMessageDialog(null, 
				"Gary Vang\n" + "CSc-133\n" + "Version 0.3.5",
				"About",
				JOptionPane.INFORMATION_MESSAGE);
	}
}