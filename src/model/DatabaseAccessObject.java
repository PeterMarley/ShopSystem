package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.util.converter.LocalDateStringConverter;
import model.HumanResourcesModel.Employee;
import model.HumanResourcesModel.Person;

/**
 * This DatabaseAccessObject provides methods that allow interaction with an sqlite3 database
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
class DatabaseAccessObject {

	private String dbFilepath;
	private LocalDateStringConverter DATE_CONVERTER = new LocalDateStringConverter();
	private final HumanResourcesModel model = new HumanResourcesModel();

	/**
	 * Construct DataBaseAccessObject.
	 * 
	 * @param databaseFilepath - the relative filepath to the sqlite3 database
	 */
	public DatabaseAccessObject(String databaseFilepath) throws SQLException {
		this.setDatabaseLocation(databaseFilepath);
		this.DATE_CONVERTER = new LocalDateStringConverter();
	}

	/**
	 * Sets the database relative filepath
	 * 
	 * @param dbFilepath
	 */
	private void setDatabaseLocation(String dbFilepath) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
		System.out.println("Model: Database connection valid! @ " + dbFilepath);
		connection.close();
		this.dbFilepath = dbFilepath;
	}

	/**
	 * Creates and returns an SQL database connection
	 * 
	 * @return a Connection object
	 * @throws SQLException if database access error occurs
	 */
	private Connection getConnection() throws SQLException {
		Connection connection = null;
		String url = "jdbc:sqlite:" + dbFilepath;
		connection = DriverManager.getConnection(url);
		return connection;
	}

	/**
	 * Queries the sqlite3 database for all employee data and constructs an {@code ArrayList<Employee>} with all employees
	 * 
	 * @return an {@code ArrayList<Employee>}
	 */
	public ArrayList<Employee> getEmployees() {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		try {
			Statement statement = getConnection().createStatement();
			statement.execute("Select * FROM person INNER JOIN employee ON employee.personID=person.personID;");
			ResultSet results = statement.getResultSet();
			while (results.next()) {
				employees.add(model.new Employee(
						results.getString("forename"),
						results.getString("surname"),
						results.getString("email"),
						results.getString("phoneNumber"),
						results.getInt("hourlyRateInPence"),
						results.getDouble("hoursPerWeek"),
						DATE_CONVERTER.fromString(results.getString("startDate")),
						DATE_CONVERTER.fromString(results.getString("endDate"))));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}

	/**
	 * Adds a {@code Employee} to database in {@code person} {@code employee} tables, linking the employee to the person via the {@code person.personID}
	 * primary key
	 * 
	 * @param e a Employee
	 */
	public void addEmployee(Employee e) {
		try {
			int personID = addPerson(e);
			PreparedStatement addEmployeeStatement = null;
			if (e.getEndDateAsLocalDate() == null) {
				addEmployeeStatement = getConnection().prepareStatement(
						"INSERT INTO employee (hourlyRateInPence,hoursPerWeek,startDate,personID)" +
								" VALUES (?,?,?,?);");
			} else {
				addEmployeeStatement = getConnection().prepareStatement(
						"INSERT INTO employee (hourlyRateInPence,hoursPerWeek,startDate,personID,endDate)" +
								" VALUES (?,?,?,?,?);");
				addEmployeeStatement.setString(5, DATE_CONVERTER.toString(e.getEndDateAsLocalDate()));

			}
			addEmployeeStatement.setInt(1, e.getHourlyRate());
			addEmployeeStatement.setDouble(2, e.getHoursPerWeek());
			addEmployeeStatement.setString(3, DATE_CONVERTER.toString(e.getStartDateAsLocalDate()));
			addEmployeeStatement.setInt(4, personID);
			addEmployeeStatement.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Adds a {@code Person} to database in {@code person} table, and return that row's {@code personID} primary key as an int
	 * 
	 * @param p a Person object
	 * @return the new person row's primary key (named personID)
	 * @throws SQLException if getConnection() fails
	 */
	private int addPerson(Person p) throws SQLException {
		String sql = "INSERT INTO person (forename,surname,email,phonenumber) VALUES(?,?,?,?);";
		PreparedStatement addPersonStatement = getConnection().prepareStatement(sql);
		addPersonStatement.setString(1, p.getForename());
		addPersonStatement.setString(2, p.getSurname());
		addPersonStatement.setString(3, p.getEmail());
		addPersonStatement.setString(4, p.getPhoneNumber());
		addPersonStatement.executeUpdate();
		ResultSet rs = addPersonStatement.getGeneratedKeys();
		return rs.getInt(1);
	}

}