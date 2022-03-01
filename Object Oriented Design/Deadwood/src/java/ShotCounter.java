
//this class represents a shot counter 
//a shot counter is specifically on a room
//this class only contains getters and setters
public class ShotCounter {
	
	private String image;
	private int xCord;
	private int yCord;
	private int height;
	private int width;

	public ShotCounter() {
		image = null;
		
		
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
			
	}

	public int getxCord() {
		return xCord;
	}

	public void setxCord(int xCord) {
		this.xCord = xCord;
	}

	public int getyCord() {
		return yCord;
	}

	public void setyCord(int yCord) {
		this.yCord = yCord;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
