package log;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestLog {

	private static Log log;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		log = new Log("./unitTestLog");
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
