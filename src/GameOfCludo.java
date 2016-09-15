import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class GameOfCludo {
	//sets up the board
	private Board board = new Board(); 

	public Board getBoard() {
		return board;
	}
	
	// returns opposite room location when player wants to take a short cut
	public Location shortCut(String curRoom) {
		if (curRoom == "Spa") {
			return new Location(20,21,"Guest House"); //current room is spa so returns guest house location
		}
		if (curRoom == "Observatory") {
			return new Location(6,22,"Kitchen"); //current room is Observatory so returns Kitchen location
			
		}
		if (curRoom == "Guest House") {
			return new Location(5,5,"Spa"); //current room is guest house so returns spa location
			
		}
		if (curRoom == "Kitchen") {
			return new Location(22,8,"Observatory"); //current room is Kitchen so returns Observatory location
		}
		return null; // if current room wasn't a corner room return null
	}
	
	/** finds the room that corresponds to the door the player is about to enter **/
	public String curRoom(String clickedSpot) {
		String room = null;
		if (clickedSpot == "S") {
			room = "Spa";
		}
		if (clickedSpot == "T") {
			room = "Theatre";
		}
		if (clickedSpot == "L") {
			room = "Living Room";
		}
		if (clickedSpot == "O") {
			room = "Observatory";
		}
		if (clickedSpot == "P") {
			room = "Patio";
		}
		if (clickedSpot == "V") {
			room = "Swimming Pool";
		}
		if (clickedSpot == "H") {
			room = "Hall";
		}
		if (clickedSpot == "G") {
			room = "Guest House";
		}
		if (clickedSpot == "D") {
			room = "Dinning Room";
		}
		if (clickedSpot == "K") {
			room = "Kitchen";
		}
		return room;
	}

	/** deals out the cards to each player randomly **/
	public ArrayList<Player> dealCards(GameOfCludo game,
			ArrayList<Player> players) {
		
		ArrayList<Player> temp = players;
		Random random = new Random();
		
		while (!game.getBoard().getCards().isEmpty()) { // while cards not empty
			for (Player p : temp) { // for each player
				if (!game.getBoard().getCards().isEmpty()) { // if cards not empty
					ArrayList<Card> listOfCards = game.getBoard().getCards(); // gets remaining cards list
					int pileSize = listOfCards.size();
					int r = random.nextInt(pileSize); // creates a new random int between 0 - cards list size
					p.addCard(listOfCards.get(r)); // adds the random card to the player
					game.getBoard().removeCard(listOfCards.get(r)); // removes the added card from list
				} else {
					break; // if cards is empty the end
				}
			}
		}
		return temp;
	}
	/** sets the secert envelope at the start **/
	public void setSecertEnvelope(GameOfCludo game) {
		ArrayList<Card> temp = new ArrayList<Card>();

		boolean weapon = false;
		boolean room = false;
		boolean person = false;

		Random random = new Random();
		while (weapon == false || room == false || person == false) { // while at least 1 of the 3 cards still needs to be placed
			ArrayList<Card> listOfCards = game.getBoard().getCards(); // get cards from the "deck"
			int pileSize = listOfCards.size();
			int r = random.nextInt(pileSize); // random int used as index for card array
			// gets the room
			if (listOfCards.get(r).getType() == "room" && room == false) { // if card at index is a room and roon has not already been choosen
				temp.add(listOfCards.get(r)); // adds the card to envelope
				game.getBoard().removeCard(listOfCards.get(r)); // removes the card from the list
				room = true;
			}
			// gets the weapon
			if (listOfCards.get(r).getType() == "weapon" && weapon == false) {
				temp.add(listOfCards.get(r));
				game.getBoard().removeCard(listOfCards.get(r));
				weapon = true;
			}
			//gets the person
			if (listOfCards.get(r).getType() == "person" && person == false) {
				temp.add(listOfCards.get(r));
				game.getBoard().removeCard(listOfCards.get(r));
				person = true;
			}
		}
		game.getBoard().setEnvelope(temp);
	}
}
