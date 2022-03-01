//This is the actor class which has a lot of attributes
public class Actor {
	public int dollars;
	public int credits;
	public int rank;
	public int chips;
	public boolean inRole;
	public boolean onCard;
	private String name;
	public Room currentRoom;
	public Scene scene; 
	public Role currentRole;
	private String[] diceSet;
	private String image;
	private int xCord;
	private int yCord;
	private int height = 25;
	private int width = 25;
	
	public Actor() {
		rank = 1; 
		dollars = 0;
		credits = 0;
		chips = 0;
		inRole = false;
		currentRole = null; //maybe delete
		
	}
	 //getters
	public int getDollars() {
		return dollars;
	}
	public String getName() {
		return name;
	}
	
	public boolean getInRole() {
		return inRole;
	}
	
	public boolean getOnCard() {
		return onCard;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getCredits() {
		return credits;
	}
	public int getChips() {
		return chips;
	}
	
	public String[] getDiceSet() {
		return diceSet;
	}
	
	public String getImage() {
		return image;
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
	
	public Scene getScene() {
		return scene;
	}
	public int getScore() {
		int score = rank*5;
		score += dollars;
		score += credits;
		return score;
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public Role getRole() {
		return currentRole;
	}
	//end of getters
	
	//setters
	public void setRank(int rank) {
		this.rank = rank;
		if(diceSet != null) {
			image = diceSet[rank-1];
		}
	}
	
	public void setCredits(int credits) {
		this.credits = credits;
	}
	
	public void setChips(int chips) {
		this.chips = chips;
	}
	
	public void setInRole(boolean status) {
		inRole = status;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCurrentRoom(Room newRoom) {
	 	currentRoom = newRoom;
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
	//sets a role, needs to get if its an onCard role or offCard 
	public void setRole(Role newRole) {
	 	currentRole = newRole;
	 	inRole = true;
	 	newRole.setRoleTaken(true);
	 	onCard = newRole.getOnCard();
	 	setHeight(currentRole.getHeight());
	 	setWidth(currentRole.getWidth());
	 	
	 	if(!currentRole.getOnCard()) {
	 		xCord = currentRole.getxCord();
	 		yCord = currentRole.getyCord();
	 		
	 	} else if(currentRole.getOnCard()) {
	 		xCord = currentRole.getxCord() + currentRoom.getxCord();
	 		yCord = currentRole.getyCord() + currentRoom.getyCord();
	 	}
	 	
	 	
	 	
	}
	
	public void setScene(Scene scene) { //possibly delete
		this.scene = scene;
	}
	 
	
	
	public void setOnCard(boolean onCard) {
		this.onCard = onCard;
	}
	

	public boolean setInRole() {
		return inRole;
	}
	public void setDollars(int dollars) {
		this.dollars = dollars;
	}
	//this will set the coordinates of the players room to itself to represent itself on a GUI
	public void setCoordsToRoom() {
			int xCord = currentRoom.getxCord();
			xCord += (currentRoom.getIndexOfActor(this))*width;
			int yCord = currentRoom.getyCord();
			
			
			if(!currentRoom.getName().equals("office") && !currentRoom.getName().equals("trailer")) {
				yCord+= currentRoom.getWidth()+5;
			} else {
				yCord = currentRoom.getyCord()+20;
			}
		
			setxCord(xCord);
			setyCord(yCord);
		
		
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public void setDiceSet(String[] diceSet) {
		this.diceSet = diceSet;
		setImage(diceSet[0]);
	}
	
	//adds dollars to the players total
	public void addDollars(int dollars) {
		this.dollars += dollars;
	}
	//adds credits ot the players total
	public void addCredits(int credits) {
		this.credits += credits;
	}
	
	

	
	
	//rehearses the players current scene 
	public boolean rehearse() {
		
		if(inRole) {
			int budget = this.getScene().getBudget();
		
			if(chips+1 < budget) {
				chips++;
				System.out.println("You now have an extra practice chip");
				System.out.println("You have: "+chips + " practice chip(s)");
				return true;
			} else {
				System.out.println("You cannot get another practice chip for this scene");
				System.out.println("you have: "+ chips+ " budget is: "+ budget);
				return false;
			}
		} else {
			System.out.println("You cannot rehearse, you are not in a scene");
			return false;
		}
	}
	//does a reset when a player finishes a scene
	public void finishScene() {
		onCard = false;
		inRole = false;
		chips = 0;
		currentRole = null;
		scene = null;
		setHeight(25);
		setWidth(25);
		setCoordsToRoom();
	}
	
	//prints out information of the player
	//information: stats, room, scene, and role info
	public void printInfo() {
		System.out.println("Rank: \t Chips: \t Dollars: \t Credits: \t");
		System.out.println(rank + "\t " + chips+ "\t\t " + dollars + "\t\t " + credits);
		System.out.println();
		System.out.println(name + " is in the room: "+ currentRoom.getName());
		if(inRole) {
			System.out.print(name + " is working on the scene: "+ scene.getName() + " working on the part: "+ currentRole.getRoleName());
			System.out.print(", which is a");
			if(currentRole.getOnCard()) {
				System.out.println(" star role.");
			} else {
				System.out.println("n extra role.");
			}
			System.out.println(" "+ currentRole.getLine());
		}
		System.out.println();
	}
	
	//displays all of the legal actions that a player can take
	//booleans will help determine preconditions
	public void displayActions(boolean hasMoved,boolean legalAction) { 
		
		//these actions are always available
		System.out.println(name + " can: \n");
		System.out.println("'players' to see all players and their location"); 
		System.out.println("'info' to display your information and your current room's scene information"); 
		System.out.println("'scenes' to display all rooms and their scenes, if they have one");
		
		//the following actions need precondition checks
		
		if(!legalAction && rank < 6 && currentRoom.getName().equals("office")) {
			System.out.println("'rank' to rank up");
			
		}
		//if a player is a scene
		if(inRole && !legalAction) {
			System.out.println("'act' to act your scene");
			
			if(chips+1 < scene.getBudget() && !legalAction) {
			System.out.println("'rehearse' to rehearse your scene");
			}
		}
		
		if(!legalAction && (currentRoom.getHasScene()) && (!inRole)){
			System.out.println("'role' to view and/or take roles in your current room");
			
		}
		
		if(!inRole && !hasMoved) {
			System.out.println("'move' to move to a neighboring room");
		}
		
		//always available
		System.out.println("'end' to end your turn.");
		
	}
	
	
}
