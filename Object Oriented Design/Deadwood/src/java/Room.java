import java.io.InputStreamReader;
import java.util.*;

public class Room {
	ArrayList <Actor> actors = new ArrayList<Actor>();
	ArrayList <Actor> workingActors = new ArrayList<Actor>();
	private int shotCount;
	private int shotsRemaining;
	private Scene presentScene;
	private boolean hasScene;
	private int xCord;
	private int yCord;
	private int height;
	private int width;
	private int actorQueue;
	
	
	
	private int numberWorking;
	private String roomName;
	
	private ArrayList <String> neighborRooms = new ArrayList<String>();
	private ArrayList <Role> roles = new ArrayList<Role>();
	private ArrayList<ShotCounter> shots = new ArrayList<ShotCounter>();
	
	
	public Room() {
		
	}
	//getters
	public int getNumberOfNeighbors() {
		return neighborRooms.size();
	}
	
	public ArrayList<String> getNeighborRooms() {
		return neighborRooms ;
		
	}
	public int getNeighborRoomsSize() {
		return neighborRooms.size();
		
	}
	public ArrayList<Actor> getPresentActors() {
		return actors;	
		
	}
	
	public int getNumberOfActors() {
		return actorQueue;
		
	}
	
	public int getShotSize() {
		return shots.size();
	}
	public ArrayList<ShotCounter> getCounterArray() {
		return shots;
	}
	
	public int getxCord() {
		return xCord;
	}
	
	public int getyCord() {
		return yCord;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public String getName() {
		return roomName;
	}
	public Scene getScene() {
		return presentScene ;
		
	}
	public int getShotCount() {
		return shotCount;
	}
	
	public int getShotsRemaining() {
		return shotsRemaining;
	}
	
	
	public void decShotsRemaining() {
		shotsRemaining--;
	}
	
	public boolean getHasScene() {
		return hasScene;
	}
	
	public ArrayList<Role> getRoles() {
		ArrayList<Role> sceneRoles = presentScene.getRoles();
		
		return roles;
	}
	
	public boolean getRoleTaken(int index) {
		return roles.get(index).getRoleTaken();
		
	}
	
	public int getRoleSize() {
		return roles.size();
	}
	//gets the index of the actor in the room, 
	//important for the GUI
	public int getIndexOfActor(Actor actor) {
		int index=0;
		for(int i = 0; i < actors.size();i++) {
			if(actors.get(i) == actor) {
				index = i;
			}
		}
		return index;
	}
	//returns the rank of the minimum available oncard role
	public int getMinOnCardRank() {
		int minRank = 7;
		for(int i = 0; i < roles.size();i++) {
			if(roles.get(i).getMinRank() < minRank && roles.get(i).getOnCard() && !roles.get(i).getRoleTaken()) {
				minRank = roles.get(i).getMinRank();
			}
		}
		return minRank;
	}
	//returns the rank of the minimum available offcard role
	public int getMinOffCardRank() {
		int minRank = 7;
		for(int i = 0; i < roles.size();i++) {
			if(roles.get(i).getMinRank() < minRank && !(roles.get(i).getOnCard()) && !roles.get(i).getRoleTaken()) {
				minRank = roles.get(i).getMinRank();
			}
		}
		return minRank;
		
	}
	
	
	
	public void setName(String name) {
		this.roomName = name;
	}
	
	public void setxCord(int xCord) {
		this.xCord = xCord;
	}
	
	public void setyCord(int yCord) {
		this.yCord = yCord;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setShot(int shot) {
		shotsRemaining = shot;
	}
	
	public void setPresentScene(Scene scene) {
		presentScene = scene;
		for(Role role: scene.roles) {
			roles.add(role);
		}
		
		
	}
	
	public void setHasScene(boolean bool) {
		hasScene = bool;
	}
	
	public void addWorker(Actor actor) {
		workingActors.add(actor);
		numberWorking++;
	}
	
	public void addActor(Actor actor) {
		actors.add(actor);
		//numberWorking++;
	}
	
	public void removeActor(Actor actor) {
		actors.remove(actor);
		numberWorking--;
	}
	
	public void addActorQueue() {
		actorQueue++;
	}
	
	public void updateQueue() {
		for(Actor actor: actors) {
			if(!actor.getInRole()) {
				actor.setCoordsToRoom();
			}
		}
	}
	
	public void decActorQueue() {
		actorQueue--;
	}
	//adds a shot to the shots arraylist
	public void addShot(ShotCounter shot) {
		shots.add(shot);
	}
	
	//displays the names of all neighbor rooms
	public void displayNeighborRooms() {
		System.out.println("You can move to: ");
		
		for(int i = 0; i < neighborRooms.size();i++) {
			System.out.print(i+1 + ": "+ neighborRooms.get(i) + "\t");
			
		}
		System.out.println();
	}
	
	//increments shots, used for parsing
	public void incShot() { 
		shotCount++;
		shotsRemaining++;
	}

	
	//used in parsing
	public void addNeighbor(String room) {
		neighborRooms.add(room);
	}
	//used in parsing
	public void addRole(Role role) {
		roles.add(role);
	}
	
	public void printRoleInfo() {
		for(Role role : roles) {
			System.out.println(role.getxCord());
			System.out.println(role.getyCord());
			
		}
	}
	
	
	//this will display all of the available roles a player can take
	//this is duplicate code so we will have to make a private method called displayroleType
	public void displayRoles(Actor actor) {
		
		//displays 'extras' roles
		System.out.println("\nExtra roles that you can take: ");
		for(int i = 0; i < roles.size();i++) {
			Role role = roles.get(i);
			boolean take = actor.getRank() >= role.getMinRank();
			
			if((!roles.get(i).getOnCard()) && take && !role.getRoleTaken()) {
				System.out.print((i+1) + ". " + roles.get(i).getRoleName() + " ");
				
			} 
					
		}
		
		//displays star roles that the user can take
		//needs to be separate for the view 
		System.out.println("\nStar roles that you can take: ");
		for(int j = 0; j < roles.size();j++) {
			Role role = roles.get(j);
			boolean canTake = actor.getRank() >= role.getMinRank();
			
			if(roles.get(j).getOnCard() && canTake && !role.getRoleTaken()) {
				System.out.print((j+1) + ". " + roles.get(j).getRoleName() + " ");
			}
		}
		System.out.println();
		
		
	}
	//this will get the absolute minimum rank of all the roles
	//that exist in the current scene
	public int getMinimumRoleRank() {
		int min = 7;
		for(int i = 0; i < roles.size();i++) {
			Role role = roles.get(i);
			if( !role.getRoleTaken() && role.getMinRank() < min) {
				min = role.getMinRank();
			}
		}
		return min;
	}
	
	//displays all of the roles in the scene, it will differentiate 
	//between oncard and offcard roles
	public void displayRoleInfo() {
		
		for(Role role : roles) {
			
			if(!role.getRoleTaken()) {
				System.out.println("Role: "+ role.getRoleName() );
				System.out.println("\t min rank: " +role.getMinRank());
				if(role.getOnCard()) {
					System.out.println("\t role type: star");
				} else {
					System.out.println("\t role type: extra");
				}
				
				System.out.println("\t line: "+ role.getLine());
			}
			System.out.println();
		}
	
	}
	//this represents the takeRole action a player can take
	//reads input from the user until they cancel or select a valid Role
	//calls on takeRole(actor, int) to actually take the role
	public void takeRole(Actor activePlayer) {
		Scanner input = new Scanner(new InputStreamReader(System.in));
		boolean loop = true;
		while(loop) {
			
			try {
				
				displayRoles(activePlayer);
				System.out.println("Type a number that represents your desired role: ");
				System.out.println("Type 345 to cancel");
				
				int roleNum = input.nextInt();
				//precondition
				if(roleNum-1 < getRoleSize() && !(getRoleTaken(roleNum-1))) {
					
					takeRole(activePlayer, roleNum);
					loop = false;
					
					//cancels
				} else if(roleNum == 345) {
					System.out.println("You have chosen to cancel. ");
					loop = false;
					
				}	else  {
					System.out.println("Not a valid number, try again");
				}
				//if user inputs a string
			} catch(InputMismatchException e) {
				System.out.println("That is not a number, please try again");
				input.next();
			}
			
		}
	}
		
		
		
		
	
	//this will have the player take a role
	//it will do a lot of attribute setting.
	private void takeRole(Actor player, int roleNumber) {
		roleNumber--;
		
		Role targetRole = roles.get(roleNumber);
		
		if(player.getRank() >= targetRole.getMinRank() && !targetRole.getRoleTaken()) {
			//player.inRole =  true; i just added this to player.currentROle
			player.setRole(targetRole);
			player.setScene(getScene());
			//player.currentRole.roleTaken = true; added it in setRole as well
			if(player.getRole().getOnCard()) {
				player.setOnCard(true); 
			} else {
				player.setOnCard(false);
			}
			
		}
			
		
		//if precondition isn't met
		if(player.getRole() != null) {
			System.out.println("You are now working on the Role: "+ player.getRole().getRoleName());
			System.out.println("\t" + player.getRole().getLine());
			addActor(player);
		} else {
			System.out.println("Not a valid role, your rank is not high enough or someone is already working that role");
		}

	}
	//this method will display the room's scene information
	public void sceneInfo() {
		
		if(hasScene) {
			System.out.println("Scene name: " + presentScene.getName() +" ");
			System.out.println(presentScene.getDescription());
			System.out.println("shots remaining: "+ shotsRemaining);
			System.out.println("budget: "+presentScene.getBudget());
			System.out.println("players working on the scene: "+ numberWorking);
			System.out.println();
		} else {
			System.out.println("this room doesn't have a scene");
		}
	}
	//this removes the scene from the current room and 
	//changes some attributes
	public void finishScene() {
		payActors();
		hasScene = false;
		for(int i = 0; i < roles.size(); i++) {
			if(roles.get(i).getOnCard()) {
				roles.remove(i);
				i--;
			}else {
				roles.get(i).setRoleTaken(false);
			}
		}
		
		
		numberWorking = 0;
		workingActors.clear();
	}
	
	
	
	public void emptyShotCounters() {
		for(int i = shots.size(); i > 0;i--) {
			
			shots.get(i-1).setImage(null);
		}
	}
	
	
	//this will perform the 'act' trial
	//if a player + a dice roll > than the current scenes budget, it will result in a success
	public void actTrial(Actor actor) { 
		
		Dice d = new Dice();
		int roll = d.rollDice();
		System.out.println("Rolling...");
		System.out.println();
		System.out.println("You rolled a "+roll);
		
		if(actor.getChips()+roll >= presentScene.getBudget()) {
			System.out.println("Success!");
			
			if(actor.getOnCard()) {
				actor.addCredits(2);
				System.out.println("You earned two credits");
			} else {
				actor.addCredits(1);
				actor.addDollars(1);
				System.out.println("You earned a dollar and a credit");
			}
			//this means it succeeded
			shotsRemaining--;
			System.out.println(shotsRemaining);
			//removes the scene and pays actors. 
			if(shotsRemaining == 0) {
				System.out.println("Finishing scene");
				finishScene();
				
				
			}
			
			//doesn't succeed
			//if the player is an extra role, they will still get paid
		} else {
			System.out.println("You did not succeed your attempt");
			if(!actor.getOnCard()) {
				System.out.println("You still earned a dollar");
				actor.addDollars(1);
			}
			
		}
		
	}
	//this will pay all of the actors that are working on the scene. 
	public void payActors() {
		Dice d = new Dice();
		
		
		//this will pay all of the offCard actors
		for(int i = 0; i < workingActors.size();i++) {
			Actor currActor = workingActors.get(i);
			
			if(!currActor.getOnCard()) {
				
				currActor.addDollars(currActor.getRole().getMinRank());
				
				workingActors.remove(i);
				
				currActor.finishScene();
				
				
				 i--;//decrement because one was found, and we're getting the first element in remove
			}
		}
		
		//payOnCard Actors
		if(workingActors.size() > 0) {
			
			int[] diceRolls = new int[presentScene.getBudget()];
			for(int i = 0; i < diceRolls.length;i++) {
				diceRolls[i] = d.rollDice();
			}
			
			
			
			//sorts both dice and actors descendingly
			workingActors = reverseSort(workingActors);
			reverseSort(diceRolls);
			
			
			//gives the highest dice values to the highest ranked 
			for(int i = 0; i < diceRolls.length;i++ ) {
				int value = diceRolls[i];
				workingActors.get(i%(workingActors.size())).addDollars(value);
			}
			
			int size = workingActors.size();
			for(int i = 0; i < size;i++) {		
				Actor actor = workingActors.get(0);
				actor.finishScene();
				workingActors.remove(0);
				
				
				
			}
			
		}
		
	
		
		
	}
	
	
	//insertion sort on an array
	
	private void reverseSort(int[] arr) {
		for(int i = 1; i < arr.length;i++) {
			int key = arr[i];
			int j = i-1;
			
			
			while(j >= 0 && arr[j] > key) {
				arr[j+1] = arr[j];
				j--;
			}
			arr[j+1] = key;
			
		}
	}
	//an algorithm that sorts actors based on the requirements of the 
	//scenes, there isn't a duplicate minRank of the roles
	private ArrayList<Actor> reverseSort(ArrayList<Actor> actors) {
		ArrayList<Actor> sorted = new ArrayList<Actor>();
		
		int rank = 6;
		while(rank != 0) { //ranks go down from 6-1
			for(int i = 0; i < actors.size();i++) {
				if(actors.get(i).getRole().getMinRank() == rank) {
					sorted.add(actors.get(i));
				}
			}
			rank--;
		}
		return sorted;
	}
	
	
	
	
	

	
}
