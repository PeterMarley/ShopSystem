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
import java.util.Arrays;
import java.util.Locale;

import controller.MainController;
import log.LogInterface;
import log.Logger;
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

	private static final int INVALID_KEY_RETURN = -1;

	/**
	 * Construct DataBaseAccessObject.
	 * 
	 * @param databaseRelativeFilepath - the relative filepath to the sqlite3 database
	 * @param log                      - a LogInterface object. if null, all logging actions are outputted to console.
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
			this.dbFilepath = dbFilepath;
			Logger.logThis("DatabaseAccessObject: setDatabaseLocation() successfully tested database connection and returned it " +
					"to pool! @ " + LocalDateTime.now().format(FORMAT_LOG) + " (DB: " + dbFilepath + ")");
		} catch (SQLException testConnectionEx) {
			Logger.logThis(testConnectionEx);
			throw testConnectionEx;
		}
	}

	/**
	 * Creates and returns an SQL database connection
	 * 
	 * @return a Connection object
	 * @throws SQLException if database access error occurs
	 */
	private Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
		} catch (SQLException getConnectionEx) {
			Logger.logThis(getConnectionEx);
			throw getConnectionEx;
		}
		return connection;
	}

	///////////////////////////////////////
	// SELECT							//
	/////////////////////////////////////

	/**
	 * Queries the sqlite3 database for all employee data and constructs an {@code ArrayList<Employee>} with all employees. If the database operation
	 * fails an empty {@code ArrayList<Employee>} is returned
	 * 
	 * @return an {@code ArrayList<Employee>}
	 */
	public ArrayList<Employee> getEmployees() {

		final String GET_EMPLOYEES = "Select * FROM person INNER JOIN employee ON employee.personID=person.personID;";
		ArrayList<Employee> employees = new ArrayList<Employee>();

		// try-with-resources Connection
		try (Connection connection = getConnection();
				Statement getEmployeesStatement = connection.createStatement();
				ResultSet resultSet = getEmployeesStatement.executeQuery(GET_EMPLOYEES);) {

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
							(resultSet.getString("endDate") == null || resultSet.getString("endDate").isBlank()) ? null : LocalDate.parse(resultSet.getString("endDate"), FORMAT_OBJECT)));
				} catch (DateTimeException | IllegalArgumentException employeeReadException) {
					System.err.println("DAO: date convertion exception " + employeeReadException.getClass());
					System.err.println(employeeReadException.getMessage());
				}
			}
			Logger.logThis(employees.size() + " Employees retrieved from database");

		} catch (SQLException getEmployeesEx) {
			System.err.println("DAO: getEmployees() failed");
			System.err.println(getEmployeesEx.getMessage());
			Logger.logThis(getEmployeesEx);
		}
		return employees;
	}

	///////////////////////////////////////
	// INSERT							//
	/////////////////////////////////////

	/**
	 * Adds an {@code Employee} to database in tables {@code person} and {@code employee}, linking the employee to the person via the
	 * {@code person.personID} value
	 * primary key
	 * 
	 * @param e a Employee
	 */
	public void addEmployee(Employee e) {
		// add person and get it's primary key
		int foreignKey = addPerson(e);
		String addEmployeeSQL = "INSERT INTO employee (personID,hourlyRateInPence,hoursPerWeek,startDate,endDate) VALUES (?,?,?,?,?);";

		// if person added successfully
		if (foreignKey != INVALID_KEY_RETURN) {

			// try-with-resources Connection
			try (Connection connection = getConnection();
					PreparedStatement addEmployeeStatement = connection.prepareStatement(addEmployeeSQL);) {

				// build statement
				addEmployeeStatement.setInt(1, foreignKey);
				addEmployeeStatement.setInt(2, e.getHourlyRate());
				addEmployeeStatement.setDouble(3, e.getHoursPerWeek());
				addEmployeeStatement.setString(4, e.getStartDateAsLocalDate().format(FORMAT_OBJECT));
				addEmployeeStatement.setString(5, (e.getEndDateAsLocalDate() == null) ? "" : e.getEndDateAsLocalDate().format(FORMAT_OBJECT));

				// add employee to database
				addEmployeeStatement.executeUpdate();
				Logger.logThis(new String[] { "Employee pushed to database: " + e.toString(), "personID: " + foreignKey });

			} catch (SQLException addEmployeeException) {
				System.err.println("DAO: addEmployee() failed");
				System.err.println(addEmployeeException.getMessage());
				Logger.logThis(addEmployeeException);
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

		// try-with-resources Connection & statement
		try (Connection connection = getConnection();
				PreparedStatement addPersonStatement = connection.prepareStatement(ADD_PERSON);) {

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
			Logger.logThis("Person pushed to database: " + person.toString(), "personID: " + personID);

		} catch (SQLException addPersonEx) {
			personID = INVALID_KEY_RETURN;
			Logger.logThis(addPersonEx);
		}
		return personID;
	}

	/**
	 * Queries database for person/employee {@code originalEmployee}, and edits it to reflect the {@code editiedEmployee}
	 * 
	 * @param originalEmployee
	 * @param editedEmployee
	 */
	public void editEmployee(Employee originalEmployee, Employee editedEmployee) {
		String getPersonSQL = "SELECT personID FROM person WHERE forename=? AND surname=? AND email=? AND phoneNumber=?;";
		String getEmployeeSQL = "SELECT * FROM employee INNER JOIN person ON person.personID = employee.personID WHERE employee.personID=?;";
		String updatePersonSQL = "UPDATE person SET forename=?, surname=?, email=?, phoneNumber=? WHERE personID=?";
		String updateEmployeeSQL = "UPDATE employee SET hourlyRateInPence=?, hoursPerWeek=?, startDate=?, endDate=? WHERE personID=?";

		// establish connection
		try (Connection connection = getConnection()) {
			// create SQL Query to get personID of this person
			try (PreparedStatement stmtGetPersonId = connection.prepareStatement(getPersonSQL)) {
				stmtGetPersonId.setString(1, originalEmployee.getForename());
				stmtGetPersonId.setString(2, originalEmployee.getSurname());
				stmtGetPersonId.setString(3, originalEmployee.getEmail());
				stmtGetPersonId.setString(4, originalEmployee.getPhoneNumber());
				// execute query
				try (ResultSet rsPersonId = stmtGetPersonId.executeQuery()) {
					// save personID
					int personID = rsPersonId.getInt(1);
					// construct SQL Query to get this employee with this personID
					try (PreparedStatement stmtgetEmployee = connection.prepareStatement(getEmployeeSQL)) {
						stmtgetEmployee.setInt(1, personID);
						// execute query
						try (ResultSet rsGetEmployee = stmtgetEmployee.executeQuery()) {
							// update person and employee row with the new data
							try (PreparedStatement stmtUpdatePerson = connection.prepareStatement(updatePersonSQL);
									PreparedStatement stmtUpdateEmployee = connection.prepareStatement(updateEmployeeSQL);) {
								stmtUpdatePerson.setString(1, editedEmployee.getForename());
								stmtUpdatePerson.setString(2, editedEmployee.getSurname());
								stmtUpdatePerson.setString(3, editedEmployee.getEmail());
								stmtUpdatePerson.setString(4, editedEmployee.getPhoneNumber());
								stmtUpdatePerson.setInt(5, personID);
								stmtUpdatePerson.executeUpdate();
								stmtUpdateEmployee.setInt(1, editedEmployee.getHourlyRate());
								stmtUpdateEmployee.setDouble(2, editedEmployee.getHoursPerWeek());
								stmtUpdateEmployee.setString(3, editedEmployee.getStartDateAsString());
								stmtUpdateEmployee.setString(4, editedEmployee.getEndDateAsString());
								stmtUpdateEmployee.setInt(5, personID);
								stmtUpdateEmployee.executeUpdate();
								Logger.logThis(new String[] {
										"Employee updated in database",
										"Original Employee: " + originalEmployee.toString(),
										"Edited Employee: " + editedEmployee.toString()
								});
							}
						}
					}
				}
			}
		} catch (SQLException | IllegalArgumentException editEmployeeEx) {
			editEmployeeEx.printStackTrace();
			Logger.logThis(editEmployeeEx);
		}
		System.out.println("DAO editEmployee() stub");
		System.out.println("Employee Pre-Edit" + originalEmployee);
		System.out.println("Employee Post-Edit " + editedEmployee);
	}

	/**
	 * Deletes an Employee from employee table (and it's corresponding Person in person table)
	 * 
	 * @param employeeToDelete
	 */
	public void deleteEmployee(Employee employeeToDelete) {
		String getPersonSQL = "SELECT personID FROM person WHERE forename=? AND surname=? AND email=? AND phoneNumber=?;";
		String getPersonSQLConfirmResultNumber = "SELECT COUNT(*) FROM person WHERE forename=? AND surname=? AND email=? AND phoneNumber=?;";
		String deletePersonSQL = "DELETE FROM person WHERE personID=?;";
		String deleteEmployeeSQL = "DELETE FROM employee WHERE personID=?;";
		// try with resources get connection
		try (Connection connection = getConnection()) {
			// try with resources - a statement to get the total number of results returned by the query
			try (PreparedStatement stmtGetPersonIDConfirmResultNumber = connection.prepareStatement(getPersonSQLConfirmResultNumber)) {
				stmtGetPersonIDConfirmResultNumber.setString(1, employeeToDelete.getForename());
				stmtGetPersonIDConfirmResultNumber.setString(2, employeeToDelete.getSurname());
				stmtGetPersonIDConfirmResultNumber.setString(3, employeeToDelete.getEmail());
				stmtGetPersonIDConfirmResultNumber.setString(4, employeeToDelete.getPhoneNumber());
				try (ResultSet rsGetPersonIDConfirmResultNumber = stmtGetPersonIDConfirmResultNumber.executeQuery()) {
					int rowCount = 0;
					while (rsGetPersonIDConfirmResultNumber.next()) {
						rowCount++;
					}
					if (rowCount == 1) {
						try (PreparedStatement stmtGetPersonID = connection.prepareStatement(getPersonSQL)) {
							stmtGetPersonID.setString(1, employeeToDelete.getForename());
							stmtGetPersonID.setString(2, employeeToDelete.getSurname());
							stmtGetPersonID.setString(3, employeeToDelete.getEmail());
							stmtGetPersonID.setString(4, employeeToDelete.getPhoneNumber());
							try (ResultSet rsGetPersonID = stmtGetPersonID.executeQuery()) {
								int personID;
								rsGetPersonID.next();
								personID = rsGetPersonID.getInt(1);
								System.out.println(personID);
								try (PreparedStatement stmtDeletePerson = connection.prepareStatement(deletePersonSQL);
										PreparedStatement stmtDeleteEmployee = connection.prepareStatement(deleteEmployeeSQL)) {
									stmtDeletePerson.setInt(1, personID);
									stmtDeleteEmployee.setInt(1, personID);
									stmtDeletePerson.executeUpdate();
									stmtDeleteEmployee.executeUpdate();
									Logger.logThis(new String[] {
											"Employee deleted from database",
											"Employee: " + employeeToDelete.toString()
									});
								}
							}
						}
					}
				}
			}
		} catch (SQLException deleteEmployeeEx) {
			deleteEmployeeEx.printStackTrace();
			Logger.logThis(deleteEmployeeEx);
		}
	}

	//	private Employee buildEmployeeFromResult(ResultSet rs) throws IllegalArgumentException, SQLException {
	//		return MODEL.new Employee(
	//				rs.getString("forename"),
	//				rs.getString("surname"),
	//				rs.getString("email"),
	//				rs.getString("phoneNumber"),
	//				rs.getInt("hourlyRateInPence"),
	//				rs.getDouble("hoursPerWeek"),
	//				LocalDate.parse(rs.getString("startDate"), FORMAT_OBJECT),
	//				LocalDate.parse(rs.getString("endDate"), FORMAT_OBJECT));
	//	}
}