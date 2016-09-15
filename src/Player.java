import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Player {

	private ArrayList<Card> cards; // players hand
	private String name; // players real name
	private String character; // players character 
	private Location location; // current location player is on the board
	private ImageIcon icon; // number the players should be shown on the board
	private boolean movement; // variable used to check if play can move around

	public Player(String name, String character, Location location, ArrayList<Card> cards, ImageIcon icon) {
		this.name = name;
		this.character = character;
		this.location = location;
		this.cards = cards;
		this.icon = icon;
		this.movement = true;
	}
	
	public boolean getMovement(){
		return this.movement;
	}
	
	public void setMovement(boolean m){
		this.movement = m;
	}
	
	public ImageIcon getIcon(){
		return this.icon;
	}
	
	public Location getLocation() {
		return this.location;
	}

	public ArrayList<Card> getCards() {
		return this.cards;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getChar(){
		return this.character;
	}

	public void addCard(Card c) {
		this.cards.add(c);
	}
}
