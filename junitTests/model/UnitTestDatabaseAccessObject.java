package model;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.HumanResourcesModel.Employee;

class UnitTestDatabaseAccessObject {

	private static DatabaseAccessObject dao;
	private static final String TEST_DB = "test.db";
	private static final String TEST_DB_REL_JUNIT = "jdbc:sqlite:./junitTests/model/";
	private static final String TEST_DB_REL_SRC = "jdbc:sqlite:../../junitTests/model/";
	private static Connection con = null;

	/**
	 * Drop the following tables from database:<br>
	 * - employee<br>
	 * - person
	 * 
	 * @throws SQLException
	 */
	private static void dropTables() throws SQLException {
		Statement s = getConnection().createStatement();
		s.execute("DROP TABLE person;");
		s.execute("DROP TABLE employee;");
	}

	/**
	 * Get an sqlite3 connection safely
	 * 
	 * @return Connection object
	 */
	private static Connection getConnection() {
		if (con == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection(TEST_DB_REL_JUNIT + TEST_DB);
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println("getConnection() exception!");
			}
		}
		return con;
	}

	/**
	 * Create person and employee tables, populate with 1 row
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		/*
		 CREATE TABLE shifts (
		shiftID INTEGER,
		date TEXT,
		hours REAL,
		employeeID INTEGER,
		PRIMARY KEY (shiftID),
		FOREIGN KEY (employeeID) REFERENCES employee(employeeID)
		);
		CREATE TABLE employee (
		employeeID INTEGER,
		hourlyRateInPence INTEGER,
		hoursPerWeek REAL,
		startDate TEXT,
		endDate TEXT,
		personID INTEGER,
		PRIMARY KEY (employeeID),
		FOREIGN KEY (personID) REFERENCES person(personID)
		);
		CREATE TABLE person (
		personID INTEGER,
		forename TEXT,
		surname TEXT,
		email TEXT,
		phoneNumber TEXT,
		PRIMARY KEY(personID)
		); 
		 */

		// ensure database does not have relevant tables before setting up test database
		System.out.println("@BeforeAll beginning");
		try {
			dropTables();
		} catch (Exception e) {
			System.out.println("@BeforeAll dropTables() not necessary because \"" + e.getMessage() + "\"");
		}

		try {
			// create table person
			PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE person (" +
					"	personID INTEGER," +
					"	forename TEXT," +
					"	surname TEXT," +
					"	email TEXT," +
					"	phoneNumber TEXT," +
					"	PRIMARY KEY(personID)" +
					");");
			ps.executeUpdate();

			// create table employee
			ps = getConnection().prepareStatement("CREATE TABLE employee (" +
					"	employeeID INTEGER," +
					"	personID INTEGER," +
					"	hourlyRateInPence INTEGER," +
					"	hoursPerWeek REAL," +
					"	startDate TEXT," +
					"	endDate TEXT," +
					"	PRIMARY KEY (employeeID)," +
					"	FOREIGN KEY (personID) REFERENCES person(personID)" +
					");");
			ps.executeUpdate();

			// insert a person and get their primary key (personID)
			ps = getConnection().prepareStatement("INSERT INTO person (forename,surname,email,phoneNumber) VALUES ('aforename','asurname','abasic@test.email','123');",
					Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();

			// insert an employee associated to the same key (this employees foreign key - personID)
			ps = getConnection().prepareStatement("INSERT INTO employee (personID,hourlyRateInPence,hoursPerWeek,startDate,endDate)" +
					"VALUES (?,801,37.5,'2000-06-25','2000-06-26');");
			ps.setInt(1, rs.getInt(1));
			ps.executeUpdate();
			ps.close();
			System.out.println("@BeforeAll complete");
		} catch (SQLException e) {
			System.err.println("@BeforeAll failed!");
			System.out.println(e.getMessage());
			breakDown();
			System.exit(1);
		}
	}

	@AfterAll
	static void breakDown() throws SQLException {
		System.out.println("@AfterAll beginning");
		try {
			dropTables();
			if (con != null) {
				con.close();
			}
			System.out.println("@AfterAll complete");
		} catch (SQLException e) {
			System.err.println("@AfterAll failed!");
			System.err.println(e.getMessage());
		}
	}

	@BeforeEach
	void setUp() throws Exception {
		dao = new DatabaseAccessObject(TEST_DB_REL_SRC + TEST_DB);
	}
	
	@AfterEach
	void closeCon() {
		dao.closeConnection();
	}

	@Test
	void testTablesExist() {
		try {

			Statement s = getConnection().createStatement();
			s.execute("SELECT * FROM sqlite_schema WHERE type='table'");
			ResultSet rs = s.getResultSet();
			boolean personTableFound = false, employeeTableFound = false;
			while (rs.next()) {
				if (rs.getString(2).equals("person"))
					personTableFound = true;
				if (rs.getString(2).equals("employee"))
					employeeTableFound = true;
			}
			assertTrue(personTableFound);
			assertTrue(employeeTableFound);
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("SQLException thrown in testTablesExist()!");
		}
	}
	
	@Test
	void testEmployeeAddedInBeforeAll() {
		ArrayList<Employee> employees = dao.getEmployees();
		assertNotNull(employees);
	}

	//	@Test
	//	void testInitialEmployeeExistsAndIsCorrect() {
	//		try {
	//			Statement s = getConnection().createStatement();
	//			s.execute("SELECT * FROM employee INNER JOIN person ON person.personID=employee.employeeID WHERE surname='asurname'");
	//			ResultSet rs = s.getResultSet();
	//			rs.last();
	//			assertEquals(rs.getRow(), 1);
	//			rs.beforeFirst();
	//		} catch (SQLException e) {
	//			fail("SQLException thrown by testInitialEmployeeExistsAndIsCorrect()");
	//		}
	//	}

}
