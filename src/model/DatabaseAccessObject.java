package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;

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
	private DateTimeFormatter DATE_FORMATTER;
	private final HumanResourcesModel model = new HumanResourcesModel();

	/**
	 * Construct DataBaseAccessObject.
	 * 
	 * @param databaseFilepath - the relative filepath to the sqlite3 database
	 */
	public DatabaseAccessObject(String databaseFilepath) throws SQLException {
		this.setDatabaseLocation(databaseFilepath);
		this.DATE_FORMATTER = new DateTimeFormatterBuilder().appendPattern("dd-MM-yyyy").toFormatter(Locale.ENGLISH);
	}

	/**
	 * Sets the database relative filepath
	 * 
	 * @param dbFilepath
	 */
	private void setDatabaseLocation(String dbFilepath) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilepath);
		System.out.println("Model: Database connection valid! (" + LocalDateTime.now() + ")@ " + dbFilepath);
		connection.close();
		this.dbFilepath = dbFilepath;
	}

	public void closeConnection() {
		try {
			getConnection().close();
		} catch (SQLException e) {
			System.err.println("Model: DAO connection close threw SQLException!");
			System.out.println(e.getMessage());
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
		Connection connection = null;
		Statement getEmployeesStatement = null;
		ResultSet resultSet = null;
		try {
			// configure SQL objects
			connection = getConnection();
			getEmployeesStatement = connection.createStatement();
			getEmployeesStatement.execute("Select * FROM person INNER JOIN employee ON employee.personID=person.personID;");
			resultSet = getEmployeesStatement.getResultSet();
			// process results
			while (resultSet.next()) {
				employees.add(model.new Employee(
						resultSet.getString("forename"),
						resultSet.getString("surname"),
						resultSet.getString("email"),
						resultSet.getString("phoneNumber"),
						resultSet.getInt("hourlyRateInPence"),
						resultSet.getDouble("hoursPerWeek"),
						LocalDate.parse(resultSet.getString("startDate"), DATE_FORMATTER),
						LocalDate.parse(resultSet.getString("endDate"), DATE_FORMATTER)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(resultSet, getEmployeesStatement, connection);
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
		Connection connection = null;
		PreparedStatement addEmployeeStatement = null;
		try {
			// add person superclass fields to database, and return the primary key (personID) - to be used as a foreign key
			int foreignKey = addPerson(e);

			// configure SQL objects
			connection = getConnection();
			if (e.getEndDateAsLocalDate() == null) {
				addEmployeeStatement = connection.prepareStatement(
						"INSERT INTO employee (hourlyRateInPence,hoursPerWeek,startDate,personID)" +
								" VALUES (?,?,?,?);");
			} else {
				addEmployeeStatement = connection.prepareStatement(
						"INSERT INTO employee (hourlyRateInPence,hoursPerWeek,startDate,personID,endDate)" +
								" VALUES (?,?,?,?,?);");
				addEmployeeStatement.setString(5, e.getEndDateAsLocalDate().format(DATE_FORMATTER));

			}
			addEmployeeStatement.setInt(1, e.getHourlyRate());
			addEmployeeStatement.setDouble(2, e.getHoursPerWeek());
			addEmployeeStatement.setString(3, e.getStartDateAsLocalDate().format(DATE_FORMATTER));
			addEmployeeStatement.setInt(4, foreignKey);

			// add employee to database
			addEmployeeStatement.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			// close SQL objects quietly
			closeQuietly(null, addEmployeeStatement, connection);
		}
	}

	/**
	 * Adds a {@code Person} to database in {@code person} table, and return that row's {@code personID} primary key as an int
	 * 
	 * @param p a Person object
	 * @return the new person row's primary key (named personID)
	 * @throws SQLException from getConnection() if method fails
	 */
	private int addPerson(Person p) throws SQLException {
		// configure SQL objects
		Connection con = getConnection();
		PreparedStatement addPersonStatement = con.prepareStatement("INSERT INTO person (forename,surname,email,phonenumber) VALUES(?,?,?,?);");

		// add person to database
		addPersonStatement.setString(1, p.getForename());
		addPersonStatement.setString(2, p.getSurname());
		addPersonStatement.setString(3, p.getEmail());
		addPersonStatement.setString(4, p.getPhoneNumber());
		addPersonStatement.executeUpdate();
		ResultSet rs = addPersonStatement.getGeneratedKeys();
		int key = rs.getInt(1);

		// close SQL objects quietly
		closeQuietly(rs, addPersonStatement, con);

		// return personID of the new row
		return key;
	}

	///////////////////////////////////////
	// Quiet Close overloaded methods	//
	/////////////////////////////////////

	/**
	 * Try to close an SQL connection object, if it is not null.
	 * 
	 * @param connection
	 */
	private void closeQuietly(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Try to close an SQL statement object, if it is not null
	 * 
	 * @param statement
	 */
	private void closeQuietly(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Try to close an SQL ResultSet object, if it is not null
	 * 
	 * @param resultSet
	 */
	private void closeQuietly(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Try to close an SQL PreparedStatement objecgt, if it is not null
	 * 
	 * @param ps
	 */
	private void closeQuietly(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Try to close SQL Statement, ResultSet, and Connection objects, if they are not null
	 * 
	 * @param statement
	 * @param resultSet
	 * @param connection
	 */
	private void closeQuietly(ResultSet resultSet, Statement statement, Connection connection) {
		// safe order: ResultSet > Statement > Connection
		closeQuietly(resultSet);
		closeQuietly(statement);
		closeQuietly(connection);
	}

	//	/**
	//	 * Try to close SQL PreparedStatement, ResultSet, and Connection objects, if they are not null
	//	 * 
	//	 * @param preparedStatement
	//	 * @param resultSet
	//	 * @param connection
	//	 */
	//	private void closeQuietly(PreparedStatement preparedStatement, ResultSet resultSet, Connection connection) {
	//		closeQuietly(preparedStatement);
	//		closeQuietly(resultSet);
	//		closeQuietly(connection);
	//	}
}