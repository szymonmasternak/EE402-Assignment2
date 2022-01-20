import java.awt.Choice;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.HashMap;

//Class for Creating Color DropDown
public class ColorDropDown extends Choice{
	//HashMap that contains Color object and corresponding string of that color object
	HashMap<Color, String> hashmap = new HashMap<Color, String>();
	
	//Initialises the Hashmap with values
	private void initialiseHashMap() {
		hashmap.put(Color.BLACK, "BLACK");
		hashmap.put(Color.BLUE, "BLUE");
		hashmap.put(Color.CYAN, "CYAN");
		hashmap.put(Color.DARK_GRAY, "DARK_GRAY");
		hashmap.put(Color.GRAY, "GRAY");
		hashmap.put(Color.GREEN, "GREEN");
		hashmap.put(Color.LIGHT_GRAY, "LIGHT_GRAY");
		hashmap.put(Color.MAGENTA, "MAGENTA");
		hashmap.put(Color.ORANGE, "ORANGE");
		hashmap.put(Color.PINK, "PINK");
		hashmap.put(Color.RED, "RED");
		hashmap.put(Color.WHITE, "WHITE");
		hashmap.put(Color.YELLOW, "YELLOW");
	}
	
	//Constructor for Color DropDown
	public ColorDropDown(){
		initialiseHashMap();
		for(Color key: hashmap.keySet())
			this.add(hashmap.get(key));
	}
	
}
