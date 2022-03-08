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

/**
 * Abstract Controller class for ViewEmployeeDetail objects
 * 
 * @author Peter Marley
 * @StudentNumber 13404067
 * @Email pmarley03@qub.ac.uk
 * @GitHub https://github.com/PeterMarley
 *
 */
public abstract class ViewEmployeeDetailController {

	// JavaFX components accessed via FXML annotations
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

	private String validationErrorMsg = "";
	private static final int FIELDS_TO_VALIDATE = 8;

	//**************************************************************\
	//																*
	//		JavaFX Control manipulation methods										*
	//																*
	//**************************************************************/

	/**
	 * A abstract method for pushing a new employee to database, or editing the current one
	 */
	public abstract void employeeToDatabase();

	/**
	 * The number of JavaFX Controls to validate data from
	 * 
	 * @return
	 */
	protected int getNumberOfFieldsToValidate() {
		return FIELDS_TO_VALIDATE;
	}

	/**
	 * Set all JavaFX controls to empty, and set borders and fills to default (black 1px border white fill)
	 */
	public void clearFields() {
		this.setForenameTextField("");
		this.setSurnameTextField("");
		this.setEmailTextField("");
		this.setPhoneNumberTextField("");
		this.setHourlyRateTextField("");
		this.setWeeklyHoursTextField("");
		this.setStartDateDatePicker(null);
		this.setEndDateDatePicker(null);
		colorBordersToDefault();
	}

	void colorBordersToDefault() {
		colorBorders(new boolean[] { true, true, true, true, true, true, true, true });
	}

	/**
	 * Colour JavaFX TextField and DatePicker controls depending on data validation status. Each element of the boolean corresponds to a JavaFX control in
	 * this SceneGraph.
	 * If false at that position, colour borders red and fill with lighter red. If true at that position colour borders black and fill with white.<br>
	 * 
	 * @param validations a boolean[] containing the validation status of the following JavaFX controls:<br>
	 *                    0 - (TextField) textFieldForename<br>
	 *                    1 - (TextField) textFieldSurname<br>
	 *                    2 - (TextField) textFieldEmail<br>
	 *                    3 - (TextField) textFieldPhoneNumber<br>
	 *                    4 - (TextField) textFieldHourlyRate<br>
	 *                    5 - (TextField) textFieldWeeklyHours<br>
	 *                    6 - (DatePicker) datePickerStartDate<br>
	 *                    7 - (DatePicker) datePickerEndDate<br>
	 */
	void colorBorders(boolean[] validations) {
		// default colours
		String borderBlack = "-fx-border-color: black;";
		String fillWhite = "-fx-background-color: white;";

		// failed validation colours
		String borderRed = "-fx-border-color: red;";
		String fillRed = "-fx-background-color: #ffc2ca;";

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
		} else if (validations == null) {
			System.err.println("ViewEmployeeDetailController.colorBorders(boolean[]) - parameter array was null");
		} else {
			System.err.println("ViewEmployeeDetailController.colorBorders(boolean[]) - parameter array was incorrect length. Was " + validations.length + " but should be " + FIELDS_TO_VALIDATE);
		}
	}

	/**
	 * This method pulls data from the ViewEmployeeDetail window and returns a boolean[] containing the validation status of each JavaFX Control's data
	 * 
	 * @return validations a boolean[] containing the validation status of the following JavaFX controls:<br>
	 *         0 - (TextField) textFieldForename<br>
	 *         1 - (TextField) textFieldSurname<br>
	 *         2 - (TextField) textFieldEmail<br>
	 *         3 - (TextField) textFieldPhoneNumber<br>
	 *         4 - (TextField) textFieldHourlyRate<br>
	 *         5 - (TextField) textFieldWeeklyHours<br>
	 *         6 - (DatePicker) datePickerStartDate<br>
	 *         7 - (DatePicker) datePickerEndDate<br>
	 */
	protected boolean[] validateEmployeeDataFromGUI() {
		boolean[] validations = new boolean[getNumberOfFieldsToValidate()];
		// validate data inputed on AddEmployee.fxml was of acceptable format

		// validate forename
		try {
			HumanResourcesModelValidator.name(getForename());
			validations[0] = true;
		} catch (IllegalArgumentException illegalForenameEx) {
			validationErrorMsg += String.format("%s%n", illegalForenameEx.getMessage());
		}

		// validate surname
		try {
			HumanResourcesModelValidator.name(getSurname());
			validations[1] = true;
		} catch (IllegalArgumentException illegalSurnameEx) {
			validationErrorMsg += String.format("%s%n", illegalSurnameEx.getMessage());
		}

		// validate email
		try {
			HumanResourcesModelValidator.email(getEmail());
			validations[2] = true;
		} catch (IllegalArgumentException illegalEmailEx) {
			validationErrorMsg += String.format("%s%n", illegalEmailEx.getMessage());
		}

		// validate phone number
		try {
			HumanResourcesModelValidator.phoneNumber(getPhoneNumber());
			validations[3] = true;
		} catch (IllegalArgumentException illegalPhoneNumEx) {
			validationErrorMsg += String.format("%s%n", illegalPhoneNumEx.getMessage());
		}

		// hourly rate entered as standard currency double, and converted from pounds to pence (8.51 -> 851)
		try {
			HumanResourcesModelValidator.hourlyRateInPence((int) (Double.valueOf(getHourlyRate()) * 100));
			validations[4] = true;
		} catch (NumberFormatException invalidDoubleEx) {
			validationErrorMsg += String.format("%s%n", getHourlyRate() + " is not a valid decimal");
		} catch (IllegalArgumentException illegalHourlyRateEx) {
			validationErrorMsg += String.format("%s%n", illegalHourlyRateEx.getMessage());
		}

		// weekly hours entered as a double
		try {
			HumanResourcesModelValidator.hoursPerWeek(Double.valueOf(getWeeklyHours()));
			validations[5] = true;
		} catch (NumberFormatException invalidDoubleEx) {
			validationErrorMsg += String.format("%s%n", getWeeklyHours() + " is not a valid decimal");
		} catch (IllegalArgumentException illegalWeeklyHoursEx) {
			validationErrorMsg += String.format("%s%n", illegalWeeklyHoursEx.getMessage());
		}

		// ensure start date is not empty
		try {
			HumanResourcesModelValidator.startDate(getStartDate());
			validations[6] = true;
		} catch (IllegalArgumentException illegalStartDate) {
			validationErrorMsg += String.format("%s%n", illegalStartDate.getMessage());
		}

		try {
			HumanResourcesModelValidator.endDate(getStartDate(), getEndDate());
			validations[7] = true;
		} catch (IllegalArgumentException illegalEndDateEx) {
			validationErrorMsg += String.format("%s%n", illegalEndDateEx.getMessage());
		}

		return validations;

	}

	//**************************************************************\
	//																*
	//		Getters and Setters										*
	//																*
	//**************************************************************/

	/**
	 * Get the validationErrorMsg value, and then set to blank. Allows children of this class to access the validationErrorMsg
	 * 
	 * @return a String - the validation error message
	 */
	protected String getAndClearValidationMessage() {
		String msg = validationErrorMsg;
		validationErrorMsg = "";
		return msg;
	}

	/**
	 * @return the textFieldForename text
	 */
	protected String getForename() {
		return this.textFieldForename.getText();
	}

	/**
	 * Sets the textFieldForename text to forename
	 * 
	 * @param forename
	 */
	void setForenameTextField(String forename) {
		this.textFieldForename.setText(forename);
	}

	/**
	 * @return the textFieldForename JavaFX Control object
	 */
	TextField getForenameTextField() {
		return this.textFieldForename;
	}

	/**
	 * @return the textFieldSurname text
	 */
	protected String getSurname() {
		return this.textFieldSurname.getText();
	}

	/**
	 * Sets the textFieldSurname text to surname
	 * 
	 * @param surname
	 */
	void setSurnameTextField(String surname) {
		this.textFieldSurname.setText(surname);
	}

	/**
	 * @return the textFieldSurname JavaFX Control object
	 */
	TextField getSurnameTextField() {
		return this.textFieldSurname;
	}

	/**
	 * @return the textFieldEmail text
	 */
	protected String getEmail() {
		return this.textFieldEmail.getText();
	}

	/**
	 * Sets the textFieldEmail text to email
	 * 
	 * @param email
	 */
	void setEmailTextField(String email) {
		this.textFieldEmail.setText(email);
	}

	/**
	 * @return the textFieldEmail JavaFX Control object
	 */
	TextField getEmailTextField() {
		return this.textFieldEmail;
	}

	/**
	 * @return the textFieldPhoneNumber text
	 */
	protected String getPhoneNumber() {
		return this.textFieldPhoneNumber.getText();
	}

	/**
	 * Sets the textFieldPhoneNumber text to phoneNumber
	 * 
	 * @param phoneNumber
	 */
	void setPhoneNumberTextField(String phoneNumber) {
		this.textFieldPhoneNumber.setText(phoneNumber);
	}

	/**
	 * @return the textFieldPhoneNumber JavaFX Control object
	 * 
	 */
	TextField getPhoneNumberTextField() {
		return this.textFieldPhoneNumber;
	}

	/**
	 * @return the textFieldHourlyRate text
	 */
	protected String getHourlyRate() {
		return this.textFieldHourlyRate.getText();
	}

	/**
	 * Sets the textFieldHourlyRate text to hourlyRate
	 * 
	 * @param hourlyRate
	 */
	void setHourlyRateTextField(String hourlyRate) {
		this.textFieldHourlyRate.setText(hourlyRate);
	}

	/**
	 * @return the textFieldHourlyRate JavaFX Control object
	 */
	TextField getHourlyRateTextField() {
		return this.textFieldHourlyRate;
	}

	/**
	 * @return the textFieldWeeklyHours text
	 */
	protected String getWeeklyHours() {
		return this.textFieldWeeklyHours.getText();
	}

	/**
	 * Sets the textFieldWeeklyHours text to weeklyHours
	 * 
	 * @param weeklyHours
	 */
	void setWeeklyHoursTextField(String weeklyHours) {
		this.textFieldWeeklyHours.setText(weeklyHours);
	}

	/**
	 * @return the textFieldWeeklyHours JavaFX Control object
	 */
	TextField getWeeklyHoursTextField() {
		return this.textFieldWeeklyHours;
	}

	/**
	 * @return the datePickerStartDate LocalDate
	 */
	protected LocalDate getStartDate() {
		return this.datePickerStartDate.getValue();
	}

	/**
	 * Set the datePickerStartDate date to startDate
	 * 
	 * @param startDate
	 */
	void setStartDateDatePicker(LocalDate startDate) {
		this.datePickerStartDate.setValue(startDate);
	}

	/**
	 * @return the datePickerStartDate JavaFX Control object
	 */
	DatePicker getStartDateDatePicker() {
		return this.datePickerStartDate;
	}

	/**
	 * @return the datePickerEndDate LocalDate
	 */
	protected LocalDate getEndDate() {
		return this.datePickerEndDate.getValue();
	}

	/**
	 * Set the datePickerEndDate date to endDate
	 * 
	 * @param endDate
	 */
	void setEndDateDatePicker(LocalDate endDate) {
		this.datePickerEndDate.setValue(endDate);
	}

	/**
	 * @return the datePickerEndDate JavaFX Control object
	 */
	DatePicker getEndDateDatePicker() {
		return this.datePickerEndDate;
	}

}
