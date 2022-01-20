/* The Connection Handler Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 */

import java.net.*;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ThreadedConnectionHandler extends Thread{
	//List That contains all ID's of all connected Clients
	static List<Integer> idList = new ArrayList<Integer>(); 
	
    private Socket clientSocket = null;				// Client socket object
    private ObjectInputStream is = null;			// Input stream
    private ObjectOutputStream os = null;			// Output stream
    private DateTimeService theDateService;
   
	// The constructor for the connection handler
    public ThreadedConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        //Set up a service object to get the current date and time
        theDateService = new DateTimeService();
    }
    

    // Runs the thread
    public void run() {
         try {
            this.is = new ObjectInputStream(clientSocket.getInputStream());
            this.os = new ObjectOutputStream(clientSocket.getOutputStream());
            sendID();
            while (this.readCommand()) {}
         } 
         catch (IOException e) 
         {
        	System.out.println("XX. There was a problem with the Input/Output Communication:");
            e.printStackTrace();
         }
    }

    // Method for reading the command sent from the client
    private boolean readCommand() {
    	Object o = null;
        Message m = null;
        try {
            o = is.readObject();
            
        	m = (Message) o;
        	
        	if(m.Disconnect == true) {
        		DroneCanvas.dronelist.remove(m.ID);
        		idList.remove(m.ID);
            	rightColumn.connectionLabel.setText(String.valueOf(DroneCanvas.dronelist.size()));
        	}
        	
        	System.out.println("01. <- Received a Message object from the client");
            System.out.println(m);
            
            DroneCanvas.dronelist.get(m.ID).color = m.Colour;
            DroneCanvas.dronelist.get(m.ID).name = m.Name;
            DroneCanvas.dronelist.get(m.ID).translate(m.Position[0], m.Position[1], m.Position[2]);
            DroneCanvas.dronelist.get(m.ID).rotateIcon(m.rotation);

            os.flush();
            return true;

        } 
        catch (Exception e){    // catch a general exception
        	this.closeSocket();
            return false;
        }
        

    }

    // Method for sending a generic object to the client
    private void send(Object o) {
        try {
            System.out.println("02. -> Sending (" + o +") to the client.");
            this.os.writeObject(o);
            this.os.flush();
        } 
        catch (Exception e) {
            System.out.println("XX." + e.getStackTrace());
        }
    }
    
    //Method For sending a new unique ID to the client
    public void sendID() {
    	int id = getNewID();
    	
    	this.send(new Message(id));
    	System.out.println("The ID IS:" + String.valueOf(id));
    }
    
    //Method for generating new ID and adding Drone to dronelist
    public static int getNewID() {
    	int i=0;
    	Point position = DroneIcon.generatePosition(rightColumn.randomBool);
    	
    	if(DroneCanvas.dronelist.isEmpty()) {
    		DroneCanvas.dronelist.add(new DroneIcon(position, "Test"));
        	rightColumn.connectionLabel.setText(String.valueOf(DroneCanvas.dronelist.size()));
        	idList.add(i);
    		return i;
    	}
    	
    	while(idList.contains(i)) {
    		i++;
    	}
    	
    	idList.add(i);
    	DroneCanvas.dronelist.add(new DroneIcon(position, "Test"));
    	rightColumn.connectionLabel.setText(String.valueOf(DroneCanvas.dronelist.size()));
    	return i;
    }
    
    // Method for Closing the Socket
    public void closeSocket() {
        try {
            this.os.close();
            this.is.close();
            this.clientSocket.close();
        } 
        catch (Exception e) {
            System.out.println("XX. " + e.getStackTrace());
        }
    }
}