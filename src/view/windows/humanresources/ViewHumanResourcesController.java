package view.windows.humanresources;

import java.io.IOException;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.HumanResourcesModel.Employee;
import model.HumanResourcesModel.Person;

public class ViewHumanResourcesController {
	// Employee Table
	@FXML
	private TableView<Employee> tableEmployee;
	@FXML
	private TableColumn<Employee, String> employeeForename;
	@FXML
	private TableColumn<Employee, String> employeeSurname;
	@FXML
	private TableColumn<Employee, String> employeeEmail;
	@FXML
	private TableColumn<Employee, String> employeePhoneNumber;
	@FXML
	private TableColumn<Employee, Integer> employeeHourlyRate;
	@FXML
	private TableColumn<Employee, Double> employeeWeeklyHours;
	@FXML
	private TableColumn<Employee, String> employeeStartDate;
	@FXML
	private TableColumn<Employee, String> employeeEndDate;
	
	// Person Table
	@FXML
	private TableView<Person> tablePerson;
	@FXML
	private TableColumn<Person, String> personForename;
	@FXML
	private TableColumn<Person, String> personSurname;
	@FXML
	private TableColumn<Person, String> personEmail;
	@FXML
	private TableColumn<Person, String> personPhoneNumber;
	@FXML
	private TableColumn<Person, Integer> personHourlyRate;
	@FXML
	private TableColumn<Person, Double> personWeeklyHours;
	@FXML
	private TableColumn<Person, String> personStartDate;
	@FXML
	private TableColumn<Person, String> personEndDate;

	@FXML
	public void initialize() {
		TableViewSelectionModel<Employee> s = tableEmployee.getSelectionModel();
		s.setSelectionMode(SelectionMode.SINGLE);
		tableEmployee.setSelectionModel(s);
		fillTableViewEmployees(null);
	}

	//**************************************************************\
	//																*
	//		Employee Tab Operations									*
	//																*
	//**************************************************************/
	
	/**
	 * Draw employee data from database and display in TableView
	 * 
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	public void fillTableViewEmployees(ActionEvent e) {
		System.out.println("doing a refresh");
		employeeForename.setCellValueFactory(new PropertyValueFactory<>("Forename"));
		employeeSurname.setCellValueFactory(new PropertyValueFactory<>("Surname"));
		employeeEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
		employeePhoneNumber.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
		employeeHourlyRate.setCellValueFactory(new PropertyValueFactory<>("HourlyRateAsCurrency"));
		employeeWeeklyHours.setCellValueFactory(new PropertyValueFactory<>("HoursPerWeek"));
		employeeStartDate.setCellValueFactory(new PropertyValueFactory<>("StartDateAsString"));
		employeeEndDate.setCellValueFactory(new PropertyValueFactory<>("EndDateAsString"));

		employeeSurname.setSortType(SortType.ASCENDING);

		tableEmployee.getSortOrder().setAll(employeeSurname);
		SortedList<Employee> employeesModelSorted = new SortedList<>(FXCollections.observableArrayList(MainController.getEmployeesFromDatabase()));
		employeesModelSorted.comparatorProperty().bind(tableEmployee.comparatorProperty());
		//		for (Employee em : employeesModelSorted) {
		//			System.out.println(em.toString());
		//		}
		tableEmployee.setItems(employeesModelSorted);
		tableEmployee.refresh();
	}

	/**
	 * Open the Add Employee window
	 */
	public void addEmployee() {
		MainController.showEmployeeDetailAdd();
	}

	/**
	 * Open the Edit Employee window if an employee has been selected from the {@code tableEmployee}. Otherwise display
	 * an appropriate alert.
	 */
	public void editEmployee() {
		ObservableList<Employee> selectedEmployee = getSelectedEmployeeAsObservableList();
		if (selectedEmployee.size() == 1) {
			Employee e = selectedEmployee.get(0);
			//this.selectedEmployee = e.clone();
			MainController.showEmployeeDetailEdit(e);
		} else {
			alertNoEmployeeSelected("You must select an employee!");
		}
	}

	/**
	 * 
	 */
	public void deleteEmployee() {
		ObservableList<Employee> selectedEmployee = getSelectedEmployeeAsObservableList();
		if (selectedEmployee.size() == 1) {
			Employee e = selectedEmployee.get(0);
			MainController.showEmployeeConfirmDeletion(e);
		} else {
			alertNoEmployeeSelected("You must select an employee!");
		}
		System.out.println("ViewHumanResourcesController.deleteEmployee() stub");
	}

	ObservableList<Employee> getSelectedEmployeeAsObservableList() {
		TableViewSelectionModel<Employee> selection = tableEmployee.getSelectionModel();
		ObservableList<Employee> selectedEmployee = selection.getSelectedItems();
		return selectedEmployee;
	}
	
	Employee getSelectedEmployeeAsEmployee() {
		TableViewSelectionModel<Employee> selection = tableEmployee.getSelectionModel();
		ObservableList<Employee> selectedEmployee = selection.getSelectedItems();
		return selectedEmployee.get(0);
	}

	private void alertNoEmployeeSelected(String alertText) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setContentText(alertText);
		alert.show();
	}
	
	//**************************************************************\
	//																*
	//		Person Tab Operations									*
	//																*
	//**************************************************************/

	public void fillTableViewPeople(ActionEvent e) {
		
	}
	
	public void addPerson() {
		
	}
	
	public void editPerson() {
		
	}
	
	public void deletePerson() {
		
	}
	

}
