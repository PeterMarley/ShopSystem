package log;

import java.util.ArrayList;

public interface LogInterface {
	/**
	 * Logs a series of messages in a String array
	 * 
	 * @param message
	 */
	public void log(String[] message);

	/**
	 * Logs a single message in a String
	 *
	 * @param message
	 */
	public void log(String message);

	/**
	 * Logs a series of messages in an {@code ArrayList<String>}
	 * 
	 * @param messages
	 */
	public void log(ArrayList<String> messages);

	/**
	 * Logs a series of messages from an Exception's details
	 * 
	 * @param exception
	 */
	public void log(Exception exception);
}
