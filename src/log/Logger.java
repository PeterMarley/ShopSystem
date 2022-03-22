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
 * A simple logging system. <br>
 * 
 * This class is a singleton, only one instance at a time can be created. All parts of the program must then
 * get this instance to log any actions
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public class Logger implements AutoCloseable {

	private static Logger instance;

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
	private static final String logFileLocation = "./src/log/logs/";
	private static final String logFilePrefix = "ShopSystem";
	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd-'['HH'h'.mm'm'.ss's'.SSSS'ms]'")
			.toFormatter();
	private static final String logHeaders = "LoggedFrom,DateTimeStamp,LogType,Messages";

	//**************************************************************\
	//																*
	//		Configure Log											*
	//																*
	//**************************************************************/

	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger(logFileLocation + logFilePrefix);
		}
		return instance;
	}

	/**
	 * Constructs an instance of a logger
	 * 
	 * @param logfilepath relative filepath to log file (relative to working directory) ending in the prefix to the log file
	 */
	private Logger(String logfilepath) {
		try {
			this.setLogfile(logfilepath);
			this.writeHeaders();
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
	private String getRelevantStackInfo() {
		ArrayList<StackTraceElement> stack = new ArrayList<StackTraceElement>();
		//TODO figure out what part of stack trace to return thats actually useful
		for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
			if (!e.toString().startsWith("java") && !e.toString().startsWith("log") && !e.toString().startsWith("Log")) {
				stack.add(e);
			}
		}
		//return Thread.currentThread().get
		return stack.toString().replace(',', '-');
		//return (stack.length >= 3) ? stack[0].toString() + " - " + stack[1].toString() + " - " + stack[2].toString() : stack[0].toString();
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
	public void setLogfile(String logfilepath) throws IllegalArgumentException, IOException {
		if (logfilepath == null || logfilepath.isBlank()) {
			throw new IllegalArgumentException("logfilepath cannot be null or blank");
		}
		File previousLogfile = logfile;
		stamp = LocalDateTime.now();
		logfile = new File(logfilepath + "-" + stamp.format(formatter) + ".csv");
		try (BufferedWriter bw = getBufferedWriter()) {
		} catch (IOException newLogfileInvalidEx) {
			System.out.printf("logfilepath invalid%n" +
					"\"" + logfilepath + "\"%n" +
					"Reset back to " + previousLogfile.toString() + "%n" +
					newLogfileInvalidEx.getMessage());
			logfile = previousLogfile;
		}
		System.out.println(logfile);
	}
	
	public void setLogfilePath() {
		//TODO
	}
	
	public void setLogfilePrefix() {
		//TODO
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

	public static void closeLogger() {
		instance.close();
	}

	//**************************************************************\
	//																*
	//		Logging Operations										*
	//																*
	//**************************************************************/

	private String[] constructLogMessage(String[] messages, LogMessageType type) {

		//"LoggedFrom,DateTimeStamp,LogType,Messages";
		String[] logMessage = new String[logHeaders.split(",").length + messages.length - 1];
		logMessage[0] = getRelevantStackInfo();
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

		if (getInstance() != null && logMessage != null) {
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
	public static void logThis(String[] message) {
		getInstance();
		instance.log(message, LogMessageType.GENERAL);
	}

	/**
	 * Logs a single message in a String
	 *
	 * @param message
	 */
	public static void logThis(String message) {
		getInstance();
		instance.log(new String[] { message }, LogMessageType.GENERAL);
	}

	/**
	 * Logs a series of messages in an {@code ArrayList<String>}
	 * 
	 * @param messages
	 */
	public static void logThis(ArrayList<String> messages) {
		getInstance();
		String[] msgArr = new String[messages.size()];
		for (int i = 0; i < messages.size(); i++) {
			msgArr[i] = messages.get(i);
		}
		instance.log(msgArr, LogMessageType.GENERAL);
	}

	/**
	 * Logs a series of messages from an Exception's details
	 * 
	 * @param exception
	 */
	public static void logThis(Exception exception) {
		getInstance();
		String[] messages = new String[exception.getStackTrace().length + 2];
		messages[0] = (exception.getMessage() != null) ? exception.getMessage() : "No Message";
		messages[1] = (exception.getCause() != null) ? exception.getCause().toString() : "No Cause";
		int index = 2;
		for (StackTraceElement s : exception.getStackTrace()) {
			messages[index] = s.toString();
			index++;
		}
		instance.log(messages, LogMessageType.EXCEPTION);
	}
}
