import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;

//this class will initialize the controller to set up the GUI
//this class will be edited to take in an extra argument to 
//differentiate between the GUI and the console version
public class Deadwood {
	
	
	public static void main(String[] args) throws IOException {
		String[] addArg = new String[5];
		addArg[4] = "0";
		if(args.length == 4) {
			for(int i = 0; i < args.length; i++) {
				addArg[i] = args[i];
			}
			if(Integer.parseInt(args[0]) == 1) {
				
				//sets up the controller with all of the arguments
				DeadwoodController controller = new DeadwoodController(addArg);
			} else if(Integer.parseInt(args[0]) == 0) {
				//runs the console version
				DeadwoodConsole console = new DeadwoodConsole(addArg);
			}
			
			
		} else {
		
			//if the input is a 1, do the gui version
			if(Integer.parseInt(args[0]) == 1) {
			
				//sets up the controller with all of the arguments
				DeadwoodController controller = new DeadwoodController(args);
			} else if(Integer.parseInt(args[0]) == 0) {
				//runs the console version
				DeadwoodConsole console = new DeadwoodConsole(args);
			}
		}
	
		
	}
	
	
	
	
}
