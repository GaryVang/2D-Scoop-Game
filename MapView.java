package a4;

/* Author: Gary Vang
 * 
 * Assignment: 4
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import a4.gameObjects.GameObject;

/**
 * This class is a view of the game map and is a JPanel.
 * It registers itself with the model and updates
 * its JPanel accordingly.
 * 
 * This class displays the contents of the game graphically
 * in this panel after obtaining the VTM through matrix multiplication.
 * 
 * This class requires access to GameWorld in order to draw the objects
 * in the world.
 * 
 * @author Gary
 *
 */
public class MapView extends JPanel implements Observer
{
	//Used to hold a reference to gameWorld
	private GameWorld gameW;
	
	private AffineTransform worldToND, ndToScreen, theVTM;
	
	private double windowLeft, windowRight, windowTop, windowBottom;
	
	
	/**
	 * When created, this object takes in a reference to gameworld.
	 */
	public MapView (Observable gameModel)
	{
		gameModel.addObserver(this);
		this.setBorder( new EtchedBorder());
		this.setBackground(Color.white);
		
		gameW = (GameWorld)gameModel;
		
		//Sets the default values for the window
		windowLeft = 0;
		windowBottom = 0;
		windowRight = 1;
		windowTop = 1;
	}
	
	//Default class constructor.
	//Just sets the border and makes the panel gray.
	public MapView()
	{
		this.setBorder( new EtchedBorder());
		this.setBackground(Color.white);
	}
	
	/**
	 * Calls repaint() on itself.
	 */
	public void update (Observable o, Object arg)
	{
		this.repaint();
	}
	
	/**
	 * Zooms out if mousewheel scroll forward.
	 * 
	 * Will only work during play mode.
	 */
	public void zoomIn()
	{
		double h = 1;
		double w = 1;
		windowLeft += w*0.05;
		windowRight -= w*0.05;
		windowTop -= h*0.05;
		windowBottom += h*0.05;
		this.repaint();
	}
	
	/**
	 * Zooms out if mousewheel scroll backward.
	 * 
	 * Will only work during play mode.
	 */
	public void zoomOut()
	{
		double h = 1;
		double w = 1;
		windowLeft -= w*0.05;
		windowRight += w*0.05;
		windowTop += h*0.05;
		windowBottom -= h*0.05;
		this.repaint();
	}
	
	/**
	 * Pans the camera depending on the direction that a person drags.
	 * 
	 * For example, dragging downward scrolls up.
	 * 
	 * will only work during play mode.
	 * @param direction
	 */
	public void pan(String direction)
	{
		if(direction == "left")
		{
			windowLeft -= 4*0.05;
			windowRight -= 4*0.05;
		}
		else if(direction == "right")
		{
			windowLeft += 4*0.05;
			windowRight += 4*0.05;
		}
		else if(direction == "up")
		{
			windowTop += 4*0.05;
			windowBottom += 4*0.05;
		}
		else if(direction == "down")
		{
			windowTop -= 4*0.05;
			windowBottom -= 4*0.05;
		}
		this.repaint();
	}
	
	/**
	 * Calculates the transformations required to go from world coordinates to ND to Screen.
	 * Iterates through a list of game objects and draws them.
	 * 
	 * Restores the g2d transformation at the end of the method.
	 * 
	 * Polymorphic.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform t = g2d.getTransform();
		
		worldToND = new AffineTransform();
		worldToND.scale(1/(windowTop - windowBottom), 1/(windowRight - windowLeft));
		worldToND.translate(-windowLeft, -windowBottom);
		
		ndToScreen = new AffineTransform();//(panelWidth, panelHeight);
		ndToScreen.translate(0, this.getHeight());
		ndToScreen.scale(1, -1);
		
		theVTM = (AffineTransform) ndToScreen.clone();
		theVTM.concatenate(worldToND);
		
		g2d.transform(theVTM);	
		
		Iterator theObjects = gameW.getIterator();
		while (theObjects.hasNext())
		{
			GameObject gObject = (GameObject) theObjects.getNext();
			gObject.draw(g2d);
		}
		
		g2d.setTransform(t);	
	}
}//End class