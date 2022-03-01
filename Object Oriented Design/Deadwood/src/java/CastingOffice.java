/*this class represents the room "casting office" in the game deadwood
 * this class is special because this room is not an ordinary room. 
 * it features ranks and does not have a scene
 */
import java.io.*;


import java.util.*;
import java.util.Map.Entry;
public class CastingOffice extends Room {
	
	public int[][] ranks = new int[5][3];
	
	private final HashMap<Integer,Integer> rankDollars;
	private final HashMap<Integer,Integer> rankCredits;

	private ArrayList <String> neighborRooms = new ArrayList<String>();
	private String roomName = "office";
	private int xCord;
	private int yCord;
	private int height;
	private int width;
	
			
	
	
	public CastingOffice() {
		this.rankDollars = new HashMap<Integer,Integer>();
		this.rankCredits = new HashMap<Integer,Integer>();
		reqsToRank();

		
	}
	
	public void setxCord(int xCord) {
		this.xCord = xCord;
	}
	
	public void setyCord(int yCord) {
		this.yCord = yCord;
	}
	public int getxCord() {
		return xCord;
	}
	public int getyCord() {
		return yCord;
	}


	public void setRanks(int[][] arr) {
		this.ranks = arr;
	}
	
	public int[][] getRanks() {
		return ranks;
	}
	
	public void printRanks() {
		for(int i = 0; i < ranks.length;i++) {
			for(int j = 0; j < ranks[0].length;j++) {
				System.out.println(ranks[i][j]);
			}
		}
	}
	public int getDollars(int desiredRank) {
		return this.rankDollars.get(desiredRank);		
	}
	
	public int getCredits(int desiredRank) {
		return this.rankCredits.get(desiredRank);
	}
	
	//this method will return the rank given the dollar amount
	//it checks the amount of dollars given, if its in the hashmap
	//it returns the key. 
	public int getRankFromDollars(int dollars) {
		int rank =0;
		for(Entry<Integer, Integer> entry : rankDollars.entrySet()) {
			
			if(entry.getValue() == dollars) {
				rank = entry.getKey();
			}
		}
		return rank;
	}
	//this method will return the rank given the credits amount
	//it checks the amount of dollars given, if its in the hashmap
	//it returns the key. 
	public int getRankFromCredits(int credits) {
		int rank =0;
		for(Entry<Integer, Integer> entry : rankCredits.entrySet()) {
			
			if(entry.getValue() == credits) {
				rank = entry.getKey();
			}
		}
		return rank;
	}
	
	//these are the requirements to rank up. 
	//this is some initialization for the casting office
	public void reqsToRank() {
		//dollars 
		this.rankDollars.put(2,4);
		this.rankDollars.put(3,10);
		this.rankDollars.put(4,18);
		this.rankDollars.put(5,28);
		this.rankDollars.put(6,40);
		
		//credits
		this.rankCredits.put(2,5);
		this.rankCredits.put(3,10);
		this.rankCredits.put(4,15);
		this.rankCredits.put(5,20);
		this.rankCredits.put(6,25);
		
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
	
	

}
