import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

//RightColumn Class Responsible for displaying Server Settings/Status
public class rightColumn extends Panel implements ActionListener, ItemListener{
	//Initial Variables for Checkboxes/Safety Margin
	public static boolean safetyMarginBool = true;
	public static int safetyMarginVal = 20;
	public static boolean last3PosBool = true;
	public static boolean cartesianCoOrdsBool = false;
	public static boolean randomBool = false;
	
	//DroneIcon object used to hold selected Drone
	public static DroneIcon selectedDrone = null;
	
	//Checkboxes and Labels
	public static Label connectionLabel = new Label("0");
	public static Checkbox safetyCheckBox = new Checkbox("Safety Margin",safetyMarginBool);
	public static TextField safetyTextField = new TextField(String.valueOf(safetyMarginVal));
	public static Checkbox last3PosCheckbox = new Checkbox("Last 3 Positions",last3PosBool);
	public static Checkbox CartesianCoOrdsCheckbox = new Checkbox("Cartesian Co-Ords",cartesianCoOrdsBool);
	public static Checkbox randomCheckBox = new Checkbox("Spawn Randomly", randomBool);

	//Labels for Selected Drone
	public static Label nameLabel = new Label("None");
	public static Label coOrdLabel = new Label("None,None,None");
	public static Label colorLabel = new Label("None");
	public static Label dirLabel = new Label("None");
	public static Label statusLabel = new Label("None");
	
	//RightColumn Constructor
	public rightColumn() {
		//Sets the Column Color to Green
		this.setBackground(Color.GREEN);
		this.setLayout(new GridLayout(0,1,5,5));
		
		//Panel containg Server Settings text
		Panel HeroPanel = new Panel();
		HeroPanel.setLayout(new GridLayout(1,1));
        Label l1 = new Label("Server Settings", Label.CENTER);
        l1.setFont(new Font("Arial", Font.BOLD, 14));
        HeroPanel.add(l1);
        this.add(HeroPanel);
		
        //Panel contain Connections Counter
        Panel ConnectPanel = new Panel();
        ConnectPanel.setLayout(new GridLayout(1,2));
        ConnectPanel.add(new Label("Connections:"));
        ConnectPanel.add(connectionLabel);
        this.add(ConnectPanel);
        
        //Panel containing Safety Margin Checkbox/textfield
        Panel safetypanel = new Panel();
        safetypanel.setLayout(new GridLayout(1,2));
        safetypanel.add(safetyCheckBox);
        safetypanel.add(safetyTextField);
        this.add(safetypanel);
        
        //Panel Containing checkbox enabling/disabling drone history
        Panel last3Panel = new Panel();
        last3Panel.setLayout(new GridLayout(1,2));
        last3Panel.add(last3PosCheckbox);
        this.add(last3Panel);
        
        //Panel containg checkbox enabling/disabling random drone spawn
        Panel RandomPanel = new Panel();
        RandomPanel.setLayout(new GridLayout(1,2));
        RandomPanel.add(randomCheckBox);
        this.add(RandomPanel);
        
        //Panel containg checkbox enabling/disabling x,y,z coordinate convertsion
        Panel CartesianPanel = new Panel();
        CartesianPanel.setLayout(new GridLayout(1,2));
        CartesianPanel.add(CartesianCoOrdsCheckbox);
        this.add(CartesianPanel);
        
        //Panel Containg Slected Drone text
        Panel SelectedPanel = new Panel();
        SelectedPanel.setLayout(new GridLayout(1,1));
        Label l2 = new Label("Selected Drone", Label.CENTER);
        l2.setFont(new Font("Arial", Font.BOLD, 14));
        SelectedPanel.add(l2);
        this.add(SelectedPanel);
        
        //Panel Containing Name of selected drone
        Panel NamePanel = new Panel();
        NamePanel.setLayout(new GridLayout(1,2));
        NamePanel.add(new Label("Name:"));
        NamePanel.add(nameLabel);
        this.add(NamePanel);
        
        //Panel containing coordinates of selected drone
        Panel CoOrdPanel = new Panel();
        CoOrdPanel.setLayout(new GridLayout(1,2));
        CoOrdPanel.add(new Label("Co-Ord:"));
        CoOrdPanel.add(coOrdLabel);
        this.add(CoOrdPanel);
        
        //Panel containg color of the selected drone
        Panel ColorPanel = new Panel();
        ColorPanel.setLayout(new GridLayout(1,2));
        ColorPanel.add(new Label("Color:"));
        ColorPanel.add(colorLabel);
        this.add(ColorPanel);
        
        //Panel containing direction of the selected drone
        Panel DirectionPanel = new Panel();
        DirectionPanel.setLayout(new GridLayout(1,2));
        DirectionPanel.add(new Label("Dir:"));
        DirectionPanel.add(dirLabel);
        this.add(DirectionPanel);
        
        //Panel containing status of selected drone
        Panel StatusPanel = new Panel();
        StatusPanel.setLayout(new GridLayout(1,2));
        StatusPanel.add(new Label("Status:"));
        StatusPanel.add(statusLabel);
        this.add(StatusPanel);
        
        //Dummy Labels to fill Frame
        this.add(new Label());
        this.add(new Label());
        this.add(new Label());
        this.add(new Label());
        
        //Actionlistener for all textfields and checkboxes
        safetyTextField.addActionListener(this);
        safetyCheckBox.addItemListener(this);
        last3PosCheckbox.addItemListener(this);
        CartesianCoOrdsCheckbox.addItemListener(this);
        randomCheckBox.addItemListener(this);
        
        //Thread is started to update all parameters on screen
        this.startThread();
	}


	
	//Checks for change in safety text field value
	//and assigns it to static variable
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(safetyTextField)) {
			int temp = Integer.valueOf(safetyTextField.getText());
			if(temp > -1 && temp < 101) {
				safetyTextField.setText(String.valueOf(temp));
				safetyMarginVal = temp;
				DroneCanvas.SafetyMargin = safetyMarginVal;
			}
			else {
				safetyTextField.setText(String.valueOf(safetyMarginVal));
			}
		}
	}
	
	//Checks for checkbox change and assigns
	//appropriate static variable based on checkbox status
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource().equals(safetyCheckBox)) {
			safetyMarginBool = safetyCheckBox.getState();
			DroneCanvas.SafetyMarginEnabled = safetyMarginBool;
			System.out.println("Safety Box Triggered");
		}
		if(e.getSource().equals(last3PosCheckbox)) {
			last3PosBool = last3PosCheckbox.getState();
			DroneCanvas.enableDroneHistory = last3PosBool;
			System.out.println("Last 3 Pos Triggered");
		}
		if(e.getSource().equals(CartesianCoOrdsCheckbox)) {
			cartesianCoOrdsBool = CartesianCoOrdsCheckbox.getState();
			System.out.println("Java CoOrds Triggered");
		}
		
		if(e.getSource().equals(randomCheckBox)) {
			randomBool = randomCheckBox.getState();
			System.out.println("Random Checkbox Triggered");
		}
		
	}
	
	//Thread Responsible for periodically updating drone parameters
	//on the screen every 100ms
	public void startThread() {
    	new Thread(new Runnable(){
     	   public void run(){
     		   while(true) {
     			   if(selectedDrone != null && DroneCanvas.dronelist.contains(selectedDrone)) {
     				   nameLabel.setText(selectedDrone.name);
     				   coOrdLabel.setText(selectedDrone.getPositionString());
     				   colorLabel.setText(selectedDrone.getColorString());
     				   dirLabel.setText(selectedDrone.getDirection());
     				   statusLabel.setText(selectedDrone.getStatus());
     			   }
     			   else {
     				  nameLabel.setText("None");
    				  coOrdLabel.setText("None");
    				  colorLabel.setText("None");
    				  dirLabel.setText("None");
    				  statusLabel.setText("None");
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
}
