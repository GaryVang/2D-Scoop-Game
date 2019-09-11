package a4;

/* Author: Gary Vang
 * 
 * Assignment: 4
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import a4.commands.*;
import a4.gameObjects.Cat;
import a4.gameObjects.Dog;
import a4.gameObjects.GameObject;
import a4.gameObjects.ICollideable;

/**
 * This is the primary class and Controller of the game.
 * This class contains several components: a GameWorld which holds a collection of game objects and
 * a set of methods to accept and execute user commands.
 * Refer to the GameWorld class for a more detailed description.
 * 
 * It also creates and registers the observers to the observables.
 * 
 * User input is accepted in the form of keybinds or JComponents and the appropriate methods are then invoked.
 * 
 * A4: User can now pan, zoom, and drag to change the world window.
 * 
 * Also includes a timer to drive the animation.
 * 
 * @author Gary
 *
 */
public class Game extends JFrame implements ActionListener, MouseListener, MouseWheelListener, MouseMotionListener
{
	/**
	 * Declaration of fields.
	 */
	//Model
	private GameWorld gw;
	
	//Views
	private MapView mv;
	private ScoreView sv;
	
	//A3
	//Timer
	private Timer timer;
	private final int DELAY_IN_MSEC = 20;
	
	//Commands
	private ExpandNetCommand expandNetCommand;
	private ContractNetCommand contractNetCommand;
	private ScoopCommand scoopCommand;
	private MoveNetRCommand moveNetRCommand;
	private MoveNetLCommand moveNetLCommand;
	private MoveNetUCommand moveNetUCommand;
	private MoveNetDCommand moveNetDCommand;
	private KittenColCommand kittenColCommand;
	private CatDogFightCommand catDogFightCommand;
	private ClockTickCommand clockTickCommand;
	private QuitCommand quitCommand;
	
	private HealCommand healCommand;
	private PauseAndPlayCommand pauseAndPlayCommand;
	
	//Commands - File Menu
	private NewGameCommand newGameCommand;
	private SaveGameCommand saveGameCommand;
	private UndoCommand undoCommand;
	private SoundCommand soundCommand;
	private AboutCommand aboutCommand;
	
	//JPanel
	private JPanel controlPanel;
		
	//JButtons on controlPanel
	private JButton expandNet, shrinkNet, scoop, moveR, moveL, moveU,
		moveD, heal, pausePlay;
	
	//JMenuBar
	private JMenuBar bar;
	
	//Menus
	private JMenu fileMenu, commandMenu;
		
	//Menu Item
	private JMenuItem newItem, saveItem, undoItem, aboutItem;
	private JCheckBoxMenuItem soundItem;
	private JMenuItem expandN, shrinkN, quitGame;

	//Keystroke
	private KeyStroke scoopKey, moveRKey, moveLKey, moveUKey, 
		moveDKey, quitKey;
	
	//Input and Action map
	private int gameMap;
	private InputMap imap;
	private ActionMap amap;
	
	//A4
	private Point prevPoint = null;
	private Point currentPoint = null;
	
	/**
	 * Default constructor for the class.
	 * When an object of type Game is created, it also creates a GameWorld object
	 * before proceeding with setting up the initial layout of the game.
	 * After doing so, it then call the play method which will start asking the
	 * user for input and will invoke the appropriate method.
	 */
	public Game()
	{
		gw = new GameWorld();
		
		//Creates and registers views
		mv = new MapView(gw); //A3 constructor call change
		sv = new ScoreView();
		gw.addObserver(sv);
		
		gw.initLayout();	

		setSize(1000, 800); 
		setTitle("Dog Catcher");
		
		getContentPane().setLayout(new BorderLayout()); //Sets the frame's layout to borderlayout
		
		//Adding mouse listeners to the map view
		mv.addMouseListener(this);
		mv.addMouseWheelListener(this);
		mv.addMouseMotionListener(this);
		
		//Command Objects
		//*************************************
		expandNetCommand = new ExpandNetCommand(gw);
		contractNetCommand = new ContractNetCommand(gw);
		scoopCommand = new ScoopCommand(gw);
		moveNetRCommand = new MoveNetRCommand(gw);
		moveNetLCommand = new MoveNetLCommand(gw);
		moveNetUCommand = new MoveNetUCommand(gw);
		moveNetDCommand = new MoveNetDCommand(gw);
		kittenColCommand = new KittenColCommand(gw);
		catDogFightCommand = new CatDogFightCommand(gw);
		clockTickCommand = new ClockTickCommand(gw);
		quitCommand = new QuitCommand();
		
		healCommand = new HealCommand(gw);
		pauseAndPlayCommand = new PauseAndPlayCommand(gw, this);
		
		//Exclusively JMenuBar Commands
		newGameCommand = new NewGameCommand();
		saveGameCommand = new SaveGameCommand();
		undoCommand = new UndoCommand();
		soundCommand = new SoundCommand(gw);
		aboutCommand = new AboutCommand();
		
		//JPanels - Make panels and buttons private before submitting
		//******************************************************
		//MapView Panel
		getContentPane().add(sv, BorderLayout.NORTH); //Adds the scoreView(sv) to north
		
		//************************************************************
		//Control Panel
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(10, 0)); 
		controlPanel.setBorder(new TitledBorder(" Commands: "));
		
		expandNet = new JButton("Expand Net");
		controlPanel.add(expandNet);
		
		shrinkNet = new JButton("Shrink Net");
		controlPanel.add(shrinkNet);
		
		scoop = new JButton("Scoop");
		controlPanel.add(scoop);
		
		moveR = new JButton("Move Right");
		controlPanel.add(moveR);
		
		moveL = new JButton("Move Left");
		controlPanel.add(moveL);
		
		moveU = new JButton("Move Up");
		controlPanel.add(moveU);
		
		moveD = new JButton("Move Down");
		controlPanel.add(moveD);
		
		heal = new JButton("Heal");
		controlPanel.add(heal);
		
		pausePlay = new JButton("Pause");
		controlPanel.add(pausePlay);
		
		getContentPane().add(controlPanel, BorderLayout.WEST);
		
			//setAction for buttons
			expandNet.setAction(expandNetCommand);
			shrinkNet.setAction(contractNetCommand);
			scoop.setAction(scoopCommand);
			moveR.setAction(moveNetRCommand);
			moveL.setAction(moveNetLCommand);
			moveU.setAction(moveNetUCommand);
			moveD.setAction(moveNetDCommand);
			heal.setAction(healCommand);
			pausePlay.setAction(pauseAndPlayCommand);
		
		//***********************************************************
		//mapPanel
		getContentPane().add(mv, BorderLayout.CENTER);
		
		//************************************************
		
		buildMenuBar();//Calls a method that builds the MenuBar
		
		//Gets the input map for the map panel
		gameMap = JComponent.WHEN_IN_FOCUSED_WINDOW;	
		imap = mv.getInputMap(gameMap);
		
		//Gets the action map for map panel
		amap = mv.getActionMap();
		
		//KeyStrokes
		scoopKey = KeyStroke.getKeyStroke('s');
		moveRKey = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0);
		moveLKey = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0);
		moveUKey = KeyStroke.getKeyStroke(KeyEvent.VK_UP,0);
		moveDKey = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0);
		quitKey = KeyStroke.getKeyStroke('q');
		
		//imap
		imap.put(scoopKey, "scoop");
		imap.put(moveRKey, "moveR");
		imap.put(moveLKey, "moveL");
		imap.put(moveUKey, "moveU");
		imap.put(moveDKey, "moveD");
		imap.put(quitKey, "quit");
		
		//amap
		amap.put("scoop", scoopCommand);
		amap.put("moveR", moveNetRCommand);
		amap.put("moveL", moveNetLCommand);
		amap.put("moveU", moveNetUCommand);
		amap.put("moveD", moveNetDCommand);
		amap.put("quit",  quitCommand);
		
		//A3
		//creates a timer and starts it
		timer = new Timer(DELAY_IN_MSEC, this);
		timer.start();
		
		//Checks the current game mode and disables
		//the heal command if game mode is "Play"
		if(gw.getMode() == 1)
			healCommand.setEnabled(false);
		
		setVisible(true);
	}//End of class constructor
	
	/**
	 * Invoked mainly by the pause action command.
	 * Enables and disables the appropriate commands.
	 */
	public void startTimer()
	{
		timer.start();
		
		healCommand.setEnabled(false);
		expandNetCommand.setEnabled(true);
		contractNetCommand.setEnabled(true);
		scoopCommand.setEnabled(true);
		moveNetRCommand.setEnabled(true);
		moveNetLCommand.setEnabled(true);
		moveNetUCommand.setEnabled(true);
		moveNetDCommand.setEnabled(true);		
	}
	
	/**
	 * Invoked mainly by the pause action command.
	 * Enables and disables the appropriate commands.
	 */
	public void stopTimer()
	{
		timer.stop();
		
		healCommand.setEnabled(true);
		expandNetCommand.setEnabled(false);
		contractNetCommand.setEnabled(false);
		scoopCommand.setEnabled(false);
		moveNetRCommand.setEnabled(false);
		moveNetLCommand.setEnabled(false);
		moveNetUCommand.setEnabled(false);
		moveNetDCommand.setEnabled(false);
	}
	
	
	/**
	 * This method builds a JMenuBar with 2 Menus: File and Command.
	 * After doing so, it'll create menu items and add them to those menus.
	 */
	private void buildMenuBar()
	{
		bar = new JMenuBar();
		
		//File Menu
		fileMenu = new JMenu("File");
			
			//Creates and adds menu items
			newItem = new JMenuItem("New");
				fileMenu.add(newItem);
			saveItem = new JMenuItem("Save");
				fileMenu.add(saveItem);
			undoItem = new JMenuItem("Undo");
				fileMenu.add(undoItem);
			soundItem = new JCheckBoxMenuItem("Sound");
				fileMenu.add(soundItem);
			aboutItem = new JMenuItem("About");
				fileMenu.add(aboutItem);

				//File Menu setAction
				newItem.setAction(newGameCommand);
				saveItem.setAction(saveGameCommand);
				undoItem.setAction(undoCommand);
				soundItem.setAction(soundCommand);
				aboutItem.setAction(aboutCommand);			
		
		//Command Menu
		commandMenu = new JMenu("Commands");
		
			//Creates and adds menu items
			expandN = new JMenuItem("Expand Net");
				commandMenu.add(expandN);
			shrinkN = new JMenuItem("Shrink Net");
				commandMenu.add(shrinkN);
			quitGame = new JMenuItem("Quit");
				commandMenu.add(quitGame);
				
				//Command Menu setAction
				expandN.setAction(expandNetCommand);
				shrinkN.setAction(contractNetCommand);
				quitGame.setAction(quitCommand);
		
		//Adds both menus to the menu bar
		bar.add(fileMenu);
		bar.add(commandMenu);
		
		//Adds menu bar to the frame
		this.setJMenuBar(bar);
	}

	/**
	 * This method handles the timer tick action event and
	 * checks for collision between collideable objects.
	 * 
	 * It will only perform any actions if the game mode is set
	 * to "Play."
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if(gw.getMode() == 1)
		{
			clockTickCommand.actionPerformed(arg0, DELAY_IN_MSEC);
			
			Iterator iter = gw.getIterator();
			while(iter.hasNext())
			{
				GameObject gObject = (GameObject) iter.getNext();
				
				if( gObject instanceof ICollideable)
				{
					ICollideable curObj = (ICollideable) gObject;
					
					Iterator iter2 = gw.getIterator();	
					
					while(iter2.hasNext())
					{
						GameObject gObject2 = (GameObject) iter2.getNext();
						
						if(gObject2 instanceof ICollideable)
						{	
							ICollideable otherObj = (ICollideable) gObject2;
							
							if(otherObj != curObj)
							{
								if(curObj.collidesWith(otherObj) && curObj.inColList(otherObj) == false)//added &&
								{
									curObj.handleCollision(otherObj);
									curObj.addToColList(otherObj);
									otherObj.addToColList(curObj);
									
									if(curObj instanceof Cat && otherObj instanceof Dog)
									{
										catDogFightCommand.actionPerformed(arg0, (Dog) otherObj);
									}
									else if(curObj instanceof Dog && otherObj instanceof Cat)
									{
										catDogFightCommand.actionPerformed(arg0, (Dog) curObj);
									}
									else if(curObj instanceof Cat && otherObj instanceof Cat)
									{
										if(((Cat)curObj).isKitten() != true && ((Cat)otherObj).isKitten() != true)
											kittenColCommand.actionPerformed(arg0, (Cat) curObj);
									}
								}
								else if(curObj.collidesWith(otherObj) == false)
								{
									curObj.removeFromColList(otherObj);
									otherObj.removeFromColList(curObj);
								}
							}
						}
					}
				}
			}//End of outer While Loop
			mv.repaint();
		}
		else
		{
			timer.stop();
		}
	}

	/**
	 * Handles mouse click events, but will only do something if
	 * the game is paused. (healCommand is enabled when game mode is paused)
	 * 
	 * Checks to see if a "Selectable" object has been clicked.
	 * If so, the object will redraw it.
	 * If ctrl is held, objects will be not be unselected if 
	 * the mouse clicks something other than a selectable object.
	 * 
	 * If ctrl isn't held down when the mouse click event is triggered,
	 * any clicks on selectable objects will make the clicked object
	 * selected and unselect all other objects.
	 * 
	 * Clicks on anything else will unselect all selectable objects.
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		if(healCommand.isEnabled())
		{
			Point p = arg0.getPoint();
			Iterator iter = gw.getIterator();
			
			while(iter.hasNext())
			{
				GameObject gObject = (GameObject) iter.getNext();
				
				if(gObject instanceof Dog)
				{
					if(((Dog) gObject).contains(p))
					{
						((Dog) gObject).setSelected(true);
					}
					else if (arg0.isControlDown() != true)//If ctrl is pressed when clicked, nothing will be unselected
					{
						((Dog) gObject).setSelected(false);
					}
					mv.repaint();
				}
			}
		}
	}

	//Mouse events that are not used but requires concrete implementations.
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{	}

	/**
	 * Stores the initial point before the mousedrag.
	 */
	@Override
	public void mousePressed(MouseEvent arg0) 
	{	
		prevPoint = arg0.getPoint();
	}

	/**
	 * Clears the two points used for determing the drag direction.
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{	
		prevPoint = null;
		currentPoint = null;
	}

	/**
	 * Scrolling up will zoom in.
	 * Scrolling down will zoom out.
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) 
	{
		if(gw.getMode() == 1)
		{
			int wheelDirection = arg0.getWheelRotation();
			
			if(wheelDirection<0)//Wheel moved up
			{
				mv.zoomIn();
		
			}
			else
			{
				mv.zoomOut();
			}
		}
	}

	/**
	 * Dragging the mouse while the game is in Play mode will
	 * pan the world window.
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		if(gw.getMode() == 1)
		{
			currentPoint = arg0.getPoint();
			
			if(prevPoint.y > currentPoint.y + 10)
			{
				mv.pan("down");
			}
			else if (prevPoint.y < currentPoint.y - 10)
			{
				mv.pan("up");
			}
			else if (prevPoint.x > currentPoint.x)
			{
				mv.pan("right");
			}
			else if (prevPoint.x < currentPoint.x)
			{
				mv.pan("left");
			}	
		}
	}
	
	//MouseMotio event that does nothing
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}//End of class