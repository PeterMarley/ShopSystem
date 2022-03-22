package controller;

import view.View;
import view.windows.humanresources.ViewEmployeeDetailController;
import view.windows.humanresources.ViewEmployeeDetailControllerEdit;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

import javafx.application.Application;
import log.Logger;
import log.LogInterface;
import model.DatabaseAccessObject;
import model.HumanResourcesModel.Employee;

/**
 * This class facilitates interaction between the GUI and data models. It contains the programs {@code main()} method.
 * <hr>
 * <b><i>Components</i></b><br>
 * <br>
 * 
 * <b>View</b><br>
 * A JavaFX GUI containing many subcomponents.<br>
 * - {@link view.View} A container for all the subcomponents of the GUI<br>
 * <br>
 * <b>Model</b> Contains several components: <br>
 * - {@link model.DatabaseAccessObject} allows interaction with an sqlite3 database<br>
 * - {@link model.HumanResourcesModel} contains all person/ human resources based objects<br>
 * - {@link model.StockItemModel} contains all stock (shelf/ floor items, solid fuels, liquid fuels<br>
 * - {@link model.ModelEnums} enumerated types use by model classes<br>
 * - {@link model.shop.db} the actual sqlite3 database
 * 
 * @param args
 */
public class MainController {

	/**
	 * All access to database through this object
	 */
	private static DatabaseAccessObject dao;
	/**
	 * All access to JavaFX GUI through this object
	 */
	private static View view;
	/**
	 * Logging system object
	 */
	private static Logger logger;

	public static void main(String[] args) {
		try {
			logger = Logger.getInstance();
			dao = new DatabaseAccessObject("./src/model/shop.db");
			view = new View();
			Logger.logThis("MainController initialised - starting View");
			Application.launch(View.class);
		} catch (Exception controllerInitException) {
			System.err.println(controllerInitException.getMessage());
			Logger.logThis(controllerInitException);
		} finally {
			Logger.closeLogger();
		}

	}

	//**************************************************************\
	//																*
	//		Database Operations										*
	//																*
	//**************************************************************/

	/**
	 * Queries the database for all employees and return it in the form of an {@code ArrayList<Employee>}
	 * 
	 * @return an {@code ArrayList<Employee>} containing all employees from database
	 */
	public static ArrayList<Employee> getEmployeesFromDatabase() {
		Logger.logThis("Employees retrieved from database");
		return dao.getEmployees();
	}

	/**
	 * Adds an employee to the database
	 * 
	 * @param employee
	 */
	public static void addEmployeeToDatabase(Employee employee) {
		dao.addEmployee(employee);
		view.getViewHumanResources().refreshEmployeeTableView();
		view.getViewHumanResources().getEmployeeDetailAdd().getController().clearFields();
		view.getViewHumanResources().getEmployeeDetailAdd().getStage().close();
	}

	/**
	 * Queries a database an originalEmployee, then updates that with the the editedEmployees data
	 * 
	 * @param originalEmployee
	 * @param editedEmployee
	 */
	public static void editEmployeeInDatabase(Employee originalEmployee, Employee editedEmployee) {
		dao.editEmployee(originalEmployee, editedEmployee);
		view.getViewHumanResources().getEmployeeDetailEdit().getController().clearFields();
		view.getViewHumanResources().getEmployeeDetailEdit().getStage().close();
		view.getViewHumanResources().refreshEmployeeTableView();
	}

	/**
	 * Searches the database for an employeeToDelete, and removes that entry (from both person and employee tables)
	 * 
	 * @param employeeToDelete
	 */
	public static void deleteEmployeeInDatabase() {
		dao.deleteEmployee(getSelectedEmployee());
		view.getViewHumanResources().getEmployeeDeleteConfirm().getStage().close();
		view.getViewHumanResources().refreshEmployeeTableView();
	}

	//**************************************************************\
	//																*
	//		JavaFX View Operations	- Human Resources				*
	//																*
	//**************************************************************/

	/**
	 * Shows the ViewEmployeeDetails window - Add Employee variant
	 */
	public static void showEmployeeDetailAdd() {
		view.getViewHumanResources().getEmployeeDetailAdd().getStage().show();
	}

	/**
	 * Shows the ViewEmployeeDetails window - Edit Employee variant
	 */
	public static void showEmployeeDetailEdit(Employee e) {
		((ViewEmployeeDetailControllerEdit) view.getViewHumanResources().getEmployeeDetailEdit().getController()).setSelectedEmployee(e.clone());
		view.getViewHumanResources().getEmployeeDetailEdit().getStage().show();
	}

	/**
	 * Shows the ViewHumanResources window
	 */
	public static void showHumanResources() {
		view.getViewHumanResources().getStage().show();
	}

	/**
	 * Shows the user a window that requires them to check a CheckBox and confirm the imminent deletion of this employee from the database (both person
	 * and employee tables)
	 * 
	 * @param employeeToDelete
	 */
	public static void showEmployeeConfirmDeletion(Employee employeeToDelete) {
		view.getViewHumanResources().getEmployeeDeleteConfirm().getStage().show();
	}

	/**
	 * Returns the employee currently selection in the Employees tab of the {@link view.windows.humanresources.ViewHumanResources} TableView JavaFX
	 * control
	 * 
	 * @return the currently selected {@code Employee} from the employees {@code TableView} in {@link view.windows.humanresources.ViewHumanResources}
	 */
	public static Employee getSelectedEmployee() {
		return view.getViewHumanResources().getSelectedEmployee();
	}


	//	/**
	//	 * Logs the contents of an Exception
	//	 * 
	//	 * @param exceptionToLog
	//	 */
	//	public static void log.log(Exception exceptionToLog) {
	//		log.log.log(exceptionToLog);
	//	}
	//
	//	/**
	//	 * Logs the contents of a String array
	//	 * 
	//	 * @param messagesToLog
	//	 */
	//	public static void log.log(String[] messagesToLog) {
	//		log.log.log(messagesToLog);
	//	}
	//
	//	/**
	//	 * Logs a String
	//	 * 
	//	 * @param messageToLog
	//	 */
	//	public static void log.log(String messageToLog) {
	//		log.log.log(messageToLog);
	//	}

}
