import java.util.*;
import java.io.*;


/*this class represents a scene in the deadwood game
 * a scene is represented by a card and is located inside of a room
 * a scene has multiple roles on it which actors can take
 *this class only contains getters and setters
 */
public class Scene {
	
	public ArrayList <Role> roles= new ArrayList <Role>();
	private int shotCount;
	private String description;
	private String name;
	private int budget;
	private String img = "src/main/resources/img/cardback.png";
	private String sceneImage;
	private int xCord;
	private int yCord;
	private int height;
	private int width;
	//int number; wasn't needed for our implementation, keeping just in case
	
	
	public Scene() {
			
		
	}
	
	
	
	public int getShot() {
		return shotCount;
	}
	
	public String getImage() {
		return img;
	}
	
	public int getBudget() {
		return budget;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ArrayList<Role> getRoles() {
		return roles;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setDesription(String line) {
		this.description = line;
	}
	
	public void setBudget(int budget) {
		this.budget = budget;
	}
	
	public void setSceneImage(String image) {
		String path = "src/main/resources/img/";
		
		sceneImage = path+image;
	}
	//this will flip the photo once a player has entered the room where this scene resides
	public void flipPhoto() {
		if(img.equals("src/main/resources/img/cardback.png")) {
			img = sceneImage;
		}
	}
	
	public void setImage(String image) {
		img = image;
	}
	
	
	
	//adds an onCard role to the scene
	public void addRole(Role role) {
		roles.add(role);
	}



	public int getWidth() {
		return width;
	}



	public void setWidth(int width) {
		this.width = width;
	}



	public int getHeight() {
		return height;
	}



	public void setHeight(int height) {
		this.height = height;
	}



	public int getyCord() {
		return yCord;
	}



	public void setyCord(int yCord) {
		this.yCord = yCord;
	}



	public int getxCord() {
		return xCord;
	}



	public void setxCord(int xCord) {
		this.xCord = xCord;
	}

}
