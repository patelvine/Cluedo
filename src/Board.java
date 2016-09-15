import java.util.ArrayList;


public class Board {
	private ArrayList<Card> cards = new ArrayList<Card>();
	private ArrayList<Card> secretEnvelope = new ArrayList<Card>();
	private ArrayList<Room> rooms = new ArrayList<Room>();
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	private ArrayList<Person> characters = new ArrayList<Person>();
	private ArrayList<Player> players = new ArrayList<Player>();
	private String [][] map;
		
	
	public Board(){
		makeMap();
		
		characters.add(new Person("Kasandra Scarlett","person"));
		characters.add(new Person("Jack Mustard","person"));
		characters.add(new Person("Diane White","person"));
		characters.add(new Person("Jacob Green","person"));
		characters.add(new Person("Eleanor Peacock","person"));
		characters.add(new Person("Victor Plum","person"));
		
		weapons.add(new Weapon("Rope", "weapon"));
		weapons.add(new Weapon("CandleStick", "weapon"));
		weapons.add(new Weapon("Knife", "weapon"));
		weapons.add(new Weapon("Pistol", "weapon"));
		weapons.add(new Weapon("Baseball Bat", "weapon"));
		weapons.add(new Weapon("Dumbbell", "weapon"));
		weapons.add(new Weapon("Trophy", "weapon"));
		weapons.add(new Weapon("Posion", "weapon"));
		weapons.add(new Weapon("Axe", "weapon"));
		
		rooms.add(new Room("Spa","room")); 
		rooms.add(new Room("Patio","room"));
		rooms.add(new Room("Kitchen","room"));
		rooms.add(new Room("Theatre","room"));
		rooms.add(new Room("Living Room","room"));
		rooms.add(new Room("Observatory","room"));
		rooms.add(new Room("Hall","room"));
		rooms.add(new Room("Guest House","room"));
		rooms.add(new Room("Dinning Room","room"));
		rooms.add(new Room("Swimming Pool","room"));
		
		// sets cards array
		cards.addAll(rooms);
		cards.addAll(weapons);
		cards.addAll(characters);
	}
	
	/** map layout **/
	public void makeMap(){
		this.map = new String[][] {
				{"X","R","R","R","R","W"," "," ","W","R","R","R","W"," ","W","R","R","R","R","W"," "," ","W","X"},//1
				{"R","R","R","R","R","W"," "," ","W","R","R","R","W"," ","W","R","R","R","R","W"," "," ","W","R"},//2
				{"R","R","R","R","R","W"," "," ","W","R","R","R","W"," ","W","R","R","R","R","W"," "," ","W","R"},//3
				{"R","R","R","R","R","W"," "," ","W","R","R","R","W"," ","W","R","R","R","R","W"," "," ","W","R"},//4
				{"R","R","R","R","R","W"," "," ","W","R","R","R","W"," ","W","R","R","R","R","W"," "," ","W","R"},//5
				{"R","R","R","R","W","S"," "," ","W","R","R","R","W"," ","W","R","R","R","R","W"," "," ","W","R"},//6
				{"R","R","R","R","W"," "," "," ","W","R","R","R","W"," ","W","R","R","R","R","W"," "," ","W","R"},//7
				{"W","W","W","W","W"," "," "," ","W","W","T","W","W"," ","W","W","R","W","W","W"," "," ","W","R"},//8
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," ","W","L","W"," "," "," "," ","O","W"},//9
				{" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},//10
				{"W","W","W","P"," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},//11
				{"R","R","R","W","W","W","W","W"," "," ","W","W","W","W","V","W","W","W"," ","W","W","W","H","W"},//12
				{"R","R","R","R","R","R","R","W"," "," ","W","R","R","R","R","R","R","W"," ","W","R","R","R","R"},//13
				{"R","R","R","R","R","R","R","W"," "," ","W","R","R","R","R","R","R","W"," ","H","R","R","R","R"},//14
				{"R","R","R","R","R","R","R","W"," "," ","W","R","R","R","R","R","R","W"," ","H","R","R","R","R"},//15
				{"R","R","R","R","R","R","R","W"," "," ","W","R","R","R","R","R","R","W"," ","W","R","R","R","R"},//16
				{"R","R","R","R","R","R","R","W"," "," ","V","W","W","W","W","W","W","V"," ","W","R","R","R","R"},//17
				{"R","R","R","W","W","W","W","W"," "," "," "," "," "," "," "," "," "," "," ","W","W","W","W","W"},//18
				{"W","W","W","W"," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},//19
				{" "," "," "," "," "," "," "," "," "," ","W","W","D","W","W","W"," "," "," "," "," "," "," "," "},//20
				{" "," "," "," "," "," "," "," "," "," ","W","R","R","R","R","W"," "," "," "," "," ","G","W","W"},//21
				{"W","W","W","W","W","W"," "," "," "," ","W","R","R","R","R","D"," "," "," "," ","G","R","R","R"},//22
				{"R","R","R","R","R","W","K"," "," "," ","W","R","R","R","R","W"," "," "," "," ","W","R","R","R"},//23
				{"R","R","R","R","R","R","W"," "," ","W","W","R","R","R","R","W","W"," "," "," ","W","R","R","R"},//24
				{"R","R","R","R","R","R","W"," "," ","W","R","R","R","R","R","R","W"," "," "," ","W","R","R","R"},//25
				{"R","R","R","R","R","R","W"," "," ","W","R","R","R","R","R","R","W"," "," "," ","W","R","R","R"},//26
				{"R","R","R","R","R","R","W"," "," ","W","R","R","R","R","R","R","W"," "," "," ","W","R","R","R"},//27
				{"R","R","R","R","R","R","W"," "," ","W","R","R","R","R","R","R","W"," "," "," ","W","R","R","R"},//28
				{"X","R","R","R","R","R","W"," "," ","W","R","R","R","R","R","R","W"," "," "," ","W","R","R","X"},//29
		};
	}
	
	public String[][] getMap() {
		return this.map;
	}
	
	public ArrayList<Card> getCards() {
		return this.cards;
	}

	public ArrayList<Room> getRooms() {
		return this.rooms;
	}

	public ArrayList<Weapon> getWeapons() {
		return this.weapons;
	}

	public ArrayList<Person> getPersons() {
		return this.characters;
	}
	
	public ArrayList<Card> getSecretEnvelope() {
		return this.secretEnvelope;
	}
	
	public void setEnvelope(ArrayList<Card> e) {
		this.secretEnvelope = e;
	}
	
	public void removeCard(Card c) {
		this.cards.remove(c);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public void removePlayer(Player player) {
		this.players.remove(player);
	}
}