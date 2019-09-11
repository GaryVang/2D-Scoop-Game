package a4;

/* Author: Gary Vang
 * 
 * Assignment: 4
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import a4.gameObjects.Cat;
import a4.gameObjects.Dog;
import a4.gameObjects.GameObject;
import a4.gameObjects.IMoveable;
import a4.gameObjects.Net;
import a4.gameObjects.ShockWave;
import a4.sounds.Sound;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.Observable;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * This class holds a collection of game objects, state variables and methods
 * for manipulating data.
 * Once this object is instantiated by the Game constructor, it then calls
 * the initLayout() method which will set the starting layout of the game.
 * 
 * All game objects are stored in a collection.
 * And access is granted via an Iterator.
 * 
 * User input for the starting number of cats/dogs is obtained in this class using
 * scanner.
 * 
 * Points are also stored in this object: score, dogs/cats captured, and
 * dogs/cats remaining, and a flag for sound.
 * 
 * This class has 2 views registered to it: mapView and scoreView.
 * View's a notified if any objects or state variables are changed.
 * 
 * This class also contains 4 sounds that are played when:
 * -theres a fight
 * -a kitten object is created
 * -scoop is invoked
 * -or as background music that loops endlessly!(disable sound because it gets old quick)
 * 
 * All sounds were obtain or created by myself using a midi synthesizer.
 * Refer to the readme file for proper credit and a link to where I found the sound file.
 * 
 * @author Gary
 *
 */
public class GameWorld extends Observable
{
	/**
	 * Fields
	 */
	//A2
	private GameObjectCollection theGameObjectCollection = new GameObjectCollection();
	private int soundFlag = 0;
	
	//Variables for the initial number of cats/dogs
	private int numCats;
	private int numDogs;
	
	/**
	 * Variables for the points.
	 * All of which are initialized to 0.
	 */
	private int dogsCaptured = 0;
	private int catsCaptured = 0;
	private int dogsRemaining = 0;
	private int catsRemaining = 0;
	private int score = 0;
	
	private Scanner sc = new Scanner(System.in);	
	
	private int[] pointsArray = {score, dogsCaptured, catsCaptured, dogsRemaining, catsRemaining, soundFlag};
	
	//A3 Sounds
	//private String soundDir = "."  + File.separator + "src"+ File.separator + "a4" +  File.separator + "sounds" + File.separator;
	private String soundDir = "." + File.separator + "a4" +  File.separator + "sounds" + File.separator;
	
	
	private Sound myBackgroundSound  = new Sound(soundDir + "background_piano_final.mid");
	private Sound myFightSound = new Sound(soundDir + "Dog_Bark.wav");
	private Sound myKittenSound = new Sound(soundDir + "Kitten_Meow.wav");
	private Sound myScoopSound = new Sound(soundDir + "scoopAudio.mid");
	
	//A3 GameWorld State
	//0 = paused, 1 = play
	private int gameMode = 1;
	
	//Getter for the current game mode
	public int getMode()
	{
		return gameMode;
	}
	
	/**
	 * Setter for the game mode.
	 * After the game mode is set, it changes some state variables
	 * accordingly.
	 * 
	 * @param newMode
	 */
	public void setMode(int newMode)
	{
		gameMode = newMode;
		if(gameMode == 0)
		{
			//soundFlag = 0;
			myBackgroundSound.stop();
		}
		else
		{
			//soundFlag = 1;
			if(soundFlag == 1)
			{
				myBackgroundSound.loop();
			}
			
			Iterator theObjects = theGameObjectCollection.getIterator();
			
			while (theObjects.hasNext())
			{
				GameObject gObject = (GameObject) theObjects.getNext();
				
				if(gObject instanceof Dog)
				{
					if( ((Dog)gObject).isSelected())
					{
						((Dog)gObject).setSelected(false);
					
						setChanged();
						notifyObservers(gObject);
					}
				}		
			}
		}	
		setChanged();
		notifyObservers(pointsArray);
	}
	
	/**
	 * Sets up the initial layout of the game.
	 * 
	 * Asks the user for the number of cats/dogs.
	 * Creates and then stores those game objects into the collection.
	 * Then updates the values for dogs/cats remaining.
	 */
	public void initLayout()
	{
		System.out.print("Number of Cats: ");
		numCats = sc.nextInt();
		System.out.print("Number of Dogs: ");
		numDogs = sc.nextInt();
			
		for(int i = 0; i < numCats; i++)
		{
			theGameObjectCollection.add(new Cat());
		}
		
		for(int i = 0; i < numDogs; i++)
		{
			theGameObjectCollection.add(new Dog());
		}
		
		theGameObjectCollection.add(new Net());
		
		dogsRemaining = numDogs;
		catsRemaining = numCats;
		
		pointsArray[3] = dogsRemaining;
		pointsArray[4] = catsRemaining;
		setChanged();
		notifyObservers(pointsArray);
	}
	
	/**
	 * Obtains an iterator for the collection.
	 * Locate and calls the heal method for all game objects
	 * that are selected.
	 */
	public void heal()
	{
		Iterator theObjects = theGameObjectCollection.getIterator();
		
		while (theObjects.hasNext())
		{
			GameObject gObject = (GameObject) theObjects.getNext();
			
			if(gObject instanceof Dog)
			{
				if( ((Dog)gObject).isSelected())
				{
					((Dog)gObject).heal();
				
					setChanged();
					notifyObservers(gObject);
				}
			}		
		}
	}
	
	/**
	 * Updates fields affected by a cat being captured.
	 * Notifies observers of change.
	 * 
	 * Fields:
	 * cats captured
	 * score
	 * cats remaining
	 */
	private void catCaught()
	{
		catsCaptured++;
		score = score - 10;
		catsRemaining--;
		
		pointsArray[2] = catsCaptured;
		pointsArray[0] = score;
		pointsArray[4] = catsRemaining;
		setChanged();
		notifyObservers(pointsArray);
	}
	
	/**
	 * Updates fields affected by a dog being captured.
	 * Notifies observers of change.
	 * 
	 * Fields:
	 * dogs captured
	 * score
	 * dogs remaining
	 * 
	 * If there are no more dog's remaining, the game ends.
	 */
	private void dogCaught(int scratches)
	{
		dogsCaptured++;
		score += 10 - scratches;
		dogsRemaining--;
		
		pointsArray[1] = dogsCaptured;
		pointsArray[0] = score;
		pointsArray[3] = dogsRemaining;
		setChanged();
		notifyObservers(pointsArray);
		
		if(dogsRemaining == 0)
		{
			gameOver();
		}
	}
	
	/**
	 * Game ends due to all dogs being captured.
	 * Game mode is set to pause.
	 * 
	 * A message dialog is displayed with the final score before
	 * exiting the game.
	 */
	private void gameOver()
	{
		setMode(0);
		
		JOptionPane.showMessageDialog(null,
			    "Final Score: " + score,
			    "A plain message",
			    JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}
	
	/**
	 * Locates the net and tells it to expand by calling its
	 * expand() method.
	 * Notifies observers of change.
	 */
	public void expandNet()
	{
		Iterator theObjects = theGameObjectCollection.getIterator();
		
		while (theObjects.hasNext())
		{
			GameObject gObject = (GameObject) theObjects.getNext();
			if( gObject instanceof Net)
			{
				((Net) gObject).expand();
				break;
			}
		}
		displayMap();
	}
	
	/**
	 * Locates the net and tells it to contract by calling its
	 * contract() method.
	 * Notifies observers of change.
	 */
	public void contractNet()
	{
		Iterator theObjects = theGameObjectCollection.getIterator();
		
		while (theObjects.hasNext())
		{
			GameObject gObject = (GameObject) theObjects.getNext();
			if( gObject instanceof Net)
			{
				((Net) gObject).contract();
				break;
			}
		}
		displayMap();
	}
	
	/**
	 * Locates the net and stores its location and size into a local variable to minimize typing.
	 * Then the method parses through the animals in the collection and see if any of them are
	 * completely inside the net.
	 * The netCheck method will return true if the animal is indeed inside the net.
	 * Methods:
	 * dogCaught() - adds points to the score depending on the dog's number of scratches
	 * catCaught() - subtracts 10 points from the score
	 * 
	 * The objects are removed afterwards and due to subsequent objects in the collections being shifted left by 1
	 * i, which keeps track of the current index inside the collection, needs to be decremented.
	 *
	 * Notifies observers of change.
	 * Note: Only scoop calls that remove an object will ask mapView to update.
	 * 
	 * Scooping with play a sound.
	 */
	public void scoop()
	{
		if(soundFlag == 1)
			myScoopSound.play();
		
		int netSize = 0;
		Point2D netLocation = new Point2D.Double();
		Iterator theObjects = theGameObjectCollection.getIterator();
		
		while (theObjects.hasNext())
		{
			GameObject gObject = (GameObject) theObjects.getNext();
			if( gObject instanceof Net)
			{
				netSize = ((Net) gObject).getSize();
				netLocation  = ((Net) gObject).getLocation();
				break;
			}
		}
		
		theObjects = theGameObjectCollection.getIterator(); //Resets the index: prevents ConcurrentModificationException
		while (theObjects.hasNext())
		{
			GameObject gObject = (GameObject) theObjects.getNext();
			if( gObject instanceof Dog)
			{
				if (netCheck((Dog) gObject, netSize, netLocation) == true)
				{//True means the object is inside of the net
					dogCaught(((Dog)gObject).getScratch());					
					theObjects.remove();
					theObjects = theGameObjectCollection.getIterator(); //Resets the index: prevents ConcurrentModificationException
					
					ShockWave shockWave = new ShockWave(netLocation.getX(), netLocation.getY());
					theGameObjectCollection.add(shockWave);
					displayMap();
				}
			}
			else if(gObject instanceof Cat)
			{
				if (netCheck((Cat) gObject, netSize, netLocation) == true)
				{//True means the object is inside of the net
					catCaught();			
					theObjects.remove();
					theObjects = theGameObjectCollection.getIterator(); //Resets the index: prevents ConcurrentModificationException
					
					ShockWave shockWave = new ShockWave(netLocation.getX(), netLocation.getY());
					theGameObjectCollection.add(shockWave);
					displayMap();
				}
			}
		}
	}
	
	//********************************************************************
	// Net Check
	private boolean netCheck(Dog dogObj, int netSize, Point2D netLocation)
	{
		if((dogObj.getY() + dogObj.getSize()/2 <= netLocation.getY() + netSize/2) && (dogObj.getY() - dogObj.getSize()/2 >= netLocation.getY() - netSize/2 ) && (dogObj.getX() + dogObj.getSize()/2 <= netLocation.getX() + netSize/2) && (dogObj.getX() - dogObj.getSize()/2 >= netLocation.getX() - netSize/2))
		{
			return true;
		}
		return false;
	}
	
	private boolean netCheck(Cat catObj, int netSize, Point2D netLocation)
	{
		if((catObj.getY() + (catObj.getSize() * (Math.sqrt(3)/2)) <= netLocation.getY() + netSize/2) && (catObj.getY() - (catObj.getSize() * (Math.sqrt(3)/2)) >= netLocation.getY() - netSize/2) && (catObj.getX() + (catObj.getSize()/2) <= netLocation.getX() + netSize/2) && (catObj.getX() - (catObj.getSize()/2) >= netLocation.getX() - netSize/2))
		{
			return true;
		}
		return false;
	}
	
	//*********************************************************************
	
	/**
	 * Moves the net in the appropriate direction based on input from the user: u, d, l, r.
	 * Invokes the net's guide method, passing the direction into it.
	 * Notifies observers of change.
	 * 
	 * @param d the Direction
	 */
	public void guideNet(char d)
	{
		Iterator theObjects = theGameObjectCollection.getIterator();
		
		while (theObjects.hasNext())
		{
			GameObject gObject = (GameObject) theObjects.getNext();
			if( gObject instanceof Net)
			{
				((Net) gObject).guide(d);
				break;
			}
		}
		displayMap();
	}
	
	/**
	 * Cat and cat collision creates a new cat object "Kitten" in a nearby position.
	 * Selects a cat object and creates a nearby new cat object with the kitten flag set
	 * as true.
	 * Plays a kitten sound if one is created.
	 * The object is then store into the collection.
	 * 
	 * A maximum of 30 cats can be on the screen at once.
	 * 
	 * Notifies observers of change.
	 */
	public void mate(Cat catObject)
	{
		if(catsRemaining <= 30)	//Will only create kittens if the max of 30 hasn't been reached
		{
			Cat kitten = new Cat(catObject);//Calls an overloaded class constructor to create a kitten object
	
			theGameObjectCollection.add(kitten);
			
			catsRemaining++;
			
			//Updates View
			displayMap();
			
			pointsArray[4] = catsRemaining;
			setChanged();
			notifyObservers(pointsArray);
			
			if(soundFlag == 1)
				myKittenSound.play();
		}
	}
	
	/**
	 * Cat and dog collision increments a dog's scratches counter which also decreases its speed by 1 and changes its color.
	 * Checks to see if there is at least one cat in the game world.
	 * If there is, it selects a dog object and calls its fight() method.
	 * The method will update the dog's scratches, speed, and color.
	 * Else do nothing.
	 * 
	 * Plays a sound with a dog gets into a fight.
	 * 
	 * Notifies observers of change.
	 */
	public void fight(Dog dogObject)
	{
		if(soundFlag == 1)
			myFightSound.play();
		
		dogObject.fight();
		displayMap();
	}
	
	/**
	 * The game world ticks.
	 * Locates all objects that can move and calls their move() method
	 * which will update their coordinates.
	 * 
	 * Notifies observers of change.
	 */
	public void tick(int elapsedTime)
	{
		Iterator theObjects = theGameObjectCollection.getIterator();
		
		while (theObjects.hasNext())
		{
			GameObject gObject = (GameObject) theObjects.getNext();
			
			if( gObject instanceof IMoveable)
			{
				((IMoveable)gObject).move(elapsedTime);
			}
		}

		theObjects = theGameObjectCollection.getIterator();
		
		while (theObjects.hasNext())
		{
			GameObject gObject2 = (GameObject) theObjects.getNext();
			
			if(gObject2 instanceof ShockWave)
			{
				if(((ShockWave)gObject2).isAlive())
				{
					
				}
				else
				{
					theObjects.remove();
					theObjects = theGameObjectCollection.getIterator(); //Resets the Iterator
				}
			}
		}
		displayMap();	
	}
	
	/**
	 * Iterates the collection and updates map view.
	 * 
	 * Polymorphism.
	 * 
	 * Notifies observers of change.
	 */
	public void displayMap()//Change to private
	{
		Iterator theObjects = theGameObjectCollection.getIterator();
		
		while (theObjects.hasNext())
		{
			GameObject gObject = (GameObject) theObjects.getNext();
			
			setChanged();
			notifyObservers(gObject);	
		}
		System.out.print("\n"); //For increased readability
	}
	
	/**
	 * Flips the soundFlag.
	 * 
	 * Notifies observers of change.
	 */
	public void soundSetting() // 0 = false, 1 = true
	{
		if(soundFlag == 0)
		{
			soundFlag = 1;
			pointsArray[5] = soundFlag;
			setChanged();
			notifyObservers(pointsArray);
			
			if(gameMode == 1)
				myBackgroundSound.loop();
		}
		else
		{
			soundFlag = 0;
			pointsArray[5] = soundFlag;
			setChanged();
			notifyObservers(pointsArray);
			
			myBackgroundSound.stop();
		}
	}
	
	/**
	 * A getter that returns the soudFlag.
	 * @return
	 */
	public int getSoundFlag()
	{
		return soundFlag;
	}
	
	//A3
	//Allows for something with a reference to gameWorld to obtain an iterator.
	//Mainly used by mapView
	public Iterator getIterator()
	{
		return theGameObjectCollection.getIterator();
	}	
}