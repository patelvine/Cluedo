import java.awt.Graphics;
import java.awt.Point;
import javax.swing.*;

import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.*;
import java.io.*;
import java.util.Scanner;

public class GUI {
	private GameOfCludo game;
	private int numPlayers; // stores number of players in game
	private Player player; // stores the player for the current turn
	private Random dice; 
	private int roll; // stores the dice roll
	private int turn = 0; // stores current the turn (always less then roll and greater than -1)

	/** fields needed to set up the GUI **/
	private static JFrame frame;
	private int windowSize = 400;
	private JTextField textFelid;
	private JMenuBar menuBar;
	private static JPanel playerDisplay; // panel to show player cards, dice and players name
	private static JPanel outerMostPanel; // panel used for the board
	private JLabel[][] grid; // variable to store initial board

	/** Grid Images **/
	private ImageIcon S_blank = makeImageIcon("S_blank.png"); // wall icon
	private ImageIcon S_normalSquare = makeImageIcon("S_normalSquare.png"); // grid icon
	private ImageIcon S_roomSquare = makeImageIcon("S_roomSquare.png"); // room icon
	private ImageIcon S_shortCut = makeImageIcon("S_shortCut.png"); // shortcut icon
	private ImageIcon S_door = makeImageIcon("S_door.png"); // door icon

	public GUI() {
		game = new GameOfCludo();
		// sets up interface
		setupInterface();
		// sets up the grid (board)
		setGrid(game);
		// ask for number of players playing
		numPlayers = numPlayers();
		// gets the names of each player
		ArrayList<String> playerNames = playerNameDialog(numPlayers);
		// creates and sets all players along with their character name and more information
		game.getBoard().setPlayers(choosePlayerCharacters(playerNames));
		// creates secret envelope
		game.setSecertEnvelope(game);
		// deals out the cards
		game.getBoard().setPlayers(
				game.dealCards(game, game.getBoard().getPlayers()));
		// sets initial values (for first persons turn)
		initialize();
		redraw();
	}

	private void setupInterface() {
		frame = new JFrame("Cludo"); // creates the main frame that will be used
		frame.setSize(windowSize, windowSize + 50);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);

		grid = new JLabel[29][24];  // sets initial grid dimension
		outerMostPanel = new JPanel(new GridLayout(29, 24)); // sets the panel where the board will be drawn
		frame.getContentPane().add(outerMostPanel, BorderLayout.CENTER); // adds the panel to the frame
 
		// creates and sets all menu bar options then adds it to the frame
		menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenu menuHelp = new JMenu("Help");
		JMenuItem menuRules = new JMenuItem("Rules");
		JMenuItem menuCards = new JMenuItem("Cards");
		menuBar.add(menuFile);
		menuBar.add(menuHelp);
		menuHelp.add(menuRules);
		menuHelp.add(menuCards);
		frame.setJMenuBar(menuBar);

		// creates panel where all buttons will be places and then creates all buttons
		JPanel panel = new JPanel(new GridLayout(2, 3));
		frame.add(panel, BorderLayout.NORTH);

		JButton button = new JButton("Next Turn");
		panel.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				nextTurn(); // next turn method(this method ends the current players turn and starts next players turn)
				redraw();
			}
		});

		button = new JButton("Suggestion");
		panel.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (player.getLocation().getName() == null) { // if the player isnt in a room then he/she cannot suggest
					JOptionPane.showMessageDialog(frame, "Must be in a room");
				} else {
					suggestion(); // calls suggestion method then ends players turn
					nextTurn();
				}
				redraw();
			}
		});

		button = new JButton("Accusation");
		panel.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (player.getLocation().getName() == null
						|| !player.getLocation().getName()
								.equals("Swimming Pool")) { // if current player is not in swimming pool room he/she cannot accuse
					JOptionPane.showMessageDialog(frame,
							"Must be in swimming pool room");
				} else {
					accusation(); // calls accusation method then ends players turn
					nextTurn();
				}
				redraw();
			}
		});

		button = new JButton("Short cut");
		panel.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Location l = game.shortCut(player.getLocation().getName()); // gets location of opposite corner room
				if (l == null) { // if player was not in corner room then the location above should be null. therefore player cannot use short cut
					JOptionPane.showMessageDialog(frame,
							"Must be in a corner room");
				} else if (player.getMovement() == false) { // if the player cannot move then he/she cannot use shortcut
					JOptionPane.showMessageDialog(frame,
							"Your Movement has ended");
				} else {
					player.getLocation().setLocation(l); // sets player to opposite corner room
					player.setMovement(false); // set player movement to false(cannot move in current turn now)
				}
				redraw();
			}
		});

		/** mouse listener used to move player around the board **/
		outerMostPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (player.getMovement()) { // if the player is still able to move then move the player, else reject movement
					move(e.getPoint());
				} else {
					JOptionPane.showMessageDialog(frame, 
							"Your Movement has ended");
				}
			}
		});

		/** this method listens for window close press **/
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				int i = yesNoDialog("Are you sure?", "Exit"); // makes sure player really wants to exit the game
				if (i == 1) {
					frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // if the player chooses yes then close the window
				} else if (i == 0) {
					frame.dispose(); // if the player chooses no then do nothing to the game
				} else {
					frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		frame.pack();
		frame.setVisible(true);
	}

	/** method to set all initial values **/
	private void initialize() {
		this.dice = new Random();
		this.roll = dice.nextInt(6) + 1;
		this.player = game.getBoard().getPlayers().get(turn);

		playerDisplay = new JPanel(new GridLayout(5, 4));
		frame.add(playerDisplay, BorderLayout.EAST);

		for (Card c : player.getCards()) {
			playerDisplay.add(new JLabel(makeImageIcon("C_" + c.getName()
					+ ".png"))); // shows each players card in players display
		}
		playerDisplay.add(new JLabel(makeImageIcon("dice" + roll + ".png"))); // shows dice roll in player display
		playerDisplay.add(new JTextField("Current players turn: "
				+ player.getName())); // shows players name in player display
		frame.pack();
	}

	/** method to move a player to a location **/
	public void move(Point p) {
		// finds the location the mouse was click on the board and also the difference between the player and the click
		int x = (int) p.x / 15; // click x pos on board
		int y = (int) p.y / 15; // click y pos on board
		int xDiff = Math.abs(player.getLocation().getX() - x);
		int yDiff = Math.abs(player.getLocation().getY() - y);
		int totalDiff = xDiff + yDiff; // total differnce between player and the click

		player.getLocation().setName(null); // set the player location name(current room their in) to null
		String clickedSpot = game.getBoard().getMap()[y][x]; // gets the value of the location click from the original board(the array in board class)

		if (player.getLocation().getName() != null) { // if player is already in a room he/she cannot move
			JOptionPane.showMessageDialog(frame,
					"You have entered a room you cannot move");
		} else if (clickedSpot == "W" || clickedSpot == "R"
				|| clickedSpot == "X") {
			JOptionPane.showMessageDialog(frame,
					"Can only move onto a path or door");
		} else if (totalDiff > roll) { // if player does not have enough moves due to dice roll then he/she cannot move
			JOptionPane.showMessageDialog(frame, "Cannot move that far");
		} else if (clickedSpot == "S" || clickedSpot == "P" // if player has click on the door find which door it is
				|| clickedSpot == "K" || clickedSpot == "T"
				|| clickedSpot == "V" || clickedSpot == "D"
				|| clickedSpot == "L" || clickedSpot == "O"
				|| clickedSpot == "H" || clickedSpot == "G") {
			String room = game.curRoom(clickedSpot); // get the rooms name by using the method in gameOfCludo class
			player.getLocation().setLocation(x, y); // set the now location
			player.getLocation().setName(room); // set player to be in the room
			player.setMovement(false); // set movement to false (movement ends when player enters a room)
		} else { // if player can move and click spot is on the grid outside of all rooms
			for (Player pl : game.getBoard().getPlayers()) {
				if (pl.getLocation().getX() == x
						&& pl.getLocation().getY() == y && pl != player) { // if there is a player in clicked location then player cannot move there
					JOptionPane.showMessageDialog(frame,
							"Square holds a player, cannot move");
					return;
				}
			}
			player.getLocation().setLocation(x, y); // moves the player by setting location
			roll = roll - totalDiff; // deducts from the roll (amount of moves allowed)
		}
		redraw();
	}

	/** method to start the next turn **/
	public void nextTurn() {
		turn++;
		if (turn >= game.getBoard().getPlayers().size()) { // is the last person has just had his/her turn then it starts again (creates loop)
			turn = 0;
		}
		
		this.roll = dice.nextInt(6) + 1; // re roll the dice
		player = game.getBoard().getPlayers().get(turn); // change player to next player
		player.setMovement(true); // set movement to true (player can move)

		frame.remove(playerDisplay); //remove player display from the frame (to reset it)
		playerDisplay = new JPanel(new GridLayout(3, 2)); // create new player display
		frame.add(playerDisplay, BorderLayout.EAST); // add it to frame

		for (Card c : player.getCards()) {
			playerDisplay.add(new JLabel(makeImageIcon("C_" + c.getName() // add all players cards to player display
					+ ".png")));
		}
		playerDisplay.add(new JLabel(makeImageIcon("dice" + roll + ".png"))); // add the dice to player display
		playerDisplay.add(new JTextField("Current players turn:\n"
				+ player.getName())); // add players name to player display
		frame.pack();
	}

	public void redraw() {
		outerMostPanel.removeAll(); // removes all labels from board
		setGrid(game); // resets the grid to its original state
		for (Player p : game.getBoard().getPlayers()) { 
			Location l = p.getLocation();
			grid[l.getY()][l.getX()] = new JLabel(p.getIcon()); // adds players to the grid using their locations
		}

		for (int i = 0; i < 29; i++) {
			for (int u = 0; u < 24; u++) {
				outerMostPanel.add(grid[i][u]);// adds all labels to the panel for the board
			}
		}
		frame.pack();
	}

	/** creates the original grid **/
	private void setGrid(GameOfCludo game) {
		String[][] map = game.getBoard().getMap();
		for (int i = 0; i < 29; i++) {
			for (int u = 0; u < 24; u++) {
				if (map[i][u] == " ") {
					grid[i][u] = new JLabel(S_normalSquare); // creates label for the outside grid
				} else if (map[i][u] == "W") {
					grid[i][u] = new JLabel(S_blank); // creates label for walls
				} else if (map[i][u] == "R") {
					grid[i][u] = new JLabel(S_roomSquare); // creates label for the insides of the rooms
				} else if (map[i][u] == "S" || map[i][u] == "P"
						|| map[i][u] == "K" || map[i][u] == "O"
						|| map[i][u] == "H" || map[i][u] == "G"
						|| map[i][u] == "L" || map[i][u] == "T"
						|| map[i][u] == "V" || map[i][u] == "D") {
					grid[i][u] = new JLabel(S_door); // creates panels for each door
				} else if (map[i][u] == "X") {
					grid[i][u] = new JLabel(S_shortCut); // creates panel for each short cut
				} else {
					grid[i][u] = new JLabel(S_normalSquare); 
				}
			}
		}
	}

	/** method used for when a player wants to make an accusation **/
	public void accusation() {
		String[] weaps = { "Rope", "CandleStick", "Knife", "Pistol",
				"Baseball Bat", "Dumbbell", "Trophy", "Posion", "Axe" };
		String w = dropDownDialog("Accusation: Weapon", // asks used to select a weapon
				"Select Accused Weapon", weaps);

		String[] person = { "Kasandra Scarlett", "Jack Mustard", "Diane White",
				"Jacob Green", "Eleanor Peacock", "Victor Plum" };
		String p = dropDownDialog("Accusation: Murderer", // asks user to select a person
				"Select Accused Murderer", person); 

		String[] room = { "Spa", "Patio", "Kitchen", "Theatre", "Living Room",
				"Observatory", "Hall", "Guest House", "Dinning Room",
				"Swimming Pool" };
		String r = dropDownDialog("Accusation: Room", "Select Accused Room", // asks user to select a room
				room);
		// checks if a player contains any of the choosen cards
		for (Player cur : game.getBoard().getPlayers()) {
			for (Card c : cur.getCards()) {
				if (c.getName().equals(w) || c.getName().equals(p)
						|| c.getName().equals(r)) { // if a player contains a card then player is eliminated
					JOptionPane.showMessageDialog(frame,
							"Choose a wrong card\n" + player.getName()
									+ " is ELIMINATED");
					// hands out the eliminated players cards to all remaining players
					while (player.getCards().size() != 0) {
						for (Player pl : game.getBoard().getPlayers()) {
							if (player.getCards().size() != 0) {
								pl.addCard(player.getCards().get(0));
								player.getCards().remove(0);
							}
						}
					}
					game.getBoard().removePlayer(player); // removes the eliminated player
					
					// if there is only one player left in the game then he/she wins
					if (game.getBoard().getPlayers().size() == 1) {
						JOptionPane.showMessageDialog(frame, game.getBoard()
								.getPlayers().get(0).getName()
								+ " WINS!!");
						System.exit(1);
					}
					return;
				}
			}
		}
		JOptionPane.showMessageDialog(frame, "CORRCT!!! " + player.getName()
				+ " wins!!!");
		System.exit(1);
	}

	/** method used to make a suggestion **/
	public void suggestion() {
		String[] weaps = { "Rope", "CandleStick", "Knife", "Pistol",
				"Baseball Bat", "Dumbbell", "Trophy", "Posion", "Axe" };
		String w = dropDownDialog("Suggestion: Weapon", // asks used to select a weapon
				"Select Suggested Weapon", weaps);

		String[] person = { "Kasandra Scarlett", "Jack Mustard", "Diane White",
				"Jacob Green", "Eleanor Peacock", "Victor Plum" };
		String p = dropDownDialog("Suggestion: Murderer", // asks used to select a person
				"Select Suggested Murderer", person);

		// checks if any player in the game contains a selected card
		for (Player cur : game.getBoard().getPlayers()) {
			for (Card c : cur.getCards()) {
				if (c.getName().equals(w)) { // if a player contains the weapon card say so
					JOptionPane.showMessageDialog(frame, cur.getName()
							+ " contains " + w + " card");
					return;
				}
				if (c.getName().equals(p)) { // if a player contains the person card say so
					JOptionPane.showMessageDialog(frame, cur.getName()
							+ " contains " + p + " card");
					return;
				}
				if (c.getName().equals(player.getLocation().getName())) { // if a player contains the room card say so
					JOptionPane.showMessageDialog(frame, cur.getName()
							+ " contains " + player.getLocation().getName()
							+ " card");
					return;
				}
			}
		}
		JOptionPane
				.showMessageDialog(frame, "No one contained suggested cards");
	}

	/** method used to select a character for each player by using JRadio button **/
	public static ArrayList<Player> choosePlayerCharacters(
			ArrayList<String> playerNames) {
		String[] s = { "Kasandra Scarlett", "Jack Mustard", "Diane White",
				"Jacob Green", "Eleanor Peacock", "Victor Plum" }; // list of characters
		JRadioButton1 r = new JRadioButton1(s); // creates new radio button
		r.createAndShowGUI(playerNames, s); // calls method of radio button to set it up
		return r.charSelection(); // calls charSelection method of radio button to choose characters
	}
 
	/** method used to select number of players **/
	public int numPlayers() {
		String message = "Choose amount of players between 3-6"; // message for the player
		String title = "Number of players"; // dialog title
		int n = 0;
		while (3 > n || n > 6) { // while player has not enters a number between 3-6 keep going
			n = inputIntDialog(message, title);
		}
		return n; // return the number
	}

	/** method for a drop down box **/
	public String dropDownDialog(String title, String message, String[] list) {
		String s = null;
		s = (String) JOptionPane.showInputDialog(frame, message, "title",
				JOptionPane.PLAIN_MESSAGE, null, list, null); // create a drop down box contains the list of string provided

		if ((s != null) && (s.length() > 0)) {
			return s; // if the player choose an option in the drop down box then return it
		} else {
			return dropDownDialog(title, message, list); // if the player did no choose and option then re ask
		}
	}
	
	/** method used to ask for each of the players names **/
	public ArrayList<String> playerNameDialog(int numPlayers) {
		int n = 1; // current player choosing their name
		ArrayList<String> names = new ArrayList<String>();
		while (n <= numPlayers) { // while a player has a name to choose
			String message = "Player " + n + " enter your name:\n";
			String title = "Name Setup";
			String name = inputStringDialog(message, title); // call dialog where player will type in his/her name
			if (name != null) { // if the name is not null then add it
				names.add(name);
				n++;
			} else { // is the player did not enter a name then re ask
				n--;
			}
		}
		return names; // return the list of names
	}

	/** method to ask for an integer with a dialog **/
	public int inputIntDialog(String message, String title) {
		String num = (String) JOptionPane.showInputDialog(frame, message,
				title, JOptionPane.PLAIN_MESSAGE, null, null, null);
		try {
			return Integer.parseInt(num);
		} catch (Exception e) {
			return inputIntDialog(message, title);
		}
	}

	/** method to ask for a string with a dialog **/
	public String inputStringDialog(String message, String title) {
		String name = (String) JOptionPane.showInputDialog(frame, message,
				title, JOptionPane.PLAIN_MESSAGE, null, null, null);
		return name;
	}

	/** method to ask or a yes no option using a dialog **/
	public int yesNoDialog(String message, String message2) {
		int i = JOptionPane.showConfirmDialog(frame, message, message2,
				JOptionPane.YES_NO_OPTION);
		return i;
	}

	/** method to make a new image icon containing a choosen image **/
	private static ImageIcon makeImageIcon(String filename) {
		java.net.URL imageURL = GUI.class.getResource(filename);
		ImageIcon icon = null;
		if (imageURL != null) {
			icon = new ImageIcon(imageURL);
		}
		return icon;
	}

	public static void main(String args[]) {
		GUI obj = new GUI();
	}
}
