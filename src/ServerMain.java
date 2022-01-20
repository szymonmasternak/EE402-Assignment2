import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Main Server Class that server is run from
public class ServerMain extends Frame implements ActionListener, WindowListener {
    //Port Number at which the Server will listen.
	private static int portNumber = 5050;
	
	//Server Main Constructor
	public ServerMain() {
		super("Server Main");
		this.addWindowListener(this);
		
		//Sets the Layout of the Server GUI to GridBagLayout
		this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.BOTH;
        
        //Configures the Canvas element in GUI and places
        //it in the appropriate place
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 3;
        c.weightx = 0.75;
        c.weighty = 0.75;
        DroneCanvas dc = new DroneCanvas(600,450);
        Panel CanvasPanel = new Panel();
        CanvasPanel.setBackground(Color.LIGHT_GRAY);
        CanvasPanel.add(dc);
        this.add(CanvasPanel,c);
        
        // Configure the position of the Right Column element containing
        // All the settings/parameters of the server
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.weightx = 0.25;
        c.weighty = 1.0;
        Panel rightcolumn = new rightColumn();
        this.add(rightcolumn, c);
        
        // Configure the position of the footer
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0.25;
        Panel bottomfooter = new bottomFooter();
        this.add(bottomfooter, c);
        
        //Sets the GUI to be visible and size
		this.setVisible(true);
		this.setSize(800, 600);
		this.setResizable(false);
        this.pack();
	}
	
    public void windowActivated(WindowEvent arg0) {}
    public void windowClosed(WindowEvent arg0) {}
    public void windowClosing(WindowEvent arg0) {
    	System.exit(0);
    }
    public void windowDeactivated(WindowEvent arg0) {}
    public void windowDeiconified(WindowEvent arg0) {}
    public void windowIconified(WindowEvent arg0) {}
    public void windowOpened(WindowEvent arg0) {}
    
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		return;
	}
    
    //Main Function where the server is Run from
	public static void main(String args[]) {
		ServerMain server = new ServerMain();
		
		boolean listening = true;
        ServerSocket serverSocket = null;
        
        // Set up the Server Socket
        try 
        {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("New Server has started listening on port: " + portNumber );
        } 
        catch (IOException e) 
        {
            System.out.println("Cannot listen on port: " + portNumber + ", Exception: " + e);
            System.exit(1);
        }
        
        // Server is now listening for connections or would not get to this point
        while (listening) // almost infinite loop - loop once for each client request
        {
            Socket clientSocket = null;
            try{
            	System.out.println("**. Listening for a connection...");
                clientSocket = serverSocket.accept();
                System.out.println("00. <- Accepted socket connection from a client: ");
                System.out.println("    <- with address: " + clientSocket.getInetAddress().toString());
                System.out.println("    <- and port number: " + clientSocket.getPort());
            } 
            catch (IOException e){
                System.out.println("XX. Accept failed: " + portNumber + e);
                listening = false;   // end the loop - stop listening for further client requests
            }
            
            ThreadedConnectionHandler con = new ThreadedConnectionHandler(clientSocket);
            con.start();
            
            System.out.println("02. -- Finished communicating with client:" + clientSocket.getInetAddress().toString());
        }
        // Server is no longer listening for client connections - time to shut down.
        try 
        {
            System.out.println("04. -- Closing down the server socket gracefully.");
            serverSocket.close();
        } 
        catch (IOException e) 
        {
            System.err.println("XX. Could not close server socket. " + e.getMessage());
        }
    }
	
}
