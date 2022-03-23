package log;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestLog {

	private static Logger log;
	

	@BeforeEach
	void setUp() throws Exception {
		log = Logger.initialise();
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
