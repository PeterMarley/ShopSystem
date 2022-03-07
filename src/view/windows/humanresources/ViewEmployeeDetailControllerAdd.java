package view.windows.humanresources;

import java.io.IOException;
import java.time.LocalDate;

import controller.MainController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.LocalDateTimeStringConverter;
import model.HumanResourcesModel;
import model.HumanResourcesModel.Employee;

public class ViewEmployeeDetailControllerAdd extends ViewEmployeeDetailController {

	private static Double hourlyRate, weeklyHours;

	private static HumanResourcesModel model = new HumanResourcesModel();

	private boolean[] validateEmployeeDataFromGUI() {
		boolean[] validations = new boolean[super.getNumberOfFieldsToValidate()];

		// validate data inputed on AddEmployee.fxml was of acceptable format

		// forename is not empty
		if (!super.getForename().isBlank()) {
			validations[0] = true;
		}

		// surname is not empty
		if (!super.getSurname().isBlank()) {
			validations[1] = true;
		}

		// email
		validations[2] = true;

		// phone number
		validations[3] = true;

		// hourly rate entered as standard currency double, and converted from pounds to pence (8.51 -> 851)
		if (!super.getHourlyRate().isBlank()) {
			try {
				hourlyRate = Double.valueOf((super.getHourlyRate()));
				validations[4] = true;
			} catch (NumberFormatException ex) {
			}
		}

		// weekly hours entered as a double
		if (!super.getWeeklyHours().isBlank()) {
			try {
				weeklyHours = Double.valueOf(super.getWeeklyHours());
				validations[5] = true;
			} catch (NumberFormatException ex) {
			}
		}

		// ensure start date is not empty
		if (super.getStartDate() != null && super.getStartDate() instanceof LocalDate) {
			validations[6] = true;
		}

		if (validations[6] && super.getEndDate() != null && super.getEndDate() instanceof LocalDate && super.getEndDate().isAfter(super.getStartDate())) {
			validations[7] = true;
		} else if (super.getEndDate() == null) {
			validations[7] = true;
		}

		return validations;

	}

	@Override
	public void employeeToDatabase() {
		LocalDateTimeStringConverter c = new LocalDateTimeStringConverter();
		System.out.println();

		boolean[] validations = validateEmployeeDataFromGUI();
		boolean inputsAccepted = true;
		for (boolean b : validations) {
			if (!b) {
				inputsAccepted = false;
				break;
			}
		}
		colorBorders(validations);

		// attempt to create Employee if inputsAccepted from AddEmployee.fxml
		Employee e = null;
		String errorMsg = null;
		if (inputsAccepted) {
			try {
				e = model.new Employee(
						super.getForename(),
						super.getSurname(),
						super.getEmail(),
						super.getPhoneNumber(),
						(int) (hourlyRate * 100),
						weeklyHours,
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
						MainController.getDatabase().addEmployee(e);
						MainController.getView().getViewHumanResources().refresh();
						MainController.getView().getViewHumanResources().getEmployeeDetailAdd().getStage().hide();
						/*
						 * dont have a million sinhle use methods in controller. have a Controller.getView().getHr().refersh() type deal?
						 */
					}
				}
			} catch (IllegalArgumentException ex) {
				errorMsg = ex.getMessage();
			}
		}

		if (!inputsAccepted || errorMsg != null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText((errorMsg != null) ? errorMsg : "Please fill in required fields");
			alert.show();
		}
	}

	private void colorBorders(boolean[] validations) {
		String borderRed = "-fx-border-color: red;";
		String borderBlack = "-fx-border-color: black;";
		String fillRed = "-fx-background-color: #ffc2ca;";
		String fillWhite = "-fx-background-color: white;";

		if (validations != null && validations.length == super.getNumberOfFieldsToValidate()) {
			if (!validations[0]) {
				super.getForenameTextField().setStyle(borderRed + fillRed);
			} else {
				super.getForenameTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[1]) {
				super.getSurnameTextField().setStyle(borderRed + fillRed);
			} else {
				super.getSurnameTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[2]) {
				super.getEmailTextField().setStyle(borderRed + fillRed);
			} else {
				super.getEmailTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[3]) {
				super.getPhoneNumberTextField().setStyle(borderRed + fillRed);
			} else {
				super.getPhoneNumberTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[4]) {
				super.getHourlyRateTextField().setStyle(borderRed + fillRed);
			} else {
				super.getHourlyRateTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[5]) {
				super.getWeeklyHoursTextField().setStyle(borderRed + fillRed);
			} else {
				super.getWeeklyHoursTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[6]) {
				super.getStartDateDatePicker().setStyle(borderRed + fillRed);
			} else {
				super.getStartDateDatePicker().setStyle(borderBlack + fillWhite);
			}
			if (!validations[7]) {
				super.getEndDateDatePicker().setStyle(borderRed + fillRed);
			} else {
				super.getEndDateDatePicker().setStyle(borderBlack + fillWhite);
			}
		}
	}

}
