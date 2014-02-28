package brainxone;

public class Event 
{
	private long timeInMillis;
	private int userID;
	private int quizID;
	private int id; //if needed
	
	public Event(int user, int quiz)
	{
		timeInMillis = System.currentTimeMillis();
		userID = user;
		quizID = quiz;
		id = 0; //pull from 1+ highest number in database
	}
	
	public long getTimeInMillis() 
	{
		return timeInMillis;
	}

	public int getUserID() 
	{
		return userID;
	}

	public int getQuizID() 
	{
		return quizID;
	}

	public int getId() 
	{
		return id;
	}
}
