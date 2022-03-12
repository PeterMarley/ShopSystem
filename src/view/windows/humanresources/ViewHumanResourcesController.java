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

public class ViewHumanResourcesController {
	@FXML
	private TableView<Employee> tableEmployee;
	@FXML
	private TableColumn<Employee, String> forename;
	@FXML
	private TableColumn<Employee, String> surname;
	@FXML
	private TableColumn<Employee, String> email;
	@FXML
	private TableColumn<Employee, String> phoneNumber;
	@FXML
	private TableColumn<Employee, Integer> hourlyRate;
	@FXML
	private TableColumn<Employee, Double> weeklyHours;
	@FXML
	private TableColumn<Employee, String> startDate;
	@FXML
	private TableColumn<Employee, String> endDate;

	@FXML
	public void initialize() {
		TableViewSelectionModel<Employee> s = tableEmployee.getSelectionModel();
		s.setSelectionMode(SelectionMode.SINGLE);
		tableEmployee.setSelectionModel(s);
		fillTableViewEmployees(null);
	}

	/**
	 * Draw employee data from database and display in TableView
	 * 
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	public void fillTableViewEmployees(ActionEvent e) {
		System.out.println("doing a refresh");
		forename.setCellValueFactory(new PropertyValueFactory<>("Forename"));
		surname.setCellValueFactory(new PropertyValueFactory<>("Surname"));
		email.setCellValueFactory(new PropertyValueFactory<>("Email"));
		phoneNumber.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
		hourlyRate.setCellValueFactory(new PropertyValueFactory<>("HourlyRateAsCurrency"));
		weeklyHours.setCellValueFactory(new PropertyValueFactory<>("HoursPerWeek"));
		startDate.setCellValueFactory(new PropertyValueFactory<>("StartDateAsString"));
		endDate.setCellValueFactory(new PropertyValueFactory<>("EndDateAsString"));

		surname.setSortType(SortType.ASCENDING);

		tableEmployee.getSortOrder().setAll(surname);
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

}
