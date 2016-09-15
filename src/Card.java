
public abstract class Card{
	private String name; // card name e.g. room name/ person name/ weapon name
	private String type; // what is this card type (room/weapon/person)
	public Card(String name, String type){
	this.name = name;
	this.type = type;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getType(){
		return this.type;
	}
}
