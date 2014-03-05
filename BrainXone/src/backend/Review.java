package backend;

public class Review {
	private int ID;
	private int stars;
	private String text = "";
	
	public Review(int ID, int stars) {
		this.ID = ID;
		this.stars = stars;
	}
	
	public Review(int ID, int stars, String text) {
		this.ID = ID;
		this.stars = stars;
		this.text = text;
	}
	
	public int getID(){
		return ID;
	}
	
	public String getText() {
		return text;
	}
	
	public int getStars() {
		return stars;
	}
}
