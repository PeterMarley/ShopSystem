package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;

import model.HumanResourcesModel.Employee;
import model.HumanResourcesModel.Person;

/**
 * This DatabaseAccessObject provides methods that allow interaction with the ShopSystem sqlite3 database with schema as per below.<br>
 * All object's with {@code LocalDate}s have them stored in database as Strings using {@code DateTimeFormatter} with pattern {@code yyyy-MM-dd}.<br>
 * 
 * <pre>
 * CREATE TABLE shifts (
 * 	shiftID INTEGER,
 *	date TEXT,
 *	hours REAL,
 *	employeeID INTEGER,
 *	PRIMARY KEY (shiftID),
 *	FOREIGN KEY (employeeID) REFERENCES employee(employeeID)
 * );
 * CREATE TABLE employee (
 * 	employeeID INTEGER,
 * 	hourlyRateInPence INTEGER,
 * 	hoursPerWeek REAL,
 * 	startDate TEXT,
 * 	endDate TEXT,
 * 	personID INTEGER,
 * 	PRIMARY KEY (employeeID),
 * 	FOREIGN KEY (personID) REFERENCES person(personID)
 * );
 * CREATE TABLE person (
 * 	personID INTEGER,
 * 	forename TEXT,
 * 	surname TEXT,
 * 	email TEXT,
 * 	phoneNumber TEXT,
 *	PRIMARY KEY(personID)
 * );
 * </pre>
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public class DatabaseAccessObject {

	private String dbFilepath;
	/**
	 * Date to text operations
	 */
	private final DateTimeFormatter FORMAT_OBJECT;
	private final DateTimeFormatter FORMAT_LOG;
	/**
	 * All Human Resources classes
	 */
	private final HumanResourcesModel MODEL;

	/**
	 * Construct DataBaseAccessObject.
	 * 
	 * @param databaseRelativeFilepath - the relative filepath to the sqlite3 database
	 */
	public DatabaseAccessObject(String databaseRelativeFilepath) throws SQLException {

		MODEL = new HumanResourcesModel();
		FORMAT_OBJECT = new DateTimeFormatterBuilder()
				.appendPattern("yyyy-MM-dd")
				.toFormatter(Locale.ENGLISH);
		FORMAT_LOG = new DateTimeFormatterBuilder()
				.appendPattern("dd-MM-yy HH:mm:ss.SS")
				.toFormatter();

		testConnection(databaseRelativeFilepath);

	}

	/**
	 * Sets the database relative filepath, and tests if a Connection can be established
	 * 
	 * @param dbFilepath
	 */
	private void testConnection(String dbFilepath) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);) {
			System.out.println("DatabaseAccessObject: setDatabaseLocation() successfully tested database connection and returned it " +
					"to pool! @ " + LocalDateTime.now().format(FORMAT_LOG) + " (DB: " + dbFilepath + ")");
			this.dbFilepath = dbFilepath;
		}
	}

	/**
	 * Creates and returns an SQL database connection
	 * 
	 * @return a Connection object
	 * @throws SQLException if database access error occurs
	 */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
	}

	///////////////////////////////////////
	// SELECT							//
	/////////////////////////////////////

	/**
	 * Queries the sqlite3 database for all employee data and constructs an {@code ArrayList<Employee>} with all employees
	 * 
	 * @return an {@code ArrayList<Employee>}
	 */
	public ArrayList<Employee> getEmployees() {

		final String GET_EMPLOYEES = "Select * FROM person INNER JOIN employee ON employee.personID=person.personID;";
		ArrayList<Employee> employees = new ArrayList<Employee>();

		// try-with-resources Connection
		try (Connection connection = getConnection();) {

			// try-with-resources Statement
			try (Statement getEmployeesStatement = connection.createStatement()) {

				// try-with-resources ResultSet
				try (ResultSet resultSet = getEmployeesStatement.executeQuery(GET_EMPLOYEES);) {

					// add all result rows as Employees to employees
					while (resultSet.next()) {
						try {
							employees.add(MODEL.new Employee(
									resultSet.getString("forename"),
									resultSet.getString("surname"),
									resultSet.getString("email"),
									resultSet.getString("phoneNumber"),
									resultSet.getInt("hourlyRateInPence"),
									resultSet.getDouble("hoursPerWeek"),
									LocalDate.parse(resultSet.getString("startDate"), FORMAT_OBJECT),
									( resultSet.getString("endDate") == null || resultSet.getString("endDate").isBlank()) ? null : LocalDate.parse(resultSet.getString("endDate"), FORMAT_OBJECT)));
						} catch (DateTimeException | IllegalArgumentException employeeReadException) {
							System.err.println("DAO: date convertion exception " + employeeReadException.getClass());
							System.err.println(employeeReadException.getMessage());
						}
					}
				}
			}

		} catch (SQLException e) {
			System.err.println("DAO: getEmployees() failed");
			System.err.println(e.getMessage());
		}
		return employees;
	}

	///////////////////////////////////////
	// INSERT							//
	/////////////////////////////////////

	/**
	 * Adds an {@code Employee} to database in tables {@code person} and {@code employee}, linking the employee to the person via the
	 * {@code person.personID}
	 * primary key
	 * 
	 * @param e a Employee
	 */
	public void addEmployee(Employee e) {
		// add person and get it's primary key
		int foreignKey = addPerson(e);

		// if person added successfully
		if (foreignKey != -1) {

			// try-with-resources Connection
			try (Connection connection = getConnection()) {
				
				String addEmployeeSQL = "INSERT INTO employee (personID,hourlyRateInPence,hoursPerWeek,startDate,endDate) VALUES (?,?,?,?,?);";

				// try-with-resources Statement
				try (PreparedStatement addEmployeeStatement = connection.prepareStatement(addEmployeeSQL)) {

					// build statement
					addEmployeeStatement.setInt(1, foreignKey);
					addEmployeeStatement.setInt(2, e.getHourlyRate());
					addEmployeeStatement.setDouble(3, e.getHoursPerWeek());
					addEmployeeStatement.setString(4, e.getStartDateAsLocalDate().format(FORMAT_OBJECT));
					addEmployeeStatement.setString(5, (e.getEndDateAsLocalDate() == null) ? "" : e.getEndDateAsLocalDate().format(FORMAT_OBJECT));

					// add employee to database
					addEmployeeStatement.executeUpdate();
				}

			} catch (SQLException addEmployeeException) {
				System.err.println("DAO: addEmployee() failed");
				System.err.println(addEmployeeException.getMessage());
			}
		}
	}

	/**
	 * Adds a {@code Person} to database in {@code person} table, and return that row's {@code personID} primary key as an int, or -1 in the case of an
	 * unsuccessful operation.
	 * 
	 * @param person a Person object
	 * @return the new person row's primary key (named personID), or -1 if operation unsuccessful
	 * @throws SQLException from getConnection() if method fails
	 */
	private int addPerson(Person person) {

		int personID;
		final String ADD_PERSON = "INSERT INTO person (forename,surname,email,phoneNumber) VALUES (?,?,?,?);";

		// try-with-resources Connection
		try (Connection connection = getConnection();) {

			// try-with-resources PreparedStatement
			try (PreparedStatement addPersonStatement = connection.prepareStatement(ADD_PERSON);) {

				// build PreparedStatement
				addPersonStatement.setString(1, person.getForename());
				addPersonStatement.setString(2, person.getSurname());
				addPersonStatement.setString(3, person.getEmail());
				addPersonStatement.setString(4, person.getPhoneNumber());

				// execute PreparedStatement
				addPersonStatement.executeUpdate();

				// try-with-resources ResultSet
				try (ResultSet rs = addPersonStatement.getGeneratedKeys();) {
					personID = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			personID = -1;
		}
		return personID;
	}

	///////////////////////////////////////
	// Quiet Close overloaded methods	//
	/////////////////////////////////////

	/**
	 * Try to close an SQL object, if it is not null.
	 * 
	 * @param SQLObject
	 */
	private void closeQuietly(AutoCloseable SQLObject) {
		if (SQLObject != null) {
			try {
				SQLObject.close();
			} catch (Exception e) {
			}
		}
	}
}