import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.Font;

//Drone Canvas Class
public class DroneCanvas extends Canvas implements MouseListener{
	
	//Width and Height Parameters of the Canvas
	public int width, height;
	
	//Static List that contains the list of all drones
    public static List<DroneIcon> dronelist = new ArrayList<DroneIcon>();
	
    //Variables for the DroneCanvas
    public static int SafetyMargin = 20;
    public static boolean SafetyMarginEnabled = true;
    public static boolean enableDroneHistory = true;
    
    //Constructor for Drone Canvas
    public DroneCanvas(int width, int height){
            this.setSize(width,height);
            this.width = width;
            this.height = height;
            this.update();
            this.addMouseListener(this);
            this.startThread();
    }
    
    //Thread that updates the canvas every 100ms
    public void startThread() {
    	new Thread(new Runnable(){
     	   public void run(){
     		   while(true) {
         	       update();
         	       try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
     		   }
     	   }
     	}).start();
    }
    
    //Update method repaints the canvas
    public void update() {
    	this.repaint();
    }
    
    //Draws x,y axis
    public void drawAxis(Graphics g) {
    	g.drawLine(Integer.valueOf(width/2), 0, Integer.valueOf(width/2), height);
    	g.drawLine(0, Integer.valueOf(height/2), width, Integer.valueOf(height/2));
    }
    
    //Converts Cartesian Coordinates to Java Coordinates
    public Point getJavaPoint(Point p) {
    	return new Point(width/2 + p.x, height/2 - p.y);
    }
    
    //Converts Cartesian Coordinates to Java Coordinates
    public Point getJavaPoint(int x, int y) {
    	return new Point(width/2 + x, height/2 - y);
    }
    
    //Converts Java Coordinates to Cartesian Coordinates
    public Point getCartPoint(Point p) {
    	return new Point(p.x - width/2, height/2 - p.y);
    }
    
    //Converts Java Coordinates to Cartesian Coordinates
    public Point getCartPoint(int x, int y) {
    	return new Point(x - width/2, height/2 - y);
    }
    
    //Method that paints all objects on the canvas
    public void paint(Graphics g){
    	drawAxis(g);
    	g.setColor(Color.BLUE);
    	
    	for(DroneIcon d1: dronelist) {
    		d1.drawHistoryBool = enableDroneHistory;
    		d1.draw(g);
    	}
    	
    	drawCollisionCircle(g);
    }
    
    //Draws Collision Circle around Each Drone
    public void drawCollisionCircle(Graphics g) {
    	//Set is created to house all drones in danger
    	Set<DroneIcon> droneInDanger = new HashSet<DroneIcon>();
    	
    	if(SafetyMarginEnabled == false)
    		return;
    	
    	//All Drones are checks for collision with one another
    	for(int i=0; i<dronelist.size(); i++) {
    		for(int j=i+1; j<dronelist.size(); j++) {
    			if(dronelist.get(i).getDistance(dronelist.get(j)) < (dronelist.get(i).radius + dronelist.get(j).radius + this.SafetyMargin)) {
    				droneInDanger.add(dronelist.get(i));
    				droneInDanger.add(dronelist.get(j));
    				dronelist.get(i).CollisionStatus = true;
    				dronelist.get(j).CollisionStatus = true;
    			}
    			else {
    				dronelist.get(i).CollisionStatus = false;
    				dronelist.get(j).CollisionStatus = false;
    			}
    		}
    	}
    	
    	for(DroneIcon d: droneInDanger) {
    		d.drawCircle(g, d.radius*2);
    	}
    		
    }
    
    //Returns Drone Associated with a point in the canvas
    public DroneIcon getDroneAssociatedWithPnt(Point p) {
    	for(DroneIcon di: dronelist) {
    		if(di.contains(p)) {
    			return di;
    		}
    	}
    	return null;
    }
    
    //Checks for Mouse Click Callback
	@Override
	public void mouseClicked(MouseEvent e) {
		Point p1 = new Point(e.getX(), e.getY());
		
		
		//Code Below Checks for Selected Drone and if so passes the drone object
		//to the rightcolumn class where the parameters of it can be displayed
		if(rightColumn.selectedDrone != null)
			rightColumn.selectedDrone.drawSelectionBool = false;
		
		DroneIcon temp = getDroneAssociatedWithPnt(p1);
		if( temp != null) {
			rightColumn.selectedDrone = temp;
			rightColumn.selectedDrone.drawSelectionBool = true;
		}
		else {
			rightColumn.selectedDrone = null;
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
