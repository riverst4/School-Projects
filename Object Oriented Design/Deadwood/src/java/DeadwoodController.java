import java.util.ArrayList;
import java.util.Random;

import org.w3c.dom.Document;


//this represents the controller class of deadwood
//most of the variables are put into this class
//which means the initialization of variables is done here
public class DeadwoodController implements BoardViewObserver{
	
	
	//Here is a list of all the variables that the class will use
	ArrayList<Scene> scenes = new ArrayList<Scene>();
	Room[] rooms = new Room[12];
	Scene[] activeScenes = new Scene[10];
	Actor[] actors;	
	DeadwoodView view = new DeadwoodView();
	DeadwoodModel model = new DeadwoodModel();
	String[][] colors = new String[8][6];
	Actor activePlayer;
	CastingOffice office = new CastingOffice();
	Room trailer = new Room();
	String cardPath;
	String boardPath;
	int bots = 0;
	int totalPlayers;
	int day = 0;
	int turn = 0;
	int scenesRemaining = 10;
	int maxDay = 4;
	
	//this will get what the user inputted into the program
	//we save the necessary variables.
	public DeadwoodController(String[] args) {
		boardPath = args[1];
		cardPath = args[2];
		actors = new Actor[Integer.parseInt(args[3])];
		bots = Integer.parseInt(args[4]);
		
		
		init();
		
		//register this controller as an observer for the view then run it. 
		view.register(this);
		view.updateView(actors, actors[0], activeScenes,rooms);
		
		view.run();
		view.setButtons();
		
		
		
		
	}
	//we parse the xml files for the information that we need
	//we must get data about rooms and scenes and save them into variables
	public void parseXML() {
		Document doc = null; 
		ParseXML parsing = new ParseXML();
		Room[] basicRooms = new Room[10];
		
		try {
			//get data from cards.xml
			doc = parsing.getDocFromFile(cardPath);
			scenes = parsing.initScenes(doc);
			
			//get data from board.xml
			doc = parsing.getDocFromFile(boardPath);
			basicRooms = parsing.getRoomData(doc);
			
			//initialize office and trailer (trailer is named in the xml parser)
			office = parsing.readOfficeData(doc);
			trailer = parsing.readTrailer(doc);
			trailer.setName("trailer");
			
			
			//puts all the basic rooms, office and trailer into a room array
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
	//returns all of the current scenes from the scene arraylist
	public ArrayList<Scene> getScenes() {
		return scenes;
	}
	//initializaion
	//sets player info, scene info, and gets info from the xml files
	public void init() {
		
		parseXML();
		initScenes();
		initPlayers();
	}
	
	//this will assign a scene to a room
	//this will also assign x,y coordinates to a scene card so 
	//they will be placed in the correct spot
	public void initScenes() {
		
		for(int i = 0; i < 10; i++) {
			activeScenes[i] = getScene();
			activeScenes[i].setxCord(rooms[i].getxCord());
			activeScenes[i].setyCord(rooms[i].getyCord());
			activeScenes[i].setHeight(rooms[i].getHeight());
			activeScenes[i].setWidth(rooms[i].getWidth());
			rooms[i].setPresentScene(activeScenes[i]);
			//make sure all the shot counters are null
			emptyShotCounters();
			rooms[i].setHasScene(true);
			rooms[i].workingActors.clear();
			rooms[i].setShot(rooms[i].getShotCount());
			
			
		}
	}
	
	//puts all of the actors into the trailer
	//then assigns a color set to the player based on 
	//what player they are. For example, player 1 has the yellow
	//dice set. 
	public void initPlayers() {

		
		for(int i = 0; i < actors.length-bots;i++) {
			Actor actor = new Actor();
			actors[i] = actor;
			actors[i].setCurrentRoom(rooms[10]);
			
			
		}
		
		for(int i = actors.length-bots; i < actors.length;i++) {
			
			
			actors[i] = new Computer();
			actors[i].setCurrentRoom(rooms[10]);
		}
		
		
		
		
		//assign dice color sets
		String path = "src/main/resources/img/";
		for(int j = 1; j <= 6; j++) {
			
			colors[0][j-1] =path+ "dice_y" + j + ".png";
			colors[1][j-1] =path+ "dice_g" + j + ".png";
			colors[2][j-1] =path+ "dice_c" + j + ".png";
			colors[3][j-1] =path+ "dice_b" + j + ".png";
			colors[4][j-1] =path+ "dice_r" + j + ".png";
			colors[5][j-1] =path+ "dice_v" + j + ".png";
			colors[6][j-1] =path+ "dice_p" + j + ".png";
			colors[7][j-1] =path+ "dice_o" + j + ".png";
		}
		
		
		
		//assigns player name with their colors. 
		String[] playerNames = {"yellow","green","cyan","blue","red","violet","pink","orange"};
		for(int i = 0; i < actors.length;i++) {
			rooms[10].addActor(actors[i]);
			actors[i].setDiceSet(colors[i]);
			actors[i].setName(playerNames[i]);
			actors[i].setCoordsToRoom();
			if(actors.length < 4) {
				maxDay = 3;
			} else if(actors.length ==5) {
				actors[i].setCredits(2);
			} else if(actors.length == 6) {
				actors[i].setCredits(4);
			} else if(actors.length > 6){
				actors[i].setRank(2);
			}
			actors[i].setRank(actors[i].getRank());
		}
	}
	//gets a scene from the arraylist of scenes and then removes it so it won't be used later
	private Scene getScene() {
		Random r = new Random();
		int index = r.nextInt(scenes.size());
		return scenes.remove(index);	
			 
	}
	//this will nullify all of the shot counter images, 
	//this will mainly be used once a day has finished. 
	private void emptyShotCounters() {
		for(int i = 0; i < rooms.length;i++) {
			
			ArrayList<ShotCounter> shots = rooms[i].getCounterArray();
			
			for(int j = 0; j < shots.size();j++) {
				shots.get(j).setImage(null);
			}
		}
	}
	
	
	//this method represents a users action from the board view which this
	//control observes. any time the user presses a button, the controller 
	//will be notified and pass info to the model. 
	
	@Override
	public void gotAction(String action)  {
	
		//gets the active player
		activePlayer = actors[turn%actors.length];
		
		
		//if the player acted their scene 
		if(action.contains("act")) {
			//boolean that represents if the scene is wrapped
			int roll = Integer.parseInt(action.substring(3));
			
			
			boolean finish = model.act(activePlayer,roll);
			//if its over...
			if(finish) {
				scenesRemaining--;
				//if there is one scene remaining, we must reset the day
				if(scenesRemaining ==1) {
					
					day++;
					if(day != maxDay) {
					
						initScenes();
						scenesRemaining = 10;
						//puts all of the actors back into the trailer
						trailer.getPresentActors().clear();
						for(int i = 0; i < actors.length;i++) {
							actors[i].setCurrentRoom(rooms[10]);
							rooms[10].addActor(actors[i]);
							actors[i].finishScene();
						}
						
						
					}
					
				}
			}
			
			//if the player rehearses
		} else if(action.equals("rehearse")) {
			model.rehearse(activePlayer);
			
		//if the player moves	
		} else if(action.contains("move")) {
			//the room name can be extracted by getting 4:end of the string
			String roomName = action.substring(4);
			
			model.move(activePlayer, roomName, rooms);	
			
		//if the player ranks up with dollars	
		} else if(action.contains("rankd")) {
			
			String currency = action.substring(5);
			int amount = Integer.parseInt(currency);
			int rank = office.getRankFromDollars(amount);
			model.rankUpDollars(activePlayer, amount,rank);
			
			
		//if the player ranks up with credits
		}else if(action.contains("rankc")) { 
			
			
			String currency = action.substring(5);
			int amount = Integer.parseInt(currency);
			int rank = office.getRankFromCredits(amount);
			model.rankUpCredits(activePlayer, amount,rank);
			
			
		//if the player cancels	
		} else if(action.equals("cancel")) {
		
		//if the player takes a role	
		} else if(action.contains("take")) {
		
			//gets the role name by getting substring 4:end
			String roleName = action.substring(4);
			model.takeRole(activePlayer, roleName);
			
		//if the player ends their turn, update everything for the next player	
		} else if(action.equals("end")) {
			turn++;
			view.removeDice();
			view.setMove(false);
			view.setTake(false);
			view.setValidAction(false);
			
		}
		//update the view and the buttons for the active Player
		if(maxDay != day) {
			activePlayer = actors[turn%actors.length];
			//this will have the computer player do an action if it is their turn
			while(activePlayer.getClass() == Computer.class) {
				Computer comp = (Computer) activePlayer;
				//do an action
				boolean finish = comp.doAction(model,rooms);
				//if its over...
				//check to make sure the board needs to reset
				if(finish) {
					scenesRemaining--;
					//if there is one scene remaining, we must reset the day
					if(scenesRemaining ==1) {
						
						day++;
						if(day != maxDay) {
						
							initScenes();
							scenesRemaining = 10;
							//puts all of the actors back into the trailer
							trailer.getPresentActors().clear();
							for(int i = 0; i < actors.length;i++) {
								
								actors[i].setCurrentRoom(rooms[10]);
								rooms[10].addActor(actors[i]);
								actors[i].finishScene();
								//actors[i].setCoordsToRoom();
							}
							//view.updateView(actors, activePlayer, activeScenes,rooms);
							
							
						}
						
					}
				}
				turn++;
				activePlayer = actors[turn%actors.length];
				
				
			}
			view.updateView(actors, activePlayer, activeScenes,rooms);
			view.setButtons();
		//this would mean that the game is over, display the finishGame	
		} else {
			int score = 0;
			Actor winner = null;
			for(int i = 0; i < actors.length;i++) {
				if(actors[i].getScore() > score) {
					score = actors[i].getScore();
					winner = actors[i];
				}
			}
			
			view.finishGame(winner);
		}
		
	}

}
