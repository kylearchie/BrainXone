package brainxone;

public class Challenge extends Message
{
	private int quizID;
	
	public Challenge(int from, int to, String t, String body, int quiz) 
	{
		super(from, to, t, body);
		quizID = quiz;
	}

	public int getQuizID() 
	{
		return quizID;
	}
}
