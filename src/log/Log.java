package log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple logging system. Preferred usage is to
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public class Log implements AutoCloseable {

	private enum LogMessageType {
		GENERAL("General"),
		EXCEPTION("Exception");

		private String logType;

		private LogMessageType(String logType) {
			this.logType = logType;
		}

		public String toString() {
			return this.logType;
		}
	}

	// file writing objects
	private File logfile;
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	private LocalDateTime stamp;

	// configurations
	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd-'['HH'h'.mm'm'.ss's'.SSSS'ms]'")
			.toFormatter();
	private static final String logHeaders = "LoggedFrom,DateTimeStamp,LogType,Messages";

	private static boolean logConfigured = false;

	//**************************************************************\
	//																*
	//		Configure Log											*
	//																*
	//**************************************************************/

	/**
	 * Constructs an instance of a logger
	 * 
	 * @param logfilepath relative filepath to log file (relative to working directory)
	 */
	public Log(String logfilepath) {
		try {
			this.setLogfile(logfilepath);
			this.writeHeaders();
			logConfigured = true;
		} catch (IllegalArgumentException | IOException logInstantiationEx) {
			logInstantiationEx.printStackTrace();
		}
	}

	/**
	 * Write the headers to the CSV file
	 */
	private void writeHeaders() {
		try (BufferedWriter bw = getBufferedWriter()) {
			bw.write(logHeaders);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the fully qualified name class and line of code that called the log
	 * 
	 * @return
	 */
	private String getCallingClass() {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		//TODO figure out what part of stack trace to return thats actually useful
		return (stack.length >= 3) ? stack[0].toString() + " - " + stack[1].toString() + " - " + stack[2].toString() : stack[0].toString();
	}

	/**
	 * Get a BufferedWriter to enable writing to file
	 * 
	 * @return
	 * @throws IOException
	 */
	private BufferedWriter getBufferedWriter() throws IOException {
		fileWriter = new FileWriter(logfile, true);
		bufferedWriter = new BufferedWriter(fileWriter);
		return bufferedWriter;
	}

	/**
	 * Set name and relative filepath of the log file
	 * 
	 * @param logfilepath
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private void setLogfile(String logfilepath) throws IllegalArgumentException, IOException {
		if (logfilepath == null || logfilepath.isBlank()) {
			throw new IllegalArgumentException("logfilepath cannot be null or blank");
		}
		stamp = LocalDateTime.now();
		logfile = new File(logfilepath + "-" + stamp.format(formatter) + ".csv");
		System.out.println(logfile);
	}

	/**
	 * Implementation of AutoCloseable interface .close() method to allow log use with try-with-resources block
	 */
	@Override
	public void close() {
		try {
			if (bufferedWriter != null)
				bufferedWriter.close();
		} catch (IOException e) {
		}

	}

	//**************************************************************\
	//																*
	//		Logging Operations										*
	//																*
	//**************************************************************/

	private String[] constructLogMessage(String[] messages, LogMessageType type) {

		//"LoggedFrom,DateTimeStamp,LogType,Messages";
		String[] logMessage = new String[logHeaders.split(",").length + messages.length - 1];
		logMessage[0] = getCallingClass();
		logMessage[1] = LocalDateTime.now().format(formatter);
		logMessage[2] = type.toString();
		for (int i = 0; i < messages.length; i++) {
			logMessage[i + 3] = messages[i];
		}
		return logMessage;
	}

	/**
	 * Log a message made of several strings
	 * 
	 * @param message
	 * @param LogMessageType
	 */
	private void log(String[] message, LogMessageType type) {

		String[] logMessage = constructLogMessage(message, type);

		if (logConfigured && logMessage != null) {
			try (BufferedWriter bw = getBufferedWriter()) {
				for (int i = 0; i < logMessage.length; i++) {
					bw.write((logMessage[i] != null) ? logMessage[i] : "null");
					if (i != logMessage.length - 1) {
						bw.write(",");
					}
				}
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Log is not configured! Printing to console instead");
			System.err.println("\tLog Message:");
			System.err.println("\t\tCalling Class:" + logMessage[0]);
			System.err.println("\t\tDateTimeStamp:" + logMessage[1]);
			for (int i = 2; i < logMessage.length; i++) {
				System.err.println("\t\t\tMessage " + (i - 1) + " " + logMessage[i]);
			}
		}
	}

	/**
	 * Logs a series of messages in a String array
	 * 
	 * @param message
	 */
	public void log(String[] message) {
		log(message, LogMessageType.GENERAL);
	}

	/**
	 * Logs a single message in a String
	 *
	 * @param message
	 */
	public void log(String message) {
		log(new String[] { message }, LogMessageType.GENERAL);
	}

	/**
	 * Logs a series of messages in an {@code ArrayList<String>}
	 * 
	 * @param messages
	 */
	public void log(ArrayList<String> messages) {
		String[] messageStr = new String[messages.size()];
		for (int i = 0; i < messages.size(); i++) {
			messageStr[i] = messages.get(i);
		}
		log(messageStr, LogMessageType.GENERAL);
	}

	/**
	 * Logs a series of messages from an Exception's details
	 * 
	 * @param exception
	 */
	public void log(Exception exception) {
		String[] messages = new String[exception.getStackTrace().length + 2];
		messages[0] = (exception.getMessage() != null) ? exception.getMessage() : "No Message";
		messages[1] = (exception.getCause() != null) ? exception.getCause().toString() : "No Cause";
		int index = 2;
		for (StackTraceElement s : exception.getStackTrace()) {
			messages[index] = s.toString();
			index++;
		}
		log(messages, LogMessageType.EXCEPTION);
	}
}
