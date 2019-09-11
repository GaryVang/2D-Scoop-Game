package a4;

/* Author: Gary Vang
 * 
 * Assignment: 2
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * This class is a view of the score and is a JPanel.
 * It registers itself with the model and updates
 * its JLabels accordingly.
 * 
 * @author Gary
 *
 */
public class ScoreView extends JPanel implements Observer
{
	private boolean flag;
	private int[] points;
	
	private JLabel totalPts, dogsCaptured, catsCaptured, dogsRemaining,
		catsRemaining, sound;
	
	/**
	 * Checks to see if the object is an array of integers.
	 * If so, the it proceeds to change the labels using setText.
	 */
	public void update (Observable o, Object arg)
	{// code here to update JLables from data in the Observable
		
		if(arg instanceof int[])
		{
			points = (int[]) arg;
			
			totalPts.setText("Total Points: " + points[0]);
			dogsCaptured.setText("Dogs Captured: " + points[1]);
			catsCaptured.setText("Cats Captured: " + points[2]);
			dogsRemaining.setText("Dogs Remaining: " + points[3]);
			catsRemaining.setText("Cats Remaining: " + points[4]);
			
			if(points[5] == 0)
			{
				sound.setText("Sound: " + "OFF");
			}
			else
			{
				sound.setText("Sound: " + "ON");
			}
		}	
	}
	
	//Left here in case I decide that I would rather have the observer register
	//when created, instead of doing it in separate line of code.
	public ScoreView(Observable gameModel) //Remove before submitting, unnecessary
	{
		gameModel.addObserver(this);
	}
	
	//Default class constructor.
	//Creates JLabels and add them to itself.
	public ScoreView()
	{
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
		
		this.setBorder(new LineBorder(Color.black, 2));
		
		totalPts = new JLabel("Total Points: " + 0);
			this.add(totalPts);
		dogsCaptured = new JLabel("Dogs Captured: " + 0);
			this.add(dogsCaptured);
		catsCaptured = new JLabel("Cats Captured: " + 0);
			this.add(catsCaptured);
		dogsRemaining = new JLabel("Dogs Remaining: " + 0);
			this.add(dogsRemaining);
		catsRemaining = new JLabel("Cats Remaining: " + 0);
			this.add(catsRemaining);
		sound = new JLabel("Sound: " + "OFF");
			this.add(sound);
	}
}