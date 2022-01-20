import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//Main Code for client
public class ClientMain extends Frame implements ActionListener, WindowListener, ItemListener {
	//Drone Parameters
	int RobotSpeed = 50;
	String RobotName = "Drone";
	Color RobotColor = Color.BLACK;
	int RobotID = 0;
	double defaultStep = 20;

	//Increment/Decrement Button Speed Buttons
	Button inc_speed_bt = new Button("+");
	Button dec_speed_bt = new Button("-");
	
	//Textfields for Speed and Name
	TextField field_speed = new TextField(String.valueOf(RobotSpeed));
	TextField field_name = new TextField(RobotName);
	
	//Dropdown for Colors
	ColorDropDown dropdown = new ColorDropDown();
	
	//Manouever Buttons for Drone
	Button up_bt = new Button("up");
	Button down_bt = new Button("down");
	Button left_bt = new Button("left");
	Button right_bt = new Button("right");
	Button rr_bt = new Button("RR");
	Button rl_bt = new Button("RL");
	Button ascent_bt = new Button("^");
	Button descent_bt = new Button("v");
	
	//Dimensions for Rotate Right and Rotate Left Buttons
	Dimension dim_bt = new Dimension(50,50);
	
	//Time Last Message Sent
    Long lastMesssageTime = (long) 0;
    Message lastMessage = new Message();
	
    //Socket Variables
    private static int portNumber = 5050;
    private Socket socket = null;
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;
    
    //Labels for Status, Name, Speed, Colour, LastMsg Time and ID
    public Label ConnStatusLabel = new Label("None");
    public Label NameLabel = new Label(RobotName);
    public Label SpeedLabel = new Label(String.valueOf(RobotSpeed));
    public Label ColourLabel = new Label(getColorString(RobotColor));
    public Label LastMsgLabel = new Label("None");
    public Label IdLabel = new Label(String.valueOf(RobotID));
    
    //Constructor for Client
	public ClientMain(String serverIP, String Name) {
		super("Client Main");
		
		//Sets Robot Name
		RobotName = Name;
		field_name.setText(RobotName);
		
		if (!connectToServer(serverIP)) {
    		System.out.println("XX. Failed to open socket connection to: " + serverIP);
    		System.exit(-1);
    	}
		
		//Main Frame divided into 3 Squares
		this.addWindowListener(this);		
		this.setLayout(new GridLayout(0,3,5,5));
		
		//Left Panel for Manouever Buttons
		Panel p1 = new Panel();
		p1.setBackground(Color.CYAN);
		p1.setLayout(new GridBagLayout());
		
		//Constraints for Manouever Buttons
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.BOTH;
		c1.weightx = 1.0;
		c1.weighty = 1.0;
        
		c1.gridx = 1;
        c1.gridy = 0;
		p1.add(up_bt,c1);
		
		c1.gridx = 0;
        c1.gridy = 1;
		p1.add(left_bt,c1);
		
		c1.gridx = 2;
        c1.gridy = 1;
		p1.add(right_bt,c1);
		
		c1.gridx = 1;
        c1.gridy = 1;
		p1.add(down_bt,c1);
		
		c1.fill = GridBagConstraints.NONE;
		c1.weightx = 0.0;
		c1.weighty = 0.0;
		c1.gridx = 0;
        c1.gridy = 0;
        
        //Rotate left Button is imported
        rl_bt.setPreferredSize(dim_bt);
		p1.add(rl_bt,c1);
		
		c1.weightx = 0.0;
		c1.weighty = 0.0;
		c1.gridx = 2;
        c1.gridy = 0;

        //Rotate Right Button is imported
        rr_bt.setPreferredSize(dim_bt);
		p1.add(rr_bt,c1);
		
		this.add(p1);
		
		//Panel for Middle Panel
		Panel p2 = new Panel();
		p2.setBackground(Color.RED);
		this.add(p2);
		
		//Panel for the Right Panel
		Panel p3 = new Panel();
		p3.setBackground(Color.BLUE);
		this.add(p3);
		
		//Layout and Constraints for middle panel
		p2.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.insets = new Insets(5,5,5,5);
		c2.anchor = GridBagConstraints.NORTHWEST;
		c2.gridx = 0;
        c2.gridy = 0;
        c2.gridwidth = 1;
        c2.gridheight = 1;
        
        //Speed Label/TextField is added
        p2.add(new Label("Speed"),c2);
        c2.gridx = 1;
        c2.fill = GridBagConstraints.HORIZONTAL;
        p2.add(field_speed, c2);
        
        //Increment Speed Button is added
        c2.insets = new Insets(5,0,5,0);
        c2.gridx = 2;
        p2.add(inc_speed_bt,c2);
        
        //Decrement Speed Button is added
        c2.insets = new Insets(5,0,5,5);
        c2.gridx = 3;
        p2.add(dec_speed_bt,c2);
        
        //Name Label/TextField is added
        c2.insets = new Insets(5,5,5,5);
        c2.gridx = 0;
        c2.gridy = 1;
        c2.gridwidth = 1;
        c2.gridheight = 1;
        p2.add(new Label("Name"),c2);
        c2.gridx = 1;
        p2.add(field_name, c2);
        
        //Colour Label and DropDown is added
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx=0;
        c2.gridy=2;
        p2.add(new Label("Colour"),c2);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx=1;
        c2.gridy=2;
        p2.add(dropdown,c2);
        
        //Ascent Button is added
        c2.insets = new Insets(2,10,2,10);
        c2.fill = GridBagConstraints.BOTH;
        c2.gridx=0;
        c2.gridy=3;
        c2.weighty = 0.5;
        p2.add(ascent_bt,c2);
        
        //Descent Button is added
        c2.gridx=0;
        c2.gridy=4;
        c2.weighty = 0.5;
        p2.add(descent_bt,c2);
        
        //Layout for the Right Panel is created
        p3.setLayout(new GridLayout(10,0,5,5));
        
        //Text Header is added
        Panel ClientInfo = new Panel();
        ClientInfo.setLayout(new GridLayout(1,1));
        ClientInfo.add(new Label("Client Information", Label.CENTER));
        ClientInfo.setFont(new Font("Arial", Font.BOLD, 14));
        p3.add(ClientInfo);
        
        //Connection Status Label is added
        Panel ConnStatusPanel = new Panel();
        ConnStatusPanel.setLayout(new GridLayout(1,2));
        ConnStatusPanel.add(new Label("Conn. Status:"));
        ConnStatusPanel.add(ConnStatusLabel);
        p3.add(ConnStatusPanel);
        
        //ID Status Label is added
        Panel IdPanel = new Panel();
        IdPanel.setLayout(new GridLayout(1,2));
        IdPanel.add(new Label("ID:"));
        IdPanel.add(IdLabel);
        p3.add(IdPanel);
        
        //Name Label is added
        Panel NamePanel = new Panel();
        NamePanel.setLayout(new GridLayout(1,2));
        NamePanel.add(new Label("Name:"));
        NamePanel.add(NameLabel);
        p3.add(NamePanel);
        
        //Speed Label is added
        Panel SpeedPanel = new Panel();
        SpeedPanel.setLayout(new GridLayout(1,2));
        SpeedPanel.add(new Label("Speed:"));
        SpeedPanel.add(SpeedLabel);
        p3.add(SpeedPanel);
        
        //Color Label is added
        Panel ColorPanel = new Panel();
        ColorPanel.setLayout(new GridLayout(1,2));
        ColorPanel.add(new Label("Colour:"));
        ColorPanel.add(ColourLabel);
        p3.add(ColorPanel);

        //Last Message Time Panel is added
        Panel LastMessagePanel = new Panel();
        LastMessagePanel.setLayout(new GridLayout(1,2));
        LastMessagePanel.add(new Label("Last Msg Time:"));
        LastMessagePanel.add(LastMsgLabel);
        p3.add(LastMessagePanel);
        
        //Adds action listener for all components in gui 
    	inc_speed_bt.addActionListener(this);
    	dec_speed_bt.addActionListener(this);
    	field_speed.addActionListener(this);
    	field_name.addActionListener(this);
    	dropdown.addItemListener(this);
    	up_bt.addActionListener(this);
    	down_bt.addActionListener(this);
    	left_bt.addActionListener(this);
    	right_bt.addActionListener(this);
    	rr_bt.addActionListener(this);
    	rl_bt.addActionListener(this);
    	ascent_bt.addActionListener(this);
    	descent_bt.addActionListener(this);
    	
    	//Sets size of Frame and visible to true
    	this.setVisible(true);
		this.setSize(650, 250);
		this.setResizable(false);
		
		//Waits for ID
		waitForID();	
		
		//Sends initial Message
		send(new Message(RobotID, RobotName, RobotColor));
		
		//Starts Thread for Message Sender
		startThread();
	}
	
	//Method that waits for ID of client to be sent from server
	public void waitForID() {
		while(true) {
			Object o = receive();
			
			if (o != null) {
				return;
			}
			
			System.out.println("Waiting for ID...");
		}
	}
	
	//New thread is created that sends message every 10 seconds if it hasnt been sent yet
	public void startThread() {
    	new Thread(new Runnable(){
      	   public void run(){
      		   while(true) {
          	       if(new DateTimeService().calendar.getTimeInMillis() - lastMesssageTime > 10*1000) {
          	    	   Message temp = new Message(RobotID, RobotName, RobotColor);
          	    	   send(temp);
          	       }
      			   
      			   try {
 						Thread.sleep(100);
 					} catch (InterruptedException e) {
 						e.printStackTrace();
 					}
      		   }
      	   }
      	}).start();
    }
	
	public void windowActivated(WindowEvent arg0) {}
    public void windowClosed(WindowEvent arg0) {}
    public void windowClosing(WindowEvent arg0) {
    	//When Window is closed, the client sends message to server.
    	Message mes = new Message(RobotID, RobotName, RobotColor);
    	mes.Disconnect = true;
    	send(mes);
    	System.exit(0);
    }
    public void windowDeactivated(WindowEvent arg0) {}
    public void windowDeiconified(WindowEvent arg0) {}
    public void windowIconified(WindowEvent arg0) {}
    public void windowOpened(WindowEvent arg0) {}
    
    //Function increments the speed of robot by 1
    public void incrementSpeed() {
    	if(RobotSpeed==100)
    		return;
    	RobotSpeed++;
    }
    
    //Function decrements the speed of robot by 1
    public void decrementSpeed() {
    	if(RobotSpeed==0)
    		return;
    	RobotSpeed--;
    }
    
    //Listens for item change in the colour dropdown
    public void itemStateChanged(ItemEvent e) {
    	if(e.getSource().equals(dropdown)) {
    		//Assigns the Colour select to robot and sends it as a message
			System.out.println("Selected Colour " + dropdown.getSelectedItem());
			RobotColor = (Color) dropdown.hashmap.keySet().toArray()[dropdown.getSelectedIndex()];
			ColourLabel.setText(getColorString(RobotColor));
			Message mes = new Message(RobotID, RobotName, RobotColor);
			send(mes);
    	}
	}

    @Override
	public void actionPerformed(ActionEvent e) {
    	//Checks for Increase speed button press
		if(e.getSource().equals(inc_speed_bt)) {
			System.out.println("Increment Speed Button has been pressed");
			incrementSpeed();
			field_speed.setText(String.valueOf(RobotSpeed));
			SpeedLabel.setText(String.valueOf(RobotSpeed));
		}
		
		//Checks for Decrement speed button press
		if(e.getSource().equals(dec_speed_bt)) {
			System.out.println("Decrement Speed Button has been pressed");
			decrementSpeed();
			field_speed.setText(String.valueOf(RobotSpeed));
			SpeedLabel.setText(String.valueOf(RobotSpeed));
		}
		
		//Checks for Speed Text Field change 
		if(e.getSource().equals(field_speed)) {
			System.out.println("Speed Field has been modified");
			int temp = Integer.valueOf(field_speed.getText());
			if(temp >= 0 && temp <= 100) {
				RobotSpeed = temp;
				SpeedLabel.setText(String.valueOf(RobotSpeed));
			}
			else
				field_speed.setText(String.valueOf(RobotSpeed));
		}
		
		//Checks for Field Name Change
		if(e.getSource().equals(field_name)) {
			System.out.println("Robot Name has been modified");
			RobotName = field_name.getText();
			NameLabel.setText(RobotName);
			Message mes = new Message(RobotID, RobotName, RobotColor);
			send(mes);
		}
		
		//Checks for Up Button press
		if(e.getSource().equals(up_bt)) {
			Message mes = new Message(RobotID, RobotName, RobotColor);
			mes.Position = new double[]{0.0,-calcMagnitude(),0.0};
			send(mes);
		}
		
		//Checks for Down Button press
		if(e.getSource().equals(down_bt)) {
			Message mes = new Message(RobotID, RobotName, RobotColor);
			mes.Position = new double[]{0.0,calcMagnitude(),0.0};
			send(mes);
		}
		
		//Checks for Right Button press
		if(e.getSource().equals(right_bt)) {
			Message mes = new Message(RobotID, RobotName, RobotColor);
			mes.Position = new double[]{calcMagnitude(),0.0,0.0};
			send(mes);
		}
		
		//Checks for Left Button Press
		if(e.getSource().equals(left_bt)) {
			Message mes = new Message(RobotID, RobotName, RobotColor);
			mes.Position = new double[]{-calcMagnitude(),0.0,0.0};
			send(mes);
		}
		
		//Checks for rotate right button press
		if(e.getSource().equals(rr_bt)) {
			Message mes = new Message(RobotID, RobotName, RobotColor);
			mes.rotation = (int) calcMagnitude();
			send(mes);
		}
		
		//Checks for rotate left button press
		if(e.getSource().equals(rl_bt)) {
			Message mes = new Message(RobotID, RobotName, RobotColor);
			mes.rotation = (int) -calcMagnitude();
			send(mes);
		}
		
		//Check for ascent button press
		if(e.getSource().equals(ascent_bt)) {
			Message mes = new Message(RobotID, RobotName, RobotColor);
			mes.Position = new double[] {0.0,0.0,calcMagnitude()};
			send(mes);
		}
		
		//Check for Descent Button Press
		if(e.getSource().equals(descent_bt)) {
			Message mes = new Message(RobotID,RobotName, RobotColor);
			mes.Position = new double[] {0.0,0.0,-calcMagnitude()};
			send(mes);
		}
		
		return;
	}
    
    //Calculate the Magnitude
    private double calcMagnitude() {
    	return defaultStep * (RobotSpeed/100.0);
    }
    
    //Method for connecting to Server
    private boolean connectToServer(String serverIP) {
    	try { // open a new socket to the server 
    		this.socket = new Socket(serverIP,portNumber);
    		this.os = new ObjectOutputStream(this.socket.getOutputStream());
    		this.is = new ObjectInputStream(this.socket.getInputStream());
    		System.out.println("00. -> Connected to Server:" + this.socket.getInetAddress() 
    				+ " on port: " + this.socket.getPort());
    		System.out.println("    -> from local address: " + this.socket.getLocalAddress() 
    				+ " and port: " + this.socket.getLocalPort());
    	} 
        catch (Exception e) {
        	System.out.println("XX. Failed to Connect to the Server at port: " + portNumber);
        	System.out.println("    Exception: " + e.toString());	
        	return false;
        }
		return true;
    }
	
    // Method that sends a generic object to the server
    private void send(Object o) {
    	//Sets the time at which the object was sent at
    	lastMessage = (Message) o;
    	lastMesssageTime = lastMessage.Time;
    	LastMsgLabel.setText(new DateTimeService(lastMesssageTime).getTimeString());
    	
		try {
		    System.out.println("02. -> Sending an object...");
		    System.out.println(o);
		    os.writeObject(o);
		    os.flush();
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Sending:" +  e.toString());
		    ConnStatusLabel.setText("Closed");
		    return;
		}
		ConnStatusLabel.setText("Open");
    }

    // Method for receiving objects from the server
    private Object receive(){
		Object o = null;
		try {
			System.out.println("03. -- About to receive an object...");
		    o = is.readObject();
		    System.out.println("04. <- Object received...");
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Receiving:" + e.toString());
		    ConnStatusLabel.setText("Closed");
		    return null;
		}
		
		//Sets Robot parameters when object received
		Message m = (Message)o ;
	    RobotID = Integer.valueOf(m.ID);
		System.out.println("Receiving ID from Server " + String.valueOf(RobotID));
	    ConnStatusLabel.setText("Open");
	    IdLabel.setText(String.valueOf(RobotID));

		return o;
    }
    
    //Gets the String Name for the corresponding Color Object
    public String getColorString(Color c) {
		return new ColorDropDown().hashmap.get(c);
	}
    
    //Main Function that runs the client
    public static void main(String args[]){
    	System.out.println("**. Java Client Application - EE402 OOP Module, DCU");
    	if(args.length==2){
    		ClientMain theApp = new ClientMain(args[0],args[1]);
    		//ClientMain theApp2 = new ClientMain(args[0],args[1]); //Uncomment test simultanous
		}
    	else{
    		System.out.println("Error: you must provide the address of the server and name of Robot");
    		System.out.println("Usage is: java ClientMain hostname RobotName (e.g. java Client localhost lightning)");
    	}        	
    }
}
