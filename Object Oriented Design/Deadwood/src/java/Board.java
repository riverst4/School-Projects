//this represents a board of Deadwood, a very important class for the Console version of this game
//it will have a lot of rooms and does most of the initialization

import java.util.*;

import org.w3c.dom.Document;


public class Board {
	private int scenesLeft;
	private int day = 1;
	private int maxDay;
	private Actor[] actors;
	private ArrayList<Scene> scenes = new ArrayList<Scene>();
	private Room trailer = new Room();
	private Room[] rooms   = new Room[12];
	private int[][] ranks = new int[5][3];
	public CastingOffice office = new CastingOffice();
	
	
	
	public Board(Actor[] actors, String cards, String board,CastingOffice office) {
		this.office = office;
		
		this.actors = actors;
		init(board, cards);
		
		if(actors.length <= 3) {
			maxDay = 3; 
		} else {
			maxDay = 4;
		}
		setBoard();
	}
	
	public Board() {
		
	}

	//getters
	public int getMaxDay() {
		return maxDay;
	}
	
	public int getDay() {
		return day;
	}
	
	
	//decrements then checks to see if the board needs to be set
	public void decrementScene() {
		scenesLeft--; 
		check();
			
		
	}
	//checks to see if there is one scene left
	public void check() {
		if(scenesLeft == 1) {
			day++;
			if(day != maxDay) {
				setBoard();
			} else {
				declareWinner(actors);
			}
		}
	}
	//if a player wants to move a room they will put in a number which represents out room array
	public void moveRoom(Actor player, int roomNumber) {
		
		roomNumber--; //makes sure it has valid range
		String roomName = player.getCurrentRoom().getNeighborRooms().get(roomNumber); //gets the room name of the taget room
		for(int i = 0; i < rooms.length; i++) {
			if(roomName.equalsIgnoreCase(rooms[i].getName())) { //searches array of rooms
				player.setCurrentRoom(rooms[i]);	//sets room
				//System.out.println("New Room is: "+ rooms[i].getName());
			}
		}
		
		
	}
	public Room getNeighborRoom(String roomName) {
		for(int i = 0; i < rooms.length; i++) {
			if(roomName.equalsIgnoreCase(rooms[i].getName())) {
				return rooms[i];
			}
		}
		return rooms[0];
		
	}
	
	public void moveRoom(Actor player) {
		
	}
	
	//makes sure the board is set correctly
	//a lot of initialization
	private void setBoard() {
		
		scenesLeft = 10;
		
		if(day == maxDay) {
			declareWinner(actors);
		} else {
			System.out.println("The day has started, the current day is "+ day);
		}
		
		//puts all the actors in the trailer
		for(Actor actor : actors) {
			actor.setCurrentRoom(rooms[10]);
			actor.finishScene();
		
		}
		//gets a scene from the scene deck
		//use of -2 because trailer and casting office are at [10][11]
		for(int i = 0; i < rooms.length-2;i++) {
			rooms[i].workingActors.clear();
			rooms[i].setShot(rooms[i].getShotCount());
			Scene scene = getScene();
			
			
			rooms[i].setPresentScene(scene);
			rooms[i].setHasScene(true);
			//rooms[i].setShotsRemaining = rooms[i].getShotCount();
			rooms[i].setShot(rooms[i].getShotCount());
		}
		
		
		
	}
	


	//sets the players that are on the board
	public void setPlayers(Actor[] players) {
		actors = players;
	}
	//displays every room and their scene
	public void displayScenes() {
		for(int i = 0; i < rooms.length; i++) {
			
			System.out.println("Room: " + rooms[i].getName());
			System.out.print("Scene: ");
			if(rooms[i].getHasScene()) {
				System.out.println(rooms[i].getScene().getName());
				
			} else {
				System.out.println("none");
			}
			System.out.println();
		}
		
	}
	//initializes the board, it takes in a string which represents a path 
	//the method then parses through the path to get information about board and scene
	private void init(String board, String cards) {
		
		Document doc = null;
		ParseXML parsing = new ParseXML();
		Room[] basicRooms = new Room[10]; //rooms that can have a scene
		Room trailer = new Room();
		//Room office = new Room();
		
		//creates actors and changes their stats based on the number
		//of players in the game
		int size = actors.length;
			//sets attributes
		if(size == 5) {
				
			for(Actor player : actors) {
					player.setCredits(2);
			}
			} else if (size == 6){
				
				for(Actor player: actors) {
					player.setCredits(4);
				}
			} else if (size >= 7) {
				
				for(Actor player : actors) {
					player.setRank(2);
				}
			
			}	
		
		
		try {
			//get data from cards.xml
			doc = parsing.getDocFromFile(cards);
			scenes = parsing.initScenes(doc);
			
			//get data from board.xml
			doc = parsing.getDocFromFile(board);
			basicRooms = parsing.getRoomData(doc);
			this.office = parsing.readOfficeData(doc);
			trailer = parsing.readTrailer(doc);
			
			//initialize office and trailer (trailer is named in the xml parser)
			//puts all the basic rooms, office and trailer into a room array
			this.office.setName("office"); 
			//ranks = office.arr;
			office.setRanks(this.office.ranks);
			rooms[10] = trailer;
			rooms[11] = office;
			for(int i = 0; i < 10;i++) {			
				rooms[i] = basicRooms[i];
				
			}

		} 	catch (NullPointerException e) {
        
        return;
		} 	catch (Exception e) {
        
        return;
		}
	}
	//gets a random scene from our scenes
	private Scene getScene() {
		Random r = new Random();
		int index = r.nextInt(scenes.size());
		return scenes.remove(index);	
			 
	}
	//these are the available ranks for the given parameters
	//0 represents rank, 1 represents dollars and 2 represents credits
	public void ranks(int rank, int credits, int dollars) {
		System.out.println("These are the ranks that are available to you");
		for(int i = 0; i < ranks.length;i++) {
			
			//if a rank is less than the rank
			if(rank < ranks[i][0]) { 
				
				//if a player can only pay in credits
				if(credits >= ranks[i][2] && dollars < ranks[i][1]) {
					System.out.println("Rank: " + ranks[i][0]);
					System.out.println(" \t" + ranks[i][2] + " credits.");
				//if a player can only pay in dollars	
				}else if(dollars >= ranks[i][1] && credits < ranks[i][2]) {
					System.out.println("Rank: " + ranks[i][0]);  
					System.out.println(" \t" + ranks[i][1] + " dollars.");	
				//if a player can pay in both, dollars and credits		
				} else if(dollars >= ranks[i][1] && credits >= ranks[i][2]) {
					System.out.println("Rank: "+ ranks[i][0]);
					System.out.println( " \t" + ranks[i][2] + " credits." );
					System.out.println(" \t" +ranks[i][1] + " dollars.");
				}
			}
		}
	}
	//this will display ranks to players and also take in a currency to rank up a player
	public void rankUp(Actor player) {
		int rank = player.getRank();
		int credits = player.getCredits();
		int dollars = player.getDollars();
		
		//get possible ranks
		ranks(rank, credits, dollars);
		
		Scanner record = new Scanner(System.in);
		
		int input = 345; 
		boolean loop = true;
		
		//loop until the player gives valid input or cancels
		while(loop) {
			try {
				System.out.println("Enter a rank for you to purchase \n Type 345 to decline ");
				input = record.nextInt();
				
				if(input == 345) {
					loop = false;
					System.out.println("cancelling upgrade...");
				}
				else if(input <= rank || input > 6) {
					System.out.println("That's not a valid number, try again.  ");
					
				//activates when a player chooses a valid rank	
				} else {
					loop = false;
					
					boolean rankUp =true;
					
					//loop until the player pays with a currency or cancels
					while(rankUp) {
						System.out.println();
						System.out.println("In order to rank up to " + input + " you need to pay (1) credits or (2) dollars");
						System.out.println("You would need to pay: " + (ranks[input-2][2]) +  " credits");
						System.out.println("You would need to pay: " + (ranks[input-2][1]) + " dollars");
						System.out.println("You have " + player.getCredits() + " credits and "+ player.getDollars() + " dollars.");
						System.out.println("Type '1' for credits. Type '2' for dollars. Type '3' to cancel");
						int currency = record.nextInt();
						
						//player has chosen credits, checks to see if they have enough
						if(currency == 1 && ((ranks[input-2][2]) <= credits)) {
							int updatedRank = ranks[input-2][0];
							int updatedCredits = ranks[input-2][2];
							player.setCredits(credits - updatedCredits);
							player.setRank(ranks[input-2][0]);
							System.out.println("You are now at rank " + updatedRank +" . You now have " + player.getCredits() + " credits and " + player.getDollars() + " dollar(s)" );
							rankUp = false;
						//player has chosen dollars, checks to see if they have enough	
						} else if(currency == 2 && ((ranks[input-2][1]) <= dollars)) {
							int updatedRank = ranks[input-2][0];
							int updatedDollars = ranks[input-2][1];
							player.setRank(ranks[input-2][0]);
							player.setDollars(dollars - updatedDollars);
							System.out.println("You are now at rank " + updatedRank +" . You now have " + player.getCredits() + " credits and " + player.getDollars() + " dollar(s)" );
							rankUp = false;
							
						//player has chosen to cancel	
						} else if(currency == 3) {
							System.out.println("You chose to cancel. Your  current rank has not been changed/upgraded");
							rankUp = false;
						} else {
							System.out.println("You dont have the valid currency, type '3' to cancel, or try a different currency");
						}
						
					}
					
				}
			}
			//if a player inputs a string
			catch(InputMismatchException e) {
				System.out.println("That's not a number, try ranking up again");
				record.next();
			}
			
		
		}
		return;
		}
	
	//this method will decide the winner from the array of players
	private static void declareWinner(Actor[] players) {
		ArrayList<Actor> winners = new ArrayList<Actor>();
		int score = 0;
		
		for(int i = 0 ; i < players.length;i++) {
			Actor actor = players[i]; //get score of current actor
			System.out.println("Actor: "+ players[i].getName() + " scored " + actor.getScore());
			if(actor.getScore() > score) {
				winners.clear(); //makes sure there is still only one winner
				score = actor.getScore();
				winners.add(actor);
			}else if(actor.getScore() == score) { //if there is more than one winner
				winners.add(actor);
			}
			
		}
		int winnerScore = winners.get(0).getScore(); //will always be the highest score
		//print the winner(s) and their scores
		if(winners.size() == 1) {
			System.out.println("The winner of the game is: " + winners.get(0).getName());
			System.out.println(winners.get(0).getName() + " had " + winnerScore + " points");
			
		} else {
			System.out.println("The winners are: " );
			for(Actor actor : winners) {
				String actorName = actor.getName();
				System.out.print(actorName + " \t");
				
			}
			System.out.println();
			System.out.println("They had " + winnerScore + " points");
		}
		
		
	}
	
}

