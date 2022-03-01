import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


//this class acts as the model, which will manipulate data based on what the user does
public class DeadwoodModel {
	
	//path for images
	String path = "src/main/resources/img/";
	public DeadwoodModel() {
			
	}
	//if the player rehearses, they gain a practice chip
	public void rehearse(Actor actor) {
		actor.setChips(actor.getChips()+1);
	}
	//if the player acts, they attempt to remove a shot counter
	//returns whether the scene is finished or not
	public boolean act(Actor actor,int roll) {
		boolean finish = false;
		Room room = actor.getCurrentRoom();
		ArrayList<ShotCounter> shots = room.getCounterArray();
		
		//the amount of shots remaining before the act
		int preShots = room.getShotsRemaining();
		
		//represents a success
		if(actor.getChips()+ roll >= actor.getScene().getBudget()) {
			payActor(actor);
			room.decShotsRemaining();
			
		//if the act failed, an offcard role still gets paid	
		} else if(!actor.getOnCard()){
			actor.addDollars(1);
		}
		
		int shotsRemaining = room.getShotsRemaining();
		
		
		//set the next shot image to success image
		if(shotsRemaining != preShots) {
			boolean shot = true;
			
			for(int i = shots.size()-1; i >=0; i--) {
					
				if(shots.get(i).getImage() == null && shot) {
					shots.get(i).setImage(path+"shot.png");
						shot = false;
				}
				
			}
			
		}
		
		
		//if there are no shots remaining, we need to pay the actors of the scene
		//and wrap up the scene
		if(shotsRemaining == 0) {
			
			room.getScene().setImage(null);
			room.payActors();
			room.finishScene();
			finish = true; //represents a finished scene for the controller
			
		}
		return finish;
	}
	//this pays a single actor for their work on a scene
	//if they succeed
	public void payActor(Actor actor) {
		if(actor.onCard) {
			actor.addCredits(2);
		} else { //offcard
			actor.addDollars(1);
			actor.addCredits(1);
		}
	}
	//this will move the player to the target room
	//this is a searching algorithm
	public void move(Actor actor, String roomName, Room[] rooms) {
		
		actor.getCurrentRoom().removeActor(actor); //makes sure the previous room knows this actor left.
		actor.getCurrentRoom().updateQueue();
		Room targetRoom = null;
		for(int i = 0; i < rooms.length;i++) {
			if(roomName.equals(rooms[i].getName())) {
				
				targetRoom = rooms[i];
			}
		}
		
		//now that the room is found, add the 
		//actor and other information
		actor.setCurrentRoom(targetRoom);
		targetRoom.addActorQueue();
		targetRoom.addActor(actor);
		
		if(targetRoom.getHasScene()) {
			actor.getCurrentRoom().getScene().flipPhoto();
		}
		actor.setCoordsToRoom();
		
		
	}

	//if the player takes a role, this method takes in the role name
	//and the actor thats taking a role
	public void takeRole(Actor actor, String roleName) {
		Room room = actor.getCurrentRoom();
		Role targetRole = null;
		//search for the role
		for(Role role: room.getRoles()) {
			if(role.getRoleName().equals(roleName)) {
				targetRole = role;
			}
		}
		//let the room know the actor is working
		//update actor
		room.addWorker(actor);
		actor.setRole(targetRole);
		actor.setScene(room.getScene());
		actor.getCurrentRoom().decActorQueue();
	}
	
	
	//this method takes in input from the view. This input represents a desired
	//rank and the dollars required. If the player has enough of the currency
	//the actor is ranked up, if not, this method does nothing.
	public void rankUpDollars(Actor actor,int currency,int rank) { 
		
		if(actor.getDollars() >= currency) {
			actor.setDollars(actor.getDollars()-currency);
			actor.setRank(rank);
		
		} 
	
		//this method takes in input from the view. This input represents a desired
		//rank and the credits required. If the player has enough of the currency
		//the actor is ranked up, if not, this method does nothing.
	} public void rankUpCredits(Actor actor, int currency, int rank) {
		
		if(actor.getCredits() >= currency) {
			actor.setCredits(actor.getCredits()-currency);
			actor.setRank(rank);
			
		} 
	}
	
	//returns a room type when given a string and a list of rooms
	//this is used for the bot, this method should be moved if time wasn't a factor
	public Room getRoom(String roomName, Room[] rooms) {
		
		Room targetRoom = null;
		for(int i = 0; i < rooms.length;i++) {
			if(roomName.equals(rooms[i].getName())) {
				
				targetRoom = rooms[i];
				return targetRoom;
				
			}
		}
		
		return targetRoom;
		
		
		
		
		
		
	}
	

}
