public class Location {
	private int x; // x position in the map array
	private int y; //  position in the map array
	private String name; // room the payer is currently in

	public Location(int x, int y,String name) {
		this.x = x;
		this.y = y;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(Location l) {
		this.x = l.getX();
		this.y = l.getY();
		this.name = l.getName();
	}
}
