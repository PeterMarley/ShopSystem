package controller;

import view.View;
import view.windows.humanresources.ViewEmployeeDetailController;
import view.windows.humanresources.ViewEmployeeDetailControllerEdit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import model.DatabaseAccessObject;
import model.HumanResourcesModel.Employee;

public class MainController {

	private static DatabaseAccessObject dao;
	private static View view;

	public static void main(String[] args) {
		try {
			dao = new DatabaseAccessObject("./src/model/shop.db");
			view = new View();
		} catch (SQLException controllerInitException) {
			System.err.println(controllerInitException.getMessage());
		}
		Application.launch(View.class);

	}

	//**************************************************************\
	//																*
	//		Database Operations										*
	//																*
	//**************************************************************/

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
	
	public static void editEmployeeInDatabase(Employee originalEmployee, Employee editedEmployee) {
		dao.editEmployee(originalEmployee, editedEmployee);
		view.getViewHumanResources().refreshEmployeeTableView();
		view.getViewHumanResources().getEmployeeDetailEdit().getController().clearFields();
		view.getViewHumanResources().getEmployeeDetailEdit().getStage().close();
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
		((ViewEmployeeDetailControllerEdit) view.getViewHumanResources().getEmployeeDetailEdit().getController()).setSelectedEmployee(e);
		view.getViewHumanResources().getEmployeeDetailEdit().getStage().show();
	}

	/**
	 * Shows the ViewHumanResources window
	 */
	public static void showHumanResources() {
		view.getViewHumanResources().getStage().show();
	}

	/**
	 * Queries the database for all employees and return it in the form of an {@code ArrayList<Employee>}
	 * 
	 * @return an {@code ArrayList<Employee>} containing all employees from database
	 */
	public static ArrayList<Employee> getEmployeesFromDatabase() {
		return dao.getEmployees();
	}

}
