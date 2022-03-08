package view.windows.humanresources;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import controller.MainController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.HumanResourcesModel;
import model.HumanResourcesModel.Employee;

public class ViewEmployeeDetailControllerEdit extends ViewEmployeeDetailController {
	private DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd")
			.toFormatter();
	private Employee selectedEmployee;
	private HumanResourcesModel model = new HumanResourcesModel();

	@Override
	public void employeeToDatabase() {
		boolean[] validations = super.validateEmployeeDataFromGUI();
		boolean validationsAccepted = true;
		for (boolean b : validations) {
			if (!b) {
				validationsAccepted = false;
				break;
			}
		}
		if (validationsAccepted) {
			Employee e = model.new Employee(
					super.getForename(),
					super.getSurname(),
					super.getEmail(),
					super.getPhoneNumber(),
					(int) (Double.valueOf(super.getHourlyRate()) * 100),
					Double.valueOf(super.getWeeklyHours()),
					super.getStartDate(),
					super.getEndDate());
			super.colorBordersToDefault();
			MainController.editEmployeeInDatabase(selectedEmployee, e);
		} else {
			super.colorBorders(validations);
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText(super.getAndClearValidationMessage());
			alert.show();
		}
	}

	public void setSelectedEmployee(Employee e) {
		this.selectedEmployee = e;
		super.setForenameTextField(selectedEmployee.getForename());
		super.setSurnameTextField(selectedEmployee.getSurname());
		super.setEmailTextField(selectedEmployee.getEmail());
		super.setPhoneNumberTextField(selectedEmployee.getPhoneNumber());
		super.setHourlyRateTextField(String.format("%.2f", selectedEmployee.getHourlyRate() / 100.0));
		super.setWeeklyHoursTextField(String.format("%.2f", selectedEmployee.getHoursPerWeek()));
		super.setStartDateDatePicker(selectedEmployee.getStartDateAsLocalDate());
		super.setEndDateDatePicker(selectedEmployee.getEndDateAsLocalDate());
	}

}
