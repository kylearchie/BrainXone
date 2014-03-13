package backend;

public class Review {
	public int stars;
	public String textReview;
	public String reviewerName;
	public Review(int stars, String textReview, String reviewerName){
		this.stars = stars;
		this.textReview = textReview;
		this.reviewerName = reviewerName;
	}
}
