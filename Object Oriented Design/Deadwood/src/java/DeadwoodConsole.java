/*represents the console version for deadwood
 * if input is not entered, it will prompted the user for some\\
 * 
 * UPDATE: user must now put it input for bots
 * 
 */

import java.io.*;
import java.util.*;
public class DeadwoodConsole {
	public DeadwoodConsole(String[] args) throws IOException{
		
		Actor[] players; //gets an array of actors
		Scanner input = new Scanner(new InputStreamReader(System.in));
		int numberOfPlayers = 0;
		int numberOfComputers = 0;
		String board;
		String cards;
		CastingOffice office = new CastingOffice();
		
		System.out.println("Hello welcome to this pass and play version of Deadwood");
		if(args.length >= 2) {
			
			board = args[1];
			cards = args[2];
			numberOfPlayers = Integer.parseInt(args[3]);
			numberOfComputers = Integer.parseInt(args[4]);
			
		} else {
			board = "board.xml";
			cards = "cards.xml";
			
			
			boolean getLoop = true;
			
			while(getLoop) {
				System.out.println();
				System.out.println("Enter the amount of players that are playing ");
				System.out.println("Enter a number between 2-8: ");
				String number = input.next();
				try {
					numberOfPlayers = Integer.parseInt(number);
					
					if(numberOfPlayers > 1 && numberOfPlayers < 9) {
						getLoop = false;
					} else {
						System.out.println("Not a valid number.");
					}
					
					
				} catch(NumberFormatException e) {
					System.out.println("Not a valid number, try again, must be between ");
				} 
				
				
				
				
			}
			
			
		}

		//get players into the player array
		players = new Actor[numberOfPlayers];
		for(int i = 0; i < numberOfPlayers-numberOfComputers;i++) {
			Actor actor = new Actor();
			players[i] = actor;
		}
		for(int i = 0; i < numberOfComputers;i++) {
			
		
		
			players[numberOfPlayers-numberOfComputers+i] = new Computer();
			players[numberOfPlayers-numberOfComputers+i].setName("comp"+(i+1));
		}
		
		//gets the players names
		for(int i = 0; i < players.length;i++) {
			if(players[i].getClass() == Computer.class) {
				System.out.println("Computer player added");
			}
			else {
				System.out.print("Player " + (i+1) + ", Enter your name: ");
				System.out.println();
				players[i].setName(input.next());
			}

		}
		//initializes the game
		Board game = new Board(players, cards, board,office); 
		
		
		//This is the actual code of taking turns
		String action;
		int turn = 0;
		//loop
		while (game.getMaxDay() != game.getDay()) {
			System.out.println(game.getDay());
			System.out.println(game.getMaxDay());
			Actor activePlayer = players[turn%numberOfPlayers];
			Room activeRoom = activePlayer.getCurrentRoom();
			System.out.println("It is "+ activePlayer.getName() + "'s turn\n");
			activePlayer.printInfo();

			boolean move = false; //represents if a player has moved
			boolean valid = false; //represents a legal turn 
			boolean end = false; //represents the end of a turn
			if(activePlayer.getClass() != Computer.class) {
			//turn loop
				while(!end) {
					
					activePlayer.displayActions(move,valid);
					System.out.println();
					action = input.next();
					
					//rehearse
					if(action.equalsIgnoreCase("rehearse")) {
						if(!valid) {
							valid = activePlayer.rehearse();		
						} else {
							System.out.println("You cannot rehearse, you already made a move");
						}
						
					} else if(action.equalsIgnoreCase("scenes")) {
						game.displayScenes();
					}
					
					//this will display all of the players and their location
					else if(action.equalsIgnoreCase("players")) {
						
						for(Actor player : players) {
							
							System.out.println("name: "+ player.getName() + "\t location: " + player.getCurrentRoom().getName());
							
							
						}
						System.out.println();
					
					
					//displays the active players information
					} else if(action.equalsIgnoreCase("info")) {
						activePlayer.printInfo();
						activeRoom.sceneInfo();
						
					//lets the user act	
					} else if(action.equalsIgnoreCase("act")) {
						
						if(!valid && activePlayer.getInRole()) {
							
							activeRoom.actTrial(activePlayer);
							
							//this means the player succeeded and finished the scene
							
							if(!activePlayer.getInRole()) {
								activeRoom.setHasScene(false);
								game.decrementScene();
								move = true;
							
							}
							
							valid = true;
							
							
						} else {
							System.out.println("You cannot act");
						}
						
						
					//this will rank up the player	
					} else if(action.equalsIgnoreCase("rank")) {
						int rank = activePlayer.getRank();
						
						//pre-condition: player must be in the office and not rank 6. Ranking up represents a valid action
						if(!valid && activeRoom.getName().equals("office") && (!(rank == 6))) {
							office.rankUp(activePlayer);
						} else {
							System.out.println("You cannot rank up");
						}
						
						
						//postcondtion: represents the rank change
						//if(rank != activePlayer.getRank()) {
							//valid = true; //set this to false if you can rank up, move, then take a role. 
						//}
					//this loop will move the player to a neighbor room
					}	else if(action.equalsIgnoreCase("move")) {
						if(!move && !activePlayer.getInRole()) {
							
							activeRoom.displayNeighborRooms();
							
							boolean loop = true;
							while(loop) {
								try {
									System.out.println("Type a number that represents the desired room");
									System.out.println("Or type 345 to cancel");
									int room = input.nextInt();
									if(room-1 < activeRoom.getNumberOfNeighbors()) {
										game.moveRoom(activePlayer, room);
										//valid = true;
										move = true;
										loop = false;
										activeRoom = activePlayer.getCurrentRoom();
									} else if(room == 345){
										System.out.println("You have chose to cancel");
										loop = false;
									} else {
										System.out.println("Not a valid number, Try again");
									}
									
								} catch(InputMismatchException e) {
									System.out.println("That is not a number, please pick a number to move to.");
									input.next();
								}
							}
							
							
						} else {
							System.out.println("You cannot move");
						}
						
						//if the player wants to view/take roles
					} else if(action.equalsIgnoreCase("role")) {
						//preconditions
						if(!valid && !activePlayer.getInRole() && activeRoom.getHasScene()) {
							
							activeRoom.displayRoles(activePlayer);
							boolean roleLoop = true;
							while(roleLoop) {
								System.out.println("Type 'take' to take a role, ");
								System.out.println("Type 'cancel' to not take a role");
								System.out.println("Type 'info' to display information on every role");
								System.out.println("Type 'available' to see roles you can join");
								System.out.println();
								String role = input.next();
								
								//information on all roles in the room
								if(role.equalsIgnoreCase("info")) {
									activeRoom.displayRoleInfo();
									//exit loop, no role
								} else if(role.equalsIgnoreCase("cancel")) {
									
									roleLoop = false;
									
								} else if(role.equalsIgnoreCase("take")) {
									
									valid = true;
									roleLoop = false;
									activeRoom.takeRole(activePlayer); //may have to change back
									activeRoom.addWorker(activePlayer);
									if(!activePlayer.getInRole()) {
										valid = false;
									}
									
								
									
								} else if(role.equalsIgnoreCase("available")) {
									activeRoom.displayRoles(activePlayer);
								} else {
									System.out.println("The option isn't availble");
									System.out.println();
								}
								
							}
							
						} else if(activePlayer.getInRole()){
							System.out.println("You cannot take a role, you are already in a role");
							System.out.println("Current Role: " + activePlayer.getRole().getRoleName());
						} else {
							System.out.println("You cannot take a role right now");
						}
						
					} else if(action.equals("end")) {
						end = true;
						game.check();
						
					} else {
					
						System.out.println("not a valid action");	
						
					} 
	
				} //end of turn loop
			} else if(activePlayer.getClass() == Computer.class){
				Computer comp  = (Computer) activePlayer;
				boolean finishScene = comp.doAction(game);
				if(finishScene) {
					game.decrementScene();
				}
				
			}
	
		turn++;		
		} //end of days loop
		
	} //end of main loop
	
	

}
