package a4.sounds;
/* Author: Gary Vang
 * 
 * Assignment: 4
 * 
 * Class: CSc-133
 * Section: 02
 * Instructor: Pinar Muyan-Ozcelik
 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
/**
 * The purpose of this class to allow for the addition of
 * sound files and simplifies the process.
 * 
 * Note: Will not play mp3 files.
 * @author Gary
 *
 */
public class Sound 
{
	private AudioClip myClip ;
	
	/**
	 * Class constructor. Takes in the path to the sound file.
	 * 
	 * If the file isn't found, throws a runtimeexception error.
	 * @param fileName
	 */
	public Sound(String fileName) 
	{
		try 
		{
			File file = new File(fileName);
			if (file.exists()) 
			{
				myClip = Applet.newAudioClip(file.toURI().toURL());
			} else
			{
				throw new RuntimeException("Sound: file not found: " + fileName);
			}
		} catch (MalformedURLException e) 
		{
			throw new RuntimeException("Sound: malformed URL: " + e);
		}
	}
	
	//Plays the clip
	public void play() 
	{
		myClip.play();
	}
	
	//Plays the clip in an endless loop
	public void loop()
	{
		myClip.loop();
	}
	
	//Stops the clip
	public void stop()
	{
		myClip.stop();
	}
}