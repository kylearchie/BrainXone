package brainxone;

public class TakenEvent extends Event
{
	private int score;
	private long timeTaken;
	
	public TakenEvent(int user, int quiz, int points, long taken) 
	{
		super(user, quiz);
		score = points;
		timeTaken = taken;
	}
	
	public int getScore() {
		return score;
	}

	public long getTimeTaken() {
		return timeTaken;
	}
	
}
