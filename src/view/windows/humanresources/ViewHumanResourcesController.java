package view.windows.humanresources;

import java.io.IOException;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
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
		fillTableEmployees(null);
	}

	/**
	 * Draw employee data from database and display in TableView
	 * 
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	public void fillTableEmployees(ActionEvent e) {
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
		tableEmployee.setItems(employeesModelSorted);
	}

	public void addEmployee() {
		MainController.showEmployeeDetailAdd();
	}

	public void editEmployee() {
		//Employee e = tableEmployee.getSelectionModel().getSelectedItem();
		//MainController.getView().showHumanResourcesEditEmployee(e);
		System.out.println("HrController.editEmployee() stub");
	}

}
