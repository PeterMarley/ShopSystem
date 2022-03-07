package model;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.HumanResourcesModel.Employee;

/**
 * Database Schema:
 * 
 * CREATE TABLE shifts (shiftID INTEGER,date TEXT,hours REAL,employeeID INTEGER,PRIMARY KEY (shiftID),FOREIGN KEY (employeeID) REFERENCES
 * employee(employeeID));
 * CREATE TABLE employee (employeeID INTEGER,hourlyRateInPence INTEGER,hoursPerWeek REAL,startDate TEXT,endDate TEXT,personID INTEGER,PRIMARY KEY
 * (employeeID),FOREIGN KEY (personID) REFERENCES person(personID));
 * CREATE TABLE person (personID INTEGER,forename TEXT,surname TEXT,email TEXT,phoneNumber TEXT,PRIMARY KEY(personID));
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
class UnitTestDatabaseAccessObject {

	private static DatabaseAccessObject dao;
	private static final String TEST_DB = "test.db";
	private static final String TEST_DB_REL_JUNIT = "jdbc:sqlite:./junitTests/model/";
	private static final String TEST_DB_REL_SRC = "jdbc:sqlite:../../junitTests/model/";
	private static final int TEST_ROWS = 5;
	private static HumanResourcesModel model = new HumanResourcesModel();
	private static DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd")
			.toFormatter();

	private static Employee employee1, employee2, employee3, employee4, employee5NoEndDate, employee6, employee7, employee8;

	///////////////////////
	// JUNIT setup		//
	/////////////////////
	
	/**
	 * Ensures database has no person or employee tables
	 */
	@BeforeAll
	static void beforeAll() {
		// clear all tables
		try {
			dropTables();
		} catch (SQLException e) {
			System.out.println("@BeforeEach happy with database state prior to testing");
		}
	}

	/**
	 * Creates person and employee tables in database, and pushes test employees to it
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	void beforeEach() throws Exception {
		// instantiate test Employee objects
		employee1 = model.new Employee("aforename", "asurname", "abasic@test.email", "123", 801, 37.5, LocalDate.of(2010, 06, 25), LocalDate.of(2010, 06, 27));
		employee2 = model.new Employee("employeeTwo", "two", "two@test.test", "+123", 801, 37.5, LocalDate.of(2010, 6, 25), LocalDate.of(2010, 6, 27));
		employee3 = model.new Employee("employeeThree", "three", "three@test.test", "+2345", 1200, 33, LocalDate.of(2009, 5, 15), LocalDate.of(2010, 1, 30));
		employee4 = model.new Employee("employeeFour", "four", "four@test.test", "+34567", 2000, 38, LocalDate.of(2008, 4, 10), LocalDate.of(2015, 1, 6));
		employee5NoEndDate = model.new Employee("employeeFive", "five", "five@test.test", "+456789", 6000, 52, LocalDate.of(2007, 3, 9), null);

		employee6 = model.new Employee("employeeSix", "six", "six@test.test", "+45678910", 8000, 12, LocalDate.of(2020, 5, 5), null);
		employee7 = model.new Employee("employeeSeven", "seven", "seven@test.test", "7777", 9425, 15, LocalDate.of(2021, 6, 6), null);
		employee8 = model.new Employee("employeeEight", "eight", "eight@test.test", "+888888", 10025, 18, LocalDate.of(2021, 8, 8), LocalDate.of(2021, 9, 1));
		Employee[] employees = new Employee[] { employee1, employee2, employee3, employee4, employee5NoEndDate };
		/*
		 * Construct database schema & 2 test entry in each table
		 */

		// create tables and a single person/ employee
		try {
			createTestTables();
			createTestRows(employees);
			checkTestSetup(employees.length);
			System.out.println("@BeforeEach complete");
		} catch (SQLException e) {
			System.err.println("@BeforeEach failed! Exiting unit tests");
			System.out.println(e.getMessage());
			afterEach();
			System.exit(1);
		}

		dao = new DatabaseAccessObject(TEST_DB_REL_SRC + TEST_DB);

	}

	@AfterEach
	void afterEach() {
		System.out.println("@AfterEach beginning");
		try {
			dropTables();
			System.out.println("@AfterAll complete!");
		} catch (SQLException e) {
			System.err.println("@AfterAll failed!");
			System.err.println(e.getMessage());
		}
	}

	///////////////////////
	// Unit Tests		//
	/////////////////////
	
	/**
	 * Test {@code DatabaseAccessObject}s {@code getEmployees()} method
	 */
	@Test
	void test_getEmployees() {
		ArrayList<Employee> employees = dao.getEmployees();
		assertNotNull(employees);
		assertEquals(5, employees.size());

		assertEquals(employee1, employees.get(0));
		assertEquals(employee2, employees.get(1));
		assertEquals(employee3, employees.get(2));
		assertEquals(employee4, employees.get(3));
		assertEquals(employee5NoEndDate, employees.get(4));
	}

	/**
	 * Test {@code DatabaseAccessObject}s {@code addEmployee()} method
	 */
	@Test
	void test_addEmployee() {
		
		ArrayList<Employee> employees = dao.getEmployees();
		assertEquals(employees.size(), TEST_ROWS);

		int totalRows = TEST_ROWS;
		
		// add employee & check database state
		dao.addEmployee(employee6);
		employees = dao.getEmployees();
		assertEquals(++totalRows, employees.size());
		assertTrue(employees.contains(employee6));
		assertFalse(employees.contains(employee7));
		assertFalse(employees.contains(employee8));

		// add employee & check database state
		dao.addEmployee(employee7);
		employees = dao.getEmployees();
		assertEquals(++totalRows, employees.size());
		assertTrue(employees.contains(employee6));
		assertTrue(employees.contains(employee7));
		assertFalse(employees.contains(employee8));

		// add employee & check database state
		dao.addEmployee(employee8);
		employees = dao.getEmployees();
		assertEquals(++totalRows, employees.size());
		assertTrue(employees.contains(employee6));
		assertTrue(employees.contains(employee7));
		assertTrue(employees.contains(employee8));

	}

	///////////////////////
	// Test Harness		//
	/////////////////////
	
	/**
	 * Create employee and person tables in an empty database
	 */
	private static void createTestTables() {
		// SQL Strings
		final String CREATE_TABLE_PERSON = "CREATE TABLE person (" +
				"	personID INTEGER," +
				"	forename TEXT," +
				"	surname TEXT," +
				"	email TEXT," +
				"	phoneNumber TEXT," +
				"	PRIMARY KEY(personID)" +
				");";
		final String CREATE_TABLE_EMPLOYEE = "CREATE TABLE employee (" +
				"	employeeID INTEGER," +
				"	personID INTEGER," +
				"	hourlyRateInPence INTEGER," +
				"	hoursPerWeek REAL," +
				"	startDate TEXT," +
				"	endDate TEXT," +
				"	PRIMARY KEY (employeeID)," +
				"	FOREIGN KEY (personID) REFERENCES person(personID)" +
				");";

		// try with resources
		try (Connection connection = getConnection();
				PreparedStatement createPersonTable = connection.prepareStatement(CREATE_TABLE_PERSON);
				PreparedStatement createEmployeeTable = connection.prepareStatement(CREATE_TABLE_EMPLOYEE)) {
			// create table person
			createPersonTable.executeUpdate();
			// create table employee
			createEmployeeTable.executeUpdate();
		} catch (SQLException createTestTablesException) {
			System.err.println(createTestTablesException.getMessage());
		}
	}

	/**
	 * Check test database is correctly configured
	 * 
	 * @throws SQLException
	 */
	private static void checkTestSetup(int testRows) throws SQLException {
		final String QUERY_SCHEMA = "SELECT * FROM sqlite_schema WHERE type='table';";
		final String QUERY_PERSON = "SELECT * FROM person;";
		final String QUERY_EMPLOYEE = "SELECT * FROM employee;";

		Statement statement = null;
		ResultSet resultsSet = null;
		try (Connection connection = getConnection()) {
			// ensure that both tables successfully created
			statement = connection.createStatement();
			statement.execute(QUERY_SCHEMA);
			resultsSet = statement.getResultSet();

			boolean personTableFound = false, employeeTableFound = false;
			int personTableCount = 0, employeeTableCount = 0;
			while (resultsSet.next()) {
				if (resultsSet.getString(2).equals("person") && personTableCount < 1) {
					personTableFound = true;
					personTableCount++;
				}
				if (resultsSet.getString(2).equals("employee") && employeeTableCount < 1) {
					employeeTableFound = true;
					employeeTableCount++;
				}
			}

			// ensure person table has a single row
			statement.execute(QUERY_PERSON);
			resultsSet = statement.getResultSet();
			int totalPersonRows = 0;
			while (resultsSet.next()) {
				totalPersonRows++;
			}

			// ensure employee table has a single row
			statement.execute(QUERY_EMPLOYEE);
			resultsSet = statement.getResultSet();
			int totalEmployeeRows = 0;
			while (resultsSet.next()) {
				totalEmployeeRows++;
			}

			// ensure all checks successful
			if (!personTableFound
					|| !employeeTableFound
					|| personTableCount != 1
					|| employeeTableCount != 1
					|| totalPersonRows != testRows
					|| totalEmployeeRows != testRows) {
				throw new SQLException("Table creation in before all was not successful");
			}
		} finally {
			closeQuietly(resultsSet);
			closeQuietly(statement);
		}
	}

	/**
	 * Drop the following tables from database:<br>
	 * - employee<br>
	 * - person
	 * 
	 * @throws SQLException
	 */
	private static void dropTables() throws SQLException {
		// configure SQL objects
		Connection con = getConnection();
		Statement s = con.createStatement();
		// drop person and employee tables from database
		s.execute("DROP TABLE person;");
		s.execute("DROP TABLE employee;");
		// close SQL objects
		closeQuietly(null, s, con);
	}

	/**
	 * add all Employee objects from employees array to database
	 * 
	 * @param employees
	 */
	private static void createTestRows(Employee[] employees) {
		final String ADD_PERSON = "INSERT INTO person (forename,surname,email,phoneNumber) VALUES (?,?,?,?);";
		final String ADD_EMPLOYEE = "INSERT INTO employee (personID,hourlyRateInPence,hoursPerWeek,startDate,endDate) VALUES (?,?,?,?,?);";

		for (int i = 0; i < employees.length; i++) {
			try (Connection connection = getConnection();) {
				int personID = -1;

				// try-with-resources PreparedStatement
				try (PreparedStatement addPersonStatement = connection.prepareStatement(ADD_PERSON, Statement.RETURN_GENERATED_KEYS);) {

					// add a person and get that rows personID primary key
					addPersonStatement.setString(1, employees[i].getForename());
					addPersonStatement.setString(2, employees[i].getSurname());
					addPersonStatement.setString(3, employees[i].getEmail());
					addPersonStatement.setString(4, employees[i].getPhoneNumber());
					addPersonStatement.executeUpdate();
					personID = addPersonStatement.getGeneratedKeys().getInt(1);
					if (personID == -1) {
						throw new SQLException("DAO: Adding person to database failed");
					}

					// try-with-resources PreparedStatement
					try (PreparedStatement addEmployeeStatement = connection.prepareStatement(ADD_EMPLOYEE);) {

						// insert an employee associated to the same key (this employees foreign key - personID)
						addEmployeeStatement.setInt(1, personID);
						addEmployeeStatement.setInt(2, employees[i].getHourlyRate());
						addEmployeeStatement.setDouble(3, employees[i].getHoursPerWeek());
						addEmployeeStatement.setString(4, (employees[i].getStartDateAsLocalDate() != null) ? employees[i].getStartDateAsLocalDate().format(formatter) : "");
						addEmployeeStatement.setString(5, (employees[i].getEndDateAsLocalDate() != null) ? employees[i].getEndDateAsLocalDate().format(formatter) : "");
						addEmployeeStatement.executeUpdate();
					}
				}

			} catch (SQLException createTestRowsException) {
				System.err.println(createTestRowsException.getMessage());
			}
		}

	}

	/**
	 * Get an sqlite3 connection safely
	 * 
	 * @return Connection object
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(TEST_DB_REL_JUNIT + TEST_DB);
	}

	/**
	 * A method to close all SQL objects quietly and safely
	 * 
	 * @param resultSet
	 * @param statement
	 * @param connection
	 */
	private static void closeQuietly(ResultSet resultSet, Statement statement, Connection connection) {
		closeQuietly(resultSet);
		closeQuietly(statement);
		closeQuietly(connection);
	}

	/**
	 * Quietly close any object that implements {@code AutoCloseable}. Null and closed object safe
	 * 
	 * @param SQLObject
	 */
	private static void closeQuietly(AutoCloseable SQLObject) {
		if (SQLObject instanceof Connection || SQLObject instanceof Statement || SQLObject instanceof ResultSet) {
			if (SQLObject != null) {
				try {
					SQLObject.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
