package logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * A Static class used for simple logging system that works on a single Thread
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public class Log {

	private static PrintStream oldSystemOut;
	private static PrintStream ps;
	private static boolean loggerInitialised = false;

	public static void start() {
		// capture old system.out
		oldSystemOut = System.out;

		// assign new PrintStream to System.out
		ps = new PrintStream(new ByteArrayOutputStream());
		System.setOut(ps);

		loggerInitialised = true;
	}

	public static void stop() {

	}

}
