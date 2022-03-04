Welcome to our implementation of DeadWood<br/>
Link to rulebook: https://cheapass.com//wp-content/uploads/2016/07/Deadwood-Free-Edition-Rules.pdf<br/>

Please clone this repository to a local IDE <br/>


**important**
Our version has a bot implementation for both the console and the GUI<br/>
For the Console, you can have all of the players be bots<br/>
For the GUI, you must have at least one human player<br/>


You must have 5 arguments, <br/>
    (1) type '0' or '1' for this argument, 0 represents the console version, 1 repesents the GUI version<br/>
    (2) path to board xml file <br/>
    (3) path to cards xml file <br/>
    (4) number of players (must be 2-8 players)<br/>
    (5) number of bots to play with (this argument can be omitted if you want to play with no bots)<br/>


When playing your turn, valid commands will be displayed to you:<br/>
these commands will display in the form of buttons<br/>
A player must end their turn even after using their valid action. <br/>


**commands** (for the Console)
'move' to move to a neighbor room<br/>
'take role' to take a role
'act' to act your scene<br/>
'rehearse' to rehearse your scene<br/>
'rank' to rank up<br/>
'end' to end your turn.<br/>


**arguments**
first: (int) '0' or '1' representing the game type
second: (xml file) represents source for board<br/>
third: (xml file) represents source for card<br/>
fourth: (int) represents number of players, must be 2-8 players<br/>
fifth: (int) number of bot players 

**Troubleshooting**

If you receive a must override a superclass method error: <br/>
(for eclipse)<br/>
1) Select Project, Right-click, Properties<br/>
2) Select Java Compiler and check the checkbox "Enable project specific settings"<br/>
3) Now make Compiler compliance level to 1.6<br/>
4) Apply changes<br/>

**Bot Implementation** <br/>
The bots do not do a random move, and they by no means have the game solved. <br/>
The bots prioritize making the game faster, and taking less attempts at a scene.<br/>
If a bot is in a role, they will rehearse until they have guaranteed sucess by rolling a two <br/>

If a bot is not in a role, they will go through the rooms, and see if the room is casting office, if it is <br/>
and they are at rank 1, they will go there and rank up to rank 2. This is so the game can be finished if the <br/>
human player decides to keep ending their turn <br/>
Each time a bot goes to the casting office, they will try and rank up once. <br/>

The bots are a very recent implementation, if you run into problems contact me via text 206-747-1369 <br/>
And I will try to solve the problem quickly, or revert the repository to a previous one <br/>





