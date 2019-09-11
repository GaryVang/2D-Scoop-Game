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
import javax.swing.Timer;

import a4.Game;
import a4.GameWorld;

/**
 * This command has a reference to both gameWorld and the game.
 * A reference to the game is needed in order to stop the timer.
 * It is also binded to the pause button.
 * When the pause button is clicked, the button's name is changed to
 * "Play" and viceversa if the button is clicked again while its name
 * is "Pause."
 * 
 * This command object then checks the current game mode and changes
 * it accordingly. Starting and stopping the timer also.
 * 
 * @author Gary
 *
 */
public class PauseAndPlayCommand extends AbstractAction
{
	private GameWorld gw;
	private Game gameRef;
	
	public PauseAndPlayCommand(GameWorld gameWorldModel, Game game)
	{
		super("Pause");
		gw = gameWorldModel;
		gameRef = game;
	}
	
	//Performs the appropriate action when executed.
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if(gw.getMode() == 0)
		{
			gw.setMode(1);
			this.putValue(NAME, "Pause");
			gameRef.startTimer();
		}
		else
		{
			gw.setMode(0);
			this.putValue(NAME, "Play");
			gameRef.stopTimer();
		}	
	}
}