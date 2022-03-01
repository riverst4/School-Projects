/**
 * Example Code for parsing XML file
 * Author: Dr. Moushumi Sharmin
 * CS 345
 * 
 * Extended to include XML files for deadwood board and cards
 */

import org.w3c.dom.Document;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParseXML {


    // building a document from the XML file
    // returns a Document object after loading the book.xml file.
    public Document getDocFromFile(String filename) throws ParserConfigurationException {
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;

            try {
                doc = db.parse(filename);
            } catch (Exception ex) {
                System.out.println("XML parse failure");
                ex.printStackTrace();
            }
            return doc;
        } // exception handling
    }
    
    
    //gets information from an xml file with a set of cards
    //this then returns an array list of those cards
    public ArrayList<Scene> initScenes(Document d) {
    	
		ArrayList <Scene> scenes = new ArrayList <Scene>();
		
    	Element root = d.getDocumentElement();
    	NodeList cards = root.getElementsByTagName("card");	
    	//parses through each scene in the file
    	for(int i = 0; i < cards.getLength();i++) {
    		
    		Scene scene = new Scene();
    		
    		//gets some basic infomation about each scene
    		Node card = cards.item(i);
    		String name = card.getAttributes().getNamedItem("name").getNodeValue();
    		int budget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());
    		String image = card.getAttributes().getNamedItem("img").getNodeValue();
    		scene.setName(name);
    		scene.setBudget(budget);
    		scene.setSceneImage("card_" + image);
    		
    		NodeList sets = card.getChildNodes();
    		

    		for(int j = 0; j < sets.getLength();j++) {
    			Node set= sets.item(j);
    			Role role = new Role();
    			//gets direct scene information
    			if("scene".equals(set.getNodeName())) {
    				String description = set.getTextContent();
    				scene.setDesription(description);
    				
    				
    				//gets part information of the scene
    			} else if("part".equals(set.getNodeName())) {
    				
    				role.setOnCard(true);// = true;
    				String roleName = set.getAttributes().getNamedItem("name").getNodeValue();
    				role.setRoleName(roleName);
    				int minRank = Integer.parseInt(set.getAttributes().getNamedItem("level").getNodeValue());
    				role.setMinRank(minRank);
    				
    				NodeList part = set.getChildNodes();
    				
    				//get information on each part
    				for(int p = 0; p < part.getLength();p++) {
    					Node partDetail = part.item(p);
    					if("line".equals(partDetail.getNodeName())) {
    						String line = partDetail.getTextContent();
    						role.setLine(line);
    						scene.addRole(role);
		
    					} else if("area".equals(partDetail.getNodeName())) {
    						//this should be for role not scene
    						String x = partDetail.getAttributes().getNamedItem("x").getNodeValue();
    	    				String y = partDetail.getAttributes().getNamedItem("y").getNodeValue();
    	    				String h = partDetail.getAttributes().getNamedItem("w").getNodeValue();
    	    				String w = partDetail.getAttributes().getNamedItem("h").getNodeValue();
    	    				role.setxCord(Integer.parseInt(x));
    	    				role.setyCord(Integer.parseInt(y));
    	    				role.setHeight(Integer.parseInt(h));
    	    				role.setWidth(Integer.parseInt(w));
    	    				
    					}
    					
    				}
    				
    			}

    		} //end of j loop (40)
    		
    		scenes.add(scene);
    	}
    	
    	
    	return scenes;
    }//end of method
    
    
    //gets information from an xml file containing rooms
    public Room[] getRoomData(Document d) {
    	
    	ArrayList <Room> rooms = new ArrayList <Room>();
    	Room[] roomArray = new Room[10];
    	Element root = d.getDocumentElement();
    	
    	NodeList sets = root.getElementsByTagName("set");
    	//parses through every room
    	for(int i = 0; i < sets.getLength();i++) {
    		Room room = new Room();
    		Node set = sets.item(i);
    		
    		String roomName = set.getAttributes().getNamedItem("name").getNodeValue();
    		room.setName(roomName);
    		NodeList roomInfo = set.getChildNodes();
    		//gets all of the information in a room
    		for(int j = 0; j < roomInfo.getLength();j++) {
    			
    			
    			Node info = roomInfo.item(j);
    			
    			if("neighbors".equals(info.getNodeName())) {
    				
    				NodeList neighbors = info.getChildNodes();
    				//gets neighbor room data
    				for(int n = 0; n < neighbors.getLength();n++) {
    					String neighborRoom;
    					Node neighbor = neighbors.item(n);
    					//gets neighbor names
    					if(neighbor.hasAttributes()) {
    						neighborRoom = neighbor.getAttributes().getNamedItem("name").getNodeValue();
    						
    						room.addNeighbor(neighborRoom);
    						
    						
    					}
    					
    				}
    				
    				//gets takes data
    			} else if("area".equals(info.getNodeName())) {
    				String xCord = info.getAttributes().getNamedItem("x").getNodeValue();
    				String yCord = info.getAttributes().getNamedItem("y").getNodeValue();
    				String height = info.getAttributes().getNamedItem("w").getNodeValue();
    				String width = info.getAttributes().getNamedItem("h").getNodeValue();
    				room.setxCord(Integer.parseInt(xCord));
    				room.setyCord(Integer.parseInt(yCord));
    				room.setHeight(Integer.parseInt(height));
    				room.setWidth(Integer.parseInt(width));
    				
    					
    			} else if("takes".equals(info.getNodeName())) {
    				
    				NodeList shotInfo = info.getChildNodes();
    				//get shot information
    				for(int t = 0; t < shotInfo.getLength();t++) {
    					
    					Node shot = shotInfo.item(t);
    					//gets shot number
    					if("take".equals(shot.getNodeName())) {
    						room.incShot();
    						room.setHasScene(false);
    					} 		
    					if(shot.hasAttributes()) {
    						NodeList areaOfShots = shot.getChildNodes();
    						//will get area for shots
    						for(int a = 0; a < areaOfShots.getLength();a++) {
    							Node area = areaOfShots.item(a);
    							ShotCounter shotCounter = new ShotCounter();
    							
    							if("area".equals(area.getNodeName())) {
    								String xCord = area.getAttributes().getNamedItem("x").getNodeValue();
 /*noted please note this*/   		String yCord = area.getAttributes().getNamedItem("y").getNodeValue();
    			    				String height = area.getAttributes().getNamedItem("w").getNodeValue();
    			    				String width = area.getAttributes().getNamedItem("h").getNodeValue();
    			    				shotCounter.setxCord(Integer.parseInt(xCord));
    			    				shotCounter.setyCord(Integer.parseInt(yCord));
    			    				shotCounter.setHeight(Integer.parseInt(height));
    			    				shotCounter.setWidth(Integer.parseInt(width));
    							}
    							room.addShot(shotCounter);
    							
    							
    						}
    						
    					}
    						
    				}
    				//gets parts data from the room
    			} else if("parts".equals(info.getNodeName())) {
    				
    				
    				NodeList roleInfo = info.getChildNodes();
    				//gets every role of the rooms
    				for(int r = 0; r < roleInfo.getLength();r++) {
    					Role role = new Role();
    					Node roleNode = roleInfo.item(r);
    					if(roleNode.hasAttributes()) {
    						//role name
    						String name = roleNode.getAttributes().getNamedItem("name").getNodeValue();
    						role.setRoleName(name);
    						//minimum level of role
    						int level = Integer.parseInt(roleNode.getAttributes().getNamedItem("level").getNodeValue());
    						role.setMinRank(level);
    						
    						NodeList roleChildren = roleNode.getChildNodes();
    						for(int c = 0; c < roleChildren.getLength();c++) {
    							
    							Node roleChild = roleChildren.item(c);
    							//this will get the area attributes
    							if(roleChild.hasAttributes()) {
    								//get area attributes
    								String x = roleChild.getAttributes().getNamedItem("x").getNodeValue();
    	    	    				String y = roleChild.getAttributes().getNamedItem("y").getNodeValue();
    	    	    				String h = roleChild.getAttributes().getNamedItem("w").getNodeValue();
    	    	    				String w = roleChild.getAttributes().getNamedItem("h").getNodeValue();
    	    	    				role.setxCord(Integer.parseInt(x));
    	    	    				role.setyCord(Integer.parseInt(y));
    	    	    				role.setHeight(Integer.parseInt(h));
    	    	    				role.setWidth(Integer.parseInt(w));
    									
    								
    							} else {
    								//get line information and adds other attributes to the role
    								if("line".equals(roleChild.getNodeName())) {
    									String line = roleChild.getTextContent();
    									
    									role.setLine(line);
    									role.setOnCard(false);
    		    						room.addRole(role);
    								}
    									
    							}
    						} //end of c loop
    						
    					
    					} //end of attributes
    					
    				}//end of r loop
    			} //neighbors,parts, takes
    			
    		}//end of j loop
    		roomArray[i] = room;
    		rooms.add(room);
    		
    	}//end of i loop
    	
    	//return (Room[]) rooms.toArray();
    	return roomArray;
    	
    }
    //reads information about the office data. It will only get names of the neighbors and 
    //will get the ranks and their currencies
    public CastingOffice readOfficeData(Document d) {
    	
		CastingOffice office = new CastingOffice();
    	
    	int[][] a = new int[5][3];
    	office.setName("office");
    	
    	Element root = d.getDocumentElement();
    	
    	NodeList officeNode = root.getElementsByTagName("office");
    	
    	Node children = officeNode.item(0);
    	
    	NodeList officeChildren  = children.getChildNodes();
    	//get all of the office info
    	for(int i = 0; i < officeChildren.getLength();i++) {
    		Node info = officeChildren.item(i);
    		//gets the neighbors of the casting office
    		if("neighbors".equals(info.getNodeName())) {
    			NodeList neighbor = info.getChildNodes();
    			
    				//gets each neighbor of casting office
    				for(int j = 0; j < neighbor.getLength();j++) {
    					Node neigh = neighbor.item(j);
    					if(neigh.hasAttributes()) {
    						
    						if("neighbor".equals(neigh.getNodeName())) {
    						
    							//add the neighbor to the room
    							String name = neigh.getAttributes().getNamedItem("name").getNodeValue();
    							office.addNeighbor(name);
    						}
    					}

    				}
    			
    		//represents area for a GUI, not needed for A4
    		} else if("area".equals(info.getNodeName())) {
				String x = info.getAttributes().getNamedItem("x").getNodeValue();
				String y = info.getAttributes().getNamedItem("y").getNodeValue();
				String h = info.getAttributes().getNamedItem("w").getNodeValue();
				String w = info.getAttributes().getNamedItem("h").getNodeValue();
				office.setxCord(Integer.parseInt(x));
				office.setyCord(Integer.parseInt(y));
				
    		//this represents each upgrade for players	
    		} else if("upgrades".equals(info.getNodeName())) {
    			
    			NodeList upgrade = info.getChildNodes();
    			//gets upgrade information
    			for(int u = 0; u < upgrade.getLength();u++) {
    				
    				Node upgradeInfo = upgrade.item(u);
    				if("upgrade".equals(upgradeInfo.getNodeName())) {
    					
    					//represents the rank
    					String level = upgradeInfo.getAttributes().getNamedItem("level").getNodeValue();
    					int l = Integer.parseInt(level);

    					//represents currency
    					String currency = upgradeInfo.getAttributes().getNamedItem("currency").getNodeValue();
    					//amount of currency
    					String amount = upgradeInfo.getAttributes().getNamedItem("amt").getNodeValue();
    					int amt = Integer.parseInt(amount);
    				
    					
    				 a[l-2][0]= l;
    				 //if the currency is dollars
    		         if(currency.equals("dollar")) {
    		        	 
    		        	a[l-2][1] = amt;
    		        	//if the currency is credits
                    } else {
                    	a[l-2][2] = amt;
                    	
                    }

    				}
    				
    			}
    		}
    	}
		office.setRanks(a);
		return office;
    }
    //this method gets information from a document that has trailer
    public Room readTrailer(Document d) {
    	Room trailerInfo = new Room();
    	trailerInfo.setName("Trailer");
    	Element root = d.getDocumentElement();
    	
    	NodeList trailer = root.getElementsByTagName("trailer");
    	
    	Node trailerChildren = trailer.item(0);
    	
    	NodeList trail = trailerChildren.getChildNodes();
    	
    	for(int i = 0; i < trail.getLength();i++) {
    		Node children = trail.item(i);
    		
    		
    		//gets the neighbors
    		if("neighbors".equals(children.getNodeName())) {
    			NodeList neighbor = children.getChildNodes();
    			
    				for(int j = 0; j < neighbor.getLength();j++) {
    					Node neigh = neighbor.item(j);
    					//get neighbor name and add it to the room
    					if("neighbor".equals(neigh.getNodeName())) {
    						String name = neigh.getAttributes().getNamedItem("name").getNodeValue();
    						trailerInfo.addNeighbor(name);
    					}
    				}
    		} else if("area".equals(children.getNodeName())) {
    			String xCord = children.getAttributes().getNamedItem("x").getNodeValue();
				String yCord = children.getAttributes().getNamedItem("y").getNodeValue();
				String height = children.getAttributes().getNamedItem("w").getNodeValue();
				String width = children.getAttributes().getNamedItem("h").getNodeValue();
				
				trailerInfo.setxCord(Integer.parseInt(xCord));
				trailerInfo.setyCord(Integer.parseInt(yCord));
				trailerInfo.setHeight(Integer.parseInt(height));
				trailerInfo.setWidth(Integer.parseInt(width));
				
    			
    		}
    	}
    	return trailerInfo;
    	
    	
    }

}//class
