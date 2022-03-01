/*this class represents a bot player
 * it extends actor because it can do anything that an actor can do
 * This is a very recent class, bugs are likely
 * 
 */

import java.util.ArrayList;
import java.util.Random;



public class Computer extends Actor{
	
	//this will do an action. This is for the Console version
	public boolean doAction(Board board) {
		System.out.println(this.getName()+" is taking a turn");
		boolean actedScene = false;
		//if in a role it must act or rehearse
		if(inRole) {
			if(chips + 2 >= scene.getBudget()) {
				System.out.println("Computer acted");
				actedScene = act();
			}
			else {
				chips++;
				System.out.println("Computer rehearsed");
			}
		
		
		} else {	
			//will move, and most likely take a role
			move(board);
			System.out.println("Computer moved");
			
		}
		return actedScene;
	}
	
	//does an action but this is for the GUI version
	public boolean doAction(DeadwoodModel model,Room[] rooms) {
		
		boolean actedScene = false;
		if(inRole) {
			if(chips + 2 >= scene.getBudget()) {
				
				actedScene = act();
			}
			else {
				chips++;
				
			}
		
		
		} else {
			
				
			currentRoom.removeActor(this); //makes sure the previous room knows this actor left.
			currentRoom.updateQueue();
			move(model, rooms);
			currentRoom.addActorQueue();
			currentRoom.addActor(this);
			if(!inRole) {
				setCoordsToRoom();
			}
			if(currentRoom.getHasScene()) {
				currentRoom.getScene().flipPhoto();
			}
				
			//System.out.println("Computer moved");
			
		}
		return actedScene;
		
	}
	//this will have the computer player act on its scene
	public boolean act() {
		Dice d = new Dice();
		int roll = d.rollDice();
		//pays itself
		if(chips + roll >= scene.getBudget()) {
			if(onCard) {
				this.addCredits(2);
		
			} else {
				this.addDollars(1);
				this.addCredits(1);
			}
			
			//this will change the shot counter to the correct image
			ArrayList<ShotCounter> shots = currentRoom.getCounterArray();
			boolean shot = true;
			
			for(int i = shots.size()-1; i >=0; i--) {
					
				if(shots.get(i).getImage() == null && shot) {
					shots.get(i).setImage("src/main/resources/img/shot.png");
						shot = false;
				}
				
			}
			
			//checks to see if the current Room has any shots remaining
			currentRoom.decShotsRemaining();
			if(currentRoom.getShotsRemaining() == 0) { //if it does, finish the scene
				currentRoom.finishScene();
				currentRoom.getScene().setImage(null);
				finishScene(); //finish the scene for player (in actor class)
				return true;
				
			} else {
				return false;
			}
			
			
			
		}
		else {
			//if its an offcard role, you still receive a dollar
			if(!onCard) {
				this.addDollars(1);
			}
			return false;
		}
		
	}
	//this will allow the bot to take a role, it will prefer onCard roles
	//if it finds a role it can take it immediately takes it. 
	public void takingRole(int minOn, int minOff) {
		ArrayList<Role> roomRoles = currentRoom.getRoles();
		//checks for onCard roles
		if(minOn <= rank) {
			for(int i = roomRoles.size() -1; i >= 0; i--) {
				if(roomRoles.get(i).getMinRank() <= rank && !(roomRoles.get(i).getRoleTaken())) {
					takeRole(roomRoles.get(i));
					i = 0;
				} else if(roomRoles.get(i).getMinRank() <= rank && !(roomRoles.get(i).getRoleTaken())) {
					takeRole(roomRoles.get(i));
					i=0;
					
				}
			}
			//checks for offcard
		} else if(minOff <= rank){
			for(int i = roomRoles.size() -1; i >= 0; i--) {
				if(roomRoles.get(i).getMinRank() <= rank && !(roomRoles.get(i).getRoleTaken())) {
					takeRole(roomRoles.get(i));
					i=0;
				} else if(roomRoles.get(i).getMinRank() <= rank && !(roomRoles.get(i).getRoleTaken())) {
					takeRole(roomRoles.get(i));
					i=0;
					
				}
			}
		}
		
		
	}
	//this is the code for actually taking a role, it will set the role for the player
	//then it will communicate to the scene that there is another worker
	public void takeRole(Role role) {
		role.setRoleTaken(true);
		inRole = true;
		currentRole = role;
		scene = currentRoom.getScene();
		setRole(role);
		
		if(role.getOnCard()) {
			onCard = true;
		} else {
			onCard =false;
		}
		
		if(!currentRoom.workingActors.contains(this)) {
			currentRoom.addWorker(this);
		}
		getCurrentRoom().decActorQueue();
		
	}
	//this will allow the bot to rank up in the console version
	//if it lands in the office it will rank up to the next rank
	public void rankUp(Board board) {
		
		int[][] ranks = board.office.getRanks();
		
		if(rank != 6) {
			
			int desiredRank = rank+1;
			
		
		
		
			if(credits >= ranks[desiredRank-2][1]) {
				credits -= ranks[desiredRank-2][1];
				rank++;
			} else if(dollars >= ranks[desiredRank-2][2]) {
				dollars -= ranks[desiredRank-2][2];
				rank++;
			}
		}
		
		
		
		
	}
	//this will rank the bot up in the GUI version
	//if the bot enters the casting office room it will rank up (if it can)
	public void rankUp(Room room) {
		CastingOffice office = (CastingOffice) room;
		int[][] ranks = office.getRanks();
		
		if(rank != 6) {
			
			int desiredRank = rank+1;
		
		
		
		
			if(credits >= ranks[desiredRank-2][1]) {
				
				credits -= ranks[desiredRank-2][1];
				setRank(desiredRank);
			} else if(dollars >= ranks[desiredRank-2][2]) {
				
				dollars -= ranks[desiredRank-2][2];
				setRank(desiredRank);
			}
		}
	}
	//this will move the bot across the board in the console version
	public void move(Board board) {
		
		//looks for neighbor rooms of the bots current room
		for(int i = 0; i < currentRoom.getNeighborRooms().size();i++) {
			String roomName = currentRoom.getNeighborRooms().get(i);
			Room room = board.getNeighborRoom(roomName);
			
			
			//if its the casting office and we're at rank one, rank up right away (this will speed up the game)
			if(room.getName().equals("office") && rank ==1 && (credits >= 5 || dollars >= 4)) {
				
				currentRoom = room;
				rankUp(board);
			//if the player can take a role in that room, go to that room	
			} else if(room.getHasScene() && (room.getMinimumRoleRank() <= rank)) {
				currentRoom = room;
			
			//go to a random room	
			} else {
				Random r = new Random();
				String randomRoomName = currentRoom.getNeighborRooms().get(r.nextInt(currentRoom.getNeighborRoomsSize()));
				Room randomRoom = board.getNeighborRoom(randomRoomName);
				currentRoom = randomRoom;
			}
			
			
			
		}
		
		//if you went to a room where you can take a role, do it. 
		int minOff = currentRoom.getMinOffCardRank();
		int minOn = currentRoom.getMinOnCardRank();
		if(currentRoom.getHasScene() && (minOff <= rank || minOn  <= rank)) {
			takingRole(minOn, minOff);
		}
		
		
	}
	//this will move the bot across the board in the gui version
	//it takes in arguments that the controller has 
	public void move(DeadwoodModel model, Room[] rooms) {
		
		ArrayList<String> neighRooms = currentRoom.getNeighborRooms();
		//for all the nieghbor rooms, find a room to move to
		for(int i = 0; i < neighRooms.size();i++) {
			String roomName = neighRooms.get(i);
			Room room = model.getRoom(roomName,rooms);
			
			//if you can move to the office to speed up the game, do it. 
			if(roomName.equals("office") && rank ==1 && (credits >= 5 || dollars >= 4)) {
				currentRoom = room;
				i = neighRooms.size();
				
			//if you can move to a scene with a role that you can take, do it
			} else if(room.getHasScene() && (room.getMinimumRoleRank() <= rank)) {
				currentRoom = room;
				i = neighRooms.size();
				
			//go to a random room	
			} else {
				
				Random r = new Random();
				String randomRoomName = neighRooms.get(r.nextInt(neighRooms.size()));
				Room randomRoom = model.getRoom(randomRoomName,rooms);
				currentRoom = randomRoom;
				i = neighRooms.size();
			
			}
			
			
			
			
		}
		
		//rank up if you happen to be in the office
		if(currentRoom.getName().equals("office")) {
			rankUp(rooms[11]);
		}
		//if you can take a role in your new room, do it. 
		int minOff = currentRoom.getMinOffCardRank();
		int minOn = currentRoom.getMinOnCardRank();
		if(currentRoom.getHasScene() && (minOff <= rank || minOn  <= rank) ) {
			
			takingRole(minOn, minOff);
		}
		
		
	}
	
	

}
