import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

//Class for Bottom footer of server
public class bottomFooter extends Panel{
	
	//Bottom Footer Constructor for the Server
	public bottomFooter() {
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(new GridLayout(0,1,5,0));
        this.add(new Label("(c) Szymon Masternak XXXXXXXX EE402 2021/2022"));
	}
}
