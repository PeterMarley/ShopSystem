package model;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.converter.LocalDateStringConverter;

import model.stock.STKAbstractItem;
import model.stock.STKEnums.Categories;

public class HumanResourcesModel {

	/**
	 * Constant numbers for HREmployee class
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum EmployeeNumbers {
		INT_MIN_WAGE(800),
		INT_MIN_WEEKLY_HOURS(0);

		private int number;

		private EmployeeNumbers(int number) {
			this.number = number;
		}

		public int get() {
			return this.number;
		}
	}

	/**
	 * Constant Strings for HREmployee class
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public enum EmployeeStrings {
		MSG_HOURLY_RATE_INVALID("HREmployee Constructor: Specified hourly rate is invalid "),
		MSG_HOURS_PER_WEEK_INVALID("HREmployee Constructor: specified hoursPerWeek was a negative number"),
		MSG_DATE_OF_START_NULL("HREmployee Constructor: Date of Employment Start was null"),
		MSG_DATE_OF_END_IMPOSSIBLE("HREmployee Constructor: The employment end date was, impossibly, before the employment start date"),
		MSG_SHIFTS_EMPTY("HREmployee Constructor: Provided list of shifts was empty"),
		MSG_SHIFTS_NULL("HREmployee Constructor: Provided list of shifts was null"),
		MSG_SHIFTS_ADD_SINGLE_FAILED("HREmployee Constructor: Specified shift could not be added:");

		private String text;

		private EmployeeStrings(String text) {
			this.text = text;
		}

		public String get() {
			return this.text;
		}
	}

	/**
	 * Constant Strings for HRPerson class
	 * 
	 * @author Peter Marley
	 *
	 */
	public enum PersonStrings {
		MSG_EMAIL_INVALID("Person email parameter failed validation: "),
		MSG_NAME_BLANK("Person name (forename or surname) cannot be blank"),
		MSG_NAME_NULL("Person name (forename or surname) cannot be set to null"),
		VALUE_DEFAULT_EMAIL("no email provided"),
		VALUE_DEFAULT_PHONE_NUMBER("no phone number provided");

		private String text;

		private PersonStrings(String text) {
			this.text = text;
		}

		public String get() {
			return this.text;
		}
	}

	/**
	 * The abstract parent class for all personel objects in this program
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public abstract class Person implements Comparable<Person> {

		private String forename;
		private String surname;
		private String email;
		private String phoneNumber;

		/**
		 * Fully parameterised constructor
		 * 
		 * @param forename    - must not be blank or null
		 * @param email       - if blank or null set to {@code VALUE_DEFAULT_EMAIL.get()} String.<br>
		 *                    If email validation fails then {@code IllegalArgumentException} is thrown
		 * @param phoneNumber - if blank or null set to {@code VALUE_DEFAULT_PHONE_NUMBER.get()} String.
		 * @throws IllegalArgumentException if name blank or null, or if provided email is invalid
		 */
		public Person(String forename, String surname, String email, String phoneNumber) throws IllegalArgumentException {
			this.setForename(forename);
			this.setSurname(surname);
			this.setEmail(email);
			this.setPhoneNumber(phoneNumber);
		}

		/**
		 * @return the forename as String
		 */
		public String getForename() {
			return forename;
		}

		/**
		 * @return the forename as SimpleStringProperty
		 */
		public SimpleStringProperty getForenameAsSimpleProperty() {
			return new SimpleStringProperty(forename);
		}

		/**
		 * Set this contacts name. Must not be blank, or null.
		 * 
		 * @throws IllegalArgumentException if name is blank or null
		 */
		public void setForename(String forename) throws IllegalArgumentException {
			if (HumanResourcesModelValidator.name(forename))
				this.forename = forename;
		}

		/**
		 * @return the surname
		 */
		public String getSurname() {
			return surname;
		}

		/**
		 * @return the surname as SimpleStringProperty
		 */
		public SimpleStringProperty getSurnameAsSimpleProperty() {
			return new SimpleStringProperty(surname);
		}

		/**
		 * Set this contacts surnname. Must not be blank, or null.
		 * 
		 * @return the name
		 * @throws IllegalArgumentException if name is blank or null
		 */
		public void setSurname(String surname) throws IllegalArgumentException {
			if (HumanResourcesModelValidator.name(surname))
				this.surname = surname;

		}

		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * @return the email as SimpleStringProperty
		 */
		public SimpleStringProperty getEmailAsSimpleProperty() {
			return new SimpleStringProperty(email);
		}

		/**
		 * Sets this contacts email. If parameter is blank or null it is set to {@code VALUE_DEFAULT_EMAIL.get()}
		 * 
		 * @param email the email to set
		 * @throws IllegalArgumentException if email is unsuccessfully validated by {@code EmailValidator.validateEmail(String)}
		 */
		public void setEmail(String email) throws IllegalArgumentException {
			if (email != null) email = email.trim();
			if (HumanResourcesModelValidator.email(email))
				this.email = email;
		}

		/**
		 * @return the phoneNumber
		 */
		public String getPhoneNumber() {
			return phoneNumber;
		}

		/**
		 * @return the phoneNumber as SimpleStringProperty
		 */
		public SimpleStringProperty getPhoneNumberAsSimpleProperty() {
			return new SimpleStringProperty(phoneNumber);
		}

		/**
		 * @param phoneNumber the phoneNumber to set
		 */
		public void setPhoneNumber(String phoneNumber) {
			if (HumanResourcesModelValidator.phoneNumber(phoneNumber))
				this.phoneNumber = phoneNumber;
		}

		@Override
		public int compareTo(Person thatPerson) {
			int returnValue = 0;
			String thisSurname = this.getSurname().toUpperCase();
			String thatSurname = thatPerson.getSurname().toUpperCase();
			for (int i = 0; i < thisSurname.length() && i < thatSurname.length(); i++) {
				char thisChar = thisSurname.charAt(i);
				char thatChar = thatSurname.charAt(i);
				if (thisChar != thatChar) {
					if (thisChar < thatChar) {
						returnValue = -1;
					} else if (thisChar > thatChar) {
						returnValue = 1;
					} else {
						returnValue = 0;
					}
					break;
				}
			}
			return returnValue;
		}

	}

	/**
	 * Represents a company employee, and specifically is meant for interaction with this programs JavaFX GUI
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public class Employee extends Person {

		private int hourlyRateInPence;
		private double hoursPerWeek;
		private LocalDate startDate;
		private LocalDate endDate;
		private Hashtable<LocalDate, Integer> shifts;
		private LocalDateStringConverter converter;

		//private static final int MINIMUM_WAGE = 800;

		/**
		 * Fully parameterised constructor for Employee for all fields
		 * 
		 * @param forename
		 * @param surname
		 * @param email
		 * @param phoneNumber
		 * @param hourlyRate  - in pence
		 * @param weeklyHours
		 * @param start       - a LocalDate object
		 * @throws IllegalArgumentException if name if blank or null
		 */
		public Employee(String forename, String surname, String email, String phoneNumber, int hourlyRate, double weeklyHours, LocalDate start, LocalDate end)
				throws IllegalArgumentException {
			super(forename, surname, email, phoneNumber);
			this.setHourlyRate(hourlyRate);
			this.setWeeklyHours(weeklyHours);
			this.setStartDate(start);
			this.setEndDate(end);
			this.converter = new LocalDateStringConverter();
			this.shifts = new Hashtable<LocalDate, Integer>();
		}

		///////////////////////////
		// GETTERS AND SETTERS	//
		/////////////////////////

		/**
		 * @return the hourlyRateInPence
		 */
		public int getHourlyRate() {
			return hourlyRateInPence;
		}

		public String getHourlyRateAsCurrency() {
			return String.format("£%.2f", hourlyRateInPence / 100.0);
		}

		/**
		 * @return the hourlyRateInPence as SimpleIntegerProperty
		 */
		public SimpleIntegerProperty getHourlyRateAsSimpleProperty() {
			return new SimpleIntegerProperty(hourlyRateInPence);
		}

		/**
		 * @param baseHourlyRate the baseHourlyRate to set (in pence)
		 * @throws IllegalArgumentException if hourly rate is set to below MINIMUM_WAGE const
		 */
		public void setHourlyRate(int baseHourlyRate) throws IllegalArgumentException {
			if (HumanResourcesModelValidator.hourlyRateInPence(baseHourlyRate))
				this.hourlyRateInPence = baseHourlyRate;
		}

		/**
		 * @return the hoursPerWeek
		 */
		public double getWeeklyHours() {
			return hoursPerWeek;
		}

		/**
		 * @return the hourlyRateInPence as SimpleDoubleProperty
		 */
		public SimpleDoubleProperty getWeeklyHoursAsSimpleProperty() {
			return new SimpleDoubleProperty(hoursPerWeek);
		}

		/**
		 * @param hoursPerWeek the hoursPerWeek to set
		 * @throws IllegalArgumentException if hoursPerWeek is a negative number
		 */
		public void setWeeklyHours(double hoursPerWeek) throws IllegalArgumentException {
			if (HumanResourcesModelValidator.weeklyHours(hoursPerWeek))
				this.hoursPerWeek = hoursPerWeek;
		}

		/**
		 * @return the startDate as LocalDate
		 */
		public LocalDate getStartDateAsLocalDate() {
			return startDate;
		}

		/**
		 * @return the startDate as String
		 */
		public String getStartDateAsString() {
			return converter.toString(startDate);
		}

		/**
		 * @param startDate the dateOfEmploymentStart to set
		 * @throws IllegalArgumentException if dateTime is null
		 */
		void setStartDate(LocalDate startDate) throws IllegalArgumentException {
			if (HumanResourcesModelValidator.startDate(startDate))
				this.startDate = startDate;
		}

		/**
		 * @return the endDate as LocalDate
		 */
		public LocalDate getEndDateAsLocalDate() {
			return endDate;
		}

		/**
		 * @return the endDate as String
		 */
		public String getEndDateAsString() {
			return converter.toString(endDate);
		}

		/**
		 * @param endDate the dateOfEmploymentEnd to set
		 * @throws IllegalArgumentException if endDate is chronologically before {@code this.dateOfEmploymentStart}
		 */
		void setEndDate(LocalDate endDate) throws IllegalArgumentException {
			if (HumanResourcesModelValidator.endDate(this.startDate, endDate))
				this.endDate = endDate;
		}

		///////////////////////////
		// SHIFT OPERATIONS		//
		/////////////////////////

		/**
		 * @return the shifts
		 */
		public Hashtable<LocalDate, Integer> getShifts() {
			return shifts;
		}

		/**
		 * Add multiple shifts to {@code this.shifts} Hashtable
		 * 
		 * @param shiftsToAdd
		 * @throws IllegalArgumentException if shiftsToAdd is null or empty
		 */
		public void addMultipleShifts(Hashtable<LocalDate, Integer> shiftsToAdd) throws IllegalArgumentException {
			if (shiftsToAdd != null && shiftsToAdd.size() > 0) {
				Iterator<LocalDate> keys = shiftsToAdd.keySet().iterator();
				while (keys.hasNext()) {
					LocalDate key = keys.next();
					addSingleShift(key, shiftsToAdd.get(key));
				}
			} else if (shiftsToAdd == null) {
				throw new IllegalArgumentException(EmployeeStrings.MSG_SHIFTS_NULL.get());
			} else {
				throw new IllegalArgumentException(EmployeeStrings.MSG_SHIFTS_EMPTY.get());
			}
		}

		/**
		 * Add a single shift to {@code this.shifts}
		 * 
		 * @param date
		 * @param hours
		 * @throws IllegalArgumentException if date or hours is null, or if hours is less than 1
		 */
		public void addSingleShift(LocalDate date, Integer hours) throws IllegalArgumentException {
			if (date != null && hours != null && hours > 0) {
				this.shifts.put(date, hours);
			} else {
				throw new IllegalArgumentException(EmployeeStrings.MSG_SHIFTS_ADD_SINGLE_FAILED.get() + " [Date=" + ((date == null) ? "null" : date) +
						" hours=" + ((hours == null) ? "null" : hours) + "] for Employee " + super.getForename());
			}

		}

		/**
		 * Search {@code this.shifts} for the length of a shift on a given date
		 * 
		 * @param shiftDate
		 * @return an Integer - the length of the shift (or null if no shift found)
		 */
		public Integer getShiftLengthOn(LocalDate shiftDate) {
			return this.shifts.get(shiftDate);
		}

		///////////////////////////
		// UTILITY 				//
		/////////////////////////

		/**
		 * @return a String representation of this HREmployee object
		 */
		@Override
		public String toString() {
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			return "Employee [name=" + super.getForename() +
					", email=" + super.getEmail() +
					", phoneNumber=" + super.getPhoneNumber() +
					", rate=" + formatter.format(this.getHourlyRate() / 100.0) +
					", weeklyHours=" + this.getWeeklyHours() +
					", startDate=" + this.getStartDateAsLocalDate() +
					", endDate=" + ((this.getEndDateAsLocalDate() == null) ? "Still Employed" : this.getEndDateAsLocalDate()) + "]";
		}

	}

	/**
	 * Represents a company supplier, and specifically is meant for interaction with this programs JavaFX GUI
	 * 
	 * @author Peter Marley
	 * @StudentNumber 13404067
	 * @Email pmarley03@qub.ac.uk
	 * @GitHub https://github.com/PeterMarley
	 *
	 */
	public class Supplier extends Person {

		private ArrayList<Categories> categories;
		private ArrayList<STKAbstractItem> stockItems;
		private Hashtable<LocalDate, String> transactions;

		/**
		 * Parameterised constructor for this Supplier object
		 * 
		 * @param name
		 * @param email
		 * @param phoneNumber
		 * @throws IllegalArgumentException
		 */
		public Supplier(String forename, String surname, String email, String phoneNumber) throws IllegalArgumentException {
			super(forename, surname, email, phoneNumber);

			this.categories = new ArrayList<Categories>(5);
			this.stockItems = new ArrayList<STKAbstractItem>();

			this.transactions = new Hashtable<LocalDate, String>();

		}

		/**
		 * @return the categoriesSupplierProvides {@code ArrayList<Categories>}
		 */
		public ArrayList<Categories> getCategories() {
			return categories;
		}

		/**
		 * Set the categories {@code ArrayList<Categories>} to parameter {@code categories}
		 * 
		 * @param categories
		 * @throws IllegalArgumentException if {@code categories} is null or empty (size 0)
		 */
		public void setCategories(ArrayList<Categories> categories) throws IllegalArgumentException {
			if (categories != null && categories.size() > 0) {
				this.categories = categories;
			} else if (categories == null) {
				throw new IllegalArgumentException("Supplier: cannot set categories to null");
			} else {
				throw new IllegalArgumentException("Supplier: cannot set categories to empty ArrayList<Categories>");
			}
		}

		/**
		 * Add a new category of stock that this Supplier provides
		 * 
		 * @param stockType a Categories Enum
		 * @throws IllegalArgumentException if stockType is null or is already in the {@code categoriesSupplierProvides} ArrayList
		 */
		public void addCategory(Categories stockType) throws IllegalArgumentException {
			if (stockType != null && !categories.contains(stockType)) {
				this.categories.add(stockType);
			} else if (stockType == null) {
				throw new IllegalArgumentException("Supplier: cannot add a null category to categories");
			} else {
				throw new IllegalArgumentException("Supplier: cannot add a duplicate category to categories; already contains (" + stockType + ")");
			}
		}

		/**
		 * Removes a category of stock that this Supplier provides
		 * 
		 * @param stockType a Categories Enum
		 * @throws IllegalArgumentException if the stockType is null or is not in the {@code categoriesSupplierProvides} ArrayList
		 */
		public void removeCategory(Categories stockType) throws IllegalArgumentException {
			if (stockType != null && categories.contains(stockType)) {
				this.categories.remove(stockType);
			} else if (stockType == null) {
				throw new IllegalArgumentException("Supplier: cannot remove a null category from categories");
			} else {
				throw new IllegalArgumentException("Supplier: cannot remove a category that categories does not contain (" + stockType + ")");
			}
		}

		/**
		 * Clears all {@code Categories} from the {@code categories} field
		 */
		public void clearCategories() {
			this.categories.clear();
		}

		@Override
		public int compareTo(Person o) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	public static class HumanResourcesModelValidator {

		/**
		 * This enumerated type stores the valid characters allowed, and invalid starting and ending characters
		 * in each section of an email String.
		 */
		private enum EmailParts {
			LOCAL("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYZ01234567890123456789!#$%&'*+-/=?^_`{|}~", '.'),
			DOMAIN("abcdefghijklmnopqrstuwvxyzABCDEFGHIJKLMNOPQRSTUVXYZ01234567890123456789-", '-');

			private String validChars;
			private char invalidStartAndEnd;

			private EmailParts(String validChars, char invalidStartAndEnd) {
				this.validChars = validChars;
				this.invalidStartAndEnd = invalidStartAndEnd;
			}

			public String validChars() {
				return this.validChars;
			}

			public char invalidStartAndEnd() {
				return this.invalidStartAndEnd;
			}
		}

		/**
		 * Uses rules to validate an email address's format validity<br>
		 * Rules drawn from {@link https://en.wikipedia.org/wiki/Email_address#Local-part}
		 * 
		 * @param email
		 * @return a boolean - is this email valid?
		 */
		private static boolean validateEmail(String email) {
			// if parameter is null or blank return true
			if (email == null || email.isBlank() || !email.contains("@")) {
				return false;
			}

			try {
				// validate the local and domain sections of email
				String[] splitEmail = email.split("@");
				if (splitEmail.length != 2) {
					return false;
				}
				if (validateEmailParts(splitEmail[0], EmailParts.LOCAL) && validateEmailParts(splitEmail[1], EmailParts.DOMAIN)) {
					return true;
				} else {
					return false;
				}
			} catch (IllegalArgumentException e) {
				return false;
			}
		}

		/**
		 * Validate the two parts of an email String; the local part and the domain part
		 * 
		 * @param emailPart
		 * @param partType  - LOCAL or DOMAIN
		 * @return a boolean - is this email part valid?
		 * @throws IllegalArgumentException if emailPart is null or blank, or if partType is null
		 */
		private static boolean validateEmailParts(String emailPart, EmailParts partType) throws IllegalArgumentException {
			// validate parameters
			if (partType == null || emailPart == null || emailPart.isBlank()) {
				throw new IllegalArgumentException("Arguments may not be null or blank!");
			}

			// check first and last char are legal
			if (emailPart.charAt(0) == partType.invalidStartAndEnd() || emailPart.charAt(emailPart.length() - 1) == partType.invalidStartAndEnd()) {
				return false;
			}

			if (partType == EmailParts.LOCAL && emailPart.contains(Character.toString(partType.invalidStartAndEnd()).repeat(2))) {
				// check local part does not contain repeated periods
				return false;
			}

			boolean accepted = true;
			if (partType == EmailParts.LOCAL) {
				// check all characters are valid characters
				accepted = validateEmailPartCharacters(emailPart, partType);
			} else if (partType == EmailParts.DOMAIN) {
				String[] splitDomain = emailPart.split("\\.");
				//			System.out.println("emailPart " + emailPart);
				//			System.out.println("emailPart.split(\"\\\\.\") " + Arrays.deepToString(emailPart.split("\\.")));
				// silly regular expressions use . as any character a wildcard!
				for (String s : splitDomain) {
					if (!validateEmailPartCharacters(s, partType)) {
						accepted = false;
						break;
					}
				}
			}

			// otherwise a-ok!
			return accepted;
		}

		/**
		 * Validates the specified email part contains only valid characters
		 * 
		 * @param emailPart
		 * @param partType  - LOCAL or DOMAIN
		 * @return a boolean - does this email part contain only valid characters?
		 */
		private static boolean validateEmailPartCharacters(String emailPart, EmailParts partType) {
			for (int i = 0; i < emailPart.length(); i++) {
				if (!partType.validChars().contains(Character.toString(emailPart.charAt(i)))) {
					return false;
				}
			}
			return true;
		}

		/**
		 * Validate a forename or surname. If name is blank or null an IllegalArgumentException is thrown
		 * 
		 * @param name
		 * @throws IllegalArgumentException if name parameter is null or blank
		 */
		public static boolean name(String name) throws IllegalArgumentException {
			if (name != null && !name.isBlank()) {
				return true;
			} else if (name == null) {
				throw new IllegalArgumentException(PersonStrings.MSG_NAME_NULL.get());
			} else {
				throw new IllegalArgumentException(PersonStrings.MSG_NAME_BLANK.get());
			}
		}

		/**
		 * Validates an email address.
		 * 
		 * @param email the email to set
		 * @throws IllegalArgumentException if email is unsuccessfully validated by {@code EmailValidator.validateEmail(String)}
		 */
		public static boolean email(String email) {
			if (email == null || email.isBlank()) {
				return false;
			} else if (validateEmail(email)) {
				return true;
			} else {
				throw new IllegalArgumentException(PersonStrings.MSG_EMAIL_INVALID.get() + email);
			}
		}

		/**
		 * @param phoneNumber the phoneNumber to set
		 */
		public static boolean phoneNumber(String phoneNumber) {
			if (phoneNumber == null || phoneNumber.isBlank()) {
				return false;
			} else {
				return true;
			}
		}

		/**
		 * @param baseHourlyRate the baseHourlyRate to set (in pence)
		 * @throws IllegalArgumentException if hourly rate is set to below MINIMUM_WAGE const
		 */
		public static boolean hourlyRateInPence(int baseHourlyRate) throws IllegalArgumentException {
			if (baseHourlyRate >= EmployeeNumbers.INT_MIN_WAGE.get()) {
				return true;
			} else {
				throw new IllegalArgumentException(String.format("Hourly rate cannot be set to below minimum wage. Minimum wage is £%.2f but specified Hourly Rate was £%.2f.",
						(EmployeeNumbers.INT_MIN_WAGE.get() / 100.0), (baseHourlyRate / 100.0)));
			}
		}

		/**
		 * @param hoursPerWeek the hoursPerWeek to set
		 * @throws IllegalArgumentException if hoursPerWeek is a negative number
		 */
		public static boolean weeklyHours(double hoursPerWeek) throws IllegalArgumentException {
			if (hoursPerWeek >= 0) {
				return true;
			} else {
				throw new IllegalArgumentException("Hours per week cannot be set to a negative value.");
			}
		}

		/**
		 * @param startDateTime the dateOfEmploymentStart to set
		 * @throws IllegalArgumentException if dateTime is null
		 */
		public static boolean startDate(LocalDate startDateTime) throws IllegalArgumentException {
			if (startDateTime != null) {
				return true;
			} else {
				throw new IllegalArgumentException("Start date and time cannot be set to null.");
			}
		}

		/**
		 * @param endDate the dateOfEmploymentEnd to set
		 * @throws IllegalArgumentException if endDate is chronologically before {@code this.dateOfEmploymentStart}
		 */
		public static boolean endDate(LocalDate startDate, LocalDate endDate) throws IllegalArgumentException {
			if (endDate == null || (endDate != null && !endDate.isBefore(startDate))) {
				return true;
			} else {
				throw new IllegalArgumentException(EmployeeStrings.MSG_DATE_OF_END_IMPOSSIBLE.get());
			}

		}
	}

}
