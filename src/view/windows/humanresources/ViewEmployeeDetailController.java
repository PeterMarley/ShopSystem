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
	
	private static final int FIELDS_TO_VALIDATE = 8;	
	
	public abstract void employeeToDatabase();
	
	public int getNumberOfFieldsToValidate() {
		return FIELDS_TO_VALIDATE;
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
	void setTextFieldForename(String forename) {
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
