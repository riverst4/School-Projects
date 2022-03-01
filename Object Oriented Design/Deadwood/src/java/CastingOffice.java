import java.io.*;
import java.util.Scanner;

public class CastingOffice {
	
	public void displayRanks() {
		System.out.println("The following ranks and their costs are: \n");
		System.out.println("Rank \t Dollars\t Credits");
		System.out.println("Two \t 4 \t\t 5");
		System.out.println("Three \t 10 \t\t 10");
		System.out.println("Four \t 18 \t\t 15");
		System.out.println("Five \t 28 \t\t 20");
		System.out.println("Six \t 40 \t\t 25");
	}
	
	
	//this will tell the user whether or not they can rank up. 
	//we will need to have a cancel option
	public void requestRankUp(Actor player, int rank) {
		Scanner input = new Scanner(new InputStreamReader(System.in));
		int wantedRank = 0;
		System.out.println(player.name +", which rank would you like to rank up to?");
		boolean valid = false;
		while (!valid) {
			wantedRank = input.nextInt();
			if(wantedRank >1 && wantedRank < 7) {
				valid = true;
			} else {
				System.out.println("That option is not available, must be a number between 2-6");
			}
		}
		if(player.rank >= rank) {
			System.out.println("Your rank is already higher than that rank");
		} else if (player.dollars < payDollars(rank) && player.credits >= payCredits(rank)) {
			System.out.println("You don't have enough dollars, you will have to pay in credits");
			System.out.println("You will need to give " + (rank-1)*5 + " credits"); 
			System.out.println("Do you want to pay the credits? \n Type 'y' for yes and 'n' for no");
			if(input.next().equals("y")) {
				getRank('c',player, rank);
			}
		} else if(player.dollars >= payDollars(rank) && player.credits < payCredits(rank)) {
			System.out.println("You don't have enough credits, you will have to pay in dollars");
			System.out.println("You will need to give " + ((rank*rank)+rank-2) + " dollars");
			System.out.println("Do you want to pay the dollars? \n Type 'y' for yes and 'n' for no");
			if(input.next().equals("y")) {
				getRank('d',player, rank);
			} else if(player.dollars < payDollars(rank) && player.credits < payCredits(rank)) {
				System.out.println("You don't have enough dollars or credits to rank up");
			}
		} else {
			System.out.println();
			System.out.println("If you want to rank up to " + rank);
			System.out.println("You will either need to give " + ((rank*rank)+rank-2) + " dollars");
			System.out.println("or you will need to give " + (rank-1)*5 + " credits");
			System.out.println("Do you want to rank up? \n Type 'd' for dollars and 'c' for credits");
			if(input.next().equals("d")) {
				getRank('d',player, rank);
			} else if (input.next().equals("c")){
				getRank('c',player, rank);
			}
		
		}
	}
	
	private void getRank(char c, Actor player, int rank) {
		if(c == 'd') {
			player.setDollars(player.dollars - payDollars(rank)); 
		}
		else {
			player.setCredits(player.credits - payCredits(rank));
		}
		System.out.println("success, you are now at rank " + rank);
	}
	
	private int payDollars(int rank) {
		return rank*rank+rank-2;
	}
	
	private int payCredits(int rank) {
		return (rank-1)*5;
	}
	
	

}
