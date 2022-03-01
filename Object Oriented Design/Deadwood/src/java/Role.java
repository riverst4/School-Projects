/*this represents a role, a role is either on a card or
 *off of a card. a player can only have one role
 *this class only contains getters and setters
 */
public class Role {
	private boolean roleTaken;
	private String roleName;
	private int minRank;
	private String line;
	private boolean onCard;
	private int xCord;
	private int yCord;
	private int height;
	private int width;
	
	public Role(String roleName, int minRank, String line, boolean onCard) {
		this.roleName = roleName;
		this.minRank = minRank;
		this.line = line;
		this.onCard = onCard;
	}
	
	public Role() {
		
	}
	//getters
	public boolean getRoleTaken() {
		return roleTaken;
	}
	
	public int getMinRank() {
		return minRank;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public boolean getOnCard() {
		return onCard;
	}
	
	public String getLine() {
		return line;
	}
	
	//setters
	public void setRoleTaken(boolean bool) {
		roleTaken = bool;
		
	}
	
	public void setLine(String line) {
		this.line = line;
	}
	
	public void setMinRank(int number) {
		minRank = number;
	}
	
	public void setRoleName(String name) {
		roleName = name;
	}
	
	public void setOnCard(boolean bool) {
		onCard = bool;
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
