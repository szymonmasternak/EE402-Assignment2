import java.awt.Color;
import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
	//Variables
	public int ID = 0;
	public String Name = "	";
	public double[] Position = new double[] {0.0,0.0,0.0};
	public int rotation = 0;
	public Long Time = new DateTimeService().calendar.getTimeInMillis();
	public Color Colour = Color.BLACK;
	public boolean Disconnect = false;
	
	//Message Constructor
	public Message() {
		
	}
	
	//Message Constructor
	public Message(int id) {
		this.ID = id;
	}
	
	//Message Constructor
	public Message(int id, String name, double[] pos, int rot, Color colour) {
		this.ID = id;
		this.Name = name;
		this.Position = pos;
		this.rotation = rot;
		this.Colour = colour;
	}
	
	//Message Constructor
	public Message(int id, String name, int rot, Color colour) {
		this.ID = id;
		this.Name = name;
		this.rotation = rot;
		this.Colour = colour;
	}
	
	//Message Constructor
	public Message(int id, String name, Color colour) {
		this.ID = id;
		this.Name = name;
		this.Colour = colour;
	}
	
	//Overrides the toString Method. Returns the string representation of object
	public String toString() {
		String s =	"-----------------\n" +
					"ID: " + String.valueOf(ID) + "\n" +
					"Name: " + Name + "\n" +
					"Pos: " + String.valueOf(Position[0]) + "," + String.valueOf(Position[1]) + "," + String.valueOf(Position[2]) + "\n" +
					"Rot: " + String.valueOf(rotation) + "\n" +
					"Date: " + String.valueOf(Time) + "\n" +
					"Color: " + Colour.toString() + "\n" +
					"-----------------";
		return s;
	}
}
