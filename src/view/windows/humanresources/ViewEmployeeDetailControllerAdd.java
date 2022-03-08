package view.windows.humanresources;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import controller.MainController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import model.HumanResourcesModel;
import model.HumanResourcesModel.Employee;

public class ViewEmployeeDetailControllerAdd extends ViewEmployeeDetailController {

	private static HumanResourcesModel model = new HumanResourcesModel();
	private static DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd")
			.toFormatter();



	@Override
	public void employeeToDatabase() {


		System.out.println();

		boolean[] validations = super.validateEmployeeDataFromGUI();
		String errorMsg = super.getAndClearValidationMessage();

		super.colorBorders(validations);

		boolean inputsAccepted = (errorMsg.isBlank()) ? true : false;


		// attempt to create Employee if inputsAccepted from AddEmployee.fxml
		Employee e = null;
		if (inputsAccepted) {
			try {
				e = model.new Employee(
						super.getForename(),
						super.getSurname(),
						super.getEmail(),
						super.getPhoneNumber(),
						(int) (Double.valueOf(super.getHourlyRate()) * 100),
						Double.valueOf(super.getWeeklyHours()),
						super.getStartDate(),
						super.getEndDate());

				// if employee successfully created then push to database
				if (e != null) {
					// confirm employee data before pushing to database
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("Add Employee Confirmation");
					System.out.println("Email: " + e.getEmail());
					alert.setContentText(String.format("Is this employee data correct?%n%s", e.toString()));
					if (alert.showAndWait().get() == ButtonType.OK) {
						MainController.addEmployeeToDatabase(e);
						/*
						 * dont have a million sinhle use methods in controller. have a Controller.getView().getHr().refersh() type deal?
						 */
					}
				}
			} catch (IllegalArgumentException ex) {
				errorMsg += ex.getMessage();
			}
		}

		//		if (!inputsAccepted || errorMsg != null) {
		//			Alert alert = new Alert(AlertType.ERROR);
		//			alert.setContentText((errorMsg != null) ? errorMsg : "Please fill in required fields");
		//			alert.show();
		//		}
	}

}
