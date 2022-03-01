package model;

import java.time.LocalDate;

import model.HumanResourcesModel.EmployeeNumbers;
import model.HumanResourcesModel.EmployeeStrings;
import model.HumanResourcesModel.PersonStrings;

public class HumanResourcesModelValidator {

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
