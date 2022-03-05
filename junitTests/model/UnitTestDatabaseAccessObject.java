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

class UnitTestDatabaseAccessObject {

	private static DatabaseAccessObject dao;
	private static final String TEST_DB = "test.db";
	private static final String TEST_DB_REL_JUNIT = "jdbc:sqlite:./junitTests/model/";
	private static final String TEST_DB_REL_SRC = "jdbc:sqlite:../../junitTests/model/";
	private static HumanResourcesModel model = new HumanResourcesModel();

	private static Employee[] employees;

	private Employee employee;

	/**
	 * Create person and employee tables, populate with 1 row, and check success of operation.
	 * If operation failed then close all SQL objects and {@code System.exit(1)} to halt all testing
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	@BeforeAll
	static void beforeAll() throws Exception {
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
			System.out.println("@BeforeAll happy with database state prior to testing");
		}
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("dd-MM-yyyy").toFormatter();
		Connection con = null;
		PreparedStatement ps = null;
		Statement s = null;
		ResultSet rs = null;
		boolean beforeAllSuccess = true;
		try {
			// create table person
			con = getConnection();
			ps = con.prepareStatement("CREATE TABLE person (" +
					"	personID INTEGER," +
					"	forename TEXT," +
					"	surname TEXT," +
					"	email TEXT," +
					"	phoneNumber TEXT," +
					"	PRIMARY KEY(personID)" +
					");");
			ps.executeUpdate();

			// create table employee
			ps = con.prepareStatement("CREATE TABLE employee (" +
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

			// test Employee object
			Employee testRowEmployee = model.new Employee("aforename", "asurname", "abasic@test.email", "123", 801, 37.5, LocalDate.of(2010, 06, 25), LocalDate.of(2010, 06, 27));

			// insert a person and get their primary key (personID)
			ps = con.prepareStatement("INSERT INTO person (forename,surname,email,phoneNumber) VALUES (?,?,?,?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, testRowEmployee.getForename());
			ps.setString(2, testRowEmployee.getSurname());
			ps.setString(3, testRowEmployee.getEmail());
			ps.setString(4, testRowEmployee.getPhoneNumber());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			// insert an employee associated to the same key (this employees foreign key - personID)
			ps = con.prepareStatement("INSERT INTO employee (personID,hourlyRateInPence,hoursPerWeek,startDate,endDate)" +
					"VALUES (?,?,?,?,?);");
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, testRowEmployee.getHourlyRate());
			ps.setDouble(3, testRowEmployee.getHoursPerWeek());
			ps.setString(4, testRowEmployee.getStartDateAsLocalDate().format(formatter));
			ps.setString(5, testRowEmployee.getEndDateAsLocalDate().format(formatter));
			ps.executeUpdate();

			// ensure that both tables successfully created
			s = con.createStatement();
			s.execute("SELECT * FROM sqlite_schema WHERE type='table'");
			rs = s.getResultSet();
			boolean personTableFound = false, employeeTableFound = false;
			int personTableCount = 0, employeeTableCount = 0;
			while (rs.next()) {
				if (rs.getString(2).equals("person") && personTableCount < 1) {
					personTableFound = true;
					personTableCount++;
				}
				if (rs.getString(2).equals("employee") && employeeTableCount < 1) {
					employeeTableFound = true;
					employeeTableCount++;
				}
			}

			// ensure person table has a single row
			s.execute("SELECT * FROM person;");
			rs = s.getResultSet();
			int totalPersonRows = 0;
			while (rs.next()) {
				totalPersonRows++;
			}

			// ensure employee table has a single row
			s.execute("SELECT * FROM employee;");
			rs = s.getResultSet();
			int totalEmployeeRows = 0;
			while (rs.next()) {
				totalEmployeeRows++;
			}

			// ensure all checks successful
			if (!personTableFound
					|| !employeeTableFound
					|| personTableCount != 1
					|| employeeTableCount != 1
					|| totalPersonRows != 1
					|| totalEmployeeRows != 1) {
				throw new SQLException("Table creation in before all was not successful");
			}

			System.out.println("@BeforeAll complete");
		} catch (SQLException e) {
			System.err.println("@BeforeAll failed!");
			beforeAllSuccess = false;
			System.out.println(e.getMessage());
		} finally {
			closeQuietly(rs, ps, con);
			closeQuietly(null, s, null);
			if (!beforeAllSuccess) {
				afterAll();
				System.exit(1);
			}
		}
	}

	@AfterAll
	static void afterAll() throws SQLException {
		System.out.println("@AfterAll beginning");
		try {
			dropTables();
			System.out.println("@AfterAll complete - no major errors in test harness");
		} catch (SQLException e) {
			System.err.println("@AfterAll failed!");
			System.err.println(e.getMessage());
		}
	}

	@BeforeEach
	void beforeEach() throws Exception {
		dao = new DatabaseAccessObject(TEST_DB_REL_SRC + TEST_DB);
	}

	@AfterEach
	void afterEach() {
	}

	@Test
	void testEmployeeAddedInBeforeAll() {
		ArrayList<Employee> employees = dao.getEmployees();
		assertNotNull(employees);
		assertEquals(1, employees.size());
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
	 * Get an sqlite3 connection safely
	 * 
	 * @return Connection object
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		//		if (con == null) {
		//			try {
		//				Class.forName("org.sqlite.JDBC");
		//				con = DriverManager.getConnection(TEST_DB_REL_JUNIT + TEST_DB);
		//			} catch (ClassNotFoundException | SQLException e) {
		//				System.out.println("getConnection() exception!");
		//			}
		//		}
		//		return con;
		
		//		try {
		//			Class.forName("org.sqlite.JDBC");
		//		} catch (ClassNotFoundException e) {
		//			e.printStackTrace();
		//		}
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
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}
}
