package brainxone;

public class Message 
{
	private int fromID;
	private int toID;
	private long timeSent;
	private String type;
	private String text;
	
	public Message(int from, int to, String t, String body)
	{
		fromID = from;
		toID = to;
		timeSent = System.currentTimeMillis();
		type = t;
		text = body;
	}
	
	public int getFromID() {
		return fromID;
	}

	public int getToID() {
		return toID;
	}

	public long getTimeSent() {
		return timeSent;
	}

	public String getType() {
		return type;
	}

	public String getText() {
		return text;
	}
}
