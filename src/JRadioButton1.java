import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class JRadioButton1 extends JPanel implements ActionListener {
	private static ArrayList<String> playerNames; // names of players in the game
	private static ArrayList<Player> temp = new ArrayList<Player>(); // holds created players

	private static JTextField textField; // used to print out players name who will be choosing
	private static JPanel radioPanel; // panel for radio buttons
	private static ButtonGroup group; // group all the buttons will be in
	private static JFrame frame;
	private static JComponent newContentPane;
	private static JRadioButton rb; // variable used to create and add all radio buttons

	public JRadioButton1(String[] list) {
		super(new BorderLayout());
		textField = new JTextField("", 30);
		add(textField, BorderLayout.NORTH); 
		group = new ButtonGroup(); 
		radioPanel = new JPanel(new GridLayout(0, 1));
		// creates then adds all radio buttons to panel with each name in the list
		for (String s : list) {
			rb = new JRadioButton(s); // sets rb to a new radio button
			rb.setActionCommand(s); // sets action command to the current name
			group.add(rb); // adds the new button to the group
			rb.addActionListener(this); // places action listener on button
			rb.setName(s); //sets buttons name
			radioPanel.add(rb); // adds button to panel
		}
		add(radioPanel, BorderLayout.LINE_START); // adds radio panel
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
	}

	/** creates a new radio button **/
	public void createAndShowGUI(ArrayList<String> playerNames,
			String[] list) {
		JRadioButton1.playerNames = playerNames; // sets playerNames (used to print out current player thats choosing)
		frame = new JFrame("Character Selection");
		newContentPane = new JRadioButton1(list);
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		frame.pack();
		frame.setVisible(true);
	}

	/** method use to select each character for each player **/
	public ArrayList<Player> charSelection() {
		textField.setText(playerNames.get(0) + " select a character");
		while (!playerNames.isEmpty()) { // while a player still have to choose
			textField.setText(playerNames.get(0) + " select a character"); // message to tell them to choose
		}
		frame.setVisible(false);
		return temp;
	}
 
	/** method which detects if a radio button was pressed **/
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand(); // retrieve the buttons name (character name)
		for (Component c : radioPanel.getComponents()) { // find the button in the panel
			if (c.getName() == s) {
				c.setEnabled(false); // disable the button
			}
		}
		temp.add(new Player(playerNames.get(0), s, getStartingLocation(s),
				new ArrayList<Card>(), getIcon(s))); // create a new player using the players name, characters name and location
		playerNames.remove(0); // remove players name from list as he/she has choosen
	}

	/** method which returns location for each character **/
	private Location getStartingLocation(String charac) {
		if (charac == "Kasandra Scarlett") {
			return new Location(18, 28,null); // location of the starting point for Kasandra Scarlett
		} else if (charac == "Jack Mustard") {
			return new Location(7, 28,null);
		} else if (charac == "Diane White") {
			return new Location(0, 19,null);
		} else if (charac == "Jacob Green") {
			return new Location(0, 9,null);
		} else if (charac == "Eleanor Peacock") {
			return new Location(6, 0,null);
		} else if (charac == "Victor Plum") {
			return new Location(20, 0,null);
		} else {
			return null;
		}
	}

	/** returns ImageIcon(token) for each character **/
	private ImageIcon getIcon(String charac) {
		if (charac == "Kasandra Scarlett") {
			return new ImageIcon(GUI.class.getResource("S_red.png")); // creates and returns a ImageIcon(token) for Kasandra Scarlett
		} else if (charac == "Jack Mustard") {
			return new ImageIcon(GUI.class.getResource("S_custard.png"));
		} else if (charac == "Diane White") {
			return new ImageIcon(GUI.class.getResource("S_white.png"));
		} else if (charac == "Jacob Green") {
			return new ImageIcon(GUI.class.getResource("S_green.png"));
		} else if (charac == "Eleanor Peacock") {
			return new ImageIcon(GUI.class.getResource("S_blue.png"));
		} else{
			return new ImageIcon(GUI.class.getResource("S_purple.png"));
		}
	}
}
