package model;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Objects;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.converter.LocalDateStringConverter;
import model.ModelEnums.Categories;
import model.ModelEnums.Messages;
import model.ModelEnums.Numbers;
import model.StockItemModel.AbstractStockItem;

public class HumanResourcesModel {

	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd")
			.toFormatter();

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
		 * Set this contacts name. Must not be blank, null, or contain chars other than 'a-z', 'A-Z' or '-'
		 * 
		 * @throws IllegalArgumentException if name is blank, null, or contains chars other than 'a-z', 'A-Z' or '-'
		 */
		public void setForename(String forename) throws IllegalArgumentException {
			this.forename = HumanResourcesModelValidator.name(forename);
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
		 * Set this contacts surnname. Must not be blank, null, or contain chars other than 'a-z', 'A-Z' or '-'
		 * 
		 * @return the name
		 * @throws IllegalArgumentException if name is blank, null, or contains chars other than 'a-z', 'A-Z' or '-'
		 */
		public void setSurname(String surname) throws IllegalArgumentException {
			this.surname = HumanResourcesModelValidator.name(surname);

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
		 * Sets this contacts email. The parameter is validated by {@code HumanResourcesModelValidator.email(String)}. If it is deemed valid it is set to
		 * email.trim(),
		 * if invalid an {@code IllegalArgumentException} is thrown. If the {@code email} parameter is {@code null} or a blank String the email field is set
		 * to {@code null}.
		 * 
		 * @param email the email to set
		 * @throws IllegalArgumentException if email is unsuccessfully validated by {@code HumanResourcesModelValidator.email(String)}
		 */
		public void setEmail(String email) throws IllegalArgumentException {
			this.email = HumanResourcesModelValidator.email(email);
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
		 * Sets this contacts phoneNumber. The parameter is validated by {@code HumanResourcesModelValidator.phoneNumber(phoneNumber)}. If the parameter is
		 * {@code null} or a blank {@code String}, it is set to {@code null}, otherwise it is set to {@code phoneNumber.trim()}
		 * 
		 * @param phoneNumber the phoneNumber to set
		 */
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = HumanResourcesModelValidator.phoneNumber(phoneNumber);
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

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!(obj instanceof Person))
				return false;
			Person other = (Person) obj;
			return (email != null) ? email.equals(other.email)
					: other.email == null
							&& forename.equals(other.forename)
							&& (phoneNumber != null) ? phoneNumber.equals(other.phoneNumber)
									: other.phoneNumber == null
											&& surname.equals(other.surname);
		}

		private HumanResourcesModel getEnclosingInstance() {
			return HumanResourcesModel.this;
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
			this.setHoursPerWeek(weeklyHours);
			this.setStartDate(start);
			this.setEndDate(end);
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
		 * @param hourlyRateInPence the hourlyRateInPence to set
		 * @throws IllegalArgumentException if parameter is less than {@code EmployeeNumbers.MINIMUM_WAGE.get()}
		 */
		public void setHourlyRate(int hourlyRateInPence) throws IllegalArgumentException {
			this.hourlyRateInPence = HumanResourcesModelValidator.hourlyRateInPence(hourlyRateInPence);
		}

		/**
		 * @return the hoursPerWeek
		 */
		public double getHoursPerWeek() {
			return hoursPerWeek;
		}

		/**
		 * @return the hourlyRateInPence as SimpleDoubleProperty
		 */
		public SimpleDoubleProperty getHoursPerWeekAsSimpleProperty() {
			return new SimpleDoubleProperty(hoursPerWeek);
		}

		/**
		 * @param hoursPerWeek the {@code hoursPerWeek} to set
		 * @throws IllegalArgumentException if {@code hoursPerWeek} is a negative number, or is greater than {@link Numbers.EMPLOYEE_MAX_WEEKLY_HOURS}
		 */
		public void setHoursPerWeek(double hoursPerWeek) throws IllegalArgumentException {
			this.hoursPerWeek = HumanResourcesModelValidator.hoursPerWeek(hoursPerWeek);
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
			return startDate.format(formatter);
		}

		/**
		 * @param startDate the dateOfEmploymentStart to set
		 * @throws IllegalArgumentException if dateTime is null
		 */
		void setStartDate(LocalDate startDate) throws IllegalArgumentException {
			this.startDate = HumanResourcesModelValidator.startDate(startDate);
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
			if (endDate == null) {
				return "";
			} else {
				return endDate.format(formatter);
			}
		}

		/**
		 * @param endDate the dateOfEmploymentEnd to set
		 * @throws IllegalArgumentException if endDate is chronologically before {@code this.dateOfEmploymentStart}
		 */
		void setEndDate(LocalDate endDate) throws IllegalArgumentException {
			this.endDate = HumanResourcesModelValidator.endDate(this.startDate, endDate);
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
				throw new IllegalArgumentException(Messages.EMPLOYEE_SET_SHIFTS_NULL.get());
			} else {
				throw new IllegalArgumentException(Messages.EMPLOYEE_SET_SHIFTS_EMPTY.get());
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
				throw new IllegalArgumentException(Messages.EMPLOYEE_ADD_SHIFT_SINGLE_FAILED.get() + " [Date=" + ((date == null) ? "null" : date) +
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
					", weeklyHours=" + this.getHoursPerWeek() +
					", startDate=" + this.getStartDateAsLocalDate() +
					", endDate=" + ((this.getEndDateAsLocalDate() == null) ? "Still Employed" : this.getEndDateAsLocalDate()) + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (!super.equals(obj))
				return false;
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof Employee))
				return false;
			Employee other = (Employee) obj;
			return ((endDate != null) ? endDate.equals(other.endDate) : endDate == other.endDate)
					&& hourlyRateInPence == other.hourlyRateInPence
					&& hoursPerWeek == other.hoursPerWeek
					&& startDate.equals(other.startDate)
					&& Objects.equals(endDate, other.endDate);
		}

		private HumanResourcesModel getEnclosingInstance() {
			return HumanResourcesModel.this;
		}

		public Employee clone() {
			Employee cloned = new Employee(
					this.getForename(),
					this.getSurname(),
					this.getEmail(),
					this.getPhoneNumber(),
					this.getHourlyRate(),
					this.getHoursPerWeek(),
					this.getStartDateAsLocalDate(),
					this.getEndDateAsLocalDate());
			return cloned;

		}

	}

	public class Supplier extends Person {
		/**
		 * Represents a company supplier, and specifically is meant for interaction with this programs JavaFX GUI
		 * 
		 * @author Peter Marley
		 * @StudentNumber 13404067
		 * @Email pmarley03@qub.ac.uk
		 * @GitHub https://github.com/PeterMarley
		 *
		 */

		private ArrayList<Categories> categories;
		private ArrayList<AbstractStockItem> stockItems;
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
			this.stockItems = new ArrayList<AbstractStockItem>();

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

		private static final String NAME_CHARS = "abcdefghijklmnopqrstuvwxywABCDEFGHIJKLMNOPQRSTUVWXYZ-";

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
		 * Validate a forename or surname. If name is blank, null, or contain chars other than 'a-z', 'A-Z' or '-' (Additionally '-' is not allowed to appear
		 * concurrently in the name parameter) an IllegalArgumentException is thrown. Otherwise the name parameter is trimmed of leading and trailing
		 * whitespace, the first letter is made upper case, and the rest are made lower case; and returned
		 * 
		 * @param name
		 * @throws IllegalArgumentException if name parameter is blank, null or contain chars other than 'a-z', 'A-Z' or '-'. Additionally '-' is not allowed
		 *                                  to appear concurrently in the name parameter
		 */
		public static String name(String name) throws IllegalArgumentException {
			if (name != null && !name.isBlank()) {
				// sanitise by removing leading and trailing spaces and make initial char upper case, and rest lower case
				name = name.trim();
				name = name.toUpperCase().charAt(0) + name.toLowerCase().substring(1, name.length());
				// check all chars in name parameter are valid chars
				boolean charsValidated = true;
				for (char c : name.toCharArray()) {
					if (!NAME_CHARS.contains(String.format("%c", c))) {
						charsValidated = false;
						break;
					}
				}
				// ensure no repeated hyphens
				if (charsValidated && name.contains("-".repeat(2))) {
					charsValidated = false;
				}
				// ensure length is minimum of 2
				if (charsValidated && name.length() >= Numbers.PERSON_NAME_MIN_LENGTH.get()) {
					return name;
				} else if (name.length() < 2) {
					throw new IllegalArgumentException(Messages.PERSON_SET_NAME_TOO_FEW_CHARS.get());
				} else {
					throw new IllegalArgumentException(Messages.PERSON_SET_NAME_INVALID_CHARS.get());
				}
			} else if (name == null) {
				throw new IllegalArgumentException(Messages.PERSON_SET_NAME_NULL.get());
			} else {
				throw new IllegalArgumentException(Messages.PERSON_SET_NAME_BLANK.get());
			}
		}

		/**
		 * Validates an email address. If the email parameter is null or a blank string, null is returned, otherwise it is trimmed of leading and trailing
		 * whitespace, and validated by rules laid out in private method {@code HumanResourcesModelValidator.validateEmail(String)}, and if valid, the leading
		 * and trailing whitespace is trimmed and the email parameter is returned
		 * 
		 * @param email
		 * @throws IllegalArgumentException if email is unsuccessfully validated by {@code HumanResourcesModelValidator.validateEmail(String)}
		 */
		public static String email(String email) {
			if (email == null || email.isBlank()) {
				return null;
			} else if (validateEmail(email.trim())) {
				return email.trim();
			} else {
				throw new IllegalArgumentException(Messages.PERSON_SET_EMAIL_INVALID.get() + email);
			}
		}

		/**
		 * Validates a phoneNumber. If the phoneNumber parameter is null or a blank String, null is returned, otherwise the phoneNumber parameter is trimmed
		 * of leading and trailing whitespace and returned
		 * 
		 * @param phoneNumber
		 */
		public static String phoneNumber(String phoneNumber) {
			if (phoneNumber == null || phoneNumber.isBlank()) {
				return null;
			} else {
				return phoneNumber.trim();
			}
		}

		/**
		 * Validates the hourlyRateInPence. The parameter must simply be greater or equal to {@code EmployeeNumbers.INT_MIN_WAGE.get()}, or an
		 * {@code IllegalArgumentException} is thrown
		 * 
		 * @param hourlyRateInPence
		 * @throws {@code IllegalArgumentException} if hourly rate is set to below {@code EmployeeNumber.MINIMUM_WAGE.get()}
		 */
		public static int hourlyRateInPence(int hourlyRateInPence) throws IllegalArgumentException {
			if (hourlyRateInPence >= Numbers.INT_MIN_WAGE.get()) {
				return hourlyRateInPence;
			} else {
				throw new IllegalArgumentException(String.format("Hourly rate cannot be set to below minimum wage. Minimum wage is £%.2f but specified Hourly Rate was £%.2f.",
						(Numbers.INT_MIN_WAGE.get() / 100.0), (hourlyRateInPence / 100.0)));
			}
		}

		/**
		 * Validates the hoursPerWeek parameter. The parameter must simply be greater than or equal to 0, and less than or equal to
		 * {@code Numbers.EMPLOYEE_MAX_WEEKLY_HOURS.get()}, or an {@code IllegalArgumentException} is thrown.
		 * 
		 * @param hoursPerWeek the hoursPerWeek to set
		 * @throws {@code IllegalArgumentException}{@code IllegalArgumentException} if hoursPerWeek is a negative number, or is greater than
		 * {@Code Numbers.EMPLOYEE_MAX_WEEKLY_HOURS.get()).
		 */
		public static double hoursPerWeek(double hoursPerWeek) throws IllegalArgumentException {
			if (hoursPerWeek >= 0 && hoursPerWeek <= Numbers.EMPLOYEE_MAX_WEEKLY_HOURS.get()) {
				return hoursPerWeek;
			} else if (hoursPerWeek < 0) {
				throw new IllegalArgumentException(Messages.EMPLOYEE_SET_WEEKLY_HOURS_NEGATIVE.get());
			} else {
				throw new IllegalArgumentException(Messages.EMPLOYEE_SET_WEEKLY_HOURS_OVER_MAX.get());
			}
		}

		/**
		 * Validates the startDate parameter. If it is not {@code null}, it is returned, if it is {@code null} an {@code IllegalArgumentException} is thrown
		 * 
		 * @param startDate the {@code startDate} to set
		 * @throws IllegalArgumentException if {@code startDate} is null
		 */
		public static LocalDate startDate(LocalDate startDate) throws IllegalArgumentException {
			if (startDate != null) {
				return startDate;
			} else {
				throw new IllegalArgumentException(Messages.EMPLOYEE_SET_DATE_OF_START_NULL.get());
			}
		}

		/**
		 * @param endDate the dateOfEmploymentEnd to set
		 * @throws IllegalArgumentException if endDate is chronologically before {@code this.dateOfEmploymentStart}
		 */
		public static LocalDate endDate(LocalDate startDate, LocalDate endDate) throws IllegalArgumentException {
			if (endDate == null || (endDate != null && startDate != null && !endDate.isBefore(startDate))) {
				return endDate;
			} else {
				throw new IllegalArgumentException(Messages.EMPLOYEE_SET_DATE_OF_END_IMPOSSIBLE.get());
			}

		}
	}
}
