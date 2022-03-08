package view.windows.humanresources;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import model.HumanResourcesModel;
import model.HumanResourcesModel.Employee;
import model.HumanResourcesModel.HumanResourcesModelValidator;

public abstract class ViewEmployeeDetailController {
	// TextFields from AddEmployee.fxml
	@FXML
	private TextField textFieldForename;
	@FXML
	private TextField textFieldSurname;
	@FXML
	private TextField textFieldEmail;
	@FXML
	private TextField textFieldPhoneNumber;
	@FXML
	private TextField textFieldHourlyRate;
	@FXML
	private TextField textFieldWeeklyHours;
	@FXML
	private DatePicker datePickerStartDate;
	@FXML
	private DatePicker datePickerEndDate;

	public static final int FIELDS_TO_VALIDATE = 8;
	private String validationErrorString = "";

	public abstract void employeeToDatabase();

	public int getNumberOfFieldsToValidate() {
		return FIELDS_TO_VALIDATE;
	}

	public void clearFields() {
		this.setForenameTextField("");
		this.setSurnameTextField("");
		this.setEmailTextField("");
		this.setPhoneNumberTextField("");
		this.setHourlyRateTextField("");
		this.setWeeklyHoursTextField("");
		this.setStartDateDatePicker(null);
		this.setEndDateDatePicker(null);
		colorBordersBlack();
	}

	private void colorBordersBlack() {
		colorBorders(new boolean[] { true, true, true, true, true, true, true, true });
	}

	void colorBorders(boolean[] validations) {
		String borderRed = "-fx-border-color: red;";
		String borderBlack = "-fx-border-color: black;";
		String fillRed = "-fx-background-color: #ffc2ca;";
		String fillWhite = "-fx-background-color: white;";

		if (validations != null && validations.length == getNumberOfFieldsToValidate()) {
			if (!validations[0]) {
				this.getForenameTextField().setStyle(borderRed + fillRed);
			} else {
				this.getForenameTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[1]) {
				this.getSurnameTextField().setStyle(borderRed + fillRed);
			} else {
				this.getSurnameTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[2]) {
				this.getEmailTextField().setStyle(borderRed + fillRed);
			} else {
				this.getEmailTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[3]) {
				this.getPhoneNumberTextField().setStyle(borderRed + fillRed);
			} else {
				this.getPhoneNumberTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[4]) {
				this.getHourlyRateTextField().setStyle(borderRed + fillRed);
			} else {
				this.getHourlyRateTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[5]) {
				this.getWeeklyHoursTextField().setStyle(borderRed + fillRed);
			} else {
				this.getWeeklyHoursTextField().setStyle(borderBlack + fillWhite);
			}
			if (!validations[6]) {
				this.getStartDateDatePicker().setStyle(borderRed + fillRed);
			} else {
				this.getStartDateDatePicker().setStyle(borderBlack + fillWhite);
			}
			if (!validations[7]) {
				this.getEndDateDatePicker().setStyle(borderRed + fillRed);
			} else {
				this.getEndDateDatePicker().setStyle(borderBlack + fillWhite);
			}
		}
	}

	protected String getAndClearValidationMessage() {
		String msg = validationErrorString;
		validationErrorString = "";
		return msg;
	}
	
	protected boolean[] validateEmployeeDataFromGUI() {
		boolean[] validations = new boolean[getNumberOfFieldsToValidate()];
		// validate data inputed on AddEmployee.fxml was of acceptable format

		// validate forename
		try {
			HumanResourcesModelValidator.name(getForename());
			validations[0] = true;
		} catch (IllegalArgumentException illegalForenameEx) {
			validationErrorString += String.format("%s%n", illegalForenameEx.getMessage());
		}

		// validate surname
		try {
			HumanResourcesModelValidator.name(getSurname());
			validations[1] = true;
		} catch (IllegalArgumentException illegalSurnameEx) {
			validationErrorString += String.format("%s%n", illegalSurnameEx.getMessage());
		}

		// validate email
		try {
			HumanResourcesModelValidator.email(getEmail());
			validations[2] = true;
		} catch (IllegalArgumentException illegalEmailEx) {
			validationErrorString += String.format("%s%n", illegalEmailEx.getMessage());
		}

		// validate phone number
		try {
			HumanResourcesModelValidator.phoneNumber(getPhoneNumber());
			validations[3] = true;
		} catch (IllegalArgumentException illegalPhoneNumEx) {
			validationErrorString += String.format("%s%n", illegalPhoneNumEx.getMessage());
		}

		// hourly rate entered as standard currency double, and converted from pounds to pence (8.51 -> 851)
		try {
			HumanResourcesModelValidator.hourlyRateInPence((int) (Double.valueOf(getHourlyRate()) * 100));
			validations[4] = true;
		} catch (NumberFormatException invalidDoubleEx) {
			validationErrorString += String.format("%s%n", getHourlyRate() + " is not a valid decimal");
		} catch (IllegalArgumentException illegalHourlyRateEx) {
			validationErrorString += String.format("%s%n", illegalHourlyRateEx.getMessage());
		}

		// weekly hours entered as a double
		try {
			HumanResourcesModelValidator.hoursPerWeek(Double.valueOf(getWeeklyHours()));
			validations[5] = true;
		} catch (NumberFormatException invalidDoubleEx) {
			validationErrorString += String.format("%s%n", getWeeklyHours() + " is not a valid decimal");
		} catch (IllegalArgumentException illegalWeeklyHoursEx) {
			validationErrorString += String.format("%s%n", illegalWeeklyHoursEx.getMessage());
		}

		// ensure start date is not empty
		try {
			HumanResourcesModelValidator.startDate(getStartDate());
			validations[6] = true;
		} catch (IllegalArgumentException illegalStartDate) {
			validationErrorString += String.format("%s%n", illegalStartDate.getMessage());
		}

		try {
			HumanResourcesModelValidator.endDate(getStartDate(), getEndDate());
			validations[7] = true;
		} catch (IllegalArgumentException illegalEndDateEx) {
			validationErrorString += String.format("%s%n", illegalEndDateEx.getMessage());
		}

		return validations;

	}
	
	/**
	 * @return the textFieldForename text
	 */
	public String getForename() {
		return this.textFieldForename.getText();
	}

	/**
	 * @param textFieldForename the textFieldForename to set
	 */
	void setForenameTextField(String forename) {
		this.textFieldForename.setText(forename);
	}

	/**
	 * @param textFieldForename the textFieldForename to set
	 */
	TextField getForenameTextField() {
		return this.textFieldForename;
	}

	/**
	 * @return the textFieldSurname text
	 */
	public String getSurname() {
		return this.textFieldSurname.getText();
	}

	/**
	 * @param textFieldSurname the textFieldSurname to set
	 */
	void setSurnameTextField(String surname) {
		this.textFieldSurname.setText(surname);
	}

	/**
	 * @param textFieldForename the textFieldForename to set
	 */
	TextField getSurnameTextField() {
		return this.textFieldSurname;
	}

	/**
	 * @return the textFieldEmail text
	 */
	public String getEmail() {
		return this.textFieldEmail.getText();
	}

	/**
	 * @param textFieldEmail the textFieldEmail to set
	 */
	void setEmailTextField(String email) {
		this.textFieldEmail.setText(email);
	}

	/**
	 * @param textFieldEmail the textFieldEmail to set
	 */
	TextField getEmailTextField() {
		return this.textFieldEmail;
	}

	/**
	 * @return the textFieldPhoneNumber text
	 */
	public String getPhoneNumber() {
		return this.textFieldPhoneNumber.getText();
	}

	/**
	 * @param textFieldPhoneNumber the textFieldPhoneNumber to set
	 */
	void setPhoneNumberTextField(String phoneNumber) {
		this.textFieldPhoneNumber.setText(phoneNumber);
	}

	/**
	 * @return the textFieldPhoneNumber text
	 */
	TextField getPhoneNumberTextField() {
		return this.textFieldPhoneNumber;
	}

	/**
	 * @return the textFieldHourlyRate text
	 */
	public String getHourlyRate() {
		return this.textFieldHourlyRate.getText();
	}

	/**
	 * @param textFieldHourlyRate the textFieldHourlyRate to set
	 */
	void setHourlyRateTextField(String hourlyRate) {
		this.textFieldHourlyRate.setText(hourlyRate);
	}

	/**
	 * @return the textFieldHourlyRate text
	 */
	TextField getHourlyRateTextField() {
		return this.textFieldHourlyRate;
	}

	/**
	 * @return the textFieldWeeklyHours text
	 */
	public String getWeeklyHours() {
		return this.textFieldWeeklyHours.getText();
	}

	/**
	 * @return the textFieldWeeklyHours text
	 */
	TextField getWeeklyHoursTextField() {
		return this.textFieldWeeklyHours;
	}

	/**
	 * @param textFieldWeeklyHours the textFieldWeeklyHours to set
	 */
	void setWeeklyHoursTextField(String weeklyHours) {
		this.textFieldWeeklyHours.setText(weeklyHours);
	}

	/**
	 * @return the datePickerStartDate text
	 */
	public LocalDate getStartDate() {
		return this.datePickerStartDate.getValue();
	}

	/**
	 * @param datePickerStartDate the datePickerStartDate to set
	 */
	void setStartDateDatePicker(LocalDate startDate) {
		this.datePickerStartDate.setValue(startDate);
	}

	/**
	 * @return the datePickerStartDate text
	 */
	DatePicker getStartDateDatePicker() {
		return this.datePickerStartDate;
	}

	/**
	 * @return the datePickerEndDate text
	 */
	public LocalDate getEndDate() {
		return this.datePickerEndDate.getValue();
	}

	/**
	 * @param datePickerEndDate the datePickerEndDate to set
	 */
	void setEndDateDatePicker(LocalDate endDate) {
		this.datePickerEndDate.setValue(endDate);
	}

	/**
	 * @return the datePickerEndDate text
	 */
	public DatePicker getEndDateDatePicker() {
		return this.datePickerEndDate;
	}

}
