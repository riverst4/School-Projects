/*This class will represent the view of the Deadwood board
 *The active Player will be able to interact with the board
 *
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import com.sun.jdi.Field;
import java.awt.event.*;


//Implements Listeners so the user can interact with the GUI
public class DeadwoodView implements MouseListener,ActionListener{
	
	//Interacts with the listener, the listerner is an observer
	public List<BoardViewObserver> observers = new ArrayList();
	
	//Non-GUI data
	Actor activePlayer;
	
	//These booleans are important for the action buttons
	boolean moveAction = false;
	boolean takeAction = false;
	boolean validAction = false;
	boolean rankUpBool = false;
	
	JFrame frame = new JFrame();
	
	//Labels and the layered pane, the layered pane will have all three of these labels
	//We have 3 layers Default-Bottom, Palette-Middle, Drag-Top
	ImageIcon dice;
	JLabel diceButton = new JLabel();
	JLabel[] sceneLabels;
	JLabel[] playerLabels;
	JLabel[] shotLabels = new JLabel[22];
	JLayeredPane layeredPanel = new JLayeredPane();
	
	//Final board dimensions and path to images
	final int boardX = 180;
	final int boardY = 25;
	String path = "src/main/resources/img/";
	
	
	//Represents the button panel and irs possible buttons
	//The button panel will have more buttons depending on the user action
	JPanel buttonPanel = new JPanel();
	JButton act;
	JButton move;
	JButton rehearse;
	JButton endTurn;
	JButton rankUp;
	JButton takeRole;
	JButton cancel;
	JButton[] neighborRooms;

	
	//This will represent the active player and their stats
	JTextArea textArea= new JTextArea();
	JTextArea scoreBoard = new JTextArea();
	
	//This will guide the user 
	JLabel menu = new JLabel("Actions you can take");
	
	//This will register observers to the BoardView
	public void register(BoardViewObserver ob) {

		observers.add(ob);
	}
	//This will communicate to the observers what buttons the user pressed
	public void notify(String input) {
		for(BoardViewObserver ob: observers) {
			
			ob.gotAction(input);
		}
	}
	
	//This will run the GUI, this method will not stop running until the game ends or the user exits the game
	public void run() {

		//Represents the frame
		frame.setTitle("Deadwood");
		frame.setVisible(true);
		frame.setSize(1600,975);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.black);
		frame.setForeground(Color.black);
        
		//Represents the board
		ImageIcon board = new ImageIcon(path+"board.png");
        JLabel boardImage = new JLabel(board);
        boardImage.setLocation(boardX, boardY);
        boardImage.setSize(1200, 900);
       
        //Represents the text area in the top right
        textArea.setBounds(1210+boardX, 25, 275, 100);
        textArea.setBackground(Color.gray);
        textArea.setEditable(false);

        //Represents the board in its entirety
        //There are 3 layers to this board
        layeredPanel.setBounds(0, 0, 1200, 900);
        layeredPanel.setLayout(null);
        layeredPanel.add(boardImage, JLayeredPane.DEFAULT_LAYER); 
        layeredPanel.add(textArea, JLayeredPane.DRAG_LAYER);
        layeredPanel.add(scoreBoard, JLayeredPane.DRAG_LAYER);
        frame.add(layeredPanel);
        
        //Represents a text telling the user what they can do
    	menu.setBounds(1200+boardX,210,400,100);
    	layeredPanel.add(menu, JLayeredPane.DRAG_LAYER);
    	
    	//Puts the button panel onto the layered panel
    	buttonPanel.setBounds(1250+boardX, 265, 150, 500);
    	buttonPanel.setVisible(true);
    	layeredPanel.add(buttonPanel, JLayeredPane.DRAG_LAYER);
    	
    	/*Represents all of the buttons*/
    	act = new JButton("Act");
    	act.addMouseListener(this);
    	
    	rehearse = new JButton("Rehearse");
    	rehearse.addMouseListener(this);
    	
    	move = new JButton("Move");
    	move.addMouseListener(this);
    	
    	takeRole = new JButton("Take Role");
    	takeRole.addMouseListener(this);
    	
    	rankUp = new JButton("Rank Up");
    	rankUp.addMouseListener(this);
    	
    	endTurn = new JButton("End Turn");
    	endTurn.addMouseListener(this);
    	
    	cancel = new JButton("Cancel");
    	cancel.addActionListener(this);
    	
    	//Sets the buttons up for the start of the game
    	setButtons();
    	buttonPanel.repaint();
    	
    	
    	
    	/*End of all of the buttons*/    	
	}
	
	//This will update the current gui based on the new information that the controller passes through
	public void updateView(Actor[] players, Actor activePlayer, Scene[] scenes,Room[] rooms) {
		
		//These are the labels that would need updated
		this.activePlayer = activePlayer;
		setSceneLabels(scenes);
		setPlayerLabels(players);
		setShotLabels(rooms);
		setText(activePlayer);
		displayScoreboard(players);
		
		
	}
	
	
    	
	//This will update all of the scene images
	//When a scene is finished a cardback will display as the image
	public void setSceneLabels(Scene[] scenes) {
		sceneLabels = new JLabel[scenes.length];
		for(int i = 0; i < scenes.length; i++) { 
			Scene scene = scenes[i];
			ImageIcon image = new ImageIcon(scene.getImage());
			JLabel label = new JLabel();
			label.setIcon(image);
			label.setBounds(scene.getxCord()+ boardX, scene.getyCord()+boardY, scene.getHeight(), scene.getWidth());
			
			sceneLabels[i] = label;
			
			//These belong on the middle layer, since actors can be on a scene card
			layeredPanel.add(sceneLabels[i], JLayeredPane.PALETTE_LAYER);
			
		}
		
	}
		
		
	
	
	//This will update all of the players, their location and their image
	//This is used in updateView
	public void setPlayerLabels(Actor[] players) {
		playerLabels = new JLabel[players.length];
		for(int i = 0; i < players.length;i++) {	
			Actor player = players[i];
			ImageIcon image = new ImageIcon(player.getImage());
			Image newImage = image.getImage();
			newImage = newImage.getScaledInstance(player.getHeight(),player.getWidth(), java.awt.Image.SCALE_SMOOTH);
			image = new ImageIcon(newImage);
			JLabel label = new JLabel();
			label.setIcon(image);
			label.setBounds(player.getxCord()+boardX, player.getyCord()+boardY, player.getHeight(), player.getWidth());
				
			//Puts on drag layer, the very top as they can be anywhere on the board
			playerLabels[i] = label;
			layeredPanel.add(playerLabels[i], JLayeredPane.DRAG_LAYER);
				
			}
			
		}
	
	//This will set the shot labels when a shot has succeeded the shot will display
	//An image to let the user know how many shots remain
	public void setShotLabels(Room[] rooms) {
		
		int total = getTotalShots(rooms);
		
		shotLabels = new JLabel[total];
		int count = 0;
		for(int i = 0; i < rooms.length;i++) {
			ArrayList<ShotCounter> shots = rooms[i].getCounterArray();
			
			//Goes through each room and looks at the image of the shot
			for(int j = 0; j < shots.size();j++) {
				
				ShotCounter shot = shots.get(j);
				ImageIcon image = new ImageIcon(shot.getImage());
				JLabel label = new JLabel();
				label.setIcon(image);
				label.setBounds(shot.getxCord()+boardX, shot.getyCord()+boardY, shot.getHeight(), shot.getWidth());
				
				shotLabels[count] = label;
				//Middle layer, theoretically a player can be over a shot counter.
				layeredPanel.add(shotLabels[count], JLayeredPane.PALETTE_LAYER);
				count++;
			}
			
		}
	}
	
	//This is a helper method to get all of the shots in the rooms, this number will be final, 
	//But if something were to change in the future, this wouldn't be final
	//Takes in the total rooms and returns an int representing the number of shot counters
	public int getTotalShots(Room[] rooms) {
		int count = 0;
		for(int i = 0; i < rooms.length;i++) {
			for(int j =0; j <  rooms[i].getCounterArray().size();j++) {
				count++;
			}
		}
		return count;
	}
	//this method will get a dice roll and display the roll on the scene
	//conveniently using the active player's color for the dice. 
	//it returns the dice value
	public int getDiceRoll() {
		String[] set = activePlayer.getDiceSet();
		Random r = new Random();
		int randomInt = r.nextInt(6);
		dice = new ImageIcon(set[randomInt]);
		
		diceButton = new JLabel(dice);
		diceButton.setBounds(1275+boardX, 125, 100,100);
		diceButton.setVisible(true);
		diceButton.addMouseListener(this);
		layeredPanel.add(diceButton,JLayeredPane.DRAG_LAYER);
		
		return randomInt+1;
		
		
	}
	//removes the dice from the view
	public void removeDice() {
		diceButton.setIcon(null);
	}
	
	//This will represent action buttons that a user can take
	//These will represent turns, and some options will lead to another set of buttons
	public void setButtons() {
		menu.setText("                          Select an option");
		
		//We must remove all of the buttons because there may be some leftover buttons
		//From other button sets
		buttonPanel.removeAll();
		
		//if a player is in a role they can act or rehearse their current scene
		//if they're in a role and haven't made an action they can act or rehearse
		if(activePlayer.getInRole() && !validAction) {
			
			buttonPanel.add(act);
			
			//makes sure the user can't rehearse if it would be a waste. 
			if(activePlayer.getChips()+1 < activePlayer.getScene().getBudget()) {	
				buttonPanel.add(rehearse);
			}
			
			
			//checks to see if the player can move, a player can't move if they have already moved 
			//or if they are in a role
		} if(!moveAction && !(activePlayer.getInRole())) {
			
			buttonPanel.add(move);
		
			//if a player is not in a role and their room has a scene, they are allowed to take a role	
		} if((!activePlayer.getInRole()) && (activePlayer.getCurrentRoom().getHasScene()) && (!validAction)) {
			
			buttonPanel.add(takeRole);
			
		} if(activePlayer.getCurrentRoom().getName().equals("office")) {
			buttonPanel.add(rankUp);
		}
		//there should always be an endTurn, paint will update the buttons
		buttonPanel.add(endTurn);
		buttonPanel.repaint();
	}
	
	//updates the moveAction boolean 
	public void setMove(boolean bool) {
		moveAction = bool;
	}
	
	//updates the takeAction boolean
	public void setTake(boolean bool) {
		takeAction = bool;
	}
	//updates the validAction boolean
	public void setValidAction(boolean bool) {
		validAction = bool;
	}
	//if the player selects move, this will display the neighbor rooms
	//in the form of buttons that the player can move to
	public void setButtonsMove() {
		
		//removes all of the action buttons
		buttonPanel.removeAll();
		menu.setText("               Click a room you can move to");
		ArrayList<String> rooms = activePlayer.currentRoom.getNeighborRooms();
		//creates a button for all of the neighbor rooms
		for(int i = 0; i < rooms.size();i++) {
			String name = rooms.get(i);
			JButton button = new JButton(name);
			button.addActionListener(this);
			buttonPanel.add(button);
		}
		//add a cancel button then update the buttonPanel
		buttonPanel.add(cancel);
		buttonPanel.repaint();
		
		//this is important for the listener
		moveAction = true;
		
		
	}
	//this will display the possible buttons for taking a role
	//the buttons will be each role that the player can take
	public void setButtonsTake() {
		
		buttonPanel.removeAll();
		menu.setText("                         Select a role to take");
		ArrayList<Role> roles = activePlayer.currentRoom.getRoles();
		for(int i = 0; i < roles.size();i++) {
			/*will check to make sure the user can take that role, this is for the convenience of the 
			user.*/ 
			if((!roles.get(i).getRoleTaken()) && roles.get(i).getMinRank() <= activePlayer.getRank()) {
				String name = roles.get(i).getRoleName();
				JButton button = new JButton(name);
				button.addActionListener(this);
				buttonPanel.add(button);
			}
			
		}
		
		//player can always cancel, repaint to update
		buttonPanel.add(cancel);
		buttonPanel.repaint();
		
		
		//important for the listener
		takeAction = true;		
	}
	
	//this will set up buttons for ranking up
	public void setButtonsRank() {
		CastingOffice office = (CastingOffice) activePlayer.getCurrentRoom();
		buttonPanel.removeAll();
		menu.setText("                        Select a rank option");
		int minRank = activePlayer.getRank();
		for(int i = minRank+1; i < 7; i++) {
			String name = ""+ (i);
			JButton button = new JButton(name);
			button.addActionListener(this);
			buttonPanel.add(button);
		}
		
		buttonPanel.add(cancel);
		buttonPanel.repaint();
		
	}
	//this will give the rank up buttons when a player uses an action rank up
	//it will display dollars and credits to rank up to the desired rank
	public void setRankUpOptionsButtons(int desiredRank) {
		
		CastingOffice office = (CastingOffice) activePlayer.getCurrentRoom();
		buttonPanel.removeAll();
		menu.setText("            Select the currency to buy rank: "+desiredRank);
		int dollars = office.getDollars(desiredRank);
		int credits = office.getCredits(desiredRank);
		int[] options = new int[2];
		options[0] = dollars;
		options[1] = credits;
		String name;
		for(int i = 0; i < options.length; i++) {
			
			if(i == 0) {
				name = "dollars: ";
			} else {
				name = "credits: ";
			}
			name +=  options[i];
			JButton button = new JButton(name);
			button.addActionListener(this);
			buttonPanel.add(button);
		}
		
		buttonPanel.add(cancel);
		buttonPanel.repaint();
	}
	
	
	
	//this will be the top right text that displays info to the user
	//it takes in an actor and then displays info about the player
	public void setText(Actor actor) {
		
		textArea.setText("\tPlayer: "+ activePlayer.getName()
		+ "\n\t--------------------"
		+ "\n\tRank: " + activePlayer.getRank()
		+ "\n\tChips: " + activePlayer.getChips()
		+ "\n\tDollars: " + activePlayer.getDollars()
		+ "\n\tCredits: " + activePlayer.getCredits());
	}
	
	
	//this will display a convenient scoreboard of the players
	//it will update after every turn and will have the winning player up top
	public void displayScoreboard(Actor[] actors) {
		scoreBoard.setTabSize(6);
		scoreBoard.setBounds(1210+boardX, 645, 275, 35*actors.length+1);
		scoreBoard.setBackground(Color.gray);
		scoreBoard.setText("     \tColor\tScore:\n\t-------------------------");
		Actor[] originalActors = actors.clone();
		sortByScore(originalActors);
		for(Actor actor: originalActors) {
			scoreBoard.append("\t\n\t" + actor.getName() + "\t|    " + actor.getScore());
		}
		
	}
	//this will sort an actor array based on their score
	//this will help for the scoreboard since we want the 
	//scoreboard to have the actual rankings. It takes in an actor
	//array and sorts it
	public void sortByScore(Actor[] actors) {
		int n = actors.length;
				
				for(int i = 0; i < n-1; i++) {
					for(int j = 0; j < n-i-1;j++) {
						if(actors[j].getScore() < actors[j+1].getScore()) {
							Actor temp = actors[j];
							actors[j] = actors[j+1];
							actors[j+1] = temp;
						}
					}
				}
	}
	
	//this will display the ending scoreboard with the winner
	//and some useful statistics at the end
	//the layeredPanel stuff will remove so the user 
	//cannot press any buttons and change the score
	public void finishGame(Actor actor) {
		buttonPanel.removeAll();
		layeredPanel.remove(scoreBoard);
		layeredPanel.remove(diceButton);
		layeredPanel.remove(textArea);
		layeredPanel.remove(menu);
		
		buttonPanel.repaint();
		
		scoreBoard.setBounds(650, 200, 275, 600);
		layeredPanel.add(scoreBoard);
		scoreBoard.append("\n\n\n\n\tWinner: "+ actor.getName()
		+ "\n\t--------------------"
		+ "\n\tRank: " + actor.getRank()
		+ "\n\tChips: " + actor.getChips()
		+ "\n\tDollars: " + actor.getDollars()
		+ "\n\tCredits: " + actor.getCredits()
		+ "\n\tScore: " + actor.getScore());
		
		layeredPanel.repaint();
		
		
	}
	
	//clears all of the labels so the info can be updated, if 
	public void clearLabels() {
		for(int i = 0; i < playerLabels.length;i++) {
			playerLabels[i].setIcon(null);
		}
		for(int i = 0; i < sceneLabels.length;i++) {
			sceneLabels[i].setIcon(null);
		}
		for(int i = 0; i < shotLabels.length;i++) {
			shotLabels[i].setIcon(null);
		}	
		
		
	}
	
	//Override method for the mouse listener, when the mouse is clicked, 
	//the actions are sent to the observers of this view
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//if the action was act
		if(e.getSource() == act) {
			//clear all of the labels
			clearLabels();
			int roll = getDiceRoll();
			//set some booleans
			validAction = true;
			moveAction = true;
			notify("act"+roll);
			
			
			
		}
		
		//if the action is rehearse
		else if(e.getSource() == rehearse) {
			clearLabels();
			validAction = true;
			notify("rehearse");
			
			
		}
		
		//if the player moves, the buttons change to display rooms the 
		//player can move to
		else if(e.getSource() == move) {
			
			setButtonsMove();
			
		}
		//if the player takes a role, the buttons change to display roles the player
		//can take
		else if(e.getSource() == takeRole) {
			
			setButtonsTake();
		}
		//if the player ranks up
		else if (e.getSource() == rankUp){
			
			setButtonsRank();
			 
        }
		
		//if the player ends their turn
        else if (e.getSource()== endTurn){
        	clearLabels();
            notify("end");
        }

	/*The following mouse options are not used*/	
		
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
		
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/*end of the unused method*/

	//the action performed method for take role buttons
	//and move buttons
	@Override
	public void actionPerformed(ActionEvent e) {
		//if the user cancels, the previous buttons are displayed
		if(e.getSource() == cancel) {
			//this means the move action was pressed
			//we need to set it back
			if(moveAction && !takeAction && !activePlayer.getCurrentRoom().getName().equals("office")) {
				moveAction = false;
				//this means the take action was was pressed
				//set it back	
			} else if(moveAction && takeAction) {
				takeAction = false;
			} else if(!moveAction && takeAction) {
				takeAction = false;
			}
			clearLabels();
			notify("cancel");
			
		} else if(e.getActionCommand().length() ==1) {
			
			
			setRankUpOptionsButtons(Integer.parseInt(e.getActionCommand()));
		} else if(e.getActionCommand().contains("dollars")) {
			
			String name = e.getActionCommand().substring(9);
			int currency = Integer.parseInt(name);
			
			
			clearLabels();
			notify("rankd"+name);
			
		} else if(e.getActionCommand().contains("credits")) {
			
			String name = e.getActionCommand().substring(9);
			int currency = Integer.parseInt(name);
			
			
			clearLabels();
			notify("rankc"+name);

			//cancel wasn't pressed, we now check to see which of the buttons the user pressed
			//these will be move buttons or role buttons only
		} else {
			
			//we need to save the button name
			String buttonName = e.getActionCommand();		
			String action = null;
			
			//if its a role, we notify the observers
			if(takeAction) {
				validAction = true;
				action = "take" + buttonName;
			
				//if its a room, we notify the observers
			} else {
				moveAction = true;
				action = "move" + buttonName;
			}
			clearLabels();
			notify(action);
		}
	}
}//end of class

